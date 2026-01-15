package es.us.dp1.lx_xy_24_25.Escape_From_Elba.room;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

@DisplayName("RoomDTO Tests")
class RoomDTOTest {

    private Room testRoom;
    private Room adjacentRoom1;
    private Room adjacentRoom2;
    private Npc testNpc;
    private Player testPlayer;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Configurar salas adyacentes
        adjacentRoom1 = new Room();
        adjacentRoom1.setId(2);
        adjacentRoom1.setName("Adjacent Room 1");
        adjacentRoom1.setBlackDice(3);
        adjacentRoom1.setWhiteDice(4);

        adjacentRoom2 = new Room();
        adjacentRoom2.setId(3);
        adjacentRoom2.setName("Adjacent Room 2");
        adjacentRoom2.setBlackDice(5);
        adjacentRoom2.setWhiteDice(2);

        // Configurar sala principal
        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");
        testRoom.setBlackDice(4);
        testRoom.setWhiteDice(3);
        testRoom.setAdjacencyList(Arrays.asList(adjacentRoom1, adjacentRoom2));

        // Configurar usuario y jugador
        testUser = new User();
        testUser.setId(100);
        testUser.setUsername("testPlayer");

        testPlayer = new Player();
        testPlayer.setId(1);
        testPlayer.setUser(testUser);

        // Configurar NPC
        testNpc = new Npc();
        testNpc.setId(1);
    }

    @Test
    @DisplayName("Debe crear RoomDTO correctamente desde Room")
    void testConstructorWithRoom() {
        // When
        RoomDTO dto = new RoomDTO(testRoom);

        // Then
        assertNotNull(dto);
        assertEquals(testRoom.getId(), dto.getId());
        assertEquals(testRoom.getName(), dto.getName());
        assertEquals(testRoom.getBlackDice(), dto.getBlackDice());
        assertEquals(testRoom.getWhiteDice(), dto.getWhiteDice());
        assertEquals(testRoom.getAdjacencyList(), dto.getAdjacencyList());
        assertEquals(0, dto.getTimesVisited());
        assertNotNull(dto.getNpcsInside());
        assertTrue(dto.getNpcsInside().isEmpty());
        assertNull(dto.getPlayerInside());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener name")
    void testSetAndGetName() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);

        // When
        dto.setName("New Room Name");

        // Then
        assertEquals("New Room Name", dto.getName());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener blackDice")
    void testSetAndGetBlackDice() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);

        // When
        dto.setBlackDice(6);

        // Then
        assertEquals(6, dto.getBlackDice());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener whiteDice")
    void testSetAndGetWhiteDice() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);

        // When
        dto.setWhiteDice(5);

        // Then
        assertEquals(5, dto.getWhiteDice());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener timesVisited")
    void testSetAndGetTimesVisited() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);

        // When
        dto.setTimesVisited(5);

        // Then
        assertEquals(5, dto.getTimesVisited());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener adjacencyList")
    void testSetAndGetAdjacencyList() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);
        List<Room> newAdjacencyList = Arrays.asList(adjacentRoom1);

        // When
        dto.setAdjacencyList(newAdjacencyList);

        // Then
        assertEquals(newAdjacencyList, dto.getAdjacencyList());
        assertEquals(1, dto.getAdjacencyList().size());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener npcsInside")
    void testSetAndGetNpcsInside() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);
        List<Npc> npcs = new ArrayList<>();
        npcs.add(testNpc);

        // When
        dto.setNpcsInside(npcs);

        // Then
        assertEquals(npcs, dto.getNpcsInside());
        assertEquals(1, dto.getNpcsInside().size());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener playerInside")
    void testSetAndGetPlayerInside() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);

        // When
        dto.setPlayerInside(testPlayer);

        // Then
        assertEquals(testPlayer, dto.getPlayerInside());
        assertEquals(testPlayer.getId(), dto.getPlayerInside().getId());
    }

    @Test
    @DisplayName("Debe permitir establecer id heredado de BaseEntity")
    void testSetAndGetId() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);

        // When
        dto.setId(999);

        // Then
        assertEquals(999, dto.getId());
    }

    @Test
    @DisplayName("Debe inicializar timesVisited en 0 al construir")
    void testInitialTimesVisitedValue() {
        // When
        RoomDTO dto = new RoomDTO(testRoom);

        // Then
        assertEquals(0, dto.getTimesVisited());
    }

    @Test
    @DisplayName("Debe inicializar npcsInside como lista vacía al construir")
    void testInitialNpcsInsideValue() {
        // When
        RoomDTO dto = new RoomDTO(testRoom);

        // Then
        assertNotNull(dto.getNpcsInside());
        assertTrue(dto.getNpcsInside().isEmpty());
    }

    @Test
    @DisplayName("Debe inicializar playerInside como null al construir")
    void testInitialPlayerInsideValue() {
        // When
        RoomDTO dto = new RoomDTO(testRoom);

        // Then
        assertNull(dto.getPlayerInside());
    }

    @Test
    @DisplayName("Debe manejar adjacencyList vacía")
    void testEmptyAdjacencyList() {
        // Given
        testRoom.setAdjacencyList(new ArrayList<>());

        // When
        RoomDTO dto = new RoomDTO(testRoom);

        // Then
        assertNotNull(dto.getAdjacencyList());
        assertTrue(dto.getAdjacencyList().isEmpty());
    }

    @Test
    @DisplayName("Debe manejar múltiples NPCs en la sala")
    void testMultipleNpcsInside() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);
        Npc npc2 = new Npc();
        npc2.setId(2);
        List<Npc> npcs = Arrays.asList(testNpc, npc2);

        // When
        dto.setNpcsInside(npcs);

        // Then
        assertEquals(2, dto.getNpcsInside().size());
        assertEquals(testNpc, dto.getNpcsInside().get(0));
        assertEquals(npc2, dto.getNpcsInside().get(1));
    }

    @Test
    @DisplayName("Debe mantener coherencia entre getters y setters")
    void testGettersAndSettersConsistency() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);

        // When
        dto.setId(100);
        dto.setName("Coherence Test Room");
        dto.setBlackDice(5);
        dto.setWhiteDice(4);
        dto.setTimesVisited(10);
        List<Room> rooms = Arrays.asList(adjacentRoom1);
        dto.setAdjacencyList(rooms);
        List<Npc> npcs = Arrays.asList(testNpc);
        dto.setNpcsInside(npcs);
        dto.setPlayerInside(testPlayer);

        // Then
        assertEquals(100, dto.getId());
        assertEquals("Coherence Test Room", dto.getName());
        assertEquals(5, dto.getBlackDice());
        assertEquals(4, dto.getWhiteDice());
        assertEquals(10, dto.getTimesVisited());
        assertEquals(rooms, dto.getAdjacencyList());
        assertEquals(npcs, dto.getNpcsInside());
        assertEquals(testPlayer, dto.getPlayerInside());
    }

    @Test
    @DisplayName("Debe permitir cambiar playerInside de null a Player")
    void testChangePlayerInsideFromNullToPlayer() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);
        assertNull(dto.getPlayerInside());

        // When
        dto.setPlayerInside(testPlayer);

        // Then
        assertNotNull(dto.getPlayerInside());
        assertEquals(testPlayer, dto.getPlayerInside());
    }

    @Test
    @DisplayName("Debe permitir cambiar playerInside de Player a null")
    void testChangePlayerInsideFromPlayerToNull() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);
        dto.setPlayerInside(testPlayer);
        assertNotNull(dto.getPlayerInside());

        // When
        dto.setPlayerInside(null);

        // Then
        assertNull(dto.getPlayerInside());
    }

    @Test
    @DisplayName("Debe copiar correctamente todos los datos de Room en constructor")
    void testCompleteRoomDataCopy() {
        // When
        RoomDTO dto = new RoomDTO(testRoom);

        // Then
        assertEquals(testRoom.getId(), dto.getId());
        assertEquals(testRoom.getName(), dto.getName());
        assertEquals(testRoom.getBlackDice(), dto.getBlackDice());
        assertEquals(testRoom.getWhiteDice(), dto.getWhiteDice());
        assertEquals(testRoom.getAdjacencyList().size(), dto.getAdjacencyList().size());
        assertEquals(testRoom.getAdjacencyList(), dto.getAdjacencyList());
    }

    @Test
    @DisplayName("Debe permitir modificar valores múltiples veces")
    void testMultipleModifications() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);

        // First modification
        dto.setTimesVisited(5);
        assertEquals(5, dto.getTimesVisited());

        // Second modification
        dto.setTimesVisited(10);
        assertEquals(10, dto.getTimesVisited());

        // Third modification
        dto.setTimesVisited(15);
        assertEquals(15, dto.getTimesVisited());
    }

    @Test
    @DisplayName("Debe manejar valores de dados entre 1 y 6")
    void testDiceValuesRange() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);

        // When/Then - Black Dice
        for (int i = 1; i <= 6; i++) {
            dto.setBlackDice(i);
            assertEquals(i, dto.getBlackDice());
        }

        // When/Then - White Dice
        for (int i = 1; i <= 6; i++) {
            dto.setWhiteDice(i);
            assertEquals(i, dto.getWhiteDice());
        }
    }

    @Test
    @DisplayName("Debe crear instancias independientes")
    void testIndependentInstances() {
        // Given
        RoomDTO dto1 = new RoomDTO(testRoom);
        RoomDTO dto2 = new RoomDTO(testRoom);

        // When
        dto1.setTimesVisited(5);
        dto2.setTimesVisited(10);

        // Then
        assertEquals(5, dto1.getTimesVisited());
        assertEquals(10, dto2.getTimesVisited());
        assertNotEquals(dto1.getTimesVisited(), dto2.getTimesVisited());
    }

    @Test
    @DisplayName("Debe mantener referencia correcta de adjacencyList")
    void testAdjacencyListReference() {
        // When
        RoomDTO dto = new RoomDTO(testRoom);

        // Then
        assertNotNull(dto.getAdjacencyList());
        assertEquals(2, dto.getAdjacencyList().size());
        assertTrue(dto.getAdjacencyList().contains(adjacentRoom1));
        assertTrue(dto.getAdjacencyList().contains(adjacentRoom2));
    }

    @Test
    @DisplayName("Debe permitir agregar y remover NPCs de la lista")
    void testAddAndRemoveNpcsFromList() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);
        List<Npc> npcs = new ArrayList<>();

        // When
        npcs.add(testNpc);
        dto.setNpcsInside(npcs);
        assertEquals(1, dto.getNpcsInside().size());

        // Remove
        npcs.remove(testNpc);
        dto.setNpcsInside(npcs);
        assertEquals(0, dto.getNpcsInside().size());

        // Then
        assertTrue(dto.getNpcsInside().isEmpty());
    }

    @Test
    @DisplayName("Debe manejar cambios en adjacencyList")
    void testAdjacencyListChanges() {
        // Given
        RoomDTO dto = new RoomDTO(testRoom);
        assertEquals(2, dto.getAdjacencyList().size());

        // When - Cambiar a una sola sala adyacente
        dto.setAdjacencyList(Arrays.asList(adjacentRoom1));

        // Then
        assertEquals(1, dto.getAdjacencyList().size());
        assertEquals(adjacentRoom1, dto.getAdjacencyList().get(0));
    }
}
