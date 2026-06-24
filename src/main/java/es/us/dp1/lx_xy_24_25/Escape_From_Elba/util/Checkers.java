package es.us.dp1.lx_xy_24_25.Escape_From_Elba.util;

import org.springframework.stereotype.Component;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.*;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

import java.util.Optional;

@Component
public class Checkers {

    private final MatchRepository matchRepository;
    private final CardRepository cardRepository; 
    private final PlayerRepository playerRepository;

    public Checkers(MatchRepository matchRepository, PlayerRepository playerRepository, CardRepository cardRepository) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.cardRepository = cardRepository; 
    }

    private static final Integer TOTAL_CARDS_TO_DRAW = 7; // máximo número de cartas que puedes robar por turno 


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

    
    public void checkPlayerAlreadyInALobby(User user){
        if(matchRepository.findLobbyWhereUserIsIn(user.getId()).isPresent()){
            throw new PlayerAlreadyInALobbyException("The player is already in a lobby");

        }
    }

    public void checkPlayerIsInTheGame(Match match, User user){
        if(playerRepository.findByMatchAndUser(match.getId(), user.getId()).isEmpty()) {
            throw new PlayerNotInTheGame("The player is not in the lobby");
        }
    }

    // CARTAS 
    public void checkCardExists(Card card) {
        if (card == null || card.getId() == null) {
            throw new IllegalArgumentException("Card id must not be null");
        }
        Optional<Card> givenCard = cardRepository.findById(card.getId()); 
        if (givenCard.isEmpty()) {
            throw new ResourceNotFoundException("This card does not exist or is not found");
        }
    }


    public void checkNoMoreThan7CardsInHand(HandInGameDTO hand ){
        // no puede pasar al siguiente jugador si tiene más de 7 cartas en la mano 
        if (hand.getCards().size() > 7 ){
            throw new MoreThan7CardsInHand("You cannot have more than 7 cards in your hand, you must discard or use them in your bag"); 
        }
    }

    public void checkWordIsValid(Boolean isValid){
        if (!isValid){
            throw new BagNotValidException("The word of the bag is not valid"); 
        }
    }

    public void checkCardsDrawnInTurn(Player player){
        if (player != null && player.getCardsDrawnInTurn() >= TOTAL_CARDS_TO_DRAW){
            throw new MoreThan7CardsDrawnException("You cannot draw more than " + TOTAL_CARDS_TO_DRAW + " cards in your turn"); 
        }

         

    }
     

    



    
}
