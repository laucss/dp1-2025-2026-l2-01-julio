package es.us.dp1.lx_xy_24_25.Escape_From_Elba.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.*;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;

import java.util.Optional;

@Component
public class Checkers {

    private final MatchRepository matchRepository;
    private final CardRepository cardRepository; 

    public Checkers(MatchRepository matchRepository, CardRepository cardRepository) {
        this.matchRepository = matchRepository;
        this.cardRepository = cardRepository; 
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

    public void checkCardExists(Card card) {
        Optional<Card> givenCard = cardRepository.findById(card.getId()); 
        if (givenCard == null)
            throw new ResourceNotFoundException("This card does not exist or is not found");
    }
     

    



    
}
