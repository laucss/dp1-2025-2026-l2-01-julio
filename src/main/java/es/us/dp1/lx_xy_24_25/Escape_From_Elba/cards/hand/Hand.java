package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand;

import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hand extends BaseEntity {

    @OneToOne 
    private Player playerOwner;

    // @OneToMany 
    // private Match match; 

    @OneToMany 
    private List<Card> cards; 
    

}
