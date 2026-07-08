package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

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

    public LoseAgainstNpcRequestDTO(Integer cardId, String fromWhere){
        this.cardId =cardId; 
        this.fromWhere = fromWhere; 
    }

    public LoseAgainstNpcRequestDTO(){}


}