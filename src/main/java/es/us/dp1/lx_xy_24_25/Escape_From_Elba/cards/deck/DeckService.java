package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;


@Service
public class DeckService {

    /*
     * Variable que vamos a usar a modo de "almacenamiento" del estado de la baraja, de los mazos. Es decir, vamos a usar un map
     * en el que la clave sea el Id de la partida y el valor, la baraja en sí -> DeckInGame (guardada en memoria)
     */
    private final Map<Integer, DeckInGame> activesDecks = new HashMap<>(); 

    public CardRepository cardRepository;
    public Checkers checkers;

    @Autowired
    public DeckService(CardRepository cardRepository, Checkers checkers) {
        this.cardRepository = cardRepository;
        this.checkers = checkers; 
    }

    // VARIABLES DE LOGICA DE NEGOCIO 
    private static final Integer numberOfInitialCards = 3; 

    /*
     * Método para hacer una copia de la baraja original de la base de datos y barajarla para iniciar el mazo al empezar una partida
     */

    @Transactional(readOnly = true)
    public DeckInGame initializeDeck(Integer macthId) {
        // TODO: revisar si debería checkear que el match exista, creo que no pq no da tiempo pero bueno
        List<Card> originalsCards = cardRepository.findAll();

        /*
         * lo creo que de esta manera, con el new ArrayList antes para que se cree una lista mutable
         * y se pueda hacer el shuffle. Si no le pongo eso delante, da error porque lo trata como una lista inmutable
         */
        List<Card> copiedCards = new ArrayList<>(
                originalsCards.stream().map(Card::getClone).toList());
        
        Collections.shuffle(copiedCards); 

        DeckInGame newDeck = new DeckInGame(copiedCards); 
        activesDecks.put(macthId, newDeck); 
        return newDeck; 

    }

    public Map<Integer, DeckInGame> getActivesDecks() {
        return activesDecks;
    }

    /*
     * Método que tras acabar una partida, borra la baraja en memoria de la partida 
     */

    @Transactional
    public void deleteDeckInGame(Integer matchId){ 
        // TODO: revisar si tengo que checkear que match exista
        activesDecks.remove(matchId); 
    }

    /* 
     * Método para buscar el mazo por su id 
     *
    */

    @Transactional(readOnly = true)
    public DeckInGame findDeckById(Integer macthId) {
        DeckInGame deck = activesDecks.get(macthId); 
        if (deck == null){
            return new DeckInGame(); 
        }
        return deck;   
    }

     
   

    /*
     * Método que una vez, acabadas las cartas para robar, coge las descartadas, las baraja de nuevo y las devuelve al mazo de robar
     * No hemos puesto que se quede al menos una carta en la pila de descarte pues el juego no lo contempla.  
     */

    
    @Transactional
    public DeckInGame shuffleAndDicardedToNotDiscarded(DeckInGame deck){
        // checkear si quedan menos de x cartas en vez de cero?


        List<Card> discardedCards = deck.getDiscardedCards();
        Collections.shuffle(discardedCards);

        deck.setNotDiscardedCards(discardedCards);

        deck.setDiscardedCards(new ArrayList<Card>()); 
        
        return deck; 
    }
    
    /*
     * Método que roba la primera carta del mazo de robar
     */

    @Transactional
    public Card drawCard(Integer matchId){
        DeckInGame deck = findDeckById(matchId); 
        if (deck.getNotDiscardedCards().isEmpty()) {
            deck = shuffleAndDicardedToNotDiscarded(deck); 
        }

        Card card = deck.getNotDiscardedCards().getLast();
        deck.getNotDiscardedCards().removeLast(); 
        activesDecks.put(matchId, deck); // actualizo la baraja 
        return card; 
        
    }

    /*
     * Método que añade una carta al mazo de descartes
     */

    @Transactional
    public void addCardToDiscardedPile (Integer macthId, Card card){
    
        DeckInGame deck = findDeckById(macthId);

        List<Card> discardedCards = deck.getDiscardedCards();

        checkers.checkCardExists(card);
        
        discardedCards.add(card); 
    }


    /*
     * Método que devuelve (y quita del mazo de descarte) la última carta que haya sido descartada
     
    */

    @Transactional
    public Card getAndRemoveLastDiscardedCard (Integer macthId){

        DeckInGame deck = findDeckById(macthId);

        List<Card> discardedCards = deck.getDiscardedCards();
        if (discardedCards.isEmpty()){
            return null; 
        } else {
            return discardedCards.removeLast(); 
        }
        
    }
     
    

    /*
     * Método que comprueba si el mazo de cartas no descartadas está vacío 
     */

    @Transactional(readOnly = true)
    public Boolean isEmpty (Integer macthId) { // (Integer deckId)
        DeckInGame deck = findDeckById(macthId);
        return deck.getNotDiscardedCards().isEmpty();  
    }

    /*
     * CONTEXTO: cada jugador debe empezar con 3 cartas al inicio de la partida 
     * Método que saca las 3 primeras cartas del mazo 
     */

    @Transactional
    public List<Card> drawInitialCardsFromDeck (Integer matchId){
        List<Card> cards = new ArrayList<>(); 
        DeckInGame deck = findDeckById(matchId); 
        // TODO: REVISAR SI ES MEJOR HACER UNA COPIA DE DECK 

        for (int i = 0; i<numberOfInitialCards; i++) {
            cards.add(deck.getNotDiscardedCards().getLast()); 
            deck.getNotDiscardedCards().removeLast(); 
        }
        
        // actualizamos la baraja en memoria 
        activesDecks.replace(matchId, deck); 

        return cards;  
    }



    public void update(DeckInGameDTO deck, Integer matchId){

        //checkear que exista el player y tal 

        DeckInGame newDeck = activesDecks.get(matchId); 

        // tengo que hacer lo de newArrayList<>(deck...) dentro del set porque si no se hace así ç
        // y le paso el stream con el tolist directamente, lo entiende como una lista inmutable y daría error 
        newDeck.setDiscardedCards(new ArrayList<>(deck.getDiscardedCards().stream()
            .map(dto -> new Card(dto.getId(), dto.getFrontImage(), dto.getBackImage(), dto.getLetter())).toList()));
        
        newDeck.setNotDiscardedCards(new ArrayList<>(deck.getNotDiscardedCards().stream()
            .map(dto -> new Card(dto.getId(),dto.getFrontImage(), dto.getBackImage(), dto.getLetter())).toList()));

        DeckInGame d = findDeckById(matchId); 
        System.out.println(d);


    }
}






