package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DTOs;


import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrawCardResultDTO {

    @NotNull
    private CardDTO card; 

    @NotNull
    private DeckInGameDTO deck; 

    @NotNull
    private HandInGameDTO hand; 


    public DrawCardResultDTO (Card card, DeckInGame deck, HandInGame hand){
        CardDTO cardDto = new CardDTO(card); 
        DeckInGameDTO deckDto = new DeckInGameDTO(deck); 
        HandInGameDTO handDto = new HandInGameDTO(hand); 

        this.card = cardDto; 
        this.deck=deckDto; 
        this.hand = handDto; 
    }
    
}
