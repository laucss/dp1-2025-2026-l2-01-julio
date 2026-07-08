package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("FightDiceUpdateDTO Tests")
class FightDiceUpdateDTOTest {

    @Test
    @DisplayName("Debería asignar y obtener campos correctamente usando constructores y setters")
    void testFightDiceUpdateDTO() {
        // Test con constructor con parámetros
        FightDiceUpdateDTO dto = new FightDiceUpdateDTO(100, 1, "user1", "WHITE", 5);

        assertEquals(100, dto.getMatchId());
        assertEquals(1, dto.getPlayerId());
        assertEquals("user1", dto.getPlayerUsername());
        assertEquals("WHITE", dto.getDiceType());
        assertEquals(5, dto.getDiceValue());

        // Test con constructor vacío y setters
        FightDiceUpdateDTO emptyDto = new FightDiceUpdateDTO();
        emptyDto.setMatchId(200);
        emptyDto.setPlayerId(2);
        emptyDto.setPlayerUsername("user2");
        emptyDto.setDiceType("BLACK");
        emptyDto.setDiceValue(3);

        assertEquals(200, emptyDto.getMatchId());
        assertEquals(2, emptyDto.getPlayerId());
        assertEquals("user2", emptyDto.getPlayerUsername());
        assertEquals("BLACK", emptyDto.getDiceType());
        assertEquals(3, emptyDto.getDiceValue());
    }
}
