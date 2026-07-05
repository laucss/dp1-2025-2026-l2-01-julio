package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.LoseAgainstNpcRequestDTO;

import static org.assertj.core.api.Assertions.*;

@DisplayName("LoseAgainstNpcRequestDTO Tests")
public class LoseAgainstNpcRequestDTOTest {

    private LoseAgainstNpcRequestDTO dto;

    @BeforeEach
    void setUp() {
        dto = new LoseAgainstNpcRequestDTO();
    }

    @Test
    @DisplayName("Should create empty LoseAgainstNpcRequestDTO with default constructor")
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
    @DisplayName("Should handle null cardId")
    void testNullCardId() {
        dto.setCardId(10);
        dto.setFromWhere("hand");

        dto.setCardId(null);

        assertThat(dto.getCardId()).isNull();
        assertThat(dto.getFromWhere()).isEqualTo("hand");
    }

    @Test
    @DisplayName("Should handle null fromWhere")
    void testNullFromWhere() {
        dto.setCardId(5);
        dto.setFromWhere("hand");

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
    @DisplayName("Should support different fromWhere sources")
    void testDifferentFromWhereSources() {
        LoseAgainstNpcRequestDTO request1 = new LoseAgainstNpcRequestDTO();
        request1.setFromWhere("hand");

        LoseAgainstNpcRequestDTO request2 = new LoseAgainstNpcRequestDTO();
        request2.setFromWhere("bag");

        assertThat(request1.getFromWhere()).isNotEqualTo(request2.getFromWhere());
        assertThat(request1.getFromWhere()).isEqualTo("hand");
        assertThat(request2.getFromWhere()).isEqualTo("bag");
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        LoseAgainstNpcRequestDTO dto1 = new LoseAgainstNpcRequestDTO();
        dto1.setCardId(1);
        dto1.setFromWhere("hand");

        LoseAgainstNpcRequestDTO dto2 = new LoseAgainstNpcRequestDTO();
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

    @Test
    @DisplayName("Should validate fromWhere field usage")
    void testFromWhereFieldUsage() {
        dto.setCardId(10);
        dto.setFromWhere("hand");

        assertThat(dto.getCardId()).isEqualTo(10);
        assertThat(dto.getFromWhere()).isEqualTo("hand");
        assertThat(dto.getFromWhere()).startsWith("h");
    }
}
