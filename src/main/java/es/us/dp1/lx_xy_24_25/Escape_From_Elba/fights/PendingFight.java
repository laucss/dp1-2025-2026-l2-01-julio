package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PendingFight extends BaseEntity{
    
    @NotNull
    private Integer matchId;

    @NotNull
    private Integer attackerUserId; 

    @NotNull
    private Integer defenserUserId; 

    @NotNull
    private Integer roomId; 
    
    @NotNull
    private boolean isNpcFight; 

    @NotNull
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
