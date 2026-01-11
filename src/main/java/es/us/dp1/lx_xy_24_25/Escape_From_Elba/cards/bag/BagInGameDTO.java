package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BagInGameDTO  {

    @NotNull
    private List<CardDTO> cards; 

    public BagInGameDTO(BagInGame bag){
        List<Card> cards = bag.getCards(); 
        List<CardDTO> newBag = new ArrayList<>(); 
        for (Card card : cards){
            newBag.add(new CardDTO(card)); 
        }
        this.cards=newBag; 
    }

    public BagInGameDTO(){}
    
}
