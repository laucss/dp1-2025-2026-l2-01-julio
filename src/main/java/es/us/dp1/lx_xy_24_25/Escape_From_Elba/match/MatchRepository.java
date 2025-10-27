package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<Match, Integer> {
    List<Match> findAll();
    List<Match> findByName(String name);
    
}
