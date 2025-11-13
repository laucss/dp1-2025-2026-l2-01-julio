package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;

public class StatisticService {
    
    private StatisticRepository statisticRepository;
    private PlayerRepository playerRepository;
    private GameRepository gameRepository;

    @Transactional(readOnly = true)
    public List<StatisticWithPlayerDTO> findAllStatistics() {
        List<Statistic> statistics = statisticRepository.findAll();
        List<StatisticWithPlayerDTO> statisticDTOs = new ArrayList<>();
        statistics.forEach(statistic -> {
            Player player = playerRepository.findByStatisticId(statistic.getId());
            StatisticWithPlayerDTO statisticDTO = new StatisticWithPlayerDTO(statistic, player.getUsername());
            statisticDTOs.add(statisticDTO);
        });
        return statisticDTOs;
    }
}
