package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.ReadyStateUpdateDTO;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ReadyStateUpdateDTO Tests")
public class ReadyStateUpdateDTOTest {

    private ReadyStateUpdateDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ReadyStateUpdateDTO();
    }

    @Test
    @DisplayName("Should create empty ReadyStateUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getPlayerRole()).isNull();
        assertThat(dto.getIsReady()).isNull();
    }

    @Test
    @DisplayName("Should create ReadyStateUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        ReadyStateUpdateDTO dto = new ReadyStateUpdateDTO(1, 10, "ATTACKER", true);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getPlayerId()).isEqualTo(10);
        assertThat(dto.getPlayerRole()).isEqualTo("ATTACKER");
        assertThat(dto.getIsReady()).isTrue();
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
    @DisplayName("Should set and get isReady flag correctly")
    void testSetAndGetIsReady() {
        dto.setIsReady(true);
        assertThat(dto.getIsReady()).isTrue();

        dto.setIsReady(false);
        assertThat(dto.getIsReady()).isFalse();
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setMatchId(1);
        dto.setPlayerId(10);
        dto.setPlayerRole("ATTACKER");
        dto.setIsReady(true);

        dto.setMatchId(null);
        dto.setPlayerId(null);
        dto.setPlayerRole(null);
        dto.setIsReady(null);

        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getPlayerRole()).isNull();
        assertThat(dto.getIsReady()).isNull();
    }

    @Test
    @DisplayName("Should handle different player roles")
    void testDifferentPlayerRoles() {
        ReadyStateUpdateDTO attacker = new ReadyStateUpdateDTO(1, 10, "ATTACKER", true);
        ReadyStateUpdateDTO defender = new ReadyStateUpdateDTO(1, 11, "DEFENDER", false);

        assertThat(attacker.getPlayerRole()).isNotEqualTo(defender.getPlayerRole());
        assertThat(attacker.getPlayerRole()).isEqualTo("ATTACKER");
        assertThat(defender.getPlayerRole()).isEqualTo("DEFENDER");
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setMatchId(1);
        dto.setPlayerId(10);
        dto.setPlayerRole("ATTACKER");
        dto.setIsReady(true);

        assertThat(dto.getIsReady()).isTrue();

        dto.setMatchId(2);
        dto.setPlayerId(11);
        dto.setPlayerRole("DEFENDER");
        dto.setIsReady(false);

        assertThat(dto.getMatchId()).isEqualTo(2);
        assertThat(dto.getPlayerRole()).isEqualTo("DEFENDER");
        assertThat(dto.getIsReady()).isFalse();
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        ReadyStateUpdateDTO dto1 = new ReadyStateUpdateDTO(1, 10, "ATTACKER", true);
        ReadyStateUpdateDTO dto2 = new ReadyStateUpdateDTO(2, 11, "DEFENDER", false);

        assertThat(dto1.getMatchId()).isNotEqualTo(dto2.getMatchId());
        assertThat(dto1.getPlayerRole()).isNotEqualTo(dto2.getPlayerRole());
        assertThat(dto1.getIsReady()).isNotEqualTo(dto2.getIsReady());
    }

    @Test
    @DisplayName("Should handle attacker ready state")
    void testAttackerReadyState() {
        ReadyStateUpdateDTO dto = new ReadyStateUpdateDTO(1, 10, "ATTACKER", true);

        assertThat(dto.getPlayerRole()).isEqualTo("ATTACKER");
        assertThat(dto.getIsReady()).isTrue();
    }

    @Test
    @DisplayName("Should handle defender not ready state")
    void testDefenderNotReadyState() {
        ReadyStateUpdateDTO dto = new ReadyStateUpdateDTO(1, 11, "DEFENDER", false);

        assertThat(dto.getPlayerRole()).isEqualTo("DEFENDER");
        assertThat(dto.getIsReady()).isFalse();
    }

    @Test
    @DisplayName("Should handle large ID values")
    void testLargeIdValues() {
        dto.setMatchId(Integer.MAX_VALUE);
        dto.setPlayerId(Integer.MAX_VALUE);

        assertThat(dto.getMatchId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getPlayerId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle zero IDs")
    void testZeroIds() {
        dto.setMatchId(0);
        dto.setPlayerId(0);

        assertThat(dto.getMatchId()).isZero();
        assertThat(dto.getPlayerId()).isZero();
    }
}
