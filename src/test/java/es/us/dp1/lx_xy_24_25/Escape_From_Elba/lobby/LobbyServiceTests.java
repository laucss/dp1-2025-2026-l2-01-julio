package es.us.dp1.lx_xy_24_25.Escape_From_Elba.lobby;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.LobbyNotFound;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations.InvitationRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyWebsocketController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
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

    @Test
    void getAllPublicLobbiesReturnsPage() {
        List<Match> matches = List.of(new Match(), new Match());
        Page<Match> page = new PageImpl<>(matches);

        when(lobbyRepository.findAllPublicGamesByStatus(eq(MatchStatus.WAITING), any(PageRequest.class)))
            .thenReturn(page);

        Page<Match> result = lobbyService.getAllPublicGamesByStatus(MatchStatus.WAITING, 0, 5);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    @Test
    void getPrivateLobbyExists() {
        Match match = new Match();
        when(lobbyRepository.findPrivateLobbyByCode("ABC")).thenReturn(Optional.of(match));

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

        assertNotNull(match);
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
        when(lobbyRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(LobbyNotFound.class, () -> lobbyService.joinLobby(1));
    }

    @Test
    void joinLobbySuccess() {
        Match match = new Match();
        match.setId(1);
        match.setPlayers(new ArrayList<>());
        match.setSpectators(new ArrayList<>());

        User user = new User();
        user.setUsername("joinedPlayer");

        when(lobbyRepository.findById(1)).thenReturn(Optional.of(match));
        when(userService.findCurrentUser()).thenReturn(user);
        when(lobbyRepository.save(any(Match.class))).thenAnswer(i -> i.getArgument(0));

        Match result = lobbyService.joinLobby(1);

        assertNotNull(result);
        assertEquals(1, result.getPlayers().size());
        verify(playerService, times(1)).save(any(Player.class));
        verify(lobbyWebsocketController, times(1)).notifyPlayerJoined(eq(1), any());
    }

    @Test
    void joinPrivateLobbySuccess() {
        Match match = new Match();
        match.setId(2);
        match.setPlayers(new ArrayList<>());
        match.setSpectators(new ArrayList<>());

        User user = new User();
        user.setUsername("privatePlayer");

        when(lobbyRepository.findPrivateLobbyByCode("SECRET")).thenReturn(Optional.of(match));
        when(userService.findCurrentUser()).thenReturn(user);
        when(lobbyRepository.save(any(Match.class))).thenAnswer(i -> i.getArgument(0));

        Match result = lobbyService.joinPrivateLobby("SECRET");

        assertNotNull(result);
        assertEquals(1, result.getPlayers().size());
        verify(lobbyWebsocketController, times(1)).notifyPlayerJoined(eq(2), any());
    }

    @Test
    void leaveLobbyAsCreatorDeletesMatch() {
        Match match = new Match();
        match.setId(1);
        match.setCreatorId(10); // ID del creador
        match.setPlayers(new ArrayList<>());
        match.setSpectators(new ArrayList<>());

        User creator = new User();
        creator.setId(10);
        creator.setUsername("creatorUser");

        Player player = new Player();
        player.setUser(creator);

        when(lobbyRepository.findById(1)).thenReturn(Optional.of(match));
        when(userService.findCurrentUser()).thenReturn(creator);
        when(playerService.findByMatchIdAndUserId(1, 10)).thenReturn(player);

        Match result = lobbyService.leaveLobby(1);

        assertNull(result); // Debe retornar null porque borra el match
        verify(invitationRepository, times(1)).deleteByMatchId(1);
        verify(lobbyRepository, times(1)).delete(match);
        verify(lobbyWebsocketController, times(1)).notifyPlayerLeft(eq(1), any());
    }

    @Test
    void leaveLobbyAsRegularPlayerRemovesPlayer() {
        Match match = new Match();
        match.setId(1);
        match.setCreatorId(10); // Otro es el creador
        match.setPlayers(new ArrayList<>());
        match.setSpectators(new ArrayList<>());

        User regularUser = new User();
        regularUser.setId(5);
        regularUser.setUsername("regularUser");

        Player player = new Player();
        player.setUser(regularUser);
        match.getPlayers().add(player);

        when(lobbyRepository.findById(1)).thenReturn(Optional.of(match));
        when(userService.findCurrentUser()).thenReturn(regularUser);
        when(playerService.findByMatchIdAndUserId(1, 5)).thenReturn(player);
        when(lobbyRepository.save(any(Match.class))).thenAnswer(i -> i.getArgument(0));

        Match result = lobbyService.leaveLobby(1);

        assertNotNull(result);
        assertTrue(result.getPlayers().isEmpty()); // El jugador común fue removido
        verify(lobbyRepository, never()).delete(any());
        verify(lobbyWebsocketController, times(1)).notifyPlayerLeft(eq(1), any());
    }
}