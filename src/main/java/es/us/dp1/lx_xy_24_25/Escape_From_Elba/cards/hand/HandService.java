package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;

@Service
public class HandService {

    Checkers checkers; 
    PlayerService playerService; 

    public HandService(Checkers checkers, PlayerService playerService){
        this.checkers = checkers; 
        this.playerService = playerService; 
    }

    /*
     * Variable que vamos a usar a modo de "almacenamiento" en memoria de las manos. 
     * 
     * Vamos a tener un map de la siguiente forma: 
     * Map<MatchId, Map<PlayerId, HandInGame>> 
     */
    private final Map<Integer, Map<Integer, HandInGame>> activesHands = new HashMap<>(); 

    /*
     * Método que crea un HandInGame para un jugador
     */
    public void createPlayerHand(Integer matchId, Integer playerId){
        playerService.findById(playerId); 
        // TODO: revisar si tengo que checkear que match exista

        activesHands.putIfAbsent(matchId, new HashMap<>()); 
        HandInGame newHand = new HandInGame();

        Map<Integer, HandInGame> playerMap = activesHands.get(matchId);
        playerMap.put(playerId, newHand); 

    }
    /*
     * Método que tras acabar una partida, borra la mano en memoria del jugador 
     */

    public void deletePlayerHand(Integer matchId, Integer playerId){
        playerService.findById(playerId); 
        // TODO: revisar si tengo que checkear que match exista

        Map<Integer, HandInGame> playerMap= activesHands.get(matchId); 
        playerMap.remove(playerId); 
    }

    /*
     * Método que busca la HandInGame del jugador en la partida dada
     */

    @Transactional(readOnly = true)
    public HandInGame findPlayerHand(Integer matchId, Integer playerId){
        Map<Integer, HandInGame> playerMap = activesHands.get(matchId); 
        HandInGame playerHand = playerMap.get(playerId); 

        if (playerHand == null) {
            throw new ResourceNotFoundException("The player hands was not found or does not exits"); 
        }

        return playerHand; 
    }
    /*
     * Método que pasa la carta de la mano del jugador a su bolsa 
     * o método que quita una carta de la mano del jugador 
     */


    /*
     * Método para pasar la carta del mazo de robar a la mano del jugador que robe 
     * O método que añade una carta a la mano 
     */
    
    @Transactional
    public void addCardToPlayerHand(Card card, Integer matchId, Integer playerId){
        HandInGame playerHand = findPlayerHand(matchId, playerId); 

        checkers.checkCardExists(card);

        playerHand.getCards().add(card); 
    }

    /*
     * Método que quita una carta de la mano del jugador 
     */


    @Transactional
    public Card removeCardFromPlayerHand(Card card, Integer matchId, Integer playerId){
        checkers.checkCardExists(card);
        
        HandInGame playerHand = findPlayerHand(matchId, playerId); 
        List<Card> playerCards = playerHand.getCards(); 
        Card removedCard = null; 
        
        for (int i=0; i<playerCards.size(); i++){
            if (playerCards.get(i).equals(card)){
                removedCard = playerCards.get(i); 
                playerCards.remove(i); 
                break; 
            }
        }
        return removedCard; 

    }
    
    /*
     * Método que mete varias cartas a la mano del jugador 
     */

    public void addFewCardsToPlayerHand (Integer matchId, Integer playerId, List<Card> cards ){
       //TODO: REVISAR ESTA DOS LINEAS, NS SI SON REPETITIVAS
       Map<Integer, HandInGame> playerMap = activesHands.get(matchId); 
       HandInGame playerHand= findPlayerHand(matchId, playerId); 

       for (Card card : cards){
        playerHand.getCards().add(card); 
       }

       playerMap.replace(playerId, playerHand); 
       activesHands.replace(matchId, playerMap); 
    }
    
}
