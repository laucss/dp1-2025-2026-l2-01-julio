package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<Match, Integer> {
    Match save(Match game);

    List<Match> findAll();

    List<Match> findByName(String name);

    Optional<Match> findByCode(String gameCode);

    // Devuelve todas las partidas en progreso (he hecho una propiedad del estilo en match)
    @Query("SELECT m FROM Match m WHERE m.startTime IS NOT NULL AND m.endTime IS NULL")
    List<Match> findInProgress();


    // Partidas no iniciadas que ya han alcanzadoo el número mínimo de jugadores y se pueden empezar
    @Query("SELECT m FROM Match m WHERE m.startTime IS NULL AND m.endTime IS NULL AND " +
           "(SELECT COUNT(p) FROM PlayerInGame p WHERE p.match = m) >= m.minPlayers")
    List<Match> findReadyToStart();

    // Partidas jugables en progreso o listas para empezar
    @Query("SELECT m FROM Match m WHERE " +
           "(m.startTime IS NOT NULL AND m.endTime IS NULL) OR " +
           "(m.startTime IS NULL AND m.endTime IS NULL AND (SELECT COUNT(p) FROM PlayerInGame p WHERE p.match = m) >= m.minPlayers)")
    List<Match> findPlayable();

}
