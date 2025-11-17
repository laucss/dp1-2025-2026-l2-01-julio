package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;

@Service
public class StatisticService {
    
    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired    
    private PlayerRepository playerRepository;
    @Autowired
    private MatchRepository matchRepository;

    @Transactional(readOnly = true)
    public List<StatisticWithPlayerDTO> findAllStatistics() {
        List<Statistic> statistics = statisticRepository.findAll();
        List<StatisticWithPlayerDTO> statisticDTOs = new ArrayList<>();
        statistics.forEach(statistic -> {
            Player player = playerRepository.findByStatisticId(statistic.getId());
            if(player != null) {
                StatisticWithPlayerDTO statisticDTO = new StatisticWithPlayerDTO(statistic, player.getUsername());
                statisticDTOs.add(statisticDTO);
            }
        });
        return statisticDTOs;
    }

    @Transactional(readOnly = true)
    public FullStatisticDTO getGlobalStatistics() {
        StatisticDTO globalStatistic = statisticRepository.findGlobalTotalStatistics();
        StatisticAvgDTO avgStatistic = statisticRepository.findGlobalAvgStatistics();
        StatisticDTO minStatistic = statisticRepository.findGlobalMinStatistics();
        StatisticDTO maxStatistic = statisticRepository.findGlobalMaxStatistics();
        
        Object[] playersPerGameStats = matchRepository.getPlayersPerGameStats().get(0);
        avgStatistic.setPlayers((Double) playersPerGameStats[0]);
        maxStatistic.setPlayers(((Number) playersPerGameStats[1]).longValue());
        minStatistic.setPlayers(((Number) playersPerGameStats[2]).longValue());

        Long finishedGamesCount = gameRepository.getFinishedGamesCount();
        globalStatistic.setGamesPlayed(finishedGamesCount);

        return new FullStatisticDTO(globalStatistic, avgStatistic, minStatistic, maxStatistic);
    }

    public StatisticService(StatisticRepository statisticRepository, PlayerRepository playerRepository,
            MatchRepository matchRepository) {
        this.statisticRepository = statisticRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }
}
