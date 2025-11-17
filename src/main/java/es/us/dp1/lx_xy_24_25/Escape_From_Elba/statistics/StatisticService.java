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


    public StatisticService(StatisticRepository statisticRepository, PlayerRepository playerRepository, MatchRepository matchRepository) {
        this.statisticRepository = statisticRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

}
