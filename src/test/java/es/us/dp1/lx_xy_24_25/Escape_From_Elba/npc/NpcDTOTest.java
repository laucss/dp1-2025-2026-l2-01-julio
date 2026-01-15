package es.us.dp1.lx_xy_24_25.Escape_From_Elba.npc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.NpcDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;

public class NpcDTOTest {

    @Test
    public void testNoArgsConstructorAndSetters() {
        NpcDTO dto = new NpcDTO();

        dto.setId(1);
        dto.setStrength(50);
        dto.setIsNiallCampbell(true);

        RoomDTO roomDto = new RoomDTO();
        roomDto.setId(100);
        dto.setRoom(roomDto);

        assertEquals(1, dto.getId());
        assertEquals(50, dto.getStrength());
        assertTrue(dto.getIsNiallCampbell());
        assertEquals(roomDto, dto.getRoom());
    }

    @Test
    public void testConstructorFromNpcWithoutRoom() {
        Npc npc = new Npc();
        npc.setId(5);
        npc.setStrength(30);
        npc.setIsNiallCampbell(false);
        npc.setRoom(null);

        NpcDTO dto = new NpcDTO(npc);

        assertEquals(5, dto.getId());
        assertEquals(30, dto.getStrength());
        assertFalse(dto.getIsNiallCampbell());
        assertNull(dto.getRoom());
    }

    @Test
    public void testConstructorFromNpcWithRoom() {
        Npc npc = new Npc();
        npc.setId(7);
        npc.setStrength(40);
        npc.setIsNiallCampbell(true);

        Room room = new Room();
        room.setId(99);
        room.setName("Test Room");
        npc.setRoom(room);

        NpcDTO dto = new NpcDTO(npc);

        assertEquals(7, dto.getId());
        assertEquals(40, dto.getStrength());
        assertTrue(dto.getIsNiallCampbell());
        assertNotNull(dto.getRoom());
        assertEquals(99, dto.getRoom().getId());
        assertEquals("Test Room", dto.getRoom().getName());
    }
}

