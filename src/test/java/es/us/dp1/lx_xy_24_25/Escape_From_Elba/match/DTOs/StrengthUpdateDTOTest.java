package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StrengthUpdateDTO Tests")
public class StrengthUpdateDTOTest {

    private StrengthUpdateDTO dto;

    @BeforeEach
    void setUp() {
        dto = new StrengthUpdateDTO();
    }

    @Test
    @DisplayName("Should create empty StrengthUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getStrength()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should create StrengthUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        StrengthUpdateDTO dto = new StrengthUpdateDTO(1, 10, "testUser", 5, 1234567890L);

        assertThat(dto.getPlayerId()).isEqualTo(1);
        assertThat(dto.getUserId()).isEqualTo(10);
        assertThat(dto.getUsername()).isEqualTo("testUser");
        assertThat(dto.getStrength()).isEqualTo(5);
        assertThat(dto.getTimestamp()).isEqualTo(1234567890L);
    }

    @Test
    @DisplayName("Should set and get playerId correctly")
    void testSetAndGetPlayerId() {
        dto.setPlayerId(42);
        assertThat(dto.getPlayerId()).isEqualTo(42);
    }

    @Test
    @DisplayName("Should set and get userId correctly")
    void testSetAndGetUserId() {
        dto.setUserId(100);
        assertThat(dto.getUserId()).isEqualTo(100);
    }

    @Test
    @DisplayName("Should set and get username correctly")
    void testSetAndGetUsername() {
        dto.setUsername("playerName");
        assertThat(dto.getUsername()).isEqualTo("playerName");
    }

    @Test
    @DisplayName("Should set and get strength correctly")
    void testSetAndGetStrength() {
        dto.setStrength(8);
        assertThat(dto.getStrength()).isEqualTo(8);
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
        dto.setPlayerId(1);
        dto.setUserId(10);
        dto.setUsername("test");
        dto.setStrength(5);
        dto.setTimestamp(123L);

        dto.setPlayerId(null);
        dto.setUserId(null);
        dto.setUsername(null);
        dto.setStrength(null);
        dto.setTimestamp(null);

        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getStrength()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setPlayerId(1);
        dto.setStrength(3);
        assertThat(dto.getPlayerId()).isEqualTo(1);
        assertThat(dto.getStrength()).isEqualTo(3);

        dto.setPlayerId(2);
        dto.setStrength(5);
        assertThat(dto.getPlayerId()).isEqualTo(2);
        assertThat(dto.getStrength()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should verify toString method works")
    void testToString() {
        StrengthUpdateDTO dto = new StrengthUpdateDTO(1, 10, "user1", 5, 1000L);
        String toString = dto.toString();

        assertThat(toString).contains("StrengthUpdateDTO");
        assertThat(toString).contains("playerId=1");
        assertThat(toString).contains("userId=10");
        assertThat(toString).contains("username=");
        assertThat(toString).contains("strength=5");
    }

    @Test
    @DisplayName("Should handle zero strength")
    void testZeroStrength() {
        dto.setStrength(0);
        assertThat(dto.getStrength()).isZero();
    }

    @Test
    @DisplayName("Should handle negative strength values")
    void testNegativeStrength() {
        dto.setStrength(-1);
        assertThat(dto.getStrength()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should handle large strength values")
    void testLargeStrengthValues() {
        dto.setStrength(100);
        assertThat(dto.getStrength()).isEqualTo(100);
    }

    @Test
    @DisplayName("Should handle large ID values")
    void testLargeIdValues() {
        dto.setPlayerId(Integer.MAX_VALUE);
        dto.setUserId(Integer.MAX_VALUE);
        dto.setTimestamp(Long.MAX_VALUE);

        assertThat(dto.getPlayerId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getUserId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getTimestamp()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        StrengthUpdateDTO dto1 = new StrengthUpdateDTO(1, 10, "user1", 5, 1000L);
        StrengthUpdateDTO dto2 = new StrengthUpdateDTO(2, 20, "user2", 10, 2000L);

        assertThat(dto1.getPlayerId()).isNotEqualTo(dto2.getPlayerId());
        assertThat(dto1.getStrength()).isNotEqualTo(dto2.getStrength());
    }
}
