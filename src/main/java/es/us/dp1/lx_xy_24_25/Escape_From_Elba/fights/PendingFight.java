package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PendingFight extends BaseEntity{
    
    private Integer matchId;

    private Integer attackerUserId; 

    private Integer defenserUserId; 

    private Integer roomId; 
    
    private boolean isNpcFight; 

    private boolean isNpcAttacker;

    public PendingFight(){}

    public PendingFight( Integer matchId, Integer attackerUserId,Integer defenserUserId, Integer roomId , boolean isNpcFight, boolean isNpcAttacker ){
        this.attackerUserId = attackerUserId; 
        this.defenserUserId = defenserUserId; 
        this.matchId = matchId; 
        this.roomId = roomId; 
        this.isNpcFight = isNpcFight; 
        this.isNpcAttacker = isNpcAttacker; 
    }
    
}
