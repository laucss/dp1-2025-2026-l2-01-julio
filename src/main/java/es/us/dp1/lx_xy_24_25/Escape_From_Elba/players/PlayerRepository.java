package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player save(Player player);

    Optional<Player> findById(Long userId);
    
    //Buscamos el jugador de un usuario en una partida concreta
    @Query("SELECT p FROM Player p WHERE p.match.id = :matchId AND p.user.id = :userId")
    Optional<Player> findByMatchAndUser(Integer matchId, Integer userId);

    @Query("SELECT p FROM Player p WHERE p.match.id = :matchId")
    java.util.List<Player> findByMatchId(Integer matchId);

}
