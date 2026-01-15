package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MoveToRoomDTO Tests")
public class MoveToRoomDTOTest {

    private MoveToRoomDTO dto;

    @BeforeEach
    void setUp() {
        dto = new MoveToRoomDTO();
    }

    @Test
    @DisplayName("Should create empty MoveToRoomDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getRoomId()).isNull();
    }

    @Test
    @DisplayName("Should create MoveToRoomDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        MoveToRoomDTO dto = new MoveToRoomDTO(10, 5);

        assertThat(dto.getUserId()).isEqualTo(10);
        assertThat(dto.getRoomId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should set and get userId correctly")
    void testSetAndGetUserId() {
        dto.setUserId(15);
        assertThat(dto.getUserId()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should set and get roomId correctly")
    void testSetAndGetRoomId() {
        dto.setRoomId(7);
        assertThat(dto.getRoomId()).isEqualTo(7);
    }

    @Test
    @DisplayName("Should handle null userId")
    void testNullUserId() {
        dto.setUserId(10);
        dto.setRoomId(5);

        dto.setUserId(null);

        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getRoomId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should handle null roomId")
    void testNullRoomId() {
        dto.setUserId(10);
        dto.setRoomId(5);

        dto.setRoomId(null);

        assertThat(dto.getUserId()).isEqualTo(10);
        assertThat(dto.getRoomId()).isNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setUserId(10);
        dto.setRoomId(5);

        assertThat(dto.getUserId()).isEqualTo(10);
        assertThat(dto.getRoomId()).isEqualTo(5);

        dto.setUserId(11);
        dto.setRoomId(6);

        assertThat(dto.getUserId()).isEqualTo(11);
        assertThat(dto.getRoomId()).isEqualTo(6);
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        MoveToRoomDTO dto1 = new MoveToRoomDTO(10, 5);
        MoveToRoomDTO dto2 = new MoveToRoomDTO(11, 6);

        assertThat(dto1.getUserId()).isNotEqualTo(dto2.getUserId());
        assertThat(dto1.getRoomId()).isNotEqualTo(dto2.getRoomId());
    }

    @Test
    @DisplayName("Should handle large ID values")
    void testLargeIdValues() {
        dto.setUserId(Integer.MAX_VALUE);
        dto.setRoomId(Integer.MAX_VALUE);

        assertThat(dto.getUserId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getRoomId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle zero IDs")
    void testZeroIds() {
        dto.setUserId(0);
        dto.setRoomId(0);

        assertThat(dto.getUserId()).isZero();
        assertThat(dto.getRoomId()).isZero();
    }

    @Test
    @DisplayName("Should handle negative ID values")
    void testNegativeIds() {
        dto.setUserId(-1);
        dto.setRoomId(-2);

        assertThat(dto.getUserId()).isEqualTo(-1);
        assertThat(dto.getRoomId()).isEqualTo(-2);
    }

    @Test
    @DisplayName("Should support multiple player movements")
    void testMultiplePlayerMovements() {
        MoveToRoomDTO move1 = new MoveToRoomDTO(10, 5);
        MoveToRoomDTO move2 = new MoveToRoomDTO(11, 5);

        assertThat(move1.getUserId()).isNotEqualTo(move2.getUserId());
        assertThat(move1.getRoomId()).isEqualTo(move2.getRoomId());
    }

    @Test
    @DisplayName("Should support movements to different rooms")
    void testMovementsToDifferentRooms() {
        MoveToRoomDTO move1 = new MoveToRoomDTO(10, 5);
        MoveToRoomDTO move2 = new MoveToRoomDTO(10, 6);

        assertThat(move1.getUserId()).isEqualTo(move2.getUserId());
        assertThat(move1.getRoomId()).isNotEqualTo(move2.getRoomId());
    }
}
