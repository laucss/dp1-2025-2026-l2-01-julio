package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Deck extends BaseEntity{

    /*
     * Entidad que reprenta los mazos del juego en general (conjunto de cartas que no pertencen a ningún jugador)
     * Serían básicamente el mazo de dónde se roba y el mazo de cartas descartadas 
     */

    @OneToMany
    private List<Card> notDiscardedCards; 

    @OneToMany
    private List<Card> discardedCards; 

    /*
     * Constructor
     */
    public Deck (List<Card> cards) {
        this.notDiscardedCards = new ArrayList<>(cards);

        this.discardedCards = new ArrayList<>();

    }
    
}
