package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DiceTotalsUpdateDTO Tests")
class DiceTotalsUpdateDTOTest {

    @Test
    @DisplayName("Debería asignar y obtener campos correctamente usando constructores y setters")
    void testDiceTotalsUpdateDTO() {
        long timestamp = System.currentTimeMillis();
        
        // Test con constructor con parámetros
        DiceTotalsUpdateDTO dto = new DiceTotalsUpdateDTO(1, 10, 2, 12, timestamp);

        assertEquals(1, dto.getAttackerId());
        assertEquals(10, dto.getAttackerTotal());
        assertEquals(2, dto.getDefenderId());
        assertEquals(12, dto.getDefenderTotal());
        assertEquals(timestamp, dto.getTimestamp());

        // Test con constructor vacío y setters
        DiceTotalsUpdateDTO emptyDto = new DiceTotalsUpdateDTO();
        emptyDto.setAttackerId(3);
        emptyDto.setAttackerTotal(15);
        emptyDto.setDefenderId(4);
        emptyDto.setDefenderTotal(8);
        emptyDto.setTimestamp(1000L);

        assertEquals(3, emptyDto.getAttackerId());
        assertEquals(15, emptyDto.getAttackerTotal());
        assertEquals(4, emptyDto.getDefenderId());
        assertEquals(8, emptyDto.getDefenderTotal());
        assertEquals(1000L, emptyDto.getTimestamp());
    }
}