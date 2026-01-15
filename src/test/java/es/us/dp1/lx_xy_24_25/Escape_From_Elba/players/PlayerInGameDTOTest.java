package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

@DisplayName("PlayerInGameDTO Tests")
class PlayerInGameDTOTest {

    private Player testPlayer;
    private User testUser;
    private Room testRoom;
    private HandInGame testHandInGame;
    private BagInGame testBagInGame;

    @BeforeEach
    void setUp() {
        // Configurar usuario
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testPlayer");

        // Configurar sala
        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");
        testRoom.setBlackDice(4);
        testRoom.setWhiteDice(3);

        // Configurar jugador
        testPlayer = new Player();
        testPlayer.setId(1);
        testPlayer.setStrength(10);
        testPlayer.setActionPoints(5);
        testPlayer.setUser(testUser);
        testPlayer.setRoom(testRoom);

        // Configurar mano y bolsa en juego
        testHandInGame = new HandInGame();

        testBagInGame = new BagInGame();
    }

    @Test
    @DisplayName("Debe crear PlayerInGameDTO con constructor vacío")
    void testDefaultConstructor() {
        // When
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // Then
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getStrength());
        assertNull(dto.getActionPoints());
        assertNull(dto.getUser());
        assertNull(dto.getCurrentRoom());
        assertNull(dto.getHand());
        assertNull(dto.getBag());
    }

    @Test
    @DisplayName("Debe crear PlayerInGameDTO correctamente desde Player")
    void testConstructorWithPlayer() {
        // When
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer);

        // Then
        assertNotNull(dto);
        assertEquals(testPlayer.getId(), dto.getId());
        assertEquals(testPlayer.getStrength(), dto.getStrength());
        assertEquals(testPlayer.getActionPoints(), dto.getActionPoints());
        assertEquals(testPlayer.getUser(), dto.getUser());
        assertNotNull(dto.getCurrentRoom());
        assertEquals(testRoom.getId(), dto.getCurrentRoom().getId());
        assertNull(dto.getHand());
        assertNull(dto.getBag());
    }

    @Test
    @DisplayName("Debe crear PlayerInGameDTO con Player, Hand y Bag")
    void testConstructorWithPlayerAndGameData() {
        // When
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer, testHandInGame, testBagInGame);

        // Then
        assertNotNull(dto);
        assertEquals(testPlayer.getId(), dto.getId());
        assertEquals(testPlayer.getStrength(), dto.getStrength());
        assertEquals(testPlayer.getActionPoints(), dto.getActionPoints());
        assertEquals(testPlayer.getUser(), dto.getUser());
        assertNotNull(dto.getCurrentRoom());
        assertEquals(testRoom.getId(), dto.getCurrentRoom().getId());
        assertNotNull(dto.getHand());
        assertNotNull(dto.getBag());
    }

    @Test
    @DisplayName("Debe manejar Player sin Room")
    void testConstructorWithPlayerWithoutRoom() {
        // Given
        testPlayer.setRoom(null);

        // When
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer);

        // Then
        assertNotNull(dto);
        assertEquals(testPlayer.getId(), dto.getId());
        assertNull(dto.getCurrentRoom());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener id")
    void testSetAndGetId() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // When
        dto.setId(100);

        // Then
        assertEquals(100, dto.getId());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener strength")
    void testSetAndGetStrength() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // When
        dto.setStrength(15);

        // Then
        assertEquals(15, dto.getStrength());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener actionPoints")
    void testSetAndGetActionPoints() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // When
        dto.setActionPoints(7);

        // Then
        assertEquals(7, dto.getActionPoints());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener user")
    void testSetAndGetUser() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // When
        dto.setUser(testUser);

        // Then
        assertEquals(testUser, dto.getUser());
        assertEquals(testUser.getId(), dto.getUser().getId());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener currentRoom")
    void testSetAndGetCurrentRoom() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();
        RoomDTO roomDto = new RoomDTO(testRoom);

        // When
        dto.setCurrentRoom(roomDto);

        // Then
        assertEquals(roomDto, dto.getCurrentRoom());
        assertEquals(testRoom.getId(), dto.getCurrentRoom().getId());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener hand")
    void testSetAndGetHand() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();
        HandInGameDTO handDto = new HandInGameDTO(testHandInGame);

        // When
        dto.setHand(handDto);

        // Then
        assertEquals(handDto, dto.getHand());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener bag")
    void testSetAndGetBag() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();
        BagInGameDTO bagDto = new BagInGameDTO(testBagInGame);

        // When
        dto.setBag(bagDto);

        // Then
        assertEquals(bagDto, dto.getBag());
    }

    @Test
    @DisplayName("Debe mantener coherencia entre getters y setters")
    void testGettersAndSettersConsistency() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();
        RoomDTO roomDto = new RoomDTO(testRoom);
        HandInGameDTO handDto = new HandInGameDTO(testHandInGame);
        BagInGameDTO bagDto = new BagInGameDTO(testBagInGame);

        // When
        dto.setId(50);
        dto.setStrength(12);
        dto.setActionPoints(6);
        dto.setUser(testUser);
        dto.setCurrentRoom(roomDto);
        dto.setHand(handDto);
        dto.setBag(bagDto);

        // Then
        assertEquals(50, dto.getId());
        assertEquals(12, dto.getStrength());
        assertEquals(6, dto.getActionPoints());
        assertEquals(testUser, dto.getUser());
        assertEquals(roomDto, dto.getCurrentRoom());
        assertEquals(handDto, dto.getHand());
        assertEquals(bagDto, dto.getBag());
    }

    @Test
    @DisplayName("Debe copiar correctamente todos los datos del Player")
    void testCompletePlayerDataCopy() {
        // When
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer);

        // Then
        assertEquals(testPlayer.getId(), dto.getId());
        assertEquals(testPlayer.getStrength(), dto.getStrength());
        assertEquals(testPlayer.getActionPoints(), dto.getActionPoints());
        assertEquals(testPlayer.getUser().getId(), dto.getUser().getId());
        assertEquals(testPlayer.getRoom().getId(), dto.getCurrentRoom().getId());
    }

    @Test
    @DisplayName("Debe permitir modificar valores múltiples veces")
    void testMultipleModifications() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer);

        // First modification
        dto.setStrength(8);
        assertEquals(8, dto.getStrength());

        // Second modification
        dto.setStrength(12);
        assertEquals(12, dto.getStrength());

        // Third modification
        dto.setStrength(15);
        assertEquals(15, dto.getStrength());
    }

    @Test
    @DisplayName("Debe manejar valores null para currentRoom")
    void testCurrentRoomNull() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // When
        dto.setCurrentRoom(null);

        // Then
        assertNull(dto.getCurrentRoom());
    }

    @Test
    @DisplayName("Debe manejar valores null para user")
    void testUserNull() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // When
        dto.setUser(null);

        // Then
        assertNull(dto.getUser());
    }

    @Test
    @DisplayName("Debe manejar valores null para hand")
    void testHandNull() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // When
        dto.setHand(null);

        // Then
        assertNull(dto.getHand());
    }

    @Test
    @DisplayName("Debe manejar valores null para bag")
    void testBagNull() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // When
        dto.setBag(null);

        // Then
        assertNull(dto.getBag());
    }

    @Test
    @DisplayName("Debe cambiar currentRoom de null a Room")
    void testChangeCurrentRoomFromNullToRoom() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();
        assertNull(dto.getCurrentRoom());
        RoomDTO roomDto = new RoomDTO(testRoom);

        // When
        dto.setCurrentRoom(roomDto);

        // Then
        assertNotNull(dto.getCurrentRoom());
        assertEquals(roomDto, dto.getCurrentRoom());
    }

    @Test
    @DisplayName("Debe cambiar currentRoom de Room a null")
    void testChangeCurrentRoomFromRoomToNull() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer);
        assertNotNull(dto.getCurrentRoom());

        // When
        dto.setCurrentRoom(null);

        // Then
        assertNull(dto.getCurrentRoom());
    }

    @Test
    @DisplayName("Debe crear instancias independientes")
    void testIndependentInstances() {
        // Given
        PlayerInGameDTO dto1 = new PlayerInGameDTO(testPlayer);
        PlayerInGameDTO dto2 = new PlayerInGameDTO(testPlayer);

        // When
        dto1.setStrength(5);
        dto2.setStrength(10);

        // Then
        assertEquals(5, dto1.getStrength());
        assertEquals(10, dto2.getStrength());
        assertNotEquals(dto1.getStrength(), dto2.getStrength());
    }

    @Test
    @DisplayName("Debe manejar strength con valores 0 y positivos")
    void testStrengthValues() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // When/Then - Zero strength
        dto.setStrength(0);
        assertEquals(0, dto.getStrength());

        // When/Then - Positive strength
        dto.setStrength(20);
        assertEquals(20, dto.getStrength());

        // When/Then - Large strength value
        dto.setStrength(100);
        assertEquals(100, dto.getStrength());
    }

    @Test
    @DisplayName("Debe manejar actionPoints con valores 0 y positivos")
    void testActionPointsValues() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO();

        // When/Then - Zero action points
        dto.setActionPoints(0);
        assertEquals(0, dto.getActionPoints());

        // When/Then - Positive action points
        dto.setActionPoints(10);
        assertEquals(10, dto.getActionPoints());

        // When/Then - Large action points value
        dto.setActionPoints(50);
        assertEquals(50, dto.getActionPoints());
    }

    @Test
    @DisplayName("Debe mantener referencia de user correctamente")
    void testUserReference() {
        // When
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer);

        // Then
        assertNotNull(dto.getUser());
        assertSame(testPlayer.getUser(), dto.getUser());
        assertEquals(testPlayer.getUser().getId(), dto.getUser().getId());
    }

    @Test
    @DisplayName("Debe crear RoomDTO desde Room en constructor")
    void testRoomDTOCreation() {
        // When
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer);

        // Then
        assertNotNull(dto.getCurrentRoom());
        assertIsInstance(RoomDTO.class, dto.getCurrentRoom());
        assertEquals(testRoom.getId(), dto.getCurrentRoom().getId());
        assertEquals(testRoom.getName(), dto.getCurrentRoom().getName());
    }

    @Test
    @DisplayName("Debe crear HandInGameDTO desde HandInGame en constructor")
    void testHandInGameDTOCreation() {
        // When
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer, testHandInGame, testBagInGame);

        // Then
        assertNotNull(dto.getHand());
        assertIsInstance(HandInGameDTO.class, dto.getHand());
    }

    @Test
    @DisplayName("Debe crear BagInGameDTO desde BagInGame en constructor")
    void testBagInGameDTOCreation() {
        // When
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer, testHandInGame, testBagInGame);

        // Then
        assertNotNull(dto.getBag());
        assertIsInstance(BagInGameDTO.class, dto.getBag());
    }

    @Test
    @DisplayName("Debe manejar cambios en múltiples campos simultáneamente")
    void testMultipleFieldChanges() {
        // Given
        PlayerInGameDTO dto = new PlayerInGameDTO(testPlayer);
        RoomDTO newRoom = new RoomDTO(testRoom);
        User newUser = new User();
        newUser.setId(2);
        newUser.setUsername("newPlayer");

        // When
        dto.setId(999);
        dto.setStrength(20);
        dto.setActionPoints(8);
        dto.setUser(newUser);
        dto.setCurrentRoom(newRoom);

        // Then
        assertEquals(999, dto.getId());
        assertEquals(20, dto.getStrength());
        assertEquals(8, dto.getActionPoints());
        assertEquals(newUser, dto.getUser());
        assertEquals(newRoom, dto.getCurrentRoom());
    }

    private void assertIsInstance(Class<?> expectedClass, Object obj) {
        assertTrue(expectedClass.isInstance(obj),
            "Expected object to be instance of " + expectedClass.getName() +
            " but was " + obj.getClass().getName());
    }
}
