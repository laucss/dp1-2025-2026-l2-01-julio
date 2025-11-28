package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Bag extends BaseEntity{

    @OneToOne 
    private Player playerOwner;

    // @OneToMany 
    // private Match match; 

    @OneToMany  
    private List<Card> cards; 
 

    /*
     * Vamos a leer carta por carta, obteniendo su letra para sacar la palabra
     * que compone la bolsa
     */

     /*
      
    public String getWordOfBag (){
        // podemos hacer un ford o un stream si se quiere que sea más limpio 
    }
      */
    
    
}
