package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiceTotalsUpdateDTO {
    private Integer attackerId;
    private Integer attackerTotal;
    private Integer defenderId;
    private Integer defenderTotal;
    private Long timestamp;

    public DiceTotalsUpdateDTO() {}

    public DiceTotalsUpdateDTO(Integer attackerId, Integer attackerTotal, Integer defenderId, Integer defenderTotal, Long timestamp) {
        this.attackerId = attackerId;
        this.attackerTotal = attackerTotal;
        this.defenderId = defenderId;
        this.defenderTotal = defenderTotal;
        this.timestamp = timestamp;
    }

    
}
