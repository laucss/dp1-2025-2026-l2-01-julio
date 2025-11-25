package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.validation.constraints.NotNull;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
    Player save(Player player);

    List<Player> findAll();

    @NotNull
    List<Player> findByUserId(Integer userId);
    
    //Buscamos el jugador de un usuario en una partida concreta
    @Query("SELECT p FROM Player p WHERE p.match.id = :matchId AND p.user.id = :userId")
    Optional<Player> findByMatchAndUser(Integer matchId, Integer userId);

    List<Player> findByMatchId(Integer matchId);

}
