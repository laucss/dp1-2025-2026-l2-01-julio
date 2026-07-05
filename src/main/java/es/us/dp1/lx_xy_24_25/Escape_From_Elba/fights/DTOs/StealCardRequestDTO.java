package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StealCardRequestDTO {

    private Integer loserId; 

    private Integer winnerId; 

    // Nullable when stealing from 'hand' to select randomly on server
    private Card card;

    /**
     * Must be either "hand" or "bag" to indicate the source.
     */
    @NotNull
    private String fromWhere;
}
