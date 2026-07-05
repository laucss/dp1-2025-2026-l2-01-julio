package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;
//cambio para merge en FSS8078
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;


public interface UserRepository extends  CrudRepository<User, Integer>{

	List<User> findAll();
	
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);

	Optional<User> findById(Integer id);

	@Query("SELECT u FROM User u WHERE u.authority.authority = :auth")
	Iterable<User> findAllByAuthority(String auth);

	@Query("SELECT m FROM Match m WHERE m.creatorId = :userId")
	List<Match> findMatchesCreatedByUser(Integer userId);

	@Query("SELECT m FROM Match m WHERE m.winner.user.id = :userId")
	List<Match> findMatchesWonByUser(Integer userId);





}
