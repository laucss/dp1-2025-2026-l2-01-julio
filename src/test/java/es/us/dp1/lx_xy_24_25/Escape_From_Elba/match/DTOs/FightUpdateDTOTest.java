package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightUpdateDTO;

import static org.assertj.core.api.Assertions.*;

@DisplayName("FightUpdateDTO Tests")
public class FightUpdateDTOTest {

    private FightUpdateDTO dto;

    @BeforeEach
    void setUp() {
        dto = new FightUpdateDTO();
    }

    @Test
    @DisplayName("Should create empty FightUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getAttackerId()).isNull();
        assertThat(dto.getAttackerUsername()).isNull();
        assertThat(dto.getDefenderId()).isNull();
        assertThat(dto.getDefenderUsername()).isNull();
        assertThat(dto.getRoomName()).isNull();
        assertThat(dto.getRoomId()).isNull();
        assertThat(dto.getAction()).isNull();
        assertThat(dto.getIsBot()).isNull();
    }

    @Test
    @DisplayName("Should create FightUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        FightUpdateDTO dto = new FightUpdateDTO(1, 10, "attacker1", 11, "defender1", "Room1", "START");

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getAttackerId()).isEqualTo(10);
        assertThat(dto.getAttackerUsername()).isEqualTo("attacker1");
        assertThat(dto.getDefenderId()).isEqualTo(11);
        assertThat(dto.getDefenderUsername()).isEqualTo("defender1");
        assertThat(dto.getRoomName()).isEqualTo("Room1");
        assertThat(dto.getAction()).isEqualTo("START");
    }

    @Test
    @DisplayName("Should set and get matchId correctly")
    void testSetAndGetMatchId() {
        dto.setMatchId(5);
        assertThat(dto.getMatchId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should set and get attackerId correctly")
    void testSetAndGetAttackerId() {
        dto.setAttackerId(15);
        assertThat(dto.getAttackerId()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should set and get attackerUsername correctly")
    void testSetAndGetAttackerUsername() {
        dto.setAttackerUsername("player1");
        assertThat(dto.getAttackerUsername()).isEqualTo("player1");
    }

    @Test
    @DisplayName("Should set and get defenderId correctly")
    void testSetAndGetDefenderId() {
        dto.setDefenderId(16);
        assertThat(dto.getDefenderId()).isEqualTo(16);
    }

    @Test
    @DisplayName("Should set and get defenderUsername correctly")
    void testSetAndGetDefenderUsername() {
        dto.setDefenderUsername("player2");
        assertThat(dto.getDefenderUsername()).isEqualTo("player2");
    }

    @Test
    @DisplayName("Should set and get roomName correctly")
    void testSetAndGetRoomName() {
        dto.setRoomName("Kitchen");
        assertThat(dto.getRoomName()).isEqualTo("Kitchen");
    }

    @Test
    @DisplayName("Should set and get roomId correctly")
    void testSetAndGetRoomId() {
        dto.setRoomId(7);
        assertThat(dto.getRoomId()).isEqualTo(7);
    }

    @Test
    @DisplayName("Should set and get action correctly")
    void testSetAndGetAction() {
        dto.setAction("START");
        assertThat(dto.getAction()).isEqualTo("START");

        dto.setAction("END");
        assertThat(dto.getAction()).isEqualTo("END");
    }

    @Test
    @DisplayName("Should set and get isBot flag correctly")
    void testSetAndGetIsBot() {
        dto.setIsBot(true);
        assertThat(dto.getIsBot()).isTrue();

        dto.setIsBot(false);
        assertThat(dto.getIsBot()).isFalse();
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setMatchId(1);
        dto.setAttackerId(10);
        dto.setAttackerUsername("player1");
        dto.setAction("START");
        dto.setIsBot(false);

        dto.setMatchId(null);
        dto.setAttackerId(null);
        dto.setAttackerUsername(null);
        dto.setAction(null);
        dto.setIsBot(null);

        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getAttackerId()).isNull();
        assertThat(dto.getAttackerUsername()).isNull();
        assertThat(dto.getAction()).isNull();
        assertThat(dto.getIsBot()).isNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setMatchId(1);
        dto.setAttackerId(10);
        dto.setAction("START");

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getAttackerId()).isEqualTo(10);
        assertThat(dto.getAction()).isEqualTo("START");

        dto.setMatchId(2);
        dto.setAttackerId(11);
        dto.setAction("END");

        assertThat(dto.getMatchId()).isEqualTo(2);
        assertThat(dto.getAttackerId()).isEqualTo(11);
        assertThat(dto.getAction()).isEqualTo("END");
    }

    @Test
    @DisplayName("Should handle fight with bot")
    void testFightWithBot() {
        FightUpdateDTO dto = new FightUpdateDTO(1, 10, "player1", 11, "NPC", "Room1", "START");
        dto.setIsBot(true);

        assertThat(dto.getDefenderUsername()).isEqualTo("NPC");
        assertThat(dto.getIsBot()).isTrue();
    }

    @Test
    @DisplayName("Should handle fight between players")
    void testFightBetweenPlayers() {
        FightUpdateDTO dto = new FightUpdateDTO(1, 10, "player1", 11, "player2", "Room1", "START");
        dto.setIsBot(false);

        assertThat(dto.getAttackerUsername()).isEqualTo("player1");
        assertThat(dto.getDefenderUsername()).isEqualTo("player2");
        assertThat(dto.getIsBot()).isFalse();
    }

    @Test
    @DisplayName("Should verify toString method works")
    void testToString() {
        FightUpdateDTO dto = new FightUpdateDTO(1, 10, "player1", 11, "player2", "Room1", "START");
        String toString = dto.toString();

        assertThat(toString).contains("FightUpdateDTO");
        assertThat(toString).contains("matchId=1");
        assertThat(toString).contains("attackerId=10");
        assertThat(toString).contains("action=");
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        FightUpdateDTO dto1 = new FightUpdateDTO(1, 10, "p1", 11, "p2", "R1", "START");
        FightUpdateDTO dto2 = new FightUpdateDTO(2, 20, "p3", 21, "p4", "R2", "END");

        assertThat(dto1.getMatchId()).isNotEqualTo(dto2.getMatchId());
        assertThat(dto1.getAttackerId()).isNotEqualTo(dto2.getAttackerId());
        assertThat(dto1.getAction()).isNotEqualTo(dto2.getAction());
    }
}
