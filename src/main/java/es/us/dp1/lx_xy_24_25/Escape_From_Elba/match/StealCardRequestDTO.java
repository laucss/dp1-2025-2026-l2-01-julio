package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StealCardRequestDTO {
    // Nullable when stealing from 'hand' to select randomly on server
    private Integer cardId;

    /**
     * Must be either "hand" or "bag" to indicate the source.
     */
    @NotNull
    private String fromWhere;
}
