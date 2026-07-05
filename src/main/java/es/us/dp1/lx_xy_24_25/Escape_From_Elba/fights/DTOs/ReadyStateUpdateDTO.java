package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadyStateUpdateDTO {
    private Integer matchId;
    private Integer playerId;
    private String playerRole; // "ATTACKER" or "DEFENDER"
    private Boolean isReady;

    public ReadyStateUpdateDTO() {}

    public ReadyStateUpdateDTO(Integer matchId, Integer playerId, String playerRole, Boolean isReady) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.playerRole = playerRole;
        this.isReady = isReady;
    }

    
}
