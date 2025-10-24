package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandRepository extends CrudRepository<Hand, Integer> {
    
}
