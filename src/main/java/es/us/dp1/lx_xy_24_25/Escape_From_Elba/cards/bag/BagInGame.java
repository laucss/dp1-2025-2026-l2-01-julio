package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BagInGame {

    /*
     * El porqué del BagInGame es el mismo que el de DeckInGame y HandInGame. Si se quiere ver, ir a esas clases. 
     */
    
    private Player owner; 

    private List<Card> cards; 


    public BagInGame(Player player, List<Card> cards){
        this.owner=player; 
        this.cards= cards; 
    }
    
}
