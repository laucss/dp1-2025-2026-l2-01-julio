package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;



@Service
public class BagService {
    
    Checkers checkers;
    PlayerService playerService; 

    public BagService(Checkers checkers, PlayerService playerService){
        this.checkers = checkers; 
        this.playerService = playerService; 
    }

    /*
     * Método que crea un HandInGame para un jugador
     */
    public void createPlayerBag(Integer matchId, Integer playerId){
        playerService.findById(playerId); 
        // TODO: revisar si tengo que checkear que match exista


    }

    /*
     * Variable que vamos a usar a modo de "almacenamiento" en memoria del estado de la bolsa
     * Map de la forma: 
     * Map<MatchId, Map<PlayerId, BagInGame>> 
     */
    private final Map<Integer, Map<Integer, BagInGame>> activesBags = new HashMap<>(); 


    /*
     * Método que busca y devuelve el BagInGame del jugador
     */

    public BagInGame findPlayerBag(Integer matchId, Integer playerId){
        Map<Integer, BagInGame> playerMap = activesBags.get(matchId); 
        BagInGame playersBag = playerMap.get(playerId); 

        if ( playersBag == null){
            throw new ResourceNotFoundException("The players bag does not exist or is not found"); 
        }
        return playersBag; 
    }

    /*
     * Método que quita una carta de la bolsa del jugador 
     */

    public Card removeCardFromPlayerBag(Card card, Integer matchId, Integer playerId){
        checkers.checkCardExists(card);
        
        BagInGame playerBag = findPlayerBag(matchId, playerId); 
        List<Card> playerCards = playerBag.getCards(); 
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
     * Valida si la palabra que forma el usuario es valida para guardarse en la bolsa
     * llamando a una api de diccionario si tiene más de letras
     * Despues, guarda y actualiza la nueva bolsa del juagador
     */

    /*
    @Transactional 
    public Boolean isValidWordForBag (String word) {

        // hay que mirar a ver cómo hacer para que tampoco acepte nombres propios 
     
        if (word.length >= 3){

            // hacer llamada a la api de free dictionary api: 
            // https://api.dictionaryapi.dev/api/v2/entries/en/<word>
            
            // basicamente hacerle una llamada a esa api con la palabra y ver si devuelve una respuesta válida

            // hay otra opción de api que es https://www.wordsapi.com/  aunque creo que es un poco menos completa pero ns 

            
            BagRepository.save(bag); 
        
            }

    }
     */



    /*
     * Método que checkea si palabra formada forma parte de la lista de armas ofrecida por el juego 
     */
    
    
}
