package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.patterns.Prototype;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cards")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Card extends BaseEntity implements Prototype<Card> {

    @NotNull
    private String frontImage; 

    @NotNull
    private String backImage; 

    @NotNull
    private String letter; 

    public Card(){}

    
    /*
     * Constructor para poder hacer una copia de carta y aplicar el patron Prototipe
     */

    public Card(Card carta){
        this.backImage = carta.backImage;
        this.frontImage = carta.frontImage;
        this.letter = carta.letter;
    }

    @JsonIgnore
    public Card getClone() {
        return new Card(this); 
    }

}
