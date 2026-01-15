package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.MiniMatchDTO.MatchPlayerDTO;

@DisplayName("MiniMatchDTO Tests")
class MiniMatchDTOTest {

    private Match testMatch;
    private User testUser1;
    private User testUser2;
    private Player testPlayer1;
    private Player testPlayer2;

    @BeforeEach
    void setUp() {
        // Configurar usuarios de prueba
        testUser1 = new User();
        testUser1.setId(1);
        testUser1.setUsername("player1");

        testUser2 = new User();
        testUser2.setId(2);
        testUser2.setUsername("player2");

        // Configurar jugadores de prueba
        testPlayer1 = new Player();
        testPlayer1.setId(101);
        testPlayer1.setUser(testUser1);

        testPlayer2 = new Player();
        testPlayer2.setId(102);
        testPlayer2.setUser(testUser2);

        // Configurar partida de prueba
        testMatch = new Match();
        testMatch.setId(1);
        testMatch.setStatus(MatchStatus.PLAYING);
        
        List<Player> players = new ArrayList<>();
        players.add(testPlayer1);
        players.add(testPlayer2);
        testMatch.setPlayers(players);
    }

    @Test
    @DisplayName("Debe crear MiniMatchDTO correctamente con todos los datos")
    void testConstructorWithFullData() {
        // When
        MiniMatchDTO dto = new MiniMatchDTO(testMatch);

        // Then
        assertNotNull(dto);
        assertEquals(testMatch.getId(), dto.getId());
        assertEquals(testMatch.getStatus().name(), dto.getStatus());
        assertNotNull(dto.getPlayers());
        assertEquals(2, dto.getPlayers().size());
        
        // Verificar primer jugador
        MatchPlayerDTO player1DTO = dto.getPlayers().get(0);
        assertEquals(testPlayer1.getId(), player1DTO.getId());
        assertEquals(testUser1.getId(), player1DTO.getUserId());
        assertEquals(testUser1.getUsername(), player1DTO.getUsername());
        
        // Verificar segundo jugador
        MatchPlayerDTO player2DTO = dto.getPlayers().get(1);
        assertEquals(testPlayer2.getId(), player2DTO.getId());
        assertEquals(testUser2.getId(), player2DTO.getUserId());
        assertEquals(testUser2.getUsername(), player2DTO.getUsername());
    }

    @Test
    @DisplayName("Debe manejar Match sin status")
    void testConstructorWithNullStatus() {
        // Given
        testMatch.setStatus(null);

        // When
        MiniMatchDTO dto = new MiniMatchDTO(testMatch);

        // Then
        assertNotNull(dto);
        assertEquals(testMatch.getId(), dto.getId());
        assertNull(dto.getStatus());
    }

    @Test
    @DisplayName("Debe manejar Match sin jugadores")
    void testConstructorWithNullPlayers() {
        // Given
        testMatch.setPlayers(null);

        // When
        MiniMatchDTO dto = new MiniMatchDTO(testMatch);

        // Then
        assertNotNull(dto);
        assertEquals(testMatch.getId(), dto.getId());
        assertNull(dto.getPlayers());
    }

    @Test
    @DisplayName("Debe manejar Match con lista vacía de jugadores")
    void testConstructorWithEmptyPlayersList() {
        // Given
        testMatch.setPlayers(new ArrayList<>());

        // When
        MiniMatchDTO dto = new MiniMatchDTO(testMatch);

        // Then
        assertNotNull(dto);
        assertEquals(testMatch.getId(), dto.getId());
        assertNotNull(dto.getPlayers());
        assertTrue(dto.getPlayers().isEmpty());
    }

    @Test
    @DisplayName("Debe crear MatchPlayerDTO correctamente")
    void testMatchPlayerDTOConstructor() {
        // When
        MatchPlayerDTO playerDTO = new MatchPlayerDTO(testPlayer1);

        // Then
        assertNotNull(playerDTO);
        assertEquals(testPlayer1.getId(), playerDTO.getId());
        assertEquals(testUser1.getId(), playerDTO.getUserId());
        assertEquals(testUser1.getUsername(), playerDTO.getUsername());
    }

    @Test
    @DisplayName("Debe manejar Player sin User en MatchPlayerDTO")
    void testMatchPlayerDTOWithNullUser() {
        // Given
        Player playerWithoutUser = new Player();
        playerWithoutUser.setId(999);
        playerWithoutUser.setUser(null);

        // When
        MatchPlayerDTO playerDTO = new MatchPlayerDTO(playerWithoutUser);

        // Then
        assertNotNull(playerDTO);
        assertEquals(999, playerDTO.getId());
        assertNull(playerDTO.getUserId());
        assertNull(playerDTO.getUsername());
    }

    @Test
    @DisplayName("Debe permitir modificar el id del DTO")
    void testSetId() {
        // Given
        MiniMatchDTO dto = new MiniMatchDTO(testMatch);

        // When
        dto.setId(999);

        // Then
        assertEquals(999, dto.getId());
    }

    @Test
    @DisplayName("Debe permitir modificar el status del DTO")
    void testSetStatus() {
        // Given
        MiniMatchDTO dto = new MiniMatchDTO(testMatch);

        // When
        dto.setStatus("FINISHED");

        // Then
        assertEquals("FINISHED", dto.getStatus());
    }

    @Test
    @DisplayName("Debe permitir modificar los jugadores del DTO")
    void testSetPlayers() {
        // Given
        MiniMatchDTO dto = new MiniMatchDTO(testMatch);
        List<MatchPlayerDTO> newPlayers = new ArrayList<>();
        MatchPlayerDTO newPlayer = new MatchPlayerDTO(testPlayer1);
        newPlayers.add(newPlayer);

        // When
        dto.setPlayers(newPlayers);

        // Then
        assertEquals(1, dto.getPlayers().size());
        assertEquals(newPlayer, dto.getPlayers().get(0));
    }

    @Test
    @DisplayName("Debe manejar diferentes estados de Match")
    void testDifferentMatchStatuses() {
        // Test WAITING
        testMatch.setStatus(MatchStatus.WAITING);
        MiniMatchDTO dtoWaiting = new MiniMatchDTO(testMatch);
        assertEquals("WAITING", dtoWaiting.getStatus());

        // Test FINISHED
        testMatch.setStatus(MatchStatus.FINISHED);
        MiniMatchDTO dtoFinished = new MiniMatchDTO(testMatch);
        assertEquals("FINISHED", dtoFinished.getStatus());

        // Test PLAYING
        testMatch.setStatus(MatchStatus.PLAYING);
        MiniMatchDTO dtoPlaying = new MiniMatchDTO(testMatch);
        assertEquals("PLAYING", dtoPlaying.getStatus());
    }

    @Test
    @DisplayName("Debe permitir setters en MatchPlayerDTO")
    void testMatchPlayerDTOSetters() {
        // Given
        MatchPlayerDTO playerDTO = new MatchPlayerDTO(testPlayer1);

        // When
        playerDTO.setId(500);
        playerDTO.setUserId(501);
        playerDTO.setUsername("newUsername");

        // Then
        assertEquals(500, playerDTO.getId());
        assertEquals(501, playerDTO.getUserId());
        assertEquals("newUsername", playerDTO.getUsername());
    }
}
