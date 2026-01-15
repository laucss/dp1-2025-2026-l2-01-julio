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

@DisplayName("MiniUserDTO Tests")
class MiniUserDTOTest {

    private User testUser;
    private Match testMatch;
    private Player testPlayer;
    private MiniMatchDTO testMiniMatchDTO;

    @BeforeEach
    void setUp() {
        // Configurar usuario de prueba
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setAvatar("/avatar.png");
        testUser.setStatus(UserStatus.ONLINE);

        // Configurar jugador de prueba
        testPlayer = new Player();
        testPlayer.setId(101);
        testPlayer.setUser(testUser);

        // Configurar partida de prueba
        testMatch = new Match();
        testMatch.setId(1);
        testMatch.setStatus(MatchStatus.PLAYING);
        
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);
        testMatch.setPlayers(players);

        testMiniMatchDTO = new MiniMatchDTO(testMatch);
    }

    @Test
    @DisplayName("Debe crear MiniUserDTO con constructor completo")
    void testConstructorWithMatch() {
        // When
        MiniUserDTO dto = new MiniUserDTO(testUser, testMiniMatchDTO);

        // Then
        assertNotNull(dto);
        assertEquals(testUser.getId(), dto.getId());
        assertEquals(testUser.getUsername(), dto.getUsername());
        assertEquals(testUser.getAvatar(), dto.getAvatar());
        assertEquals(testUser.getStatus().name(), dto.getStatus());
        assertNotNull(dto.getMatch());
        assertEquals(testMiniMatchDTO.getId(), dto.getMatch().getId());
    }

    @Test
    @DisplayName("Debe crear MiniUserDTO sin Match usando constructor simple")
    void testConstructorWithoutMatch() {
        // When
        MiniUserDTO dto = new MiniUserDTO(testUser);

        // Then
        assertNotNull(dto);
        assertEquals(testUser.getId(), dto.getId());
        assertEquals(testUser.getUsername(), dto.getUsername());
        assertEquals(testUser.getAvatar(), dto.getAvatar());
        assertEquals(testUser.getStatus().name(), dto.getStatus());
        assertNull(dto.getMatch());
    }

    @Test
    @DisplayName("Debe crear MiniUserDTO con constructor vacío")
    void testDefaultConstructor() {
        // When
        MiniUserDTO dto = new MiniUserDTO();

        // Then
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getUsername());
        assertNull(dto.getAvatar());
        assertNull(dto.getStatus());
        assertNull(dto.getMatch());
    }

    @Test
    @DisplayName("Debe manejar User con status null")
    void testConstructorWithNullStatus() {
        // Given
        testUser.setStatus(null);

        // When
        MiniUserDTO dto = new MiniUserDTO(testUser, testMiniMatchDTO);

        // Then
        assertNotNull(dto);
        assertEquals("OFFLINE", dto.getStatus());
    }

    @Test
    @DisplayName("Debe manejar constructor con match null")
    void testConstructorWithNullMatch() {
        // When
        MiniUserDTO dto = new MiniUserDTO(testUser, null);

        // Then
        assertNotNull(dto);
        assertEquals(testUser.getId(), dto.getId());
        assertNull(dto.getMatch());
    }

    @Test
    @DisplayName("Debe permitir modificar username")
    void testSetUsername() {
        // Given
        MiniUserDTO dto = new MiniUserDTO(testUser);

        // When
        dto.setUsername("newUsername");

        // Then
        assertEquals("newUsername", dto.getUsername());
    }

    @Test
    @DisplayName("Debe permitir modificar avatar")
    void testSetAvatar() {
        // Given
        MiniUserDTO dto = new MiniUserDTO(testUser);

        // When
        dto.setAvatar("/new_avatar.png");

        // Then
        assertEquals("/new_avatar.png", dto.getAvatar());
    }

    @Test
    @DisplayName("Debe permitir modificar status")
    void testSetStatus() {
        // Given
        MiniUserDTO dto = new MiniUserDTO(testUser);

        // When
        dto.setStatus("PLAYING");

        // Then
        assertEquals("PLAYING", dto.getStatus());
    }

    @Test
    @DisplayName("Debe permitir modificar match")
    void testSetMatch() {
        // Given
        MiniUserDTO dto = new MiniUserDTO(testUser);
        Match newMatch = new Match();
        newMatch.setId(999);
        newMatch.setStatus(MatchStatus.WAITING);
        MiniMatchDTO newMiniMatchDTO = new MiniMatchDTO(newMatch);

        // When
        dto.setMatch(newMiniMatchDTO);

        // Then
        assertNotNull(dto.getMatch());
        assertEquals(999, dto.getMatch().getId());
        assertEquals("WAITING", dto.getMatch().getStatus());
    }

    @Test
    @DisplayName("Debe manejar diferentes estados de usuario")
    void testDifferentUserStatuses() {
        // Test ONLINE
        testUser.setStatus(UserStatus.ONLINE);
        MiniUserDTO dtoOnline = new MiniUserDTO(testUser);
        assertEquals("ONLINE", dtoOnline.getStatus());

        // Test OFFLINE
        testUser.setStatus(UserStatus.OFFLINE);
        MiniUserDTO dtoOffline = new MiniUserDTO(testUser);
        assertEquals("OFFLINE", dtoOffline.getStatus());

        // Test PLAYING
        testUser.setStatus(UserStatus.PLAYING);
        MiniUserDTO dtoPlaying = new MiniUserDTO(testUser);
        assertEquals("PLAYING", dtoPlaying.getStatus());
    }

    @Test
    @DisplayName("Debe obtener correctamente el match")
    void testGetMatch() {
        // Given
        MiniUserDTO dto = new MiniUserDTO(testUser, testMiniMatchDTO);

        // When
        MiniMatchDTO retrievedMatch = dto.getMatch();

        // Then
        assertNotNull(retrievedMatch);
        assertEquals(testMiniMatchDTO.getId(), retrievedMatch.getId());
        assertEquals(testMiniMatchDTO.getStatus(), retrievedMatch.getStatus());
    }

    @Test
    @DisplayName("Debe mantener coherencia entre getters y setters")
    void testGettersAndSettersConsistency() {
        // Given
        MiniUserDTO dto = new MiniUserDTO();

        // When
        dto.setId(42);
        dto.setUsername("coherenceTest");
        dto.setAvatar("/test_avatar.png");
        dto.setStatus("ONLINE");
        dto.setMatch(testMiniMatchDTO);

        // Then
        assertEquals(42, dto.getId());
        assertEquals("coherenceTest", dto.getUsername());
        assertEquals("/test_avatar.png", dto.getAvatar());
        assertEquals("ONLINE", dto.getStatus());
        assertEquals(testMiniMatchDTO, dto.getMatch());
    }

    @Test
    @DisplayName("Debe manejar avatar null")
    void testWithNullAvatar() {
        // Given
        testUser.setAvatar(null);

        // When
        MiniUserDTO dto = new MiniUserDTO(testUser);

        // Then
        assertNull(dto.getAvatar());
    }

    @Test
    @DisplayName("Debe crear DTO con usuario sin id")
    void testWithoutUserId() {
        // Given
        User userWithoutId = new User();
        userWithoutId.setUsername("noIdUser");
        userWithoutId.setStatus(UserStatus.OFFLINE);

        // When
        MiniUserDTO dto = new MiniUserDTO(userWithoutId);

        // Then
        assertNotNull(dto);
        assertNull(dto.getId());
        assertEquals("noIdUser", dto.getUsername());
    }

    @Test
    @DisplayName("Debe copiar correctamente todos los campos del usuario")
    void testCompleteFieldCopy() {
        // Given
        testUser.setId(123);
        testUser.setUsername("completeUser");
        testUser.setAvatar("/complete_avatar.png");
        testUser.setStatus(UserStatus.PLAYING);

        // When
        MiniUserDTO dto = new MiniUserDTO(testUser, testMiniMatchDTO);

        // Then
        assertEquals(123, dto.getId());
        assertEquals("completeUser", dto.getUsername());
        assertEquals("/complete_avatar.png", dto.getAvatar());
        assertEquals("PLAYING", dto.getStatus());
        assertNotNull(dto.getMatch());
    }
}
