package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagRepository extends CrudRepository <Bag, Integer> {
    
}
