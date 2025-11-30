package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;



@Service
public class BagService {
    
    RestTemplate restTemplate;
    Checkers checkers;
    PlayerService playerService; 

    public BagService(Checkers checkers, PlayerService playerService, RestTemplate restTemplate){
        this.checkers = checkers; 
        this.playerService = playerService; 
        this.restTemplate=restTemplate; 
    }

    /*
     * Variable que vamos a usar a modo de "almacenamiento" en memoria del estado de la bolsa
     * Map de la forma: 
     * Map<MatchId, Map<PlayerId, BagInGame>> 
     */
    private final Map<Integer, Map<Integer, BagInGame>> activesBags = new HashMap<>(); 


    public Map<Integer, Map<Integer, BagInGame>> getActivesBags() {
        return activesBags;
    }
    /*
     * Método que crea un BagInGame para un jugador
     */
    @Transactional
    public void createPlayerbag(Integer matchId, Integer playerId){
        // TODO: revisar si tengo que checkear que match exista

        Map<Integer, BagInGame> playerMap = activesBags.computeIfAbsent(matchId, m -> new HashMap<>());

        // Solo añadimos la mano si no existe
        playerMap.putIfAbsent(playerId, new BagInGame());
        

    }

    /*
     * Método que tras acabar una partida, borra la bolsa en memoria del jugador 
     */
    @Transactional
    public void deleteMatchBags(Integer matchId){
        //playerService.findById(playerId); 
        // TODO: revisar si tengo que checkear que match exista

        activesBags.remove(matchId); 
    }

    

    /*
     * Método que busca y devuelve el BagInGame del jugador
     */

    @Transactional (readOnly = true)
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

    @Transactional 
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
     * Método que recibe un array de cartas y saca la palabra que forman
     */

    @Transactional 
    public String wordFromCards (List<CardDTO> cards) {
        StringBuilder word = new StringBuilder();
        for (CardDTO card : cards){
            word.append(card.getLetter()); 
        }
        return word.toString();
    }



    /*
     * Valida si la palabra que forma el usuario es valida para guardarse en la bolsa
     * llamando a una api de diccionario si tiene más de letras
     * Despues, guarda y actualiza la nueva bolsa del juagador
     */

 
    @Transactional 
    public Boolean isValidWordForBag (String word) {

        // hay que mirar a ver cómo hacer para que tampoco acepte nombres propios 
      
        if (word.length() >= 3){

            // hacer llamada a la api de free dictionary api: 
            // https://api.dictionaryapi.dev/api/v2/entries/en/<word>
            // hay otra opción de api que es https://www.wordsapi.com/  aunque creo que es un poco menos completa pero ns 

            String url = "https://api.dictionaryapi.dev/api/v2/entries/en/"+ word; 

            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class); 

                return response.getStatusCode().is2xxSuccessful(); // devuelve true
        
            } 
            catch (HttpClientErrorException e){
                return false; 
                
            } catch (Exception e) {
                // TODO: handle exception en el validadr bag
                return false; 
            }}
        return true; 
        

    }

    /*
     * Método que realiza la checkeación completa del array de cartas que manda el frontend 
     * (la unión de las dos funciones anteriores, básicamente)
     */

    @Transactional 
    public Boolean checkBagIsValid (List<CardDTO> cards){

        String word = wordFromCards(cards); 
        return isValidWordForBag(word); 

    }


    /*
     * Método que checkea si palabra formada forma parte de la lista de armas ofrecida por el juego 
     */
    
    


    
    public void update(BagInGameDTO bag, Integer matchId, Integer playerId){

        //checkear que exista el player y tal 

        BagInGame newBag = new BagInGame(); 

        //newBag.setCards(bag.getCards());


        Map<Integer, BagInGame> playerMap = activesBags.get(matchId);
        newBag.setCards(new ArrayList<>(bag.getCards().stream()
            .map(dto -> new Card(dto.getId(),dto.getFrontImage(), dto.getBackImage(), dto.getLetter())).toList()));

        playerMap.put(playerId, newBag); 
        activesBags.put(matchId, playerMap); 

    }
}
