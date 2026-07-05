package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.DTOs;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.Vote;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.VoteValue;
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
