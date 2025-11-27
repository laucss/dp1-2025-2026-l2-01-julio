package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;

@Service
public class StatisticService {

       
    private PlayerService playerService;
    private PlayerRepository playerRepository;
    private MatchService matchService;

    @Autowired
    public StatisticService(PlayerService playerService, PlayerRepository playerRepository, MatchService matchService) {
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.matchService = matchService;
    }
    public Integer getTotalAccionPointsByUser(Integer currentUserId) {
        return playerRepository.getTotalAccionPointsByUser(currentUserId);
    }
}