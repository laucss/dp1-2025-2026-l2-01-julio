package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckInGameDTO {

    @NotNull
    List<CardDTO> notDiscardedCards; 

    @NotNull
    List<CardDTO> discardedCards;
    
    public DeckInGameDTO(DeckInGame deck){
        
        List<CardDTO> newNotDiscardedCards = new ArrayList<>(); 
        List<CardDTO> newDiscardedCards = new ArrayList<>(); 
       
        for (Card card : deck.getNotDiscardedCards()){
            newNotDiscardedCards.add(new CardDTO(card)); 
        }

        for (Card card : deck.getDiscardedCards()){
            newDiscardedCards.add(new CardDTO(card)); 
        }

        this.discardedCards=newDiscardedCards; 
        this.notDiscardedCards=newNotDiscardedCards; 
    }

    public DeckInGameDTO(){}

    
    
}
