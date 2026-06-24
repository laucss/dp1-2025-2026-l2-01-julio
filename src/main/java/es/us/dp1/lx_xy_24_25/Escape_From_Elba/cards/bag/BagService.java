package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DictionaryService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.EmptyWeaponException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.VotingService;



@Service
public class BagService {

    private static final Integer MAX_LETTERS_ALLOWED = 2; // es el número máximo de letras que puedes guardar en tu bolsa sin tener que checkear que la palabra exista
    private static final Integer BONUS_WEAPONS = 1; // la bonificación que te llevas al formar un arma válida 

    RestTemplate restTemplate;
    Checkers checkers;
    PlayerService playerService; 
    DictionaryService dictionaryService;
    VotingService votingService;

    @Autowired
    public BagService(Checkers checkers, PlayerService playerService, DictionaryService dictionaryService, RestTemplate restTemplate, VotingService votingService){
        this.checkers = checkers; 
        this.playerService = playerService; 
        this.dictionaryService = dictionaryService;
        this.restTemplate=restTemplate; 
        this.votingService = votingService; 
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

        if ( playerMap == null ){
            return new BagInGame(); // devuelvo una lista vacía por si acaso da error porque no se carga hasta que se le da a empezar partida 
        }

        BagInGame playersBag = playerMap.get(playerId); 
        return playersBag; 
    }

    /*
     * Método que quita una carta de la bolsa del jugador 
     */

    @Transactional 
    public Card removeCardFromPlayerBag(Card card, Integer matchId, Integer playerId){
        if (card == null) {
            return null;
        }
        
        BagInGame playerBag = findPlayerBag(matchId, playerId); 
        List<Card> playerCards = playerBag.getCards(); 
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
     * Valida si la palabra que forma el usuario existe en el diccionario y por ende se puede guardar en la bolsa 
     * Primero se va a checkear si la palabra está en el diccionario local (DictionaryService) que hemos añadido con más de 100 palabras y si no está, 
     *          se hará una llamada a la API externa de free dictionary api. 
     * Se ha hecho lo del diccionario local para intentar reducir el número de llamadas a la API externa, 
     *          porque ya nos pasó que la api dejó de funcionar justo el día de la entrega y presentación del sprint. 
     * Despues, guarda y actualiza la nueva bolsa del jugador
     */

 
    @Transactional 
    public boolean doesWordExists (String word) {
      

        // hacer llamada a la api de free dictionary api: 
        // https://api.dictionaryapi.dev/api/v2/entries/en/<word>
        // hay otra opción de api que es https://www.wordsapi.com/  aunque creo que es un poco menos completa pero ns 
        
        if (dictionaryService.containsWord(word)){
            return true; 

        } else {
            String url = "https://api.dictionaryapi.dev/api/v2/entries/en/"+ word; 

            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class); 

                return response.getStatusCode().is2xxSuccessful(); // devuelve true
        
            } catch (HttpClientErrorException e){
                return false; 
                
            } catch (Exception e) {
                // TODO: handle exception en el validar bag
                return false; 
            }
        }
        
    }
        
    

    /*
     * Método que realiza la comprobación completa del array de cartas que manda el frontend de la bolsa 
     * (la unión de las dos funciones anteriores, básicamente)
     */

    @Transactional 
    public boolean checkBagIsValid (List<CardDTO> cards){

        // si la bolsa no está vacía hacemos la comprobación
        if (!cards.isEmpty()){
            if (cards.size()> MAX_LETTERS_ALLOWED){ // si tiene más de 2 letras, checkear en el diccionario
                String word = wordFromCards(cards); 
                return doesWordExists(word); 
            } else {
                return true; // si tiene 2 o menos letras, es válida automáticamente
            }
        }
        // si está vacía devolvemos true para que pueda seguir 
        return true;  

    }

    /*
     * Método al que se llama cuando se forma un arma y se quiere validar. 
     * Escenarios: 
     * - que se la palabra esté en la lista de armas dada por el juego -> es válida y se suma 1 punto 
     * - que no esté en la lista pero que sí exista la palabra en el diccionario -> se inicia una votación con el resto de participantes y se llega a un consenso
     * - que no exisa la palabra directamente en el diccionario -> petición inválida 
     */

    public WeaponValidationDTO validateWeapon(BagInGameDTO bag, Integer matchId){
        if (bag.getCards().isEmpty()){
            throw new EmptyWeaponException("Try putting a weapon or cancel the operation "); 
        } else {
            String word = wordFromCards(bag.getCards()); 
            boolean isOnTheList = isWeaponOnTheList(word); 
            if (isOnTheList){
                return new WeaponValidationDTO(word,BONUS_WEAPONS, ValidationWeaponStatus.VALID);
            } else {
                boolean existsInDictionary = doesWordExists(word);
                if (existsInDictionary){
                    votingService.startVoting(matchId, word, bag.getPlayerId()); 
                    return new WeaponValidationDTO(word,0, ValidationWeaponStatus.REQUIRES_VOTING);
                } else {
                    return new WeaponValidationDTO(word,0, ValidationWeaponStatus.INVALID);
                }
            }

        }
    }


    /*
     * Método que checkea si palabra formada es parte de la lista de armas ofrecida por el juego 
     */

    @Transactional 
    public boolean isWeaponOnTheList(String word){ 
      
        if (dictionaryService.isWeapon(word)){
            return true; 

        } else {
            return false;
        } 
    }


    /*
     * Método que checkea que el arma propuesta exista en el diccionario para que se pueda proceder a la votación 
     */
    @Transactional 
    public boolean checkProposedWeaponExists(List<CardDTO> cards){
        String word = wordFromCards(cards);
        return doesWordExists(word);
    }

    
    

    /*
     * Método que actualiza la bolsa del jugador dado 
     */
    @Transactional 
    public void update(BagInGameDTO bag, Integer matchId, Integer playerId){

        //checkear que exista el player y tal 

        BagInGame newBag = new BagInGame(); 

        Map<Integer, BagInGame> playerMap = activesBags.get(matchId);

            // tengo que hacer lo de newArrayList<>(deck...) dentro del set porque si no se hace así ç
        // y le paso el stream con el tolist directamente, lo entiende como una lista inmutable y daría error 
        newBag.setCards(new ArrayList<>(bag.getCards().stream()
            .map(dto -> new Card(dto.getId(),dto.getFrontImage(), dto.getBackImage(), dto.getLetter())).toList()));

        playerMap.put(playerId, newBag); 
        activesBags.put(matchId, playerMap); 

    }

    

    
}
