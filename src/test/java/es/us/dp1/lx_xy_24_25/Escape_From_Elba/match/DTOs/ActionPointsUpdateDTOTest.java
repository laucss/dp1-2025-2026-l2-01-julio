package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ActionPointsUpdateDTO Tests")
public class ActionPointsUpdateDTOTest {

    private ActionPointsUpdateDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ActionPointsUpdateDTO();
    }

    @Test
    @DisplayName("Should create empty ActionPointsUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getActionPoints()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should create ActionPointsUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        ActionPointsUpdateDTO dto = new ActionPointsUpdateDTO(1, 10, "testUser", 5, 1234567890L);

        assertThat(dto.getPlayerId()).isEqualTo(1);
        assertThat(dto.getUserId()).isEqualTo(10);
        assertThat(dto.getUsername()).isEqualTo("testUser");
        assertThat(dto.getActionPoints()).isEqualTo(5);
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
    @DisplayName("Should set and get actionPoints correctly")
    void testSetAndGetActionPoints() {
        dto.setActionPoints(10);
        assertThat(dto.getActionPoints()).isEqualTo(10);
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
        dto.setActionPoints(5);
        dto.setTimestamp(123L);

        dto.setPlayerId(null);
        dto.setUserId(null);
        dto.setUsername(null);
        dto.setActionPoints(null);
        dto.setTimestamp(null);

        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getActionPoints()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setPlayerId(1);
        dto.setActionPoints(3);
        assertThat(dto.getPlayerId()).isEqualTo(1);
        assertThat(dto.getActionPoints()).isEqualTo(3);

        dto.setPlayerId(2);
        dto.setActionPoints(5);
        assertThat(dto.getPlayerId()).isEqualTo(2);
        assertThat(dto.getActionPoints()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should verify toString method works")
    void testToString() {
        ActionPointsUpdateDTO dto = new ActionPointsUpdateDTO(1, 10, "user1", 5, 1000L);
        String toString = dto.toString();
        
        assertThat(toString).contains("ActionPointsUpdateDTO");
        assertThat(toString).contains("playerId=1");
        assertThat(toString).contains("userId=10");
        assertThat(toString).contains("username=");
        assertThat(toString).contains("actionPoints=5");
    }

    @Test
    @DisplayName("Should handle zero action points")
    void testZeroActionPoints() {
        dto.setActionPoints(0);
        assertThat(dto.getActionPoints()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle negative action points")
    void testNegativeActionPoints() {
        dto.setActionPoints(-1);
        assertThat(dto.getActionPoints()).isEqualTo(-1);
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
        ActionPointsUpdateDTO dto1 = new ActionPointsUpdateDTO(1, 10, "user1", 5, 1000L);
        ActionPointsUpdateDTO dto2 = new ActionPointsUpdateDTO(2, 20, "user2", 10, 2000L);

        assertThat(dto1.getPlayerId()).isNotEqualTo(dto2.getPlayerId());
        assertThat(dto1.getActionPoints()).isNotEqualTo(dto2.getActionPoints());
    }
}
