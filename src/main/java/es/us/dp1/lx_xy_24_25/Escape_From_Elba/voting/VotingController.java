package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/voting")
public class VotingController {

    VotingService votingService;

    @Autowired
    public VotingController(VotingService votingService){
        this.votingService=votingService;
    }

    @GetMapping("/{matchId}")
    public List<VotingDTO> getAllVoting(@PathVariable Integer matchId){
        return votingService.getVotingsByMatchId(matchId);
    }

    
    @PostMapping("/vote/{matchId}")
    public ResponseEntity<VotingDTO> submitVote (@PathVariable Integer matchId, @RequestBody VoteDTO vote){
        VotingDTO voting = votingService.submitVote(matchId, vote);
        return ResponseEntity.ok(voting);
    }


    
}
