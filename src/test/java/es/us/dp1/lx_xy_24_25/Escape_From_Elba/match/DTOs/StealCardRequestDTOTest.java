package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StealCardRequestDTO Tests")
public class StealCardRequestDTOTest {

    private StealCardRequestDTO dto;

    @BeforeEach
    void setUp() {
        dto = new StealCardRequestDTO();
    }

    @Test
    @DisplayName("Should create empty StealCardRequestDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getCardId()).isNull();
        assertThat(dto.getFromWhere()).isNull();
    }

    @Test
    @DisplayName("Should set and get cardId correctly")
    void testSetAndGetCardId() {
        dto.setCardId(5);
        assertThat(dto.getCardId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should set and get fromWhere correctly")
    void testSetAndGetFromWhere() {
        dto.setFromWhere("hand");
        assertThat(dto.getFromWhere()).isEqualTo("hand");

        dto.setFromWhere("bag");
        assertThat(dto.getFromWhere()).isEqualTo("bag");
    }

    @Test
    @DisplayName("Should handle null cardId for random selection from hand")
    void testNullCardIdForRandomSelection() {
        dto.setCardId(null);
        dto.setFromWhere("hand");

        assertThat(dto.getCardId()).isNull();
        assertThat(dto.getFromWhere()).isEqualTo("hand");
    }

    @Test
    @DisplayName("Should handle specific cardId selection")
    void testSpecificCardIdSelection() {
        dto.setCardId(10);
        dto.setFromWhere("bag");

        assertThat(dto.getCardId()).isEqualTo(10);
        assertThat(dto.getFromWhere()).isEqualTo("bag");
    }

    @Test
    @DisplayName("Should handle null fromWhere")
    void testNullFromWhere() {
        dto.setCardId(5);
        dto.setFromWhere(null);

        assertThat(dto.getCardId()).isEqualTo(5);
        assertThat(dto.getFromWhere()).isNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setCardId(1);
        dto.setFromWhere("hand");

        assertThat(dto.getCardId()).isEqualTo(1);
        assertThat(dto.getFromWhere()).isEqualTo("hand");

        dto.setCardId(2);
        dto.setFromWhere("bag");

        assertThat(dto.getCardId()).isEqualTo(2);
        assertThat(dto.getFromWhere()).isEqualTo("bag");
    }

    @Test
    @DisplayName("Should support stealing from hand")
    void testStealFromHand() {
        StealCardRequestDTO request = new StealCardRequestDTO();
        request.setFromWhere("hand");

        assertThat(request.getFromWhere()).isEqualTo("hand");
    }

    @Test
    @DisplayName("Should support stealing from bag")
    void testStealFromBag() {
        StealCardRequestDTO request = new StealCardRequestDTO();
        request.setFromWhere("bag");

        assertThat(request.getFromWhere()).isEqualTo("bag");
    }

    @Test
    @DisplayName("Should support random selection when cardId is null")
    void testRandomSelectionFromSource() {
        StealCardRequestDTO request1 = new StealCardRequestDTO();
        request1.setCardId(null);
        request1.setFromWhere("hand");

        StealCardRequestDTO request2 = new StealCardRequestDTO();
        request2.setCardId(null);
        request2.setFromWhere("hand");

        assertThat(request1.getCardId()).isNull();
        assertThat(request2.getCardId()).isNull();
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        StealCardRequestDTO dto1 = new StealCardRequestDTO();
        dto1.setCardId(1);
        dto1.setFromWhere("hand");

        StealCardRequestDTO dto2 = new StealCardRequestDTO();
        dto2.setCardId(2);
        dto2.setFromWhere("bag");

        assertThat(dto1.getCardId()).isNotEqualTo(dto2.getCardId());
        assertThat(dto1.getFromWhere()).isNotEqualTo(dto2.getFromWhere());
    }

    @Test
    @DisplayName("Should handle large cardId values")
    void testLargeCardId() {
        dto.setCardId(Integer.MAX_VALUE);
        assertThat(dto.getCardId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle zero cardId")
    void testZeroCardId() {
        dto.setCardId(0);
        assertThat(dto.getCardId()).isZero();
    }

    @Test
    @DisplayName("Should handle negative cardId values")
    void testNegativeCardId() {
        dto.setCardId(-1);
        assertThat(dto.getCardId()).isEqualTo(-1);
    }
}
