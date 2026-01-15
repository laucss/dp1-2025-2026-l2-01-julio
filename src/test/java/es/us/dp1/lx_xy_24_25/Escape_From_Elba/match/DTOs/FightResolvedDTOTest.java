package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("FightResolvedDTO Tests")
public class FightResolvedDTOTest {

    private FightResolvedDTO dto;

    @BeforeEach
    void setUp() {
        dto = new FightResolvedDTO();
    }

    @Test
    @DisplayName("Should create empty FightResolvedDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getWinnerId()).isNull();
        assertThat(dto.getWinnerPlayerId()).isNull();
        assertThat(dto.getLoserPlayerId()).isNull();
        assertThat(dto.getAction()).isEqualTo("RESOLVE");
    }

    @Test
    @DisplayName("Should create FightResolvedDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        FightResolvedDTO dto = new FightResolvedDTO(1, 10, 5, 6);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getWinnerId()).isEqualTo(10);
        assertThat(dto.getWinnerPlayerId()).isEqualTo(5);
        assertThat(dto.getLoserPlayerId()).isEqualTo(6);
        assertThat(dto.getAction()).isEqualTo("RESOLVE");
    }

    @Test
    @DisplayName("Should set and get matchId correctly")
    void testSetAndGetMatchId() {
        dto.setMatchId(3);
        assertThat(dto.getMatchId()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should set and get winnerId correctly")
    void testSetAndGetWinnerId() {
        dto.setWinnerId(15);
        assertThat(dto.getWinnerId()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should set and get winnerPlayerId correctly")
    void testSetAndGetWinnerPlayerId() {
        dto.setWinnerPlayerId(7);
        assertThat(dto.getWinnerPlayerId()).isEqualTo(7);
    }

    @Test
    @DisplayName("Should set and get loserPlayerId correctly")
    void testSetAndGetLoserPlayerId() {
        dto.setLoserPlayerId(8);
        assertThat(dto.getLoserPlayerId()).isEqualTo(8);
    }

    @Test
    @DisplayName("Should set and get action correctly")
    void testSetAndGetAction() {
        dto.setAction("FINISH");
        assertThat(dto.getAction()).isEqualTo("FINISH");

        dto.setAction("RESOLVE");
        assertThat(dto.getAction()).isEqualTo("RESOLVE");
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setMatchId(1);
        dto.setWinnerId(10);
        dto.setWinnerPlayerId(5);
        dto.setLoserPlayerId(6);

        dto.setMatchId(null);
        dto.setWinnerId(null);
        dto.setWinnerPlayerId(null);
        dto.setLoserPlayerId(null);

        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getWinnerId()).isNull();
        assertThat(dto.getWinnerPlayerId()).isNull();
        assertThat(dto.getLoserPlayerId()).isNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setMatchId(1);
        dto.setWinnerId(10);
        dto.setWinnerPlayerId(5);
        dto.setLoserPlayerId(6);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getWinnerId()).isEqualTo(10);

        dto.setMatchId(2);
        dto.setWinnerId(11);
        dto.setWinnerPlayerId(7);
        dto.setLoserPlayerId(8);

        assertThat(dto.getMatchId()).isEqualTo(2);
        assertThat(dto.getWinnerId()).isEqualTo(11);
        assertThat(dto.getWinnerPlayerId()).isEqualTo(7);
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        FightResolvedDTO dto1 = new FightResolvedDTO(1, 10, 5, 6);
        FightResolvedDTO dto2 = new FightResolvedDTO(2, 11, 7, 8);

        assertThat(dto1.getMatchId()).isNotEqualTo(dto2.getMatchId());
        assertThat(dto1.getWinnerId()).isNotEqualTo(dto2.getWinnerId());
        assertThat(dto1.getWinnerPlayerId()).isNotEqualTo(dto2.getWinnerPlayerId());
    }

    @Test
    @DisplayName("Should handle large ID values")
    void testLargeIdValues() {
        dto.setMatchId(Integer.MAX_VALUE);
        dto.setWinnerId(Integer.MAX_VALUE);
        dto.setWinnerPlayerId(Integer.MAX_VALUE);
        dto.setLoserPlayerId(Integer.MAX_VALUE);

        assertThat(dto.getMatchId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getWinnerId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should verify action field default value")
    void testActionDefaultValue() {
        FightResolvedDTO dto = new FightResolvedDTO();
        assertThat(dto.getAction()).isEqualTo("RESOLVE");
    }

    @Test
    @DisplayName("Should distinguish from other fight events")
    void testActionDistinction() {
        FightResolvedDTO dto1 = new FightResolvedDTO(1, 10, 5, 6);
        FightResolvedDTO dto2 = new FightResolvedDTO(1, 10, 5, 6);

        assertThat(dto1.getAction()).isEqualTo(dto2.getAction());
        assertThat(dto1.getAction()).isEqualTo("RESOLVE");
    }
}
