package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FightResolvedDTO {

    // si por ejemplo, gana contra npcs recibe directamente la carta
    private CardDTO card; 

    @NonNull
    @Enumerated(EnumType.STRING)
    private FightResultType fightResultType; 


    public FightResolvedDTO(Card card, FightResultType fightResultType){
        this.card = new CardDTO(card);
        this.fightResultType = fightResultType; 
    }

    public FightResolvedDTO(FightResultType fightResultType){
        this.fightResultType = fightResultType; 
    }


    
}
