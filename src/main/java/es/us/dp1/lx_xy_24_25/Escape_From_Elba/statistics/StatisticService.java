package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;

@Service
public class StatisticService {

    @Autowired    
    private PlayerService playerService;
    @Autowired
    private MatchService matchService;

    public Integer getTotalAccionPointsByUser(Integer currentUserId) {

        List<Player> userPlayers = playerService.findByUserId(currentUserId);
        if(userPlayers==null){
            userPlayers=new ArrayList<>();
        }
        return userPlayers.stream()
                .mapToInt(Player::getActionPoints)
                .sum();
    }
}
