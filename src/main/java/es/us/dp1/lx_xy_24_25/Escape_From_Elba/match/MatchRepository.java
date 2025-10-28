package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchRepository extends CrudRepository<Match, Integer> {
    Match save(Match game);

    List<Match> findAll();

    List<Match> findByName(String name);


    //Devuelve todos los lobbies para unirse publicos
    @Query( "SELECT m FROM Match m WHERE m.isPrivate=false and m.status= 'WAITING'")
    List<Match> findPublicLobbies(); // El page es para poder poner paginas 

    //Devuelve un lobby privado por su codigo de acceso
    @Query("SELECT m FROM Match m WHERE m.isPrivate=true and LOWER(m.code)= LOWER(:codeLobby) and m.status='WAITING'" )
    //Busca el juego cuyo estado sea Waiting ( eso significa que es un lobby), sea privado y cuyo codigo sea el mismo
    Optional<Match> findPrivateLobbieById(String codeLobby);

    //Devuelve si el usuario esta en algun lobby
    @Query("SELECT m FROM Match m WHERE m.status='WAITING' AND :player MEMBER OF m.players")
    Optional<Match> findLobbyWherePlayerIsIn(Player player);


    // Devuelve todas las partidas en progreso (he hecho una propiedad del estilo en match)
    @Query("SELECT m FROM Match m WHERE m.startTime IS NOT NULL AND m.endTime IS NULL")
    List<Match> findInProgress();


    // Partidas no iniciadas que ya han alcanzadoo el número mínimo de jugadores y se pueden empezar
    @Query("SELECT m FROM Match m WHERE m.startTime IS NULL AND m.endTime IS NULL AND " +
           "(SELECT COUNT(p) FROM Player p WHERE p.match = m) >= m.minPlayers")
    List<Match> findReadyToStart();

    // Partidas jugables en progreso o listas para empezar
    @Query("SELECT m FROM Match m WHERE " +
           "(m.startTime IS NOT NULL AND m.endTime IS NULL) OR " +
           "(m.startTime IS NULL AND m.endTime IS NULL AND (SELECT COUNT(p) FROM Player p WHERE p.match = m) >= m.minPlayers)")
    List<Match> findPlayable();

}
