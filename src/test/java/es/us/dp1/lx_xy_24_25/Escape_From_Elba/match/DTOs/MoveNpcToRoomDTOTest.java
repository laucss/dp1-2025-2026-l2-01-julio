package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MoveNpcToRoomDTO Tests")
public class MoveNpcToRoomDTOTest {

    private MoveNpcToRoomDTO dto;

    @BeforeEach
    void setUp() {
        dto = new MoveNpcToRoomDTO();
    }

    @Test
    @DisplayName("Should create empty MoveNpcToRoomDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getRoomId()).isNull();
        assertThat(dto.getNpcId()).isNull();
    }

    @Test
    @DisplayName("Should create MoveNpcToRoomDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        MoveNpcToRoomDTO dto = new MoveNpcToRoomDTO(10, 5, 3);

        assertThat(dto.getUserId()).isEqualTo(10);
        assertThat(dto.getRoomId()).isEqualTo(5);
        assertThat(dto.getNpcId()).isEqualTo(3);
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
    @DisplayName("Should set and get npcId correctly")
    void testSetAndGetNpcId() {
        dto.setNpcId(4);
        assertThat(dto.getNpcId()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setUserId(10);
        dto.setRoomId(5);
        dto.setNpcId(3);

        dto.setUserId(null);
        dto.setRoomId(null);
        dto.setNpcId(null);

        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getRoomId()).isNull();
        assertThat(dto.getNpcId()).isNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        dto.setUserId(10);
        dto.setRoomId(5);
        dto.setNpcId(3);

        assertThat(dto.getUserId()).isEqualTo(10);
        assertThat(dto.getRoomId()).isEqualTo(5);
        assertThat(dto.getNpcId()).isEqualTo(3);

        dto.setUserId(11);
        dto.setRoomId(6);
        dto.setNpcId(4);

        assertThat(dto.getUserId()).isEqualTo(11);
        assertThat(dto.getRoomId()).isEqualTo(6);
        assertThat(dto.getNpcId()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        MoveNpcToRoomDTO dto1 = new MoveNpcToRoomDTO(10, 5, 3);
        MoveNpcToRoomDTO dto2 = new MoveNpcToRoomDTO(11, 6, 4);

        assertThat(dto1.getUserId()).isNotEqualTo(dto2.getUserId());
        assertThat(dto1.getRoomId()).isNotEqualTo(dto2.getRoomId());
        assertThat(dto1.getNpcId()).isNotEqualTo(dto2.getNpcId());
    }

    @Test
    @DisplayName("Should handle large ID values")
    void testLargeIdValues() {
        dto.setUserId(Integer.MAX_VALUE);
        dto.setRoomId(Integer.MAX_VALUE);
        dto.setNpcId(Integer.MAX_VALUE);

        assertThat(dto.getUserId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getRoomId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getNpcId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle zero IDs")
    void testZeroIds() {
        dto.setUserId(0);
        dto.setRoomId(0);
        dto.setNpcId(0);

        assertThat(dto.getUserId()).isZero();
        assertThat(dto.getRoomId()).isZero();
        assertThat(dto.getNpcId()).isZero();
    }

    @Test
    @DisplayName("Should handle negative ID values")
    void testNegativeIds() {
        dto.setUserId(-1);
        dto.setRoomId(-2);
        dto.setNpcId(-3);

        assertThat(dto.getUserId()).isEqualTo(-1);
        assertThat(dto.getRoomId()).isEqualTo(-2);
        assertThat(dto.getNpcId()).isEqualTo(-3);
    }

    @Test
    @DisplayName("Should support multiple NPC movements")
    void testMultipleNpcMovements() {
        MoveNpcToRoomDTO move1 = new MoveNpcToRoomDTO(10, 5, 1);
        MoveNpcToRoomDTO move2 = new MoveNpcToRoomDTO(10, 6, 2);

        assertThat(move1.getUserId()).isEqualTo(move2.getUserId());
        assertThat(move1.getRoomId()).isNotEqualTo(move2.getRoomId());
        assertThat(move1.getNpcId()).isNotEqualTo(move2.getNpcId());
    }
}
