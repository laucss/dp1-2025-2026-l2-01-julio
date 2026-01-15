package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("EscapeAttemptResultDTO Tests")
public class EscapeAttemptResultDTOTest {

    private EscapeAttemptResultDTO dto;

    @BeforeEach
    void setUp() {
        dto = new EscapeAttemptResultDTO();
    }

    @Test
    @DisplayName("Should create empty EscapeAttemptResultDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.isSuccess()).isFalse();
        assertThat(dto.getWinnerUserId()).isNull();
        assertThat(dto.isDiscardRequired()).isFalse();
    }

    @Test
    @DisplayName("Should create EscapeAttemptResultDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        EscapeAttemptResultDTO dto = new EscapeAttemptResultDTO(true, 10, false);

        assertThat(dto.isSuccess()).isTrue();
        assertThat(dto.getWinnerUserId()).isEqualTo(10);
        assertThat(dto.isDiscardRequired()).isFalse();
    }

    @Test
    @DisplayName("Should set and get success flag correctly")
    void testSetAndGetSuccess() {
        dto.setSuccess(true);
        assertThat(dto.isSuccess()).isTrue();

        dto.setSuccess(false);
        assertThat(dto.isSuccess()).isFalse();
    }

    @Test
    @DisplayName("Should set and get winnerUserId correctly")
    void testSetAndGetWinnerUserId() {
        dto.setWinnerUserId(5);
        assertThat(dto.getWinnerUserId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should set and get discardRequired flag correctly")
    void testSetAndGetDiscardRequired() {
        dto.setDiscardRequired(true);
        assertThat(dto.isDiscardRequired()).isTrue();

        dto.setDiscardRequired(false);
        assertThat(dto.isDiscardRequired()).isFalse();
    }

    @Test
    @DisplayName("Should handle null winnerUserId")
    void testNullWinnerUserId() {
        dto.setSuccess(true);
        dto.setWinnerUserId(10);
        dto.setDiscardRequired(true);

        dto.setWinnerUserId(null);

        assertThat(dto.getWinnerUserId()).isNull();
        assertThat(dto.isSuccess()).isTrue();
        assertThat(dto.isDiscardRequired()).isTrue();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setSuccess(true);
        dto.setWinnerUserId(1);
        dto.setDiscardRequired(false);

        assertThat(dto.isSuccess()).isTrue();
        assertThat(dto.getWinnerUserId()).isEqualTo(1);
        assertThat(dto.isDiscardRequired()).isFalse();

        dto.setSuccess(false);
        dto.setWinnerUserId(2);
        dto.setDiscardRequired(true);

        assertThat(dto.isSuccess()).isFalse();
        assertThat(dto.getWinnerUserId()).isEqualTo(2);
        assertThat(dto.isDiscardRequired()).isTrue();
    }

    @Test
    @DisplayName("Should handle successful escape attempt with discard")
    void testSuccessfulEscapeWithDiscard() {
        EscapeAttemptResultDTO dto = new EscapeAttemptResultDTO(true, 5, true);

        assertThat(dto.isSuccess()).isTrue();
        assertThat(dto.getWinnerUserId()).isEqualTo(5);
        assertThat(dto.isDiscardRequired()).isTrue();
    }

    @Test
    @DisplayName("Should handle failed escape attempt")
    void testFailedEscapeAttempt() {
        EscapeAttemptResultDTO dto = new EscapeAttemptResultDTO(false, null, false);

        assertThat(dto.isSuccess()).isFalse();
        assertThat(dto.getWinnerUserId()).isNull();
        assertThat(dto.isDiscardRequired()).isFalse();
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        EscapeAttemptResultDTO dto1 = new EscapeAttemptResultDTO(true, 1, true);
        EscapeAttemptResultDTO dto2 = new EscapeAttemptResultDTO(false, 2, false);

        assertThat(dto1.isSuccess()).isNotEqualTo(dto2.isSuccess());
        assertThat(dto1.getWinnerUserId()).isNotEqualTo(dto2.getWinnerUserId());
        assertThat(dto1.isDiscardRequired()).isNotEqualTo(dto2.isDiscardRequired());
    }

    @Test
    @DisplayName("Should handle large winnerUserId values")
    void testLargeWinnerUserId() {
        dto.setWinnerUserId(Integer.MAX_VALUE);
        assertThat(dto.getWinnerUserId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle negative winnerUserId values")
    void testNegativeWinnerUserId() {
        dto.setWinnerUserId(-1);
        assertThat(dto.getWinnerUserId()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should handle zero winnerUserId")
    void testZeroWinnerUserId() {
        dto.setWinnerUserId(0);
        assertThat(dto.getWinnerUserId()).isZero();
    }
}
