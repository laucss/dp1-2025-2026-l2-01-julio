package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HandInGameDTO  {

    @NotNull
    private List<CardDTO> cards; 

    public HandInGameDTO(HandInGame hand){
        List<Card> cards = hand.getCards(); 
        List<CardDTO> newHand = new ArrayList<>(); 
        for (Card card : cards){
            newHand.add(new CardDTO(card)); 
        }
        this.cards=newHand; 
    }

    public HandInGameDTO(){}
    
}
