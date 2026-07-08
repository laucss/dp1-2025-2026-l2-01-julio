package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
// import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;

@Service
public class HandService {

    Checkers checkers; 
    //PlayerService playerService; 

    public HandService(Checkers checkers){
        this.checkers = checkers; 
        //this.playerService = playerService; 
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

        Map<Integer, HandInGame> playerMap = activesHands.computeIfAbsent(matchId, m -> new HashMap<>());

        // Solo añadimos la mano si no existe
        playerMap.putIfAbsent(playerId, new HandInGame());
        
    }
    /*
     * Método que tras acabar una partida, borra la mano en memoria del jugador 
     */

    public void deleteMatchHands(Integer matchId){
        //playerService.findById(playerId); 

        activesHands.remove(matchId);  
    }

    /*
     * Método que busca la HandInGame del jugador en la partida dada
     */

    @Transactional(readOnly = true)
    public HandInGame findPlayerHand(Integer matchId, Integer playerId){
        Map<Integer, HandInGame> playerMap = activesHands.get(matchId);
        if (playerMap == null) {
            return new HandInGame(); // devuelvo una lista vacía por si acaso da error porque no se carga hasta que se le da a empezar partida 
        }
        
        HandInGame playerHand = playerMap.get(playerId); 

        if (playerHand == null) { // devuelvo una lista vacía por si acaso da error porque no se carga hasta que se le da a empezar partida 
            return new HandInGame(); 
        }

        return playerHand; 
    }

    public Map<Integer, HandInGame> findPlayerMap (Integer matchId, Integer playerId){
        return activesHands.get(matchId); 
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
    public HandInGame addCardToPlayerHand(Card card, Integer matchId, Integer playerId){
        HandInGame playerHand = findPlayerHand(matchId, playerId); 

        playerHand.getCards().add(card); 

        activesHands
            .computeIfAbsent(matchId, m -> new HashMap<>())
            .put(playerId, playerHand);

        return playerHand; 

    }

    /*
     * Método que quita una carta de la mano del jugador 
     */

    @Transactional
    public Card removeCardFromPlayerHand(Card card, Integer matchId, Integer playerId){
        if (card == null) {
            return null;
        }
        
        HandInGame playerHand = findPlayerHand(matchId, playerId); 
        List<Card> playerCards = playerHand.getCards(); 
        Card removedCard = null; 
        
        // Buscar por referencia exacta primero (más confiable para objetos en memoria)
        for (int i = 0; i < playerCards.size(); i++){
            if (playerCards.get(i) == card) {
                removedCard = playerCards.get(i); 
                playerCards.remove(i);
                break; 
            }
        }
        
        // Si no se encontró por referencia, buscar por ID o letra
        if (removedCard == null) {
            for (int i = 0; i < playerCards.size(); i++){
                Card c = playerCards.get(i);
                boolean match = false;
                
                // Comparar por ID si ambos tienen ID
                if (card.getId() != null && c.getId() != null && card.getId().equals(c.getId())) {
                    match = true;
                }
                // Si no hay ID, comparar por letra
                else if (card.getLetter() != null && card.getLetter().equals(c.getLetter())) {
                    match = true;
                }
                
                if (match) {
                    removedCard = playerCards.get(i); 
                    playerCards.remove(i);
                    break; 
                }
            }
        }
        
        // Persist the updated hand back to the map
        activesHands
            .computeIfAbsent(matchId, m -> new HashMap<>())
            .put(playerId, playerHand);
        
        return removedCard; 

    }
    
    /*
     * Método que mete varias cartas a la mano del jugador 
     */

    public void addFewCardsToPlayerHand (Integer matchId, Integer playerId, List<Card> cards ){
       Map<Integer, HandInGame> playerMap = activesHands.get(matchId); 
       HandInGame playerHand= findPlayerHand(matchId, playerId); 

       for (Card card : cards){
        playerHand.getCards().add(card); 
       }

       playerMap.replace(playerId, playerHand); 
       activesHands.replace(matchId, playerMap); 
    }

    public void update(HandInGameDTO hand, Integer matchId, Integer playerId){

        //checkear que exista el player y tal 

        Map<Integer, Map<Integer, HandInGame>> allHands1 = activesHands;
        Map<Integer, HandInGame> playerMap = activesHands.get(matchId);
        
        HandInGame newHand = new HandInGame(); 

        // tengo que hacer lo de newArrayList<>(hand...) dentro del set porque si no se hace así ç
        // y le paso el stream con el tolist directamente, lo entiende como una lista inmutable y daría error 
        newHand.setCards(new ArrayList<>(hand.getCards().stream()
            .map(dto -> new Card(dto.getId(),dto.getFrontImage(), dto.getBackImage(), dto.getLetter())).toList()));

        Map<Integer, Map<Integer, HandInGame>> allHands2 = activesHands;
        playerMap.put(playerId,newHand); 

        activesHands.put(matchId, playerMap); 

    }

    public Map<Integer, Map<Integer, HandInGame>> getActivesHands() {
        return activesHands;
    }
    
}
