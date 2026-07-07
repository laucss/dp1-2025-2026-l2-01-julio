package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightDiceUpdateDTO;

import static org.assertj.core.api.Assertions.*;

@DisplayName("FightDiceUpdateDTO Tests")
public class FightDiceUpdateDTOTest {

    private FightDiceUpdateDTO dto;

    @BeforeEach
    void setUp() {
        dto = new FightDiceUpdateDTO();
    }

    @Test
    @DisplayName("Should create empty FightDiceUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getPlayerUsername()).isNull();
        assertThat(dto.getDiceType()).isNull();
        assertThat(dto.getDiceValue()).isNull();
    }

    @Test
    @DisplayName("Should create FightDiceUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        FightDiceUpdateDTO dto = new FightDiceUpdateDTO(1, 10, "player1", "WHITE", 4);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getPlayerId()).isEqualTo(10);
        assertThat(dto.getPlayerUsername()).isEqualTo("player1");
        assertThat(dto.getDiceType()).isEqualTo("WHITE");
        assertThat(dto.getDiceValue()).isEqualTo(4);
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
    @DisplayName("Should set and get playerUsername correctly")
    void testSetAndGetPlayerUsername() {
        dto.setPlayerUsername("testPlayer");
        assertThat(dto.getPlayerUsername()).isEqualTo("testPlayer");
    }

    @Test
    @DisplayName("Should set and get diceType correctly")
    void testSetAndGetDiceType() {
        dto.setDiceType("BLACK");
        assertThat(dto.getDiceType()).isEqualTo("BLACK");
    }

    @Test
    @DisplayName("Should set and get diceValue correctly")
    void testSetAndGetDiceValue() {
        dto.setDiceValue(6);
        assertThat(dto.getDiceValue()).isEqualTo(6);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setMatchId(1);
        dto.setPlayerId(10);
        dto.setPlayerUsername("player");
        dto.setDiceType("WHITE");
        dto.setDiceValue(3);

        dto.setMatchId(null);
        dto.setPlayerId(null);
        dto.setPlayerUsername(null);
        dto.setDiceType(null);
        dto.setDiceValue(null);

        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getPlayerUsername()).isNull();
        assertThat(dto.getDiceType()).isNull();
        assertThat(dto.getDiceValue()).isNull();
    }

    @Test
    @DisplayName("Should handle different dice types")
    void testDifferentDiceTypes() {
        dto.setDiceType("WHITE");
        assertThat(dto.getDiceType()).isEqualTo("WHITE");

        dto.setDiceType("BLACK");
        assertThat(dto.getDiceType()).isEqualTo("BLACK");
    }

    @Test
    @DisplayName("Should handle dice values between 1 and 6")
    void testDiceValuesRange() {
        for (int i = 1; i <= 6; i++) {
            dto.setDiceValue(i);
            assertThat(dto.getDiceValue()).isEqualTo(i);
        }
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setMatchId(1);
        dto.setPlayerId(10);
        dto.setDiceValue(3);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getPlayerId()).isEqualTo(10);
        assertThat(dto.getDiceValue()).isEqualTo(3);

        dto.setMatchId(2);
        dto.setPlayerId(11);
        dto.setDiceValue(5);

        assertThat(dto.getMatchId()).isEqualTo(2);
        assertThat(dto.getPlayerId()).isEqualTo(11);
        assertThat(dto.getDiceValue()).isEqualTo(5);
    }

    

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        FightDiceUpdateDTO dto1 = new FightDiceUpdateDTO(1, 10, "player1", "WHITE", 3);
        FightDiceUpdateDTO dto2 = new FightDiceUpdateDTO(2, 11, "player2", "BLACK", 5);

        assertThat(dto1.getMatchId()).isNotEqualTo(dto2.getMatchId());
        assertThat(dto1.getPlayerId()).isNotEqualTo(dto2.getPlayerId());
        assertThat(dto1.getDiceValue()).isNotEqualTo(dto2.getDiceValue());
    }
}
