package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("ReadyStateUpdateDTO Tests")
class ReadyStateUpdateDTOTest {

    @Test
    @DisplayName("Debería asignar y obtener campos correctamente usando constructores y setters")
    void testReadyStateUpdateDTO() {
        ReadyStateUpdateDTO dto = new ReadyStateUpdateDTO(1, 10, "ATTACKER", true);

        assertEquals(1, dto.getMatchId());
        assertEquals(10, dto.getPlayerId());
        assertEquals("ATTACKER", dto.getPlayerRole());
        assertTrue(dto.getIsReady());

        ReadyStateUpdateDTO empty = new ReadyStateUpdateDTO();
        empty.setMatchId(2);
        empty.setPlayerId(20);
        empty.setPlayerRole("DEFENDER");
        empty.setIsReady(false);

        assertEquals(2, empty.getMatchId());
        assertEquals(20, empty.getPlayerId());
        assertEquals("DEFENDER", empty.getPlayerRole());
        assertFalse(empty.getIsReady());
    }
}
