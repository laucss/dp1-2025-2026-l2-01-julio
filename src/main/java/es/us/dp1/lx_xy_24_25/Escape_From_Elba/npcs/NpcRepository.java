package es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NpcRepository extends CrudRepository<Npc, Integer> {


    Optional<Npc> findByIdAndMatchId(Integer id, Integer matchId);

    Optional<Npc> findById(Integer id);

    Npc save(Npc npc);

    //Devuelve los npc de una partida y que se encuentran en una sala concreta
    @Query("SELECT n FROM Npc n WHERE n.match.id = :matchId AND n.room.id = :roomId")
    List<Npc> findByMatchAndRoom(Integer matchId, Integer roomId);

}
