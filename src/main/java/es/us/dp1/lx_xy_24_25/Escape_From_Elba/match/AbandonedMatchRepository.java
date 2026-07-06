package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AbandonedMatchRepository extends CrudRepository<AbandonedMatch, Integer> {

    AbandonedMatch save(AbandonedMatch abandonedMatch);

    boolean existsByMatchId(Integer matchId);

    boolean existsByMatchIdAndUserId(Integer matchId, Integer userId);

  @Query("SELECT a.match FROM AbandonedMatch a WHERE a.user.id = :userId ORDER BY a.match.startTime DESC")
    Page<Match> findMatchesAbandonedByUser(Integer userId, Pageable pageable);

    

}
