package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("HandUpdateDTO Tests")
public class HandUpdateDTOTest {

    private HandUpdateDTO dto;
    private HandInGameDTO handInGameDTO;

    @BeforeEach
    void setUp() {
        dto = new HandUpdateDTO();
        handInGameDTO = new HandInGameDTO();
    }

    @Test
    @DisplayName("Should create empty HandUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getHand()).isNull();
    }

    @Test
    @DisplayName("Should create HandUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        HandUpdateDTO dto = new HandUpdateDTO(1, 10, handInGameDTO);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getPlayerId()).isEqualTo(10);
        assertThat(dto.getHand()).isEqualTo(handInGameDTO);
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
    @DisplayName("Should set and get hand correctly")
    void testSetAndGetHand() {
        HandInGameDTO hand = new HandInGameDTO();
        dto.setHand(hand);
        assertThat(dto.getHand()).isEqualTo(hand);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setMatchId(1);
        dto.setPlayerId(10);
        dto.setHand(handInGameDTO);

        dto.setMatchId(null);
        dto.setPlayerId(null);
        dto.setHand(null);

        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getHand()).isNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        HandInGameDTO hand1 = new HandInGameDTO();
        HandInGameDTO hand2 = new HandInGameDTO();

        dto.setMatchId(1);
        dto.setPlayerId(10);
        dto.setHand(hand1);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getHand()).isEqualTo(hand1);

        dto.setMatchId(2);
        dto.setPlayerId(11);
        dto.setHand(hand2);

        assertThat(dto.getMatchId()).isEqualTo(2);
        assertThat(dto.getHand()).isEqualTo(hand2);
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        HandInGameDTO hand1 = new HandInGameDTO();
        HandInGameDTO hand2 = new HandInGameDTO();

        HandUpdateDTO dto1 = new HandUpdateDTO(1, 10, hand1);
        HandUpdateDTO dto2 = new HandUpdateDTO(2, 11, hand2);

        assertThat(dto1.getMatchId()).isNotEqualTo(dto2.getMatchId());
        assertThat(dto1.getPlayerId()).isNotEqualTo(dto2.getPlayerId());
        assertThat(dto1.getHand()).isNotEqualTo(dto2.getHand());
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
    @DisplayName("Should handle multiple updates with same hand")
    void testMultipleUpdatesWithSameHand() {
        HandInGameDTO hand = new HandInGameDTO();
        dto.setHand(hand);

        dto.setMatchId(1);
        dto.setPlayerId(10);

        assertThat(dto.getHand()).isSameAs(hand);

        dto.setMatchId(2);

        assertThat(dto.getHand()).isSameAs(hand);
    }

    @Test
    @DisplayName("Should verify hand update for different players")
    void testHandUpdateForDifferentPlayers() {
        HandInGameDTO hand1 = new HandInGameDTO();
        HandInGameDTO hand2 = new HandInGameDTO();

        HandUpdateDTO update1 = new HandUpdateDTO(1, 1, hand1);
        HandUpdateDTO update2 = new HandUpdateDTO(1, 2, hand2);

        assertThat(update1.getPlayerId()).isNotEqualTo(update2.getPlayerId());
        assertThat(update1.getHand()).isNotEqualTo(update2.getHand());
    }
}
