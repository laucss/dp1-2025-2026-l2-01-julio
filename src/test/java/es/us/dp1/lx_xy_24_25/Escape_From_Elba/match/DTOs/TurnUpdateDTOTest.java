package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TurnUpdateDTO Tests")
public class TurnUpdateDTOTest {

    private TurnUpdateDTO dto;

    @BeforeEach
    void setUp() {
        dto = new TurnUpdateDTO();
    }

    @Test
    @DisplayName("Should create empty TurnUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getCurrentTurnUserId()).isNull();
        assertThat(dto.getCurrentTurnUsername()).isNull();
        assertThat(dto.getTurnNumber()).isNull();
        assertThat(dto.getTurnPhase()).isNull();
    }

    @Test
    @DisplayName("Should create TurnUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        TurnUpdateDTO dto = new TurnUpdateDTO(1, 10, "player1", 1, "MOVEMENT");

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getCurrentTurnUserId()).isEqualTo(10);
        assertThat(dto.getCurrentTurnUsername()).isEqualTo("player1");
        assertThat(dto.getTurnNumber()).isEqualTo(1);
        assertThat(dto.getTurnPhase()).isEqualTo("MOVEMENT");
    }

    @Test
    @DisplayName("Should set and get matchId correctly")
    void testSetAndGetMatchId() {
        dto.setMatchId(5);
        assertThat(dto.getMatchId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should set and get currentTurnUserId correctly")
    void testSetAndGetCurrentTurnUserId() {
        dto.setCurrentTurnUserId(15);
        assertThat(dto.getCurrentTurnUserId()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should set and get currentTurnUsername correctly")
    void testSetAndGetCurrentTurnUsername() {
        dto.setCurrentTurnUsername("testPlayer");
        assertThat(dto.getCurrentTurnUsername()).isEqualTo("testPlayer");
    }

    @Test
    @DisplayName("Should set and get turnNumber correctly")
    void testSetAndGetTurnNumber() {
        dto.setTurnNumber(3);
        assertThat(dto.getTurnNumber()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should set and get turnPhase correctly")
    void testSetAndGetTurnPhase() {
        dto.setTurnPhase("ACTION");
        assertThat(dto.getTurnPhase()).isEqualTo("ACTION");
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setMatchId(1);
        dto.setCurrentTurnUserId(10);
        dto.setCurrentTurnUsername("player");
        dto.setTurnNumber(1);
        dto.setTurnPhase("MOVEMENT");

        dto.setMatchId(null);
        dto.setCurrentTurnUserId(null);
        dto.setCurrentTurnUsername(null);
        dto.setTurnNumber(null);
        dto.setTurnPhase(null);

        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getCurrentTurnUserId()).isNull();
        assertThat(dto.getCurrentTurnUsername()).isNull();
        assertThat(dto.getTurnNumber()).isNull();
        assertThat(dto.getTurnPhase()).isNull();
    }

    @Test
    @DisplayName("Should handle different turn phases")
    void testDifferentTurnPhases() {
        dto.setTurnPhase("MOVEMENT");
        assertThat(dto.getTurnPhase()).isEqualTo("MOVEMENT");

        dto.setTurnPhase("ACTION");
        assertThat(dto.getTurnPhase()).isEqualTo("ACTION");

        dto.setTurnPhase("COMBAT");
        assertThat(dto.getTurnPhase()).isEqualTo("COMBAT");
    }

    @Test
    @DisplayName("Should handle sequential turn numbers")
    void testSequentialTurnNumbers() {
        for (int i = 1; i <= 10; i++) {
            dto.setTurnNumber(i);
            assertThat(dto.getTurnNumber()).isEqualTo(i);
        }
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setMatchId(1);
        dto.setCurrentTurnUserId(10);
        dto.setTurnNumber(1);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getCurrentTurnUserId()).isEqualTo(10);
        assertThat(dto.getTurnNumber()).isEqualTo(1);

        dto.setMatchId(2);
        dto.setCurrentTurnUserId(11);
        dto.setTurnNumber(2);

        assertThat(dto.getMatchId()).isEqualTo(2);
        assertThat(dto.getCurrentTurnUserId()).isEqualTo(11);
        assertThat(dto.getTurnNumber()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should verify toString method works")
    void testToString() {
        TurnUpdateDTO dto = new TurnUpdateDTO(1, 10, "player1", 1, "MOVEMENT");
        String toString = dto.toString();

        assertThat(toString).contains("TurnUpdateDTO");
        assertThat(toString).contains("matchId=1");
        assertThat(toString).contains("currentTurnUserId=10");
        assertThat(toString).contains("currentTurnUsername=");
        assertThat(toString).contains("turnNumber=1");
        assertThat(toString).contains("turnPhase=");
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        TurnUpdateDTO dto1 = new TurnUpdateDTO(1, 10, "p1", 1, "MOVEMENT");
        TurnUpdateDTO dto2 = new TurnUpdateDTO(2, 11, "p2", 2, "ACTION");

        assertThat(dto1.getMatchId()).isNotEqualTo(dto2.getMatchId());
        assertThat(dto1.getCurrentTurnUserId()).isNotEqualTo(dto2.getCurrentTurnUserId());
        assertThat(dto1.getTurnNumber()).isNotEqualTo(dto2.getTurnNumber());
    }

    @Test
    @DisplayName("Should handle large ID values")
    void testLargeIdValues() {
        dto.setMatchId(Integer.MAX_VALUE);
        dto.setCurrentTurnUserId(Integer.MAX_VALUE);
        dto.setTurnNumber(Integer.MAX_VALUE);

        assertThat(dto.getMatchId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getCurrentTurnUserId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getTurnNumber()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle zero turn number")
    void testZeroTurnNumber() {
        dto.setTurnNumber(0);
        assertThat(dto.getTurnNumber()).isZero();
    }

    @Test
    @DisplayName("Should handle negative turn numbers")
    void testNegativeTurnNumber() {
        dto.setTurnNumber(-1);
        assertThat(dto.getTurnNumber()).isEqualTo(-1);
    }
}
