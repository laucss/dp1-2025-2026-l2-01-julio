package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoseAgainstNpcRequestDTO {
    @NotNull
    private Integer cardId;

    @NotNull
    private String fromWhere;
}