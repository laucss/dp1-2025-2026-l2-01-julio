package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardRepository;
// import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;

@Service
public class DeckService {

    CardRepository cardRepository;
    // DeckRepository deckRepository; 
    /*
    
    @Autowired
    public DeckService(CardRepository cardRepository, DeckRepository deckRepository ) {
        this.cardRepository = cardRepository;
        this.deckRepository = deckRepository;
    }

    */

    @Autowired
    public DeckService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    

    /*
     * Método para hacer una copia de la baraja original de la base de datos y barajarla para iniciar el mazo al empezar una partida
     */

    @Transactional(readOnly = true)
    public Deck initializeDeck() {
        List<Card> originalsCards = cardRepository.findAll();

        List<Card> copiedCards = originalsCards.stream().map(carta -> carta.getClone()).toList(); 

        // barajamos las cartas ya copiadas
        Collections.shuffle(copiedCards);

        return new Deck(copiedCards); 

    }

    /* 
     * Método para buscar el mazo por su id 
     * 
    public Deck findDeckById(Integer deckId) {
        return deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("Deck", "id", deckId));
    }

     */
   

    /*
     * Método para una vez, acabadas las cartas para robar, coja las descartadas, las baraje de nuevo y las devuelva al mazo de robar
     */

    /*
    
    public Deck shuffleAndDicardedToNotDiscarded(Integer deckId){
        // IF IS EMPTY!

        // buscamos baraja
        Deck deck = findById(deckId);

        List<Card> discardedCards = deck.getDiscardedCards();
        Collections.shuffle(discardedCards);

        deck.setNotDiscardedCards(discardedCards);

        deck.setDiscardedCards(new ArrayList<Card>()); 

        return deck; 
    }
     */
    

    /*
     * Método que añade una carta al mazo de descartes
     */

    /*
    public void addCardToDiscardedPile (Integer deckId, Card card){
    
        // buscamos baraja
        Deck deck = findById(deckId);

        // cogemos pila de descartes 
        List<Card> discardedCards = deck.getDiscardedCards();

        //añadimos la carta al final 
        discardedCards.add(card); 
    }

     */

    

    /*
     * Método que añade VARIAS cartas al mazo de descartes
     */

    /*
    public void addCardToDiscardedPile (Integer deckId, List<Card> cards){
    
        // buscamos baraja
        Deck deck = findById(deckId);

        // cogemos pila de descartes 
        List<Card> discardedCards = deck.getDiscardedCards();

        //añadimos la carta al final
        for (const card in cards){
            discardedCards.add(card);
        } 
         
    }

    */

    /*
     * Método que devuelve (y quita del mazo de descarte) la última carta que haya sido descartada
     
    public Card drawLastDiscardedCard (Integer deckId){

        // buscamos baraja
        Deck deck = findById(deckId);

        // cogemos pila de descartes 
        List<Card> discardedCards = deck.getDiscardedCards();

        //devolvemos la última carta 
        return discardedCards[-1]; 
    }
     
     */

    /*
     * Método que comprueba si el mazo de cartas no descartadas está vacío 
     */

    public Boolean isEmpty (Deck deck) { // (Integer deckId)
        // Deck deck = findById(deckId);
        return deck.getNotDiscardedCards().isEmpty();  
    }

    /*
     * CONTEXTO: cada jugador debe empezar con 3 cartas al inicio de la partida 
     * Método que saca las 3 primeras cartas del mazo 
     */
}






