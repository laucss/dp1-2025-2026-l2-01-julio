package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PlayerLocationUpdateDTO Tests")
public class PlayerLocationUpdateDTOTest {

    private PlayerLocationUpdateDTO dto;
    private Player testPlayer;
    private User testUser;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");

        testUser = new User();
        testUser.setId(10);
        testUser.setUsername("testUser");

        testPlayer = new Player();
        testPlayer.setId(1);
        testPlayer.setUser(testUser);
        testPlayer.setRoom(testRoom);

        dto = new PlayerLocationUpdateDTO();
    }

    @Test
    @DisplayName("Should create empty PlayerLocationUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getNewRoom()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should create PlayerLocationUpdateDTO from Player entity")
    void testConstructorWithPlayer() {
        PlayerLocationUpdateDTO dto = new PlayerLocationUpdateDTO(testPlayer);

        assertThat(dto.getPlayerId()).isEqualTo(1);
        assertThat(dto.getUserId()).isEqualTo(10);
        assertThat(dto.getUsername()).isEqualTo("testUser");
        assertThat(dto.getNewRoom()).isNotNull();
        assertThat(dto.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should create PlayerLocationUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        RoomDTO roomDTO = new RoomDTO(testRoom);
        PlayerLocationUpdateDTO dto = new PlayerLocationUpdateDTO(5, 10, "testUser", roomDTO, 1000L);

        assertThat(dto.getPlayerId()).isEqualTo(5);
        assertThat(dto.getUserId()).isEqualTo(10);
        assertThat(dto.getUsername()).isEqualTo("testUser");
        assertThat(dto.getNewRoom()).isEqualTo(roomDTO);
        assertThat(dto.getTimestamp()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("Should set and get playerId correctly")
    void testSetAndGetPlayerId() {
        dto.setPlayerId(10);
        assertThat(dto.getPlayerId()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should set and get userId correctly")
    void testSetAndGetUserId() {
        dto.setUserId(15);
        assertThat(dto.getUserId()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should set and get username correctly")
    void testSetAndGetUsername() {
        dto.setUsername("playerName");
        assertThat(dto.getUsername()).isEqualTo("playerName");
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
    @DisplayName("Should handle null room in Player entity")
    void testConstructorWithPlayerNullRoom() {
        testPlayer.setRoom(null);
        PlayerLocationUpdateDTO dto = new PlayerLocationUpdateDTO(testPlayer);

        assertThat(dto.getPlayerId()).isEqualTo(1);
        assertThat(dto.getNewRoom()).isNull();
        assertThat(dto.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        RoomDTO room1 = new RoomDTO(testRoom);
        RoomDTO room2 = new RoomDTO(testRoom);

        dto.setPlayerId(1);
        dto.setUserId(10);
        dto.setUsername("user1");
        dto.setNewRoom(room1);
        dto.setTimestamp(1000L);

        assertThat(dto.getPlayerId()).isEqualTo(1);
        assertThat(dto.getUserId()).isEqualTo(10);

        dto.setPlayerId(2);
        dto.setUserId(11);
        dto.setUsername("user2");
        dto.setNewRoom(room2);
        dto.setTimestamp(2000L);

        assertThat(dto.getPlayerId()).isEqualTo(2);
        assertThat(dto.getUserId()).isEqualTo(11);
        assertThat(dto.getUsername()).isEqualTo("user2");
    }

    @Test
    @DisplayName("Should verify timestamp is set on creation from Player")
    void testTimestampSetOnCreation() {
        Long before = System.currentTimeMillis();
        PlayerLocationUpdateDTO dto = new PlayerLocationUpdateDTO(testPlayer);
        Long after = System.currentTimeMillis();

        assertThat(dto.getTimestamp()).isGreaterThanOrEqualTo(before);
        assertThat(dto.getTimestamp()).isLessThanOrEqualTo(after);
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        testPlayer.setId(1);
        PlayerLocationUpdateDTO dto1 = new PlayerLocationUpdateDTO(testPlayer);

        testPlayer.setId(2);
        PlayerLocationUpdateDTO dto2 = new PlayerLocationUpdateDTO(testPlayer);

        assertThat(dto1.getPlayerId()).isNotEqualTo(dto2.getPlayerId());
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        RoomDTO roomDTO = new RoomDTO(testRoom);
        dto.setPlayerId(5);
        dto.setUserId(10);
        dto.setUsername("user");
        dto.setNewRoom(roomDTO);
        dto.setTimestamp(1000L);

        dto.setPlayerId(null);
        dto.setUserId(null);
        dto.setUsername(null);
        dto.setNewRoom(null);
        dto.setTimestamp(null);

        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getNewRoom()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("Should handle large ID values")
    void testLargeIdValues() {
        dto.setPlayerId(Integer.MAX_VALUE);
        dto.setUserId(Integer.MAX_VALUE);

        assertThat(dto.getPlayerId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getUserId()).isEqualTo(Integer.MAX_VALUE);
    }
}
