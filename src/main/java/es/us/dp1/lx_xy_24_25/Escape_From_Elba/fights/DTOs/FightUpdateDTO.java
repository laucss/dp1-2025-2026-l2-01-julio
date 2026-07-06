package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FightUpdateDTO {
    private Integer matchId;
    private Integer attackerId;
    private String attackerUsername;
    private Integer defenderId;
    private String defenderUsername;
    private String roomName;
    private Integer roomId;
    private String action; // "START", "END"
    private Boolean isBot;

    public FightUpdateDTO() {}

    public FightUpdateDTO(Integer matchId,Integer attackerId,String attackerUsername,Integer defenderId,String defenderUsername,Integer roomId,String action,Boolean isBot) {

        this.matchId = matchId;
        this.attackerId = attackerId;
        this.attackerUsername = attackerUsername;
        this.defenderId = defenderId;
        this.defenderUsername = defenderUsername;
        this.roomId = roomId;
        this.action = action;
        this.isBot = isBot;
    }

    
}
