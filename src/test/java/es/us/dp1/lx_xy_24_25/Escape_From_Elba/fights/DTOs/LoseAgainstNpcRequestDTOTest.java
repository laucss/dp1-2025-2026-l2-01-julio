package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LoseAgainstNpcRequestDTO Tests")
class LoseAgainstNpcRequestDTOTest {

    @Test
    @DisplayName("Debería asignar y obtener campos correctamente usando constructores y setters")
    void testLoseAgainstNpcRequestDTO() {
        LoseAgainstNpcRequestDTO dto = new LoseAgainstNpcRequestDTO(99, "hand");

        assertEquals(99, dto.getCardId());
        assertEquals("hand", dto.getFromWhere());

        LoseAgainstNpcRequestDTO empty = new LoseAgainstNpcRequestDTO();
        empty.setCardId(88);
        empty.setFromWhere("bag");

        assertEquals(88, empty.getCardId());
        assertEquals("bag", empty.getFromWhere());
    }
}
