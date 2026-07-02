package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;

public interface LobbyRepository extends CrudRepository<Match, Integer> {

    //Devuelve todos los lobbies para unirse publicos
    @Query( "SELECT m FROM Match m WHERE m.isPrivate=false and m.status= :status")
    Page<Match> findAllPublicGamesByStatus(MatchStatus status, Pageable page); // El page es para poder poner paginas 

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
    
    
}
