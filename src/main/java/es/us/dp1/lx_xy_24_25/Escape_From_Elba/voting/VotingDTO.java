package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotingDTO {
    private Integer matchId;
    private String weaponProposed;
    private VotingStatus status;
    private VotingResult result;
    private Integer votesInFavor;
    private Integer votesAgainst;
    private Integer finalBonus;
    private List<VoteDTO> votes;

    public VotingDTO(Voting voting){
        this.matchId = voting.getMatchId();
        this.weaponProposed = voting.getWeaponProposed();
        this.status = voting.getStatus();
        this.result = voting.getResult();
        this.votesInFavor = voting.getVotesInFavor();
        this.votesAgainst = voting.getVotesAgainst();
        this.finalBonus = voting.getFinalBonus();
        this.votes = voting.getVotes().stream().map(vote -> new VoteDTO(vote)).toList();
    }

    public VotingDTO(){}
}
