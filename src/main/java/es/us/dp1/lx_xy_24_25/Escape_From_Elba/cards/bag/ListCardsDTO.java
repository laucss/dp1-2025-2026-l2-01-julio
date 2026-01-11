package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DTOs.CardDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListCardsDTO  {

    @NotNull
    List<CardDTO> cards; 
    
    public ListCardsDTO(BagInGame bag){
        List<Card> cards = bag.getCards(); 
        List<CardDTO> newList = new ArrayList<>(); 
        for (Card card : cards){
            newList.add(new CardDTO(card)); 
        }
        this.cards=newList; 
    }

    public ListCardsDTO(){}
}
