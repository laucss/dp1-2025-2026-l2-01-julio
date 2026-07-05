package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FightResultRequestDTO {

    private Integer matchId;

    private Integer attackerId; 

    private Integer defenderId; 

    // si está involucrado un npc 
    private boolean isNpcFight; 

    private boolean isNpcAttacker; 

    private boolean attackerWins; 

    // habitación donde se produce la pelea
    private Integer defenderRoomId;
    
    public FightResultRequestDTO(Integer attackerId, Integer defenderId, boolean isNpcFight, boolean isNpcAttacker, 
            boolean attackerWins, Integer defenderRoomId) {
        this.attackerId = attackerId;
        this.defenderId = defenderId;
        this.isNpcFight = isNpcFight;
        this.attackerWins = attackerWins;
        this.defenderRoomId = defenderRoomId;
        this.isNpcAttacker = isNpcAttacker; 
    }

    
}
