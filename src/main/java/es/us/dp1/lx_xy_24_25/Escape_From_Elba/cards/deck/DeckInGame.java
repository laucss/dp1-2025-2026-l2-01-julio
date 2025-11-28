package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DeckInGame{

    /*
     * Vamos a usar DeckInGame para guardar las cartas de la baraja de la partida en memoria y no en la base de datos por:  
     * 1. Es más rápido, vamos a modificar mucho el estado de la baraja del jugador y hacer tantas llamadas a la BD es más lento
     * 2. No vamos a necesitar guardar tras finalizar la partida ningún dato del mazo, por lo que no necesito usar un repository. 
     */

    @OneToMany
    private List<Card> notDiscardedCards; 

    @OneToMany
    private List<Card> discardedCards; 

    public DeckInGame (List<Card> cards) {
        if (cards ==null){
            this.notDiscardedCards = new ArrayList<>();

            this.discardedCards = new ArrayList<>();

        } else {
            this.notDiscardedCards = new ArrayList<>(cards);

            this.discardedCards = new ArrayList<>();
        }

    }
    public DeckInGame(){}
    
}
