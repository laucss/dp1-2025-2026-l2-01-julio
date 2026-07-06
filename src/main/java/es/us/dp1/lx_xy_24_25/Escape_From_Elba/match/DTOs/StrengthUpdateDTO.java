package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrengthUpdateDTO {
    private Integer playerId;
    private Integer userId;
    private String username;
    private Integer strength;
    private Integer npcId; // para actualizar en el caso de que el perdedor sea un npc (así actualizamos el panel automaticamente)
    private Long timestamp;

    public StrengthUpdateDTO() {}

    public StrengthUpdateDTO(Integer playerId, Integer userId, String username, Integer strength, Long timestamp) {
        this.playerId = playerId;
        this.userId = userId;
        this.username = username;
        this.strength = strength;
        this.timestamp = timestamp;
    }

    public StrengthUpdateDTO(Integer npcId, Integer strength, Long timestamp) {
        this.npcId = npcId;
        this.strength = strength;
        this.timestamp = timestamp;
    }



    @Override
    public String toString() {
        return "StrengthUpdateDTO{" +
                "playerId=" + playerId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", strength=" + strength +
                ", timestamp=" + timestamp +
                '}';
    }
}
