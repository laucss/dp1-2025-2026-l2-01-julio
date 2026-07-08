package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("WeaponsUpdateDTO Tests")
class WeaponsUpdateDTOTest {

    @Test
    @DisplayName("Debería gestionar correctamente los datos internos de Weapons y su salida estructurada")
    void testWeaponsUpdateDTOAndWeaponData() {
        // Test de la clase estática interna WeaponData
        WeaponsUpdateDTO.WeaponData weapon1 = new WeaponsUpdateDTO.WeaponData("Knife", 2);
        WeaponsUpdateDTO.WeaponData weapon2 = new WeaponsUpdateDTO.WeaponData();
        weapon2.setName("Pistol");
        weapon2.setBonus(4);

        assertEquals("Knife", weapon1.getName());
        assertEquals(2, weapon1.getBonus());
        assertEquals("Pistol", weapon2.getName());
        assertEquals(4, weapon2.getBonus());

        List<WeaponsUpdateDTO.WeaponData> weaponsList = new ArrayList<>(List.of(weapon1, weapon2));
        
        // Test de la clase contenedora principal
        WeaponsUpdateDTO dto = new WeaponsUpdateDTO(1, 10, "ATTACKER", weaponsList, 5, 2);
        
        assertEquals(1, dto.getMatchId());
        assertEquals(10, dto.getPlayerId());
        assertEquals("ATTACKER", dto.getPlayerRole());
        assertEquals(2, dto.getWeapons().size());
        assertEquals(5, dto.getTotalAttacker());
        assertEquals(2, dto.getTotalDefender());
        
        // Comprobación de que el método toString personalizado funciona correctamente
        String dtoString = dto.toString();
        assertTrue(dtoString.contains("matchId=1"));
        assertTrue(dtoString.contains("playerRole='ATTACKER'"));
    }
}
