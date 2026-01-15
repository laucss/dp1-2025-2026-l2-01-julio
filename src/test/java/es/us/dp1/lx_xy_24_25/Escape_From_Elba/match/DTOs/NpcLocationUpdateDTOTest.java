package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("NpcLocationUpdateDTO Tests")
public class NpcLocationUpdateDTOTest {

    private NpcLocationUpdateDTO dto;
    private Npc testNpc;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");

        testNpc = new Npc();
        testNpc.setId(1);
        testNpc.setStrength(5);
        testNpc.setIsNiallCampbell(false);
        testNpc.setRoom(testRoom);

        dto = new NpcLocationUpdateDTO();
    }

    @Test
    @DisplayName("Should create empty NpcLocationUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getNpcId()).isNull();
        assertThat(dto.getIsNiallCampbell()).isNull();
        assertThat(dto.getNewRoom()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should create NpcLocationUpdateDTO from Npc entity")
    void testConstructorWithNpc() {
        NpcLocationUpdateDTO dto = new NpcLocationUpdateDTO(testNpc);

        assertThat(dto.getNpcId()).isEqualTo(1);
        assertThat(dto.getIsNiallCampbell()).isFalse();
        assertThat(dto.getNewRoom()).isNotNull();
        assertThat(dto.getNewRoom().getId()).isEqualTo(1);
        assertThat(dto.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should create NpcLocationUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        RoomDTO roomDTO = new RoomDTO(testRoom);
        NpcLocationUpdateDTO dto = new NpcLocationUpdateDTO(5, true, roomDTO, 1000L);

        assertThat(dto.getNpcId()).isEqualTo(5);
        assertThat(dto.getIsNiallCampbell()).isTrue();
        assertThat(dto.getNewRoom()).isEqualTo(roomDTO);
        assertThat(dto.getTimestamp()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("Should set and get npcId correctly")
    void testSetAndGetNpcId() {
        dto.setNpcId(10);
        assertThat(dto.getNpcId()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should set and get isNiallCampbell correctly")
    void testSetAndGetIsNiallCampbell() {
        dto.setIsNiallCampbell(true);
        assertThat(dto.getIsNiallCampbell()).isTrue();

        dto.setIsNiallCampbell(false);
        assertThat(dto.getIsNiallCampbell()).isFalse();
    }

    @Test
    @DisplayName("Should set and get newRoom correctly")
    void testSetAndGetNewRoom() {
        RoomDTO roomDTO = new RoomDTO(testRoom);
        dto.setNewRoom(roomDTO);
        assertThat(dto.getNewRoom()).isEqualTo(roomDTO);
    }

    @Test
    @DisplayName("Should set and get timestamp correctly")
    void testSetAndGetTimestamp() {
        Long timestamp = System.currentTimeMillis();
        dto.setTimestamp(timestamp);
        assertThat(dto.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should handle null room in Npc entity")
    void testConstructorWithNpcNullRoom() {
        testNpc.setRoom(null);
        NpcLocationUpdateDTO dto = new NpcLocationUpdateDTO(testNpc);

        assertThat(dto.getNpcId()).isEqualTo(1);
        assertThat(dto.getIsNiallCampbell()).isFalse();
        assertThat(dto.getNewRoom()).isNull();
        assertThat(dto.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle Niall Campbell NPC")
    void testNiallCampbellNpc() {
        testNpc.setIsNiallCampbell(true);
        NpcLocationUpdateDTO dto = new NpcLocationUpdateDTO(testNpc);

        assertThat(dto.getIsNiallCampbell()).isTrue();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        RoomDTO room1 = new RoomDTO(testRoom);
        RoomDTO room2 = new RoomDTO(testRoom);

        dto.setNpcId(1);
        dto.setIsNiallCampbell(false);
        dto.setNewRoom(room1);
        dto.setTimestamp(1000L);

        assertThat(dto.getNpcId()).isEqualTo(1);
        assertThat(dto.getNewRoom()).isEqualTo(room1);

        dto.setNpcId(2);
        dto.setIsNiallCampbell(true);
        dto.setNewRoom(room2);
        dto.setTimestamp(2000L);

        assertThat(dto.getNpcId()).isEqualTo(2);
        assertThat(dto.getIsNiallCampbell()).isTrue();
    }

    @Test
    @DisplayName("Should verify timestamp is set on creation from Npc")
    void testTimestampSetOnCreation() {
        Long before = System.currentTimeMillis();
        NpcLocationUpdateDTO dto = new NpcLocationUpdateDTO(testNpc);
        Long after = System.currentTimeMillis();

        assertThat(dto.getTimestamp()).isGreaterThanOrEqualTo(before);
        assertThat(dto.getTimestamp()).isLessThanOrEqualTo(after);
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        testNpc.setId(1);
        NpcLocationUpdateDTO dto1 = new NpcLocationUpdateDTO(testNpc);

        testNpc.setId(2);
        NpcLocationUpdateDTO dto2 = new NpcLocationUpdateDTO(testNpc);

        assertThat(dto1.getNpcId()).isNotEqualTo(dto2.getNpcId());
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        RoomDTO roomDTO = new RoomDTO(testRoom);
        dto.setNpcId(5);
        dto.setIsNiallCampbell(true);
        dto.setNewRoom(roomDTO);
        dto.setTimestamp(1000L);

        dto.setNpcId(null);
        dto.setIsNiallCampbell(null);
        dto.setNewRoom(null);
        dto.setTimestamp(null);

        assertThat(dto.getNpcId()).isNull();
        assertThat(dto.getIsNiallCampbell()).isNull();
        assertThat(dto.getNewRoom()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }
}
