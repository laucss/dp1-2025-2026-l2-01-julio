package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DiceTotalsUpdateDTO Tests")
public class DiceTotalsUpdateDTOTest {

    private DiceTotalsUpdateDTO dto;

    @BeforeEach
    void setUp() {
        dto = new DiceTotalsUpdateDTO();
    }

    @Test
    @DisplayName("Should create empty DiceTotalsUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getAttackerId()).isNull();
        assertThat(dto.getAttackerTotal()).isNull();
        assertThat(dto.getDefenderId()).isNull();
        assertThat(dto.getDefenderTotal()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should create DiceTotalsUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        DiceTotalsUpdateDTO dto = new DiceTotalsUpdateDTO(1, 10, 2, 8, 1234567890L);

        assertThat(dto.getAttackerId()).isEqualTo(1);
        assertThat(dto.getAttackerTotal()).isEqualTo(10);
        assertThat(dto.getDefenderId()).isEqualTo(2);
        assertThat(dto.getDefenderTotal()).isEqualTo(8);
        assertThat(dto.getTimestamp()).isEqualTo(1234567890L);
    }

    @Test
    @DisplayName("Should set and get attackerId correctly")
    void testSetAndGetAttackerId() {
        dto.setAttackerId(5);
        assertThat(dto.getAttackerId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should set and get attackerTotal correctly")
    void testSetAndGetAttackerTotal() {
        dto.setAttackerTotal(15);
        assertThat(dto.getAttackerTotal()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should set and get defenderId correctly")
    void testSetAndGetDefenderId() {
        dto.setDefenderId(7);
        assertThat(dto.getDefenderId()).isEqualTo(7);
    }

    @Test
    @DisplayName("Should set and get defenderTotal correctly")
    void testSetAndGetDefenderTotal() {
        dto.setDefenderTotal(12);
        assertThat(dto.getDefenderTotal()).isEqualTo(12);
    }

    @Test
    @DisplayName("Should set and get timestamp correctly")
    void testSetAndGetTimestamp() {
        Long timestamp = System.currentTimeMillis();
        dto.setTimestamp(timestamp);
        assertThat(dto.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setAttackerId(1);
        dto.setAttackerTotal(10);
        dto.setDefenderId(2);
        dto.setDefenderTotal(8);
        dto.setTimestamp(123L);

        dto.setAttackerId(null);
        dto.setAttackerTotal(null);
        dto.setDefenderId(null);
        dto.setDefenderTotal(null);
        dto.setTimestamp(null);

        assertThat(dto.getAttackerId()).isNull();
        assertThat(dto.getAttackerTotal()).isNull();
        assertThat(dto.getDefenderId()).isNull();
        assertThat(dto.getDefenderTotal()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should handle different dice totals")
    void testDifferentDiceTotals() {
        dto.setAttackerTotal(6);
        dto.setDefenderTotal(5);

        assertThat(dto.getAttackerTotal()).isGreaterThan(dto.getDefenderTotal());
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setAttackerId(1);
        dto.setAttackerTotal(10);
        assertThat(dto.getAttackerId()).isEqualTo(1);
        assertThat(dto.getAttackerTotal()).isEqualTo(10);

        dto.setAttackerId(3);
        dto.setAttackerTotal(12);
        assertThat(dto.getAttackerId()).isEqualTo(3);
        assertThat(dto.getAttackerTotal()).isEqualTo(12);
    }

    @Test
    @DisplayName("Should verify toString method works")
    void testToString() {
        DiceTotalsUpdateDTO dto = new DiceTotalsUpdateDTO(1, 10, 2, 8, 1000L);
        String toString = dto.toString();

        assertThat(toString).contains("DiceTotalsUpdateDTO");
        assertThat(toString).contains("attackerId=1");
        assertThat(toString).contains("attackerTotal=10");
        assertThat(toString).contains("defenderId=2");
    }

    @Test
    @DisplayName("Should handle zero dice totals")
    void testZeroDiceTotals() {
        dto.setAttackerTotal(0);
        dto.setDefenderTotal(0);

        assertThat(dto.getAttackerTotal()).isZero();
        assertThat(dto.getDefenderTotal()).isZero();
    }

    @Test
    @DisplayName("Should handle large dice totals")
    void testLargeDiceTotals() {
        dto.setAttackerTotal(100);
        dto.setDefenderTotal(99);

        assertThat(dto.getAttackerTotal()).isEqualTo(100);
        assertThat(dto.getDefenderTotal()).isEqualTo(99);
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        DiceTotalsUpdateDTO dto1 = new DiceTotalsUpdateDTO(1, 10, 2, 8, 1000L);
        DiceTotalsUpdateDTO dto2 = new DiceTotalsUpdateDTO(3, 12, 4, 9, 2000L);

        assertThat(dto1.getAttackerId()).isNotEqualTo(dto2.getAttackerId());
        assertThat(dto1.getAttackerTotal()).isNotEqualTo(dto2.getAttackerTotal());
    }
}
