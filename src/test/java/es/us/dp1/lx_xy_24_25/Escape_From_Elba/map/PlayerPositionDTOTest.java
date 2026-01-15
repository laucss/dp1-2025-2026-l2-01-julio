package es.us.dp1.lx_xy_24_25.Escape_From_Elba.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PlayerPositionDTO Tests")
public class PlayerPositionDTOTest {

    private PlayerPositionDTO dto;

    @BeforeEach
    void setUp() {
        dto = new PlayerPositionDTO();
    }

    @Test
    @DisplayName("Should create empty PlayerPositionDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getRoomId()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should create PlayerPositionDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        PlayerPositionDTO dto = new PlayerPositionDTO(1, 10, 1234567890L);

        assertThat(dto.getPlayerId()).isEqualTo(1);
        assertThat(dto.getRoomId()).isEqualTo(10);
        assertThat(dto.getTimestamp()).isEqualTo(1234567890L);
    }

    @Test
    @DisplayName("Should set and get playerId correctly")
    void testSetAndGetPlayerId() {
        dto.setPlayerId(5);
        assertThat(dto.getPlayerId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should set and get roomId correctly")
    void testSetAndGetRoomId() {
        dto.setRoomId(20);
        assertThat(dto.getRoomId()).isEqualTo(20);
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
        dto.setRoomId(2);
        dto.setTimestamp(123L);

        dto.setPlayerId(null);
        dto.setRoomId(null);
        dto.setTimestamp(null);

        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getRoomId()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should update playerId from non-null to different value")
    void testUpdatePlayerId() {
        dto.setPlayerId(1);
        assertThat(dto.getPlayerId()).isEqualTo(1);

        dto.setPlayerId(2);
        assertThat(dto.getPlayerId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should update roomId from non-null to different value")
    void testUpdateRoomId() {
        dto.setRoomId(5);
        assertThat(dto.getRoomId()).isEqualTo(5);

        dto.setRoomId(10);
        assertThat(dto.getRoomId()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should update timestamp from non-null to different value")
    void testUpdateTimestamp() {
        dto.setTimestamp(1000L);
        assertThat(dto.getTimestamp()).isEqualTo(1000L);

        dto.setTimestamp(2000L);
        assertThat(dto.getTimestamp()).isEqualTo(2000L);
    }

    @Test
    @DisplayName("Should handle large playerId values")
    void testLargePlayerId() {
        dto.setPlayerId(Integer.MAX_VALUE);
        assertThat(dto.getPlayerId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle large roomId values")
    void testLargeRoomId() {
        dto.setRoomId(Integer.MAX_VALUE);
        assertThat(dto.getRoomId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle large timestamp values")
    void testLargeTimestamp() {
        dto.setTimestamp(Long.MAX_VALUE);
        assertThat(dto.getTimestamp()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("Should verify toString contains field values")
    void testToString() {
        dto.setPlayerId(1);
        dto.setRoomId(5);
        dto.setTimestamp(999L);

        String result = dto.toString();
        assertThat(result).contains("playerId=1");
        assertThat(result).contains("roomId=5");
        assertThat(result).contains("timestamp=999");
    }

    @Test
    @DisplayName("Should create independent instances")
    void testIndependentInstances() {
        PlayerPositionDTO dto1 = new PlayerPositionDTO(1, 10, 1000L);
        PlayerPositionDTO dto2 = new PlayerPositionDTO(2, 20, 2000L);

        assertThat(dto1.getPlayerId()).isNotEqualTo(dto2.getPlayerId());
        assertThat(dto1.getRoomId()).isNotEqualTo(dto2.getRoomId());
        assertThat(dto1.getTimestamp()).isNotEqualTo(dto2.getTimestamp());

        dto1.setPlayerId(99);
        assertThat(dto2.getPlayerId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should handle zero values")
    void testZeroValues() {
        dto.setPlayerId(0);
        dto.setRoomId(0);
        dto.setTimestamp(0L);

        assertThat(dto.getPlayerId()).isEqualTo(0);
        assertThat(dto.getRoomId()).isEqualTo(0);
        assertThat(dto.getTimestamp()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Should support equals and hashCode for Lombok @Data")
    void testEqualsAndHashCode() {
        PlayerPositionDTO dto1 = new PlayerPositionDTO(1, 10, 1000L);
        PlayerPositionDTO dto2 = new PlayerPositionDTO(1, 10, 1000L);
        PlayerPositionDTO dto3 = new PlayerPositionDTO(2, 20, 2000L);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }
}
