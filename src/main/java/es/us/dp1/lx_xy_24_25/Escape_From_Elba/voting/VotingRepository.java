package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VotingRepository extends CrudRepository<Voting, Integer> {

    Voting save(Voting voting); 

    Optional<Voting> findById(Integer id); 

    List<Voting> findAll();

    @Query("SELECT v FROM Voting v WHERE v.matchId = ?1 AND v.status = 'PENDING'")
    Optional<Voting> findPendingVotingByMatchId(Integer matchId);

    // ESTO ERA PARA HACER COMPROBACIONES probablemente luego no lo use
    @Query("SELECT v FROM Voting v WHERE v.matchId = ?1")
    List<Voting> findByMatchId(Integer matchId);

    
}
