package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("FightResultRequestDTO Tests")
class FightResultRequestDTOTest {

    @Test
    @DisplayName("Debería asignar y obtener campos correctamente usando constructores y setters")
    void testFightResultRequestDTO() {
        FightResultRequestDTO dto = new FightResultRequestDTO(1, 2, true, true, false, 100);
        dto.setMatchId(42);

        assertEquals(42, dto.getMatchId());
        assertEquals(1, dto.getAttackerId());
        assertEquals(2, dto.getDefenderId());
        assertTrue(dto.isNpcFight());
        assertTrue(dto.isNpcAttacker());
        assertFalse(dto.isAttackerWins());
        assertEquals(100, dto.getDefenderRoomId());

        FightResultRequestDTO empty = new FightResultRequestDTO();
        empty.setNpcFight(false);
        empty.setNpcAttacker(false);
        empty.setAttackerWins(true);
        
        assertFalse(empty.isNpcFight());
        assertFalse(empty.isNpcAttacker());
        assertTrue(empty.isAttackerWins());
    }
}
