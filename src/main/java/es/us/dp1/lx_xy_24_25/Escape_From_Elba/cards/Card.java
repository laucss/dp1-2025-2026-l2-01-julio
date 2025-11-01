package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Card extends BaseEntity{

    @NotNull
    private String frontImage; 

    @NotNull
    private String backImage; 

    @NotNull
    private String letter; 

    private Boolean isInDeck; 

    private Boolean isDiscarted; 

    private Boolean isInHand;
    
    private Boolean isInBag; 
    
}
