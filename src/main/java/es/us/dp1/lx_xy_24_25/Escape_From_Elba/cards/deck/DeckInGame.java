package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckInGame {

    List<Card> notDiscardedCards; 

    List<Card> discardedCards; 

    public DeckInGame (List<Card> cards) {
        this.notDiscardedCards = new ArrayList<>(cards);

        this.discardedCards = new ArrayList<>();

    }
}
