package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardRepository;


@Service
public class DeckService {

    CardRepository cardRepository;

    @Autowired
    public DeckService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    /*
     * Método para hacer una copia de la baraja original de la base de datos y barajarla para iniciar el mazo al empezar una partida
     */

    @Transactional(readOnly = true)
    public Deck initializeDeck() {
        List<Card> cartasOriginales = cardRepository.findAll();

        List<Card> cartasCopiadas = cartasOriginales.stream().map(carta -> carta.getClone()).toList(); 

        // barajamos las cartas ya copiadas
        Collections.shuffle(cartasCopiadas);

        return new Deck(cartasCopiadas); 

    }

    /*
     * Método para una vez, acabadas las cartas para robar, coja las descartadas, las baraje de nuevo y las devuelva al mazo de robar
     */
    

    /*
     * Método que añade una carta al mazo de descartes
     */

    /*
     * Método que añade VARIAS cartas al mazo de descartes
     */


    /*
     * Método que devuelve (y quita del mazo de descarte) la última carta que haya sido descartada
     */


    /*
     * Método que comprueba si el mazo de cartas no descartadas está vacío 
     */


    /*
     * CONTEXTO: cada jugador debe empezar con 3 cartas al inicio de la partida 
     * Método que saca las 3 primeras cartas del mazo 
     */
}
