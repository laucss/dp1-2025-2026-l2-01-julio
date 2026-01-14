package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, Integer> {

    Vote save(Vote vote); 

    Optional<Vote> findById(Integer id); 

    List<Vote> findAll();



    
}
