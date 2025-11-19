package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class BagInGame {

    /*
     * El porqué del BagInGame es el mismo que el de DeckInGame y HandInGame. Si se quiere ver, ir a esas clases. 
     */
    
    @OneToOne
    private Player owner; 

    @OneToMany
    private List<Card> cards; 

    
    public BagInGame(List<Card> cards){
        this.cards= cards; 
    }

    public BagInGame(){ 
        this.cards= new ArrayList<>(); 
    }
}
