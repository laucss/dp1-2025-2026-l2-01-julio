package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Vote extends BaseEntity {

    private Integer playerId; 

    private VoteValue inFavor; 

    @ManyToOne()
    private Voting voting;
    
    /* 
    @Version
    private Integer version; ??????????????
    
    */

    public Vote(){}

    public Vote(Integer playerId, Voting voting) {
        this.playerId = playerId;
        this.voting = voting;
    }


    
}
