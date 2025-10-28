package es.us.dp1.lx_xy_24_25.Escape_From_Elba.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.*;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;

@Component
public class Checkers {

    private final MatchRepository matchRepository;

    public Checkers(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Autowired
    public MatchService matchService;


    public void checkGameStatus(Match match, String status){
        if(!match.getStatus().equals(status)){
            throw new GameIsNotALobbyException("The game is not in the correct status");
        }
    }


    public void checkNumberOfPlayers(Match match){
        if(match.getPlayers().size()>=match.getMaxPlayers()){
            throw new LobbyIsFullException("The lobby is full");
        }
    }

    
    public void checkPlayerAlreadyInALobby(Player player){
        if(matchRepository.findLobbyWherePlayerIsIn(player).isPresent()){
            throw new PlayerAlreadyInALobbyException("The player is already in a lobby");

        }
    }
     

    



    
}
