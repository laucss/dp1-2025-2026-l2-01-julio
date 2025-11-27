package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards;


import org.springframework.boot.context.config.ConfigDataEnvironmentUpdateListener;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmDiscardDTO {

    @NotNull
    private HandInGameDTO hand; 
    
    @NotNull
    private BagInGameDTO bag;

    @NotNull
    private DeckInGameDTO deck; 

    @NotNull
    private Integer playerId; 

    public ConfirmDiscardDTO(){}

    public ConfirmDiscardDTO(HandInGame hand, BagInGame bag, DeckInGame deck, Integer playerId){
        this.hand= new HandInGameDTO(hand); 
        this.bag = new BagInGameDTO(bag); 
        this.deck = new DeckInGameDTO(deck); 
        this.playerId=playerId;
    }

    public ConfirmDiscardDTO(HandInGameDTO hand, BagInGameDTO bag, DeckInGameDTO deck, Integer playerId){
        this.hand= hand; 
        this.bag = bag; 
        this.deck = deck; 
        this.playerId=playerId;
    }

    
}
