package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FightUpdateDTO Tests")
class FightUpdateDTOTest {

    @Test
    @DisplayName("Debería asignar y obtener campos correctamente usando constructores y setters")
    void testFightUpdateDTO() {
        FightUpdateDTO dto = new FightUpdateDTO(1, 10, "att", 20, "def", 105, "START", false);
        dto.setRoomName("Cell Block A");

        assertEquals(1, dto.getMatchId());
        assertEquals(10, dto.getAttackerId());
        assertEquals("att", dto.getAttackerUsername());
        assertEquals(20, dto.getDefenderId());
        assertEquals("def", dto.getDefenderUsername());
        assertEquals(105, dto.getRoomId());
        assertEquals("Cell Block A", dto.getRoomName());
        assertEquals("START", dto.getAction());
        assertFalse(dto.getIsBot());

        FightUpdateDTO empty = new FightUpdateDTO();
        empty.setIsBot(true);
        assertTrue(empty.getIsBot());
    }
}
