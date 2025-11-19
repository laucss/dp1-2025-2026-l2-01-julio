package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public DeckService(CardRepository cardRepository, @Lazy Checkers checkers) {
        this.cardRepository = cardRepository;
        this.checkers = checkers; 
    }
    

    /*
     * Método para hacer una copia de la baraja original de la base de datos y barajarla para iniciar el mazo al empezar una partida
     */

    @Transactional(readOnly = true)
    public DeckInGame initializeDeck(Integer macthId) {
        // TODO: revisar si debería checkear que el match exista, creo que no pq no da tiempo pero bueno
        List<Card> originalsCards = cardRepository.findAll();
        //System.out.println("originalsCards: " + originalsCards);

        List<Card> copiedCards = new ArrayList<>(
                originalsCards.stream().map(Card::getClone).toList());
        
        Collections.shuffle(copiedCards); //TODO: ESTO DA ERROR

        DeckInGame newDeck = new DeckInGame(copiedCards); 
        activesDecks.put(macthId, newDeck); 
        return newDeck; 

    }

    /*
     * Método que tras acabar una partida, borra la baraja en memoria de la partida 
     */

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
        if (deck==null)
            throw new ResourceNotFoundException("This deck does not exist or is not found"); 
        return deck;   
    }

     
   

    /*
     * Método para una vez, acabadas las cartas para robar, coja las descartadas, las baraje de nuevo y las devuelva al mazo de robar
     */

    
    @Transactional
    public DeckInGame shuffleAndDicardedToNotDiscarded(Integer macthId){
        // checkear si quedan menos de x cartas en vez de cero 

        DeckInGame deck = findDeckById(macthId);

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
            throw new RuntimeException("No hay cartas para robar");
}

        Card card = deck.getNotDiscardedCards().getLast();
        deck.getNotDiscardedCards().removeLast(); 
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
     * Método que añade VARIAS cartas al mazo de descartes
     */

     /*

    public void addFewCardsToDiscardedPile (Integer macthId, List<Card> cards){
    
        DeckInGame deck = findDeckById(macthId);
 
        List<Card> discardedCards = deck.getDiscardedCards();

        for (const card in cards){
            checkers.checkCardExists(card);
            discardedCards.add(card);
        } 
         
    }

    */

    /*
     * Método que devuelve (y quita del mazo de descarte) la última carta que haya sido descartada
     
    */

    @Transactional
    public Card getAndRemoveLastDiscardedCard (Integer macthId){

        DeckInGame deck = findDeckById(macthId);

        List<Card> discardedCards = deck.getDiscardedCards();

        return discardedCards.remove(-1); 
    }
     
    

    /*
     * Método que comprueba si el mazo de cartas no descartadas está vacío 
     */

    @Transactional
    public Boolean isEmpty (Integer macthId) { // (Integer deckId)
        DeckInGame deck = findDeckById(macthId);
        return deck.getNotDiscardedCards().isEmpty();  
    }

    /*
     * CONTEXTO: cada jugador debe empezar con 3 cartas al inicio de la partida 
     * Método que saca las 3 primeras cartas del mazo 
     */
}






