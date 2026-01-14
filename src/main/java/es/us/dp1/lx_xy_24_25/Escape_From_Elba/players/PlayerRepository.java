package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jakarta.validation.constraints.NotNull;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

    List<Player> findAll();

    Optional<Player> findById(Integer id);

    @NotNull
    List<Player> findByUserId(Integer userId);
    
    //Buscamos el jugador de un usuario en una partida concreta
    @Query("SELECT p FROM Player p WHERE p.match.id = :matchId AND p.user.id = :userId")
    Optional<Player> findByMatchAndUser(Integer matchId, Integer userId);

    List<Player> findByMatchId(Integer matchId);

    @Query("SELECT SUM(p.actionPoints) FROM Player p WHERE p.user.id = :userId")
    Integer getTotalAccionPointsByUser(Integer userId);

    @Query("SELECT SUM(p.battlesWon) FROM Player p WHERE p.user.id = :userId")
    Integer getBattlesWonByUser(Integer userId);

    @Query("SELECT SUM(p.battlesPlayed) FROM Player p")
    Integer getTotalBattlesDisputed();

    @Query("SELECT SUM(p.roomsVisited) FROM Player p WHERE p.user.id = :userId")
    Integer getTotalRoomsVisitedByUser(Integer userId);

}
