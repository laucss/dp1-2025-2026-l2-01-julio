package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;



public interface MatchRepository extends CrudRepository<Match, Integer> {
    Match save(Match game);

    List<Match> findAll();

    List<Match> findByName(String name);

    Optional<Match> findById(int id);

    //Devuelve todos los lobbies para unirse publicos
    @Query( "SELECT m FROM Match m WHERE m.isPrivate=false and m.status= 'WAITING'")
    List<Match> findPublicLobbies(); // El page es para poder poner paginas 

    //Devuelve todos los lobbies privados
    @Query( "SELECT m FROM Match m WHERE m.isPrivate=true and m.status= 'WAITING'")
    List<Match> findPrivateLobbies(); 

    //Devuelve un lobby privado por su codigo de acceso
    @Query("SELECT m FROM Match m WHERE m.isPrivate=true and LOWER(m.code)= LOWER(:codeLobby) and m.status='WAITING'" )
    //Busca el juego cuyo estado sea Waiting ( eso significa que es un lobby), sea privado y cuyo codigo sea el mismo
    Optional<Match> findPrivateLobbyByCode(String codeLobby);

    //Devuelve si el usuario esta en algun lobby
    @Query("SELECT m FROM Match m JOIN m.players p WHERE m.status = 'WAITING'AND p.user.id = :userId")
    Optional<Match> findLobbyWhereUserIsIn(Integer userId);

    // Devuelve todas las partidas en progreso (he hecho una propiedad del estilo en match)
    @Query("SELECT m FROM Match m WHERE m.startTime IS NOT NULL AND m.endTime IS NULL")
    List<Match> findInProgress();

    //Devuelve todas las partidas finalizadas
    @Query("SELECT m FROM Match m WHERE m.endTime IS NOT NULL")
    List<Match> findFinished();

    // Partidas no iniciadas que ya han alcanzadoo el número mínimo de jugadores y se pueden empezar
    @Query("SELECT m FROM Match m WHERE m.startTime IS NULL AND m.endTime IS NULL AND " +
           "(SELECT COUNT(p) FROM Player p WHERE p.match = m) >= m.minPlayers")
    List<Match> findReadyToStart();

    // Partidas jugables en progreso o listas para empezar
    @Query("SELECT m FROM Match m WHERE " +
           "(m.startTime IS NOT NULL AND m.endTime IS NULL) OR " +
           "(m.startTime IS NULL AND m.endTime IS NULL AND (SELECT COUNT(p) FROM Player p WHERE p.match = m) >= m.minPlayers)")
    List<Match> findPlayable();

    // Devuelve el ID de la partida donde el usuario está
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN MAX(m.id) ELSE NULL END " +
           "FROM Match m JOIN m.players p WHERE p.user.id = :userId AND m.endTime IS NULL")
    Integer userInMatch(Integer userId);


       //Devuelves todas las partidas jugadas por un usuario 
       @Query("SELECT m FROM Match m JOIN m.players p WHERE p.user.id = :userId AND m.endTime IS NOT NULL")
       List<Match> findMatchesPlayedByUser(Integer userId);

       //Devuelves todas las partidas jugadas por un usuario y creadas por él
       @Query("SELECT m FROM Match m JOIN m.players p WHERE p.user.id = :userId AND m.endTime IS NOT NULL AND m.creatorId = :userId")
       List<Match> findMatchesPlayedAndCreatedByUser( Integer userId);

       //Devuelve todas las partidas ganadas por un usuario
       @Query("SELECT m FROM Match m JOIN m.players p WHERE p.user.id = :userId AND m.endTime IS NOT NULL AND m.winner = p")
       List<Match> findMatchesWonByUser( Integer userId);




}
