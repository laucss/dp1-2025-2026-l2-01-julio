package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;; 

@Repository
public interface CardRepository extends CrudRepository<Card, Integer>  {
    List<Card> findAll(); 

    Optional<Card> findById(Integer id); 
}
