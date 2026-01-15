package es.us.dp1.lx_xy_24_25.Escape_From_Elba.npc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.NpcDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.NpcRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomRepository;

@SpringBootTest
@Transactional
public class NpcRepositoryTests {

    @Autowired
    private NpcRepository npcRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private RoomRepository roomRepository;


    @Test
    public void findByIdNonExistingReturnsEmpty() {
        Optional<Npc> npc = npcRepository.findById(999);
        assertTrue(npc.isEmpty());
    }

    @Test
    public void findByIdReturnsNpc() {
        Npc npc = new Npc();
        npc.setStrength(5);
        npc.setIsNiallCampbell(false);
        npcRepository.save(npc);

        Optional<Npc> result = npcRepository.findById(npc.getId());

        assertTrue(result.isPresent());
        assertEquals(npc.getId(), result.get().getId());
    }



    @Test
    public void findByIdAndMatchIdNonExistingReturnsEmpty() {
        Optional<Npc> npc = npcRepository.findByIdAndMatchId(999, 999);
        assertTrue(npc.isEmpty());
    }

    @Test
    public void findByIdAndMatchIdReturnsNpc() {
        Match match = new Match();
        match.setIsPrivate(false);
        matchRepository.save(match);

        Npc npc = new Npc();
        npc.setStrength(7);
        npc.setIsNiallCampbell(true);
        npc.setMatch(match);
        npcRepository.save(npc);

        Optional<Npc> result =
                npcRepository.findByIdAndMatchId(npc.getId(), match.getId());

        assertTrue(result.isPresent());
        assertEquals(npc.getId(), result.get().getId());
        assertEquals(match.getId(), result.get().getMatch().getId());
    }

    

    @ParameterizedTest
    @ValueSource(ints = { 100, 200, 300 })
    public void findByIdAndMatchIdWithRandomIdsReturnsEmpty(Integer id) {
        Optional<Npc> npc = npcRepository.findByIdAndMatchId(id, id);
        assertTrue(npc.isEmpty());
    }


    @Test
    public void saveNpcCreatesNpc() {
        // Creamos la entidad directamente
        Npc npc = new Npc();
        npc.setStrength(10);
        npc.setIsNiallCampbell(false);

        Npc saved = npcRepository.save(npc);

        assertThat(saved.getId()).isNotNull();
        assertEquals(10, saved.getStrength());
        assertFalse(saved.getIsNiallCampbell());
    }

    @Test
    public void saveNpcUpdatesExistingNpc() {
        // Guardamos un NPC primero
        Npc npc = new Npc();
        npc.setStrength(5);
        npc.setIsNiallCampbell(false);
        npcRepository.save(npc);

        // Modificamos la entidad existente
        npc.setStrength(20);
        npc.setIsNiallCampbell(true);

        Npc updated = npcRepository.save(npc);

        assertEquals(npc.getId(), updated.getId());
        assertEquals(20, updated.getStrength());
        assertTrue(updated.getIsNiallCampbell());
    }
}
