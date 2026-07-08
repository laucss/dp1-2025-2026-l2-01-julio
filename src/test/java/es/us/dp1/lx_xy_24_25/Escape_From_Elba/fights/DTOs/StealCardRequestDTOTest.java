package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;

@DisplayName("StealCardRequestDTO Tests")
class StealCardRequestDTOTest {

    @Test
    @DisplayName("Debería asignar y obtener campos correctamente")
    void testStealCardRequestDTO() {
        Card card = new Card();
        card.setId(7);

        StealCardRequestDTO dto = new StealCardRequestDTO();
        dto.setWinnerId(1);
        dto.setLoserId(2);
        dto.setFromWhere("bag");
        dto.setCard(card);

        assertEquals(1, dto.getWinnerId());
        assertEquals(2, dto.getLoserId());
        assertEquals("bag", dto.getFromWhere());
        assertNotNull(dto.getCard());
        assertEquals(7, dto.getCard().getId());
    }
}
