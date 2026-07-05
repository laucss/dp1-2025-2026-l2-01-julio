package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.WeaponsUpdateDTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("WeaponsUpdateDTO Tests")
public class WeaponsUpdateDTOTest {

    private WeaponsUpdateDTO dto;

    @BeforeEach
    void setUp() {
        dto = new WeaponsUpdateDTO();
    }

    @Test
    @DisplayName("Should create empty WeaponsUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getPlayerRole()).isNull();
        assertThat(dto.getWeapons()).isNull();
        assertThat(dto.getTotalAttacker()).isNull();
        assertThat(dto.getTotalDefender()).isNull();
    }

    @Test
    @DisplayName("Should create WeaponsUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        List<WeaponsUpdateDTO.WeaponData> weapons = new ArrayList<>();
        weapons.add(new WeaponsUpdateDTO.WeaponData("Sword", 2));

        WeaponsUpdateDTO dto = new WeaponsUpdateDTO(1, 10, "ATTACKER", weapons, 5, 3);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getPlayerId()).isEqualTo(10);
        assertThat(dto.getPlayerRole()).isEqualTo("ATTACKER");
        assertThat(dto.getWeapons()).hasSize(1);
        assertThat(dto.getTotalAttacker()).isEqualTo(5);
        assertThat(dto.getTotalDefender()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should set and get matchId correctly")
    void testSetAndGetMatchId() {
        dto.setMatchId(5);
        assertThat(dto.getMatchId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should set and get playerId correctly")
    void testSetAndGetPlayerId() {
        dto.setPlayerId(15);
        assertThat(dto.getPlayerId()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should set and get playerRole correctly")
    void testSetAndGetPlayerRole() {
        dto.setPlayerRole("DEFENDER");
        assertThat(dto.getPlayerRole()).isEqualTo("DEFENDER");

        dto.setPlayerRole("ATTACKER");
        assertThat(dto.getPlayerRole()).isEqualTo("ATTACKER");
    }

    @Test
    @DisplayName("Should set and get weapons list correctly")
    void testSetAndGetWeapons() {
        List<WeaponsUpdateDTO.WeaponData> weapons = new ArrayList<>();
        weapons.add(new WeaponsUpdateDTO.WeaponData("Sword", 2));
        weapons.add(new WeaponsUpdateDTO.WeaponData("Shield", 1));

        dto.setWeapons(weapons);

        assertThat(dto.getWeapons()).hasSize(2);
        assertThat(dto.getWeapons().get(0).getName()).isEqualTo("Sword");
        assertThat(dto.getWeapons().get(1).getBonus()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should set and get totalAttacker correctly")
    void testSetAndGetTotalAttacker() {
        dto.setTotalAttacker(10);
        assertThat(dto.getTotalAttacker()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should set and get totalDefender correctly")
    void testSetAndGetTotalDefender() {
        dto.setTotalDefender(8);
        assertThat(dto.getTotalDefender()).isEqualTo(8);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        List<WeaponsUpdateDTO.WeaponData> weapons = new ArrayList<>();
        dto.setMatchId(1);
        dto.setPlayerId(10);
        dto.setPlayerRole("ATTACKER");
        dto.setWeapons(weapons);
        dto.setTotalAttacker(5);
        dto.setTotalDefender(3);

        dto.setMatchId(null);
        dto.setPlayerId(null);
        dto.setPlayerRole(null);
        dto.setWeapons(null);
        dto.setTotalAttacker(null);
        dto.setTotalDefender(null);

        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getPlayerRole()).isNull();
        assertThat(dto.getWeapons()).isNull();
        assertThat(dto.getTotalAttacker()).isNull();
        assertThat(dto.getTotalDefender()).isNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        List<WeaponsUpdateDTO.WeaponData> weapons1 = new ArrayList<>();
        List<WeaponsUpdateDTO.WeaponData> weapons2 = new ArrayList<>();
        weapons2.add(new WeaponsUpdateDTO.WeaponData("Axe", 3));

        dto.setMatchId(1);
        dto.setPlayerId(10);
        dto.setWeapons(weapons1);

        assertThat(dto.getMatchId()).isEqualTo(1);

        dto.setMatchId(2);
        dto.setPlayerId(11);
        dto.setWeapons(weapons2);

        assertThat(dto.getMatchId()).isEqualTo(2);
        assertThat(dto.getWeapons()).hasSize(1);
    }

    @Test
    @DisplayName("Should handle empty weapons list")
    void testEmptyWeaponsList() {
        dto.setWeapons(new ArrayList<>());
        assertThat(dto.getWeapons()).isEmpty();
    }

    @Test
    @DisplayName("Should handle multiple weapons")
    void testMultipleWeapons() {
        List<WeaponsUpdateDTO.WeaponData> weapons = new ArrayList<>();
        weapons.add(new WeaponsUpdateDTO.WeaponData("Sword", 2));
        weapons.add(new WeaponsUpdateDTO.WeaponData("Shield", 1));
        weapons.add(new WeaponsUpdateDTO.WeaponData("Armor", 1));

        dto.setWeapons(weapons);

        assertThat(dto.getWeapons()).hasSize(3);
    }

    @Test
    @DisplayName("Should verify WeaponData constructor")
    void testWeaponDataConstructor() {
        WeaponsUpdateDTO.WeaponData weapon = new WeaponsUpdateDTO.WeaponData("Dagger", 1);

        assertThat(weapon.getName()).isEqualTo("Dagger");
        assertThat(weapon.getBonus()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should verify WeaponData setters and getters")
    void testWeaponDataSettersAndGetters() {
        WeaponsUpdateDTO.WeaponData weapon = new WeaponsUpdateDTO.WeaponData();

        weapon.setName("Lance");
        weapon.setBonus(3);

        assertThat(weapon.getName()).isEqualTo("Lance");
        assertThat(weapon.getBonus()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should verify toString method works")
    void testToString() {
        List<WeaponsUpdateDTO.WeaponData> weapons = new ArrayList<>();
        weapons.add(new WeaponsUpdateDTO.WeaponData("Sword", 2));

        WeaponsUpdateDTO dto = new WeaponsUpdateDTO(1, 10, "ATTACKER", weapons, 5, 3);
        String toString = dto.toString();

        assertThat(toString).contains("WeaponsUpdateDTO");
        assertThat(toString).contains("matchId=1");
        assertThat(toString).contains("playerId=10");
        assertThat(toString).contains("playerRole=");
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        List<WeaponsUpdateDTO.WeaponData> weapons1 = new ArrayList<>();
        weapons1.add(new WeaponsUpdateDTO.WeaponData("Sword", 2));

        List<WeaponsUpdateDTO.WeaponData> weapons2 = new ArrayList<>();
        weapons2.add(new WeaponsUpdateDTO.WeaponData("Bow", 2));

        WeaponsUpdateDTO dto1 = new WeaponsUpdateDTO(1, 10, "ATTACKER", weapons1, 5, 3);
        WeaponsUpdateDTO dto2 = new WeaponsUpdateDTO(2, 11, "DEFENDER", weapons2, 4, 6);

        assertThat(dto1.getMatchId()).isNotEqualTo(dto2.getMatchId());
        assertThat(dto1.getPlayerRole()).isNotEqualTo(dto2.getPlayerRole());
    }

    @Test
    @DisplayName("Should handle large total values")
    void testLargeTotalValues() {
        dto.setTotalAttacker(100);
        dto.setTotalDefender(99);

        assertThat(dto.getTotalAttacker()).isEqualTo(100);
        assertThat(dto.getTotalDefender()).isEqualTo(99);
    }

    @Test
    @DisplayName("Should handle zero totals")
    void testZeroTotals() {
        dto.setTotalAttacker(0);
        dto.setTotalDefender(0);

        assertThat(dto.getTotalAttacker()).isZero();
        assertThat(dto.getTotalDefender()).isZero();
    }
}
