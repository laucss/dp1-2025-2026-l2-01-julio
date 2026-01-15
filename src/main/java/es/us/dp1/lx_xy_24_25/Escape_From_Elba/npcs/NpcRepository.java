package es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface NpcRepository extends CrudRepository<Npc, Integer> {


    Optional<Npc> findByIdAndMatchId(Integer id, Integer matchId);

    Optional<Npc> findById(Integer id);

    Npc save(Npc npc);

}
