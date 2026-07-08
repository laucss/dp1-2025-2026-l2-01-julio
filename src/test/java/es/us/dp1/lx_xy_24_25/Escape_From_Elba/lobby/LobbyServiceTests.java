package es.us.dp1.lx_xy_24_25.Escape_From_Elba.lobby;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.LobbyNotFound;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations.InvitationRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyWebsocketController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;

@ExtendWith(MockitoExtension.class)
public class LobbyServiceTests {

    private LobbyService lobbyService;

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserService userService;

    @Mock
    private PlayerService playerService;

    @Mock
    private Checkers checkers;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private LobbyWebsocketController lobbyWebsocketController;

    @BeforeEach
    void setup() {
        lobbyService = new LobbyService(
            lobbyRepository,
            checkers,
            userService,
            playerService,
            lobbyWebsocketController,
            invitationRepository
        );
    }


    // CORREGIR: CAMBIÉ LOS PARAMETROS QUE SE LE PASAN
    /* 
    @Test
    void getAllPublicLobbiesReturnsPage() {
        List<Match> matches = List.of(new Match(), new Match());
        Page<Match> page = new PageImpl<>(matches);

        when(matchRepository.findAllPublicGamesByStatus(PageRequest.of(0, 5)))
            .thenReturn(page);

        Page<Match> result = lobbyService.getAllPublicGamesByStatus(0, 5);

        assertEquals(2, result.getContent().size());
    }
        */

    @Test
    void getPrivateLobbyExists() {
        Match match = new Match();
        when(lobbyRepository.findPrivateLobbyByCode("ABC"))
            .thenReturn(Optional.of(match));

        Optional<Match> result = lobbyService.getPrivateLobby("ABC");

        assertTrue(result.isPresent());
    }


    @Test
    void createLobbyNoUserThrowsUnauthorized() {
        when(userService.findCurrentUser()).thenReturn(null);

        assertThrows(ResponseStatusException.class, () ->
            lobbyService.createLobby(false, "Lobby", 4, 3)
        );
    }

    @Test
    void createPublicLobbySuccess() {
        User user = new User();
        user.setId(1);
        user.setUsername("player1");

        when(userService.findCurrentUser()).thenReturn(user);
        doNothing().when(checkers).checkPlayerAlreadyInALobby(user);
        when(lobbyRepository.save(any(Match.class))).thenAnswer(i -> i.getArgument(0));

        Match match = lobbyService.createLobby(false, "Lobby", 4, 3);

        assertEquals("Lobby", match.getName());
        assertEquals(4, match.getMaxPlayers());
        assertEquals(MatchStatus.WAITING, match.getStatus());
        assertEquals(1, match.getPlayers().size());
        assertFalse(match.getIsPrivate());
    }

    @Test
    void createPrivateLobbyGeneratesCode() {
        User user = new User();
        user.setId(1);

        when(userService.findCurrentUser()).thenReturn(user);
        doNothing().when(checkers).checkPlayerAlreadyInALobby(user);
        when(lobbyRepository.save(any(Match.class))).thenAnswer(i -> i.getArgument(0));

        Match match = lobbyService.createLobby(true, "Privado", 4, 3);

        assertTrue(match.getIsPrivate());
        assertNotNull(match.getCode());
    }


    @Test
    void joinLobbyNotFoundThrows() {
        lenient().when(lobbyRepository.findById(1))
            .thenReturn(Optional.empty());

        assertThrows(LobbyNotFound.class, () ->
            lobbyService.joinLobby(1)
        );
    }

}

