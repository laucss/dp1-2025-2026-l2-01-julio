package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDTO {

    private Integer playerId;

    private VoteValue inFavor;

    public VoteDTO(Vote vote){
        this.playerId = vote.getPlayerId();
        this.inFavor = vote.getInFavor();
    }

    public VoteDTO(){}
}
