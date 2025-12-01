package es.us.dp1.lx_xy_24_25.Escape_From_Elba.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NpcPositionDTO {
    private Integer npcId;
    private Integer roomId;
    private Long timestamp;
}