package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@Transactional
public class VotingRepositoryTest {

    @Autowired
    private VotingRepository votingRepository;

    /* 
     *  ----------------- findById -----------------
     */

    @Test
    public void findByIdNonExistingReturnsEmpty() {
        Optional<Voting> voting = votingRepository.findById(999);
        assertTrue(voting.isEmpty());
    }

    @Test
    public void findByIdReturnsVoting() {
        Voting voting = new Voting();
        voting.setMatchId(1);
        voting.setStatus(VotingStatus.PENDING);
        votingRepository.save(voting);

        Optional<Voting> result = votingRepository.findById(voting.getId());

        assertTrue(result.isPresent());
        assertEquals(voting.getId(), result.get().getId());
    }


    /* 
     * ----------------- findAll -----------------
     */

    @Test
    public void findAllReturnsAllVotings() {
        Voting v1 = new Voting();
        v1.setMatchId(1);
        v1.setStatus(VotingStatus.PENDING);

        Voting v2 = new Voting();
        v2.setMatchId(2);
        v2.setStatus(VotingStatus.FINISHED);

        votingRepository.save(v1);
        votingRepository.save(v2);

        List<Voting> votings = votingRepository.findAll();

        assertEquals(2, votings.size());
    }



    /* 
     * ----------------- findPendingVotingByMatchId -----------------
     */

    // Match id no existe -> devuelve empty
    @Test
    public void findPendingVotingByMatchIdNonExistingReturnsEmpty() {
        Optional<Voting> voting =
                votingRepository.findPendingVotingByMatchId(999);

        assertTrue(voting.isEmpty());
    }

    // caso positivo, devuelve voting con matchid existente y status PENDING
    @Test
    public void findPendingVotingByMatchIdReturnsVoting() {
        Voting voting = new Voting();
        voting.setMatchId(10);
        voting.setStatus(VotingStatus.PENDING);
        votingRepository.save(voting);

        Optional<Voting> result =
                votingRepository.findPendingVotingByMatchId(10);

        assertTrue(result.isPresent());
        assertEquals(10, result.get().getMatchId());
        assertEquals(VotingStatus.PENDING, result.get().getStatus());
    }

    // que no devuelva voting si el status no es PENDING
    @Test
    public void findPendingVotingByMatchIdIgnoresNonPending() {
        Voting voting = new Voting();
        voting.setMatchId(20);
        voting.setStatus(VotingStatus.FINISHED);
        votingRepository.save(voting);

        Optional<Voting> result =
                votingRepository.findPendingVotingByMatchId(20);

        assertTrue(result.isEmpty());
    }


     /* 
     * ----------------- findByMatchId -----------------
     */

    // caso positivo, devuelve votings asociados con matchid existente
    @Test
    public void findByMatchIdReturnsAllMatchVotings() {
        Voting v1 = new Voting();
        v1.setMatchId(30);
        v1.setStatus(VotingStatus.PENDING);

        Voting v2 = new Voting();
        v2.setMatchId(30);
        v2.setStatus(VotingStatus.FINISHED);

        votingRepository.save(v1);
        votingRepository.save(v2);

        List<Voting> votings =
                votingRepository.findByMatchId(30);

         assertEquals(2, votings.size());
    }

    // que devuelva vacío si no existen matches con estos ids
    @ParameterizedTest
    @ValueSource(ints = { 100, 200, 300 })
    public void findByMatchIdWithRandomIdsReturnsEmpty(Integer matchId) {
        List<Voting> votings =
                votingRepository.findByMatchId(matchId);

        assertTrue(votings.isEmpty());
    }
    
}
