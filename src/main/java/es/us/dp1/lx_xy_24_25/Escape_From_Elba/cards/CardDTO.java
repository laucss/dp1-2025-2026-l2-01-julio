
package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDTO {

    @NotNull
    private String frontImage; 

    @NotNull
    private String backImage; 

    @NotNull
    private String letter; 

    public CardDTO(Card card){
        this.backImage = card.getBackImage(); 
        this.frontImage = card.getFrontImage(); 
        this.letter = card.getLetter(); 
    }

    public CardDTO(){}
    
}


