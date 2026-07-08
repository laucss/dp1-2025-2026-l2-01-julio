package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyInt;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.InvalidMovementException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.MoreThan7CardsDrawnException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.NoActionPointsException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DrawCardResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.ActionPointsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.CardsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.EscapeAttemptResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchHistorialDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.TurnUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyWebsocketController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.NpcRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;
import org.springframework.security.access.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MatchServiceTests {

    private MatchService matchService;

    @Mock
    private MatchRepository matchRepo;

    @Mock
    private PlayerRepository playerRepo;

    @Mock
    private RoomRepository roomRepo;

    @Mock
    private RoomService roomService;

    @Mock
    private DeckService deckService;

    @Mock
    private HandService handService;

    @Mock
    private BagService bagService;

    @Mock
    private PlayerService playerService;

    @Mock
    private UserService userService;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private LobbyWebsocketController lobbyWebsocketController;

    @Mock
    private MatchWebsocketController matchWebsocketController;

    @Mock
    private NpcRepository npcRepository;

    @Mock
    private Checkers checkers;

    @Mock
    private AbandonedMatchService abandonedMatchService;

    @Mock
    private AbandonedMatchRepository abandonedMatchRepository; 

    @BeforeEach
    void setup() {
        matchService = new MatchService(
            matchRepo,
            playerRepo,
            roomRepo,
            abandonedMatchRepository,
            roomService,
            deckService,
            handService,
            bagService,
            playerService,
            lobbyWebsocketController,
            matchWebsocketController,
            npcRepository,
            checkers,
            userService,
            lobbyService,
            abandonedMatchService
        );
    }

    @Test
    void getAllMatchsReturnsList() {
        List<Match> items = List.of(new Match(), new Match());
        when(matchRepo.findAll()).thenReturn(items);

        var result = matchService.getAllMatchs();

        assertEquals(2, result.size());
        verify(matchRepo).findAll();
    }

    @Test
    void getMatchByIdFound() {
        Match m = new Match();
        m.setId(1);
        when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(m));

        Match result = matchService.getMatchById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }


    @Test
    public void getMatchsByNameReturnsList() {
        Match m = new Match();
        m.setName("TestMatch");
        when(matchRepo.findByName("TestMatch")).thenReturn(List.of(m));
        List<Match> result = matchService.getMatchsByName("TestMatch");
        assertEquals(1, result.size());
        assertEquals("TestMatch", result.get(0).getName());
    }

    @Test
    public void deleteInvokesRepository() {
        matchService.delete(1);
        verify(matchRepo, times(1)).deleteById(1);
    }

    @Test
    void getFinishedAndInProgressMatchesReturnsPage() {
        List<Match> list = List.of(new Match(), new Match(), new Match());
        Page<Match> page = new PageImpl<>(list);
        when(matchRepo.findFinishedAndInProgress(PageRequest.of(0, 5))).thenReturn(page);

        //Page<Match> result = matchService.getFinishedAndInProgressMatches(0, 5);

        //assertEquals(3, result.getContent().size());
    }


    @Test
    void getMatchesWonByUserCallsRepo() {
        int userId = 13;
        List<Match> list = List.of(new Match());
        Page<Match> page = new PageImpl<>(list);
        when(matchRepo.findMatchesWonByUser(eq(userId), any(PageRequest.class))).thenReturn(page);

        //Page<Match> result = matchService.getMatchesWonByUser(userId, 0, 3);

        //assertEquals(1, result.getContent().size());
        //verify(matchRepo).findMatchesWonByUser(eq(userId), any(PageRequest.class));
    }


    @Test
    void deleteMatchCardsCallsDeleteMethods() {
        int matchId = 77;
        doNothing().when(deckService).deleteDeckInGame(matchId);
        doNothing().when(handService).deleteMatchHands(matchId);
        doNothing().when(bagService).deleteMatchBags(matchId);

        matchService.deleteMatchCards(matchId);

        verify(deckService).deleteDeckInGame(matchId);
        verify(handService).deleteMatchHands(matchId);
        verify(bagService).deleteMatchBags(matchId);
    }

    @Test
    void submitDiceAndAssignOrderSetsDiceWhenNotAllRolled() {
        int matchId = 10;
        int userId = 42;
        int diceRoll = 5;

        Match m = new Match();
        m.setId(matchId);

        Player p1 = new Player();
        p1.setId(1);
        Player p2 = new Player();
        p2.setId(2);

        // both players with null diceOrder
        p1.setDiceOrder(null);
        p2.setDiceOrder(null);

        m.setPlayers(List.of(p1, p2));

        when(matchRepo.findById(Integer.valueOf(matchId))).thenReturn(Optional.of(m));
        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(p1));
        when(playerRepo.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        Match result = matchService.submitDiceAndAssignOrder(matchId, userId, diceRoll);

        assertEquals(m, result);
        assertEquals(Integer.valueOf(diceRoll), p1.getDiceOrder());
        verify(playerRepo).save(p1);
    }

    @Test
    void getMatchWinnerReturnsWinnerWhenFinished() {
        int matchId = 88;
        Match m = new Match();
        m.setId(matchId);
        m.setStatus(MatchStatus.FINISHED);
        Player winner = new Player();
        winner.setId(123);
        m.setWinner(winner);

        when(matchRepo.findById(Integer.valueOf(matchId))).thenReturn(Optional.of(m));

        Player result = matchService.getMatchWinner(matchId);

        assertEquals(winner, result);
    }

    @Test
    void getMatchesPlayedAndCreatedByUserCallsRepo() {
        int userId = 5;
        List<Match> list = List.of(new Match());
        Page<Match> page = new PageImpl<>(list);
        when(matchRepo.findMatchesPlayedAndCreatedByUser(eq(userId), any(PageRequest.class))).thenReturn(page);

        //Page<Match> result = matchService.getMatchesPlayedAndCreatedByUser(userId, 0, 3);

        //assertEquals(1, result.getContent().size());
        //verify(matchRepo).findMatchesPlayedAndCreatedByUser(eq(userId), any(PageRequest.class));
    }

    @Test
    void userInMatchReturnsValueFromRepo() {
        when(matchRepo.userInMatch(7)).thenReturn(42);
        Integer result = matchService.userInMatch(7);
        assertEquals(42, result);
        verify(matchRepo).userInMatch(7);
    }

    @Test
    void saveCallsRepoAndReturnsSameInstance() {
        Match m = new Match();
        when(matchRepo.save(m)).thenReturn(m);
        Match result = matchService.save(m);
        assertEquals(m, result);
        verify(matchRepo).save(m);
    }

    @Test
    void deleteCallsRepoDeleteById() {
        int id = 9;
        doNothing().when(matchRepo).deleteById(id);
        matchService.delete(id);
        verify(matchRepo).deleteById(id);
    }



    @Test
    void consumeActionPointForUserDecrementsAndReturnsDTO() {
        int matchId = 1;
        int userId = 2;
        Player p = new Player();
        p.setId(10);
        User u = new User(); u.setId(userId); u.setUsername("u");
        p.setUser(u);
        p.setActionPoints(3);

        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(p));
        when(playerRepo.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        ActionPointsUpdateDTO dto = matchService.consumeActionPointForUser(matchId, userId);

        assertEquals(2, p.getActionPoints());
        assertEquals(p.getId(), dto.getPlayerId());
        verify(playerRepo).save(p);
    }

    @Test
    void consumeAllActionPointForUserSetsZeroAndReturnsDTO() {
        int matchId = 3;
        int userId = 4;
        Player p = new Player();
        p.setId(20);
        User u = new User(); u.setId(userId); u.setUsername("u2");
        p.setUser(u);
        p.setActionPoints(5);

        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(p));
        when(playerRepo.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        ActionPointsUpdateDTO dto = matchService.consumeAllActionPointForUser(matchId, userId);

        assertEquals(0, p.getActionPoints());
        assertEquals(0, dto.getActionPoints());
        verify(playerRepo).save(p);
    }

    @Test
    void consumeOneActionPointReducesAndReturnsDTO() {
        int matchId = 5;
        int userId = 6;
        Player p = new Player();
        p.setId(30);
        User u = new User(); u.setId(userId); u.setUsername("u3");
        p.setUser(u);
        p.setActionPoints(1);

        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(p));
        when(playerRepo.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        ActionPointsUpdateDTO dto = matchService.consumeOneActionPoint(matchId, userId);

        assertEquals(0, p.getActionPoints());
        assertEquals(0, dto.getActionPoints());
        verify(playerRepo).save(p);
    }


    @Test
    void playerDrawsRewardCardNotifiesAndReturnsResult() {
        int matchId = 21;
        int playerId = 22;
        Card card = new Card();
        DeckInGame deck = new DeckInGame();
        HandInGame hand = new HandInGame();
        BagInGame bag = new BagInGame();

        when(deckService.drawCard(matchId)).thenReturn(card);
        when(deckService.findDeckById(matchId)).thenReturn(deck);
        when(handService.addCardToPlayerHand(card, matchId, playerId)).thenReturn(hand);
        when(handService.findPlayerHand(matchId, playerId)).thenReturn(hand);
        when(bagService.findPlayerBag(matchId, playerId)).thenReturn(bag);

        DrawCardResultDTO res = matchService.playerDrawsRewardCard(matchId, playerId);

        assertEquals(card.getId(), res.getCard().getId());
        verify(matchWebsocketController).notifyCardsUpdate(eq(matchId), any());
    }



    @Test
    void submitDiceAndAssignOrderThrowsWhenPlayerNotFound() {
        int matchId = 201; int userId = 202; int diceRoll = 4;

        Match m = new Match(); m.setId(matchId);
        when(matchRepo.findById(Integer.valueOf(matchId))).thenReturn(Optional.of(m));
        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> matchService.submitDiceAndAssignOrder(matchId, userId, diceRoll));
    }

    @Test
    void consumeActionPointForUserThrowsWhenPlayerNotFound() {
        int matchId = 301; int userId = 302;

        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> matchService.consumeActionPointForUser(matchId, userId));
    }

    @Test
    void getMatchWinnerThrowsWhenMatchNotFinished() {
        int matchId = 401;
        Match m = new Match(); m.setId(matchId); m.setStatus(MatchStatus.PLAYING);
        when(matchRepo.findById(Integer.valueOf(matchId))).thenReturn(Optional.of(m));

        assertThrows(IllegalStateException.class, () -> matchService.getMatchWinner(matchId));
    }

    @Test
    void shouldGetAllMatches() {
        Match m1 = new Match();
        Match m2 = new Match();

        when(matchRepo.findAll()).thenReturn(List.of(m1, m2));

        List<Match> result = matchService.getAllMatchs();

        assertEquals(2, result.size());
        verify(matchRepo).findAll();
    }

    @Test
    void shouldReturnMatchesByName() {
        Match match = new Match();

        when(matchRepo.findByName("Test")).thenReturn(List.of(match));

        List<Match> result = matchService.getMatchsByName("Test");

        assertEquals(1, result.size());
        verify(matchRepo).findByName("Test");
    }

    @Test
    void shouldReturnRunningMatches() {

        Match m1 = new Match();
        Match m2 = new Match();

        when(matchRepo.findAll()).thenReturn(List.of(m1, m2));

        List<Match> result = matchService.getRunningMatches();

        assertEquals(2, result.size());
    }

    @Test
    void shouldSaveMatch() {

        Match match = new Match();

        when(matchRepo.save(match)).thenReturn(match);

        Match result = matchService.save(match);

        assertEquals(match, result);

        verify(matchRepo).save(match);
    }

    @Test
    void shouldThrowWhenMatchDoesNotExist() {

        when(matchRepo.findById(100))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.getMatchById(100));
    }

    @Test
    void shouldDeleteMatch() {

        matchService.delete(4);

        verify(matchRepo).deleteById(4);
    }

    @Test
    void shouldReturnUserInMatch() {

        when(matchRepo.userInMatch(5))
                .thenReturn(8);

        Integer result = matchService.userInMatch(5);

        assertEquals(8, result);

        verify(matchRepo).userInMatch(5);
    }

    @Test
    void shouldReturnMatchDTOWhenUserIsPlayer() {

        User user = new User();
        user.setId(1);

        Player player = new Player();
        player.setId(10);
        player.setUser(user);

        Match match = new Match();
        match.setId(1);
        match.setPlayers(new ArrayList<>(List.of(player)));
        match.setSpectators(new ArrayList<>());

        when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

        when(userService.findCurrentUser())
                .thenReturn(user);

        when(deckService.findDeckById(1))
                .thenReturn(new DeckInGame());

        when(handService.findPlayerHand(1, 10))
                .thenReturn(new HandInGame());

        when(bagService.findPlayerBag(1, 10))
                .thenReturn(new BagInGame());

        MatchDTO dto = matchService.getMatchDTOById(1);

        assertNotNull(dto);

        verify(matchRepo).findById(Integer.valueOf(1));;
        verify(userService).findCurrentUser();
        verify(deckService).findDeckById(1);
    }
    @Test
    void shouldReturnMatchDTOWhenUserIsSpectator() {

        User spectator = new User();
        spectator.setId(2);

        Match match = new Match();
        match.setId(1);
        match.setPlayers(new ArrayList<>());
        match.setSpectators(new ArrayList<>(List.of(spectator)));

        when(matchRepo.findById(Integer.valueOf(1)))
                .thenReturn(Optional.of(match));

        when(userService.findCurrentUser())
                .thenReturn(spectator);

        when(deckService.findDeckById(1))
                .thenReturn(new DeckInGame());

        MatchDTO dto = matchService.getMatchDTOById(1);

        assertNotNull(dto);

        verify(matchRepo).findById(Integer.valueOf(1));
        verify(userService).findCurrentUser();
    }
    @Test
    void shouldThrowWhenMatchNotFound() {

        when(matchRepo.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> matchService.getMatchDTOById(1)
        );
    }
    @Test
    void shouldCreatePlayerDTOForEveryPlayer() {

        User u1 = new User();
        u1.setId(1);

        User u2 = new User();
        u2.setId(2);

        Player p1 = new Player();
        p1.setId(11);
        p1.setUser(u1);

        Player p2 = new Player();
        p2.setId(22);
        p2.setUser(u2);

        Match match = new Match();
        match.setId(1);
        match.setPlayers(List.of(p1, p2));
        match.setSpectators(new ArrayList<>());

        when(matchRepo.findById(Integer.valueOf(1)))
                .thenReturn(Optional.of(match));

        when(userService.findCurrentUser())
                .thenReturn(u1);

        when(deckService.findDeckById(Integer.valueOf(1)))
                .thenReturn(new DeckInGame());

        when(handService.findPlayerHand(anyInt(), anyInt()))
                .thenReturn(new HandInGame());

        when(bagService.findPlayerBag(anyInt(), anyInt()))
                .thenReturn(new BagInGame());

        MatchDTO dto = matchService.getMatchDTOById(1);

        assertNotNull(dto);

        verify(handService, times(2)).findPlayerHand(anyInt(), anyInt());
        verify(bagService, times(2)).findPlayerBag(anyInt(), anyInt());
    }


    @Test
    void shouldReturnFinishedAndInProgressMatches() {

        User creator = new User();
        creator.setId(1);

        Match match = new Match();
        match.setId(1);
        match.setCreatorId(1);

        Page<Match> page = new PageImpl<>(List.of(match));

        when(matchRepo.findFinishedAndInProgress(any(Pageable.class)))
                .thenReturn(page);

        when(userService.findUser(1)).thenReturn(creator);

        Page<MatchHistorialDTO> result =
                matchService.getFinishedAndInProgressMatches(0, 10);

        assertEquals(1, result.getTotalElements());

        verify(matchRepo).findFinishedAndInProgress(any(Pageable.class));
        verify(userService).findUser(1);
    }
    @Test
    void shouldReturnInProgressMatches() {

        User creator = new User();
        creator.setId(5);

        Match match = new Match();
        match.setCreatorId(5);

        Page<Match> page = new PageImpl<>(List.of(match));

        when(matchRepo.findInProgress(any(Pageable.class)))
                .thenReturn(page);

        when(userService.findUser(5)).thenReturn(creator);

        Page<MatchHistorialDTO> result =
                matchService.getInProgressMatches(0, 5);

        assertEquals(1, result.getContent().size());

        verify(matchRepo).findInProgress(any(Pageable.class));
    }
    @Test
    void shouldReturnFinishedMatches() {

        User creator = new User();
        creator.setId(7);

        Match match = new Match();
        match.setCreatorId(7);

        Page<Match> page = new PageImpl<>(List.of(match));

        when(matchRepo.findFinished(any(Pageable.class)))
                .thenReturn(page);

        when(userService.findUser(7)).thenReturn(creator);

        Page<MatchHistorialDTO> result =
                matchService.getFinishedMatches(0, 10);

        assertEquals(1, result.getTotalElements());

        verify(matchRepo).findFinished(any(Pageable.class));
    }
    @Test
    void shouldReturnPlayedOrAbandonedMatches() {

        User creator = new User();
        creator.setId(1);

        Match match = new Match();
        match.setId(8);
        match.setCreatorId(1);

        Page<Match> page = new PageImpl<>(List.of(match));

        when(matchRepo.findMatchesPlayedOrAbandonedByUser(eq(2), any(Pageable.class)))
                .thenReturn(page);

        when(userService.findUser(1)).thenReturn(creator);

        when(abandonedMatchRepository.existsByMatchIdAndUserId(8, 2))
                .thenReturn(true);

        Page<MatchHistorialDTO> result =
                matchService.getAllMatchesByUser(2, 0, 10);

        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0).getAbandoned());

        verify(abandonedMatchRepository)
                .existsByMatchIdAndUserId(8, 2);
    }
    @Test
    void shouldReturnPlayedAndCreatedMatches() {

        User creator = new User();
        creator.setId(1);

        Match match = new Match();
        match.setCreatorId(1);

        when(userService.findUser(1)).thenReturn(creator);

        when(matchRepo.findMatchesPlayedAndCreatedByUser(eq(1), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(match)));

        Page<MatchHistorialDTO> result =
                matchService.getMatchesPlayedAndCreatedByUser(1, 0, 5);

        assertEquals(1, result.getTotalElements());
    }
    @Test
    void shouldReturnWonMatches() {

        User creator = new User();
        creator.setId(1);

        Match match = new Match();
        match.setCreatorId(1);

        when(userService.findUser(1)).thenReturn(creator);

        when(matchRepo.findMatchesWonByUser(eq(1), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(match)));

        Page<MatchHistorialDTO> result =
                matchService.getMatchesWonByUser(1, 0, 5);

        assertEquals(1, result.getContent().size());
    }
    @Test
    void shouldReturnAbandonedMatches() {

        User creator = new User();
        creator.setId(1);

        Match match = new Match();
        match.setCreatorId(1);

        when(userService.findUser(1)).thenReturn(creator);

        when(abandonedMatchRepository.findMatchesAbandonedByUser(eq(1), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(match)));

        Page<MatchHistorialDTO> result =
                matchService.getMatchesAbandonedByUser(1, 0, 5);

        assertEquals(1, result.getContent().size());

        verify(abandonedMatchRepository)
                .findMatchesAbandonedByUser(eq(1), any(Pageable.class));
    }

    @Test
    void shouldThrowWhenStartingUnknownMatch() {

        when(matchRepo.findById(Integer.valueOf(99))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.startMatch(99));

        verify(matchRepo).findById(Integer.valueOf(99));
    }

    @Test
    void shouldSetStatusToPlaying() {

        Match match = new Match();
        match.setId(1);
        match.setPlayers(new ArrayList<>());
        match.setNpcs(new ArrayList<>());
        match.setNumNpcs(0);

        DeckInGame deck = new DeckInGame();
        deck.setNotDiscardedCards(new ArrayList<>());

        when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

        when(deckService.initializeDeck(Integer.valueOf(1))).thenReturn(deck);
        when(roomService.initializeRoomsForMatch(match)).thenReturn(new ArrayList<>());

        when(matchRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        when(lobbyService.createLobbyUpdate(any(), eq("START"), eq("")))
                .thenReturn(mock(LobbyUpdateDTO.class));

        matchService.startMatch(Integer.valueOf(1));

        assertEquals(MatchStatus.PLAYING, match.getStatus());
        assertNotNull(match.getStartTime());
    }

    @Test
    void shouldResetPlayers() {

        Player player = new Player();

        player.setId(1);
        player.setActionPoints(4);
        player.setStrength(8);
        player.setCardsDrawnInTurn(9);
        player.setDiceOrder(3);
        player.setOrderInMatch(2);

        Match match = new Match();
        match.setId(1);
        match.setPlayers(List.of(player));
        match.setNpcs(new ArrayList<>());
        match.setNumNpcs(0);

        DeckInGame deck = new DeckInGame();

        List<Card> cards = new ArrayList<>();

        for(int i=0;i<3;i++)
            cards.add(new Card());

        deck.setNotDiscardedCards(cards);

        when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

        when(deckService.initializeDeck(Integer.valueOf(1))).thenReturn(deck);
        when(roomService.initializeRoomsForMatch(any())).thenReturn(new ArrayList<>());

        when(matchRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        when(lobbyService.createLobbyUpdate(any(), any(), any()))
                .thenReturn(mock(LobbyUpdateDTO.class));

        matchService.startMatch(Integer.valueOf(1));

        assertNull(player.getDiceOrder());
        assertNull(player.getOrderInMatch());

        assertEquals(0, player.getActionPoints());
        assertEquals(1, player.getStrength());
        assertEquals(0, player.getCardsDrawnInTurn());
    }

    @Test
    void shouldCreateNpcs() {

        Match match = new Match();
        match.setId(1);
        match.setPlayers(new ArrayList<>());
        match.setNpcs(new ArrayList<>());
        match.setNumNpcs(4);

        DeckInGame deck = new DeckInGame();
        deck.setNotDiscardedCards(new ArrayList<>());

        when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

        when(deckService.initializeDeck(Integer.valueOf(1))).thenReturn(deck);

        when(roomService.initializeRoomsForMatch(any()))
                .thenReturn(new ArrayList<>());

        when(matchRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        when(lobbyService.createLobbyUpdate(any(), any(), any()))
                .thenReturn(mock(LobbyUpdateDTO.class));

        matchService.startMatch(Integer.valueOf(1));

        assertEquals(4, match.getNpcs().size());

        verify(npcRepository, times(4))
                .save(any(Npc.class));
}
    @Test
    void shouldNotifyGameStarted() {

        Match match = new Match();

        match.setId(1);
        match.setPlayers(new ArrayList<>());
        match.setNpcs(new ArrayList<>());
        match.setNumNpcs(0);

        DeckInGame deck = new DeckInGame();
        deck.setNotDiscardedCards(new ArrayList<>());

        LobbyUpdateDTO dto = mock(LobbyUpdateDTO.class);

        when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

        when(deckService.initializeDeck(Integer.valueOf(1))).thenReturn(deck);

        when(roomService.initializeRoomsForMatch(any()))
                .thenReturn(new ArrayList<>());

        when(matchRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        when(lobbyService.createLobbyUpdate(any(), eq("START"), eq("")))
                .thenReturn(dto);

        matchService.startMatch(Integer.valueOf(1));

        verify(lobbyWebsocketController)
                .notifyGameStarted(1, dto);
    }

    @Test
    void shouldThrowWhenPlayerNotFound() {

        Match match = new Match();
        match.setId(1);

        when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

        when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(7)))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> matchService.submitDiceAndAssignOrder(Integer.valueOf(1), Integer.valueOf(7), Integer.valueOf(4)));
    }

    @Test
    void shouldThrowWhenPlayerAlreadyRolled() {

        Match match = new Match();
        match.setId(1);

        Player player = new Player();
        player.setDiceOrder(5);

        when(matchRepo.findById(1)).thenReturn(Optional.of(match));

        when(playerRepo.findByMatchAndUser(1, 1))
                .thenReturn(Optional.of(player));

        assertThrows(IllegalArgumentException.class,
                () -> matchService.submitDiceAndAssignOrder(1, 1, 3));
    }



    @Test
void shouldNotAssignOrderUntilEveryoneRolls() {

    User u1 = new User();
    u1.setId(1);

    User u2 = new User();
    u2.setId(2);

    Player p1 = new Player();
    p1.setUser(u1);

    Player p2 = new Player();
    p2.setUser(u2);
    p2.setDiceOrder(null);

    Match match = new Match();
    match.setId(1);
    match.setPlayers(List.of(p1, p2));

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1)))
            .thenReturn(Optional.of(p1));

    matchService.submitDiceAndAssignOrder(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(5));

    assertNull(match.getCurrentTurnUserId());

    verify(matchRepo, never()).save(match);
}

@Test
void shouldBreakTieUsingPlayerId() {

    User u1 = new User();
    u1.setId(1);

    User u2 = new User();
    u2.setId(2);

    Player p1 = new Player();
    p1.setId(1);
    p1.setUser(u1);

    Player p2 = new Player();
    p2.setId(2);
    p2.setUser(u2);
    p2.setDiceOrder(5);

    Match match = new Match();
    match.setId(1);
    match.setPlayers(List.of(p1, p2));

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(playerRepo.findByMatchAndUser(1, 1))
            .thenReturn(Optional.of(p1));

    matchService.submitDiceAndAssignOrder(1, 1, 5);

    assertEquals(0, p1.getOrderInMatch());
    assertEquals(1, p2.getOrderInMatch());
}
@Test
void shouldNotifyTurnUpdate() {

    User user = new User();
    user.setId(1);
    user.setUsername("Julian");

    Player p = new Player();
    p.setId(1);
    p.setUser(user);

    Match match = new Match();
    match.setId(1);
    match.setPlayers(List.of(p));

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1)))
            .thenReturn(Optional.of(p));

    matchService.submitDiceAndAssignOrder(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(6));

    verify(matchWebsocketController)
            .notifyTurnUpdate(eq(Integer.valueOf(1)), any(TurnUpdateDTO.class));
}
@Test
void shouldThrowWhenMatchDoesNotExistInNextTurn() {

    when(matchRepo.findById(1)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
            () -> matchService.nextTurn(1));
}

@Test
void shouldThrowWhenCurrentPlayerDoesNotExist() {

    Match match = new Match();
    match.setCurrentTurnUserId(1);
    match.setPlayers(new ArrayList<>());

    when(matchRepo.findById(1)).thenReturn(Optional.of(match));

    assertThrows(IllegalArgumentException.class,
            () -> matchService.nextTurn(1));
}
@Test
void shouldResetCardsDrawn() {

    User u1 = new User();
    u1.setId(1);

    Player p1 = new Player();
    p1.setId(10);
    p1.setUser(u1);
    p1.setOrderInMatch(0);
    p1.setCardsDrawnInTurn(2);

    User u2 = new User();
    u2.setId(2);

    Player p2 = new Player();
    p2.setId(20);
    p2.setUser(u2);
    p2.setOrderInMatch(1);

    Match match = new Match();
    match.setId(1);
    match.setCurrentTurnUserId(1);
    match.setPlayers(List.of(p1, p2));
    match.setTurnNumber(3);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(handService.findPlayerHand(Integer.valueOf(1), Integer.valueOf(10)))
            .thenReturn(new HandInGame());

    matchService.nextTurn(Integer.valueOf(1));

    assertEquals(0, p1.getCardsDrawnInTurn());

    verify(playerRepo).save(p1);
}
@Test
void shouldAdvanceToNextPlayer() {

    User u1 = new User();
    u1.setId(1);

    Player p1 = new Player();
    p1.setId(10);
    p1.setUser(u1);
    p1.setOrderInMatch(0);

    User u2 = new User();
    u2.setId(2);

    Player p2 = new Player();
    p2.setId(20);
    p2.setUser(u2);
    p2.setOrderInMatch(1);

    Match match = new Match();
    match.setId(1);
    match.setCurrentTurnUserId(1);
    match.setPlayers(List.of(p1, p2));
    match.setTurnNumber(1);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(handService.findPlayerHand(1, 10))
            .thenReturn(new HandInGame());

    matchService.nextTurn(1);

    assertEquals(2, match.getCurrentTurnUserId());
    assertEquals(TurnPhase.DRAW, match.getCurrentTurnPhase());

    verify(matchRepo).save(match);
}
@Test
void shouldReturnToFirstPlayer() {

    User u1 = new User();
    u1.setId(1);

    Player p1 = new Player();
    p1.setId(10);
    p1.setUser(u1);
    p1.setOrderInMatch(0);

    User u2 = new User();
    u2.setId(2);

    Player p2 = new Player();
    p2.setId(20);
    p2.setUser(u2);
    p2.setOrderInMatch(1);

    Match match = new Match();
    match.setId(1);
    match.setCurrentTurnUserId(2);
    match.setPlayers(List.of(p1, p2));
    match.setTurnNumber(4);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(handService.findPlayerHand(Integer.valueOf(1), Integer.valueOf(20)))
            .thenReturn(new HandInGame());

    matchService.nextTurn(Integer.valueOf(1));

    assertEquals(1, match.getCurrentTurnUserId());
}
@Test
void shouldThrowWhenEndingUnknownMatch() {

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
            () -> matchService.endMatch(Integer.valueOf(1), null));
}
@Test
void shouldReturnWithoutModifyingFinishedMatch() {

    Match match = new Match();
    match.setId(1);
    match.setStatus(MatchStatus.FINISHED);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    MatchDTO result = matchService.endMatch(Integer.valueOf(1), null);

    assertEquals(MatchStatus.FINISHED, result.getStatus());

    verify(matchRepo, never()).save(any());
    verify(matchWebsocketController, never()).notifyEndMatch(anyInt(), any());
}
@Test
void shouldFinishMatchWithoutWinner() {

    Match match = new Match();
    match.setId(1);
    match.setStatus(MatchStatus.PLAYING);
    match.setPlayers(new ArrayList<>());
    match.setStartTime(LocalDateTime.now().minusMinutes(10));

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(matchRepo.save(any())).thenAnswer(i -> i.getArgument(0));

    MatchDTO result = matchService.endMatch(Integer.valueOf(1), null);

    assertEquals(MatchStatus.FINISHED, match.getStatus());
    assertNull(match.getWinner());
    assertNotNull(match.getEndTime());

    verify(matchRepo).save(match);
    verify(matchWebsocketController).notifyEndMatch(eq(Integer.valueOf(1)), any(MatchDTO.class));
}
@Test
void shouldFinishMatchWithWinner() {

    User user = new User();
    user.setId(1);

    Player winner = new Player();
    winner.setId(10);
    winner.setUser(user);

    Match match = new Match();
    match.setId(1);
    match.setStatus(MatchStatus.PLAYING);
    match.setPlayers(List.of(winner));
    match.setStartTime(LocalDateTime.now().minusMinutes(5));

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(matchRepo.save(any())).thenAnswer(i -> i.getArgument(0));

    MatchDTO result = matchService.endMatch(Integer.valueOf(1), winner);

    assertEquals(winner, match.getWinner());
    assertEquals(MatchStatus.FINISHED, result.getStatus());

    verify(matchRepo).save(match);
}
@Test
void shouldThrowWhenWinnerIsNotInMatch() {

    Player winner = new Player();
    winner.setId(50);

    Match match = new Match();
    match.setId(1);
    match.setStatus(MatchStatus.PLAYING);
    match.setPlayers(new ArrayList<>());

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    assertThrows(IllegalArgumentException.class,
            () -> matchService.endMatch(Integer.valueOf(1), winner));

    verify(matchRepo, never()).save(any());
}
@Test
void shouldSetEndTime() {

    Match match = new Match();
    match.setId(1);
    match.setStatus(MatchStatus.PLAYING);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(matchRepo.save(any())).thenAnswer(i -> i.getArgument(0));

    matchService.endMatch(Integer.valueOf(1), null);

    assertNotNull(match.getEndTime());
}
@Test
void shouldNotifyEndMatch() {

    Match match = new Match();
    match.setId(1);
    match.setStatus(MatchStatus.PLAYING);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(matchRepo.save(any())).thenAnswer(i -> i.getArgument(0));

    matchService.endMatch(Integer.valueOf(1), null);

    verify(matchWebsocketController)
            .notifyEndMatch(eq(Integer.valueOf(1)), any(MatchDTO.class));
}
@Test
void shouldThrowWhenLeavingUnknownMatch() {

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
            () -> matchService.leaveMatch(Integer.valueOf(1), Integer.valueOf(1)));
}
@Test
void shouldThrowWhenPlayerIsNotInMatch() {

    Match match = new Match();
    match.setId(1);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1)))
            .thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
            () -> matchService.leaveMatch(Integer.valueOf(1), Integer.valueOf(1)));
}
@Test
void shouldSaveAbandonedMatch() {

    User user = new User();
    user.setId(1);

    Player player = new Player();
    player.setId(10);
    player.setUser(user);
    player.setOrderInMatch(0);

    Match match = new Match();
    match.setId(1);
    match.setPlayers(new ArrayList<>(List.of(player)));
    match.setCurrentTurnUserId(2);
    match.setMinPlayers(1);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1))).thenReturn(Optional.of(player));
    when(userService.findUser(Integer.valueOf(1))).thenReturn(user);

    matchService.leaveMatch(Integer.valueOf(1), Integer.valueOf(1));

    verify(abandonedMatchService).saveAbandonedMatch(user, match);
}
@Test
void shouldRemovePlayerFromMatch() {

    User user = new User();
    user.setId(1);

    Player player = new Player();
    player.setId(10);
    player.setUser(user);
    player.setOrderInMatch(0);

    Match match = new Match();
    match.setPlayers(new ArrayList<>(List.of(player)));
    match.setCurrentTurnUserId(2);
    match.setMinPlayers(1);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1))).thenReturn(Optional.of(player));
    when(userService.findUser(Integer.valueOf(1))).thenReturn(user);

    matchService.leaveMatch(Integer.valueOf(1), Integer.valueOf(1));

    assertTrue(match.getPlayers().isEmpty());
}

@Test
void shouldUpdatePlayersOrder() {

    User u1 = new User();
    u1.setId(1);

    User u2 = new User();
    u2.setId(2);

    User u3 = new User();
    u3.setId(3);

    Player p1 = new Player();
    p1.setUser(u1);
    p1.setOrderInMatch(0);

    Player p2 = new Player();
    p2.setUser(u2);
    p2.setOrderInMatch(1);

    Player p3 = new Player();
    p3.setUser(u3);
    p3.setOrderInMatch(2);

    Match match = new Match();
    match.setPlayers(new ArrayList<>(List.of(p1, p2, p3)));
    match.setCurrentTurnUserId(3);
    match.setMinPlayers(2);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(2))).thenReturn(Optional.of(p2));
    when(userService.findUser(Integer.valueOf(2))).thenReturn(u2);

    matchService.leaveMatch(Integer.valueOf(1), Integer.valueOf(2));

    assertEquals(1, p3.getOrderInMatch());

    verify(playerRepo).save(p3);
}
@Test
void shouldEndMatchIfPlayersAreLessThanMinimum() {

    User user = new User();
    user.setId(1);

    Player player = new Player();
    player.setUser(user);
    player.setOrderInMatch(0);

    Match match = new Match();
    match.setId(1);
    match.setPlayers(new ArrayList<>(List.of(player)));
    match.setCurrentTurnUserId(2);
    match.setMinPlayers(2);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1))).thenReturn(Optional.of(player));
    when(userService.findUser(Integer.valueOf(1))).thenReturn(user);

    matchService.leaveMatch(Integer.valueOf(1), Integer.valueOf(1));

    verify(matchRepo, atLeastOnce()).save(any());
}
@Test
void shouldNotifyPlayerLeft() {

    User user = new User();
    user.setId(1);

    Player player = new Player();
    player.setUser(user);
    player.setOrderInMatch(0);

    Match match = new Match();
    match.setPlayers(new ArrayList<>(List.of(player)));
    match.setCurrentTurnUserId(2);
    match.setMinPlayers(1);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1))).thenReturn(Optional.of(player));
    when(userService.findUser(Integer.valueOf(1))).thenReturn(user);

    matchService.leaveMatch(Integer.valueOf(1), Integer.valueOf(1));

    verify(matchWebsocketController)
            .notifyPlayerLeft(eq(Integer.valueOf(1)), any(MatchDTO.class));
}



@Test
void shouldThrowWhenPlayerHasDrawnTooManyCards() {

    Player player = new Player();
    player.setId(1);

    when(playerRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(player));

    doThrow(new MoreThan7CardsDrawnException("Too many cards"))
            .when(checkers)
            .checkCardsDrawnInTurn(player);

    assertThrows(MoreThan7CardsDrawnException.class,
            () -> matchService.playerDrawsCardFromDeck(Integer.valueOf(1), Integer.valueOf(1)));

    verify(deckService,never()).drawCard(anyInt());
}


@Test
void shouldThrowWhenMatchDoesNotExistInMoveLoserPlayer() {

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
            () -> matchService.moveLoserPlayer(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)));
}
@Test
void shouldThrowWhenPlayerDoesNotExistInMoveLoserPlayer() {

    Match match = new Match();
    match.setCurrentTurnPhase(TurnPhase.ACTIONS);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1))).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
            () -> matchService.moveLoserPlayer(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)));
}
@Test
void shouldThrowWhenTargetRoomDoesNotExist() {

    Match match = new Match();
    match.setCurrentTurnPhase(TurnPhase.ACTIONS);

    Player player = new Player();

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1))).thenReturn(Optional.of(player));
    when(roomRepo.findById(Integer.valueOf(1))).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
            () -> matchService.moveLoserPlayer(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)));
}
@Test
void shouldMovePlayerToTargetRoom() {

    Match match = new Match();
    match.setCurrentTurnPhase(TurnPhase.ACTIONS);

    Room currentRoom = new Room();
    currentRoom.setId(1);

    Room targetRoom = new Room();
    targetRoom.setId(2);

    Player player = new Player();
    player.setRoom(currentRoom);
    player.setStrength(3);
    player.setRoomsVisited(4);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1))).thenReturn(Optional.of(player));
    when(roomRepo.findById(Integer.valueOf(2))).thenReturn(Optional.of(targetRoom));
    when(playerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

    Player result = matchService.moveLoserPlayer(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2));

    assertEquals(targetRoom, result.getRoom());
    assertEquals(4, result.getStrength());
    assertEquals(5, result.getRoomsVisited());
}
@Test
void shouldNotIncreaseVisitedRoomsWhenRoomIsTheSame() {

    Match match = new Match();
    match.setCurrentTurnPhase(TurnPhase.ACTIONS);

    Room room = new Room();
    room.setId(5);

    Player player = new Player();
    player.setRoom(room);
    player.setRoomsVisited(7);
    player.setStrength(2);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1))).thenReturn(Optional.of(player));
    when(roomRepo.findById(Integer.valueOf(5))).thenReturn(Optional.of(room));
    when(playerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

    Player result = matchService.moveLoserPlayer(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(5));

    assertEquals(7, result.getRoomsVisited());
    assertEquals(3, result.getStrength());
}
@Test
void shouldSetTurnPhaseToActions() {

    Match match = new Match();
    match.setCurrentTurnPhase(TurnPhase.DRAW);

    Room room = new Room();
    room.setId(2);

    Player player = new Player();
    player.setStrength(1);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(playerRepo.findByMatchAndUser(Integer.valueOf(1), Integer.valueOf(1))).thenReturn(Optional.of(player));
    when(roomRepo.findById(Integer.valueOf(2))).thenReturn(Optional.of(room));
    when(playerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

    matchService.moveLoserPlayer(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2));

    assertEquals(TurnPhase.ACTIONS, match.getCurrentTurnPhase());
}

@Test
void shouldReturnAvailableRooms() {

    Room occupied = new Room();
    occupied.setId(1);

    Room free = new Room();
    free.setId(2);

    Match match = new Match();

    Player player = new Player();
    player.setRoom(occupied);

    match.setPlayers(List.of(player));
    match.setNpcs(new ArrayList<>());

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(roomRepo.findAll()).thenReturn(new ArrayList<>(List.of(occupied, free)));

    when(roomService.getAllTowers()).thenReturn(new ArrayList<>());

    List<Room> result = matchService.getAvailableRoomsForPlayer(Integer.valueOf(1));

    assertEquals(1, result.size());
    assertTrue(result.contains(free));
}
@Test
void shouldRemoveNpcRooms() {

    Room npcRoom = new Room();
    npcRoom.setId(5);

    Room free = new Room();
    free.setId(8);

    Npc npc = new Npc();
    npc.setRoom(npcRoom);

    Match match = new Match();
    match.setPlayers(new ArrayList<>());
    match.setNpcs(List.of(npc));

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(roomRepo.findAll()).thenReturn(new ArrayList<>(List.of(npcRoom, free)));

    when(roomService.getAllTowers()).thenReturn(new ArrayList<>());

    List<Room> result = matchService.getAvailableRoomsForPlayer(Integer.valueOf(1));

    assertEquals(1, result.size());
    assertFalse(result.contains(npcRoom));
}
@Test
void shouldRemoveTowerRooms() {

    Room tower = new Room();
    tower.setId(31);

    Room normal = new Room();
    normal.setId(15);

    Match match = new Match();
    match.setPlayers(new ArrayList<>());
    match.setNpcs(new ArrayList<>());

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(roomRepo.findAll()).thenReturn(new ArrayList<>(List.of(tower, normal)));

    when(roomService.getAllTowers()).thenReturn(List.of(tower));

    List<Room> result = matchService.getAvailableRoomsForPlayer(Integer.valueOf(1));

    assertEquals(1, result.size());
    assertTrue(result.contains(normal));
}
@Test
void shouldNotDuplicateOccupiedRooms() {

    Room room = new Room();
    room.setId(3);

    Player p1 = new Player();
    p1.setRoom(room);

    Player p2 = new Player();
    p2.setRoom(room);

    Match match = new Match();
    match.setPlayers(List.of(p1, p2));
    match.setNpcs(new ArrayList<>());

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(roomRepo.findAll()).thenReturn(new ArrayList<>(List.of(room)));

    when(roomService.getAllTowers()).thenReturn(new ArrayList<>());

    List<Room> result = matchService.getAvailableRoomsForPlayer(Integer.valueOf(1));

    assertTrue(result.isEmpty());
}
@Test
void shouldReturnEmptyWhenNoRoomsAvailable() {

    Room room = new Room();
    room.setId(1);

    Player player = new Player();
    player.setRoom(room);

    Match match = new Match();
    match.setPlayers(List.of(player));
    match.setNpcs(new ArrayList<>());

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(roomRepo.findAll()).thenReturn(new ArrayList<>(List.of(room)));

    when(roomService.getAllTowers()).thenReturn(new ArrayList<>());

    List<Room> result = matchService.getAvailableRoomsForPlayer(Integer.valueOf(1));

    assertTrue(result.isEmpty());
}
@Test
void shouldCallGetAllTowers() {

    Match match = new Match();
    match.setPlayers(new ArrayList<>());
    match.setNpcs(new ArrayList<>());

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));

    when(roomRepo.findAll()).thenReturn(new ArrayList<>());

    when(roomService.getAllTowers()).thenReturn(new ArrayList<>());

    matchService.getAvailableRoomsForPlayer(Integer.valueOf(1));

    verify(roomService).getAllTowers();
}
@Test
void shouldReturnMatchIfUserIsAlreadyPlayer() {

    User user = new User();
    user.setId(1);

    Player player = new Player();
    player.setUser(user);

    Match match = new Match();
    match.setPlayers(new ArrayList<>(List.of(player)));
    match.setSpectators(new ArrayList<>());

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);

    MatchDTO dto = matchService.spectateGame(Integer.valueOf(1));

    assertNotNull(dto);

    verify(matchRepo, never()).save(any());
    verify(checkers, never()).checkCanSpectateGame(any(), any());
}
@Test
void shouldReturnMatchIfUserIsAlreadySpectator() {

    User user = new User();
    user.setId(1);

    Match match = new Match();
    match.setId(1);
    match.setPlayers(new ArrayList<>());
    match.setSpectators(new ArrayList<>(List.of(user)));

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);

    MatchDTO dto = matchService.spectateGame(Integer.valueOf(1));

    assertNotNull(dto);

    verify(matchRepo, never()).save(any());
}

@Test
void shouldAddSpectator() {

    User user = new User();
    user.setId(5);
    user.setUsername("Julian");

    Match match = new Match();
    match.setId(1);
    match.setPlayers(new ArrayList<>());
    match.setSpectators(new ArrayList<>());

    LobbyUpdateDTO update = mock(LobbyUpdateDTO.class);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);

    when(lobbyService.createLobbyUpdate(match, "SPECTATOR_JOIN", "Julian"))
            .thenReturn(update);

    MatchDTO dto = matchService.spectateGame(Integer.valueOf(1));

    assertEquals(1, match.getSpectators().size());
    assertTrue(match.getSpectators().contains(user));

    verify(matchRepo).save(match);
}
@Test
void shouldCheckPermissionsBeforeJoiningAsSpectator() {

    User user = new User();
    user.setId(3);

    Match match = new Match();
    match.setPlayers(new ArrayList<>());
    match.setSpectators(new ArrayList<>());

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);

    when(lobbyService.createLobbyUpdate(any(), any(), any()))
            .thenReturn(mock(LobbyUpdateDTO.class));

    matchService.spectateGame(Integer.valueOf(1));

    verify(checkers).checkCanSpectateGame(match, 3);
}
@Test
void shouldSaveMatchWhenSpectatorJoins() {

    User user = new User();
    user.setId(2);
    user.setUsername("Player");

    Match match = new Match();
    match.setId(1);
    match.setPlayers(new ArrayList<>());
    match.setSpectators(new ArrayList<>());

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);

    when(lobbyService.createLobbyUpdate(any(), any(), any()))
            .thenReturn(mock(LobbyUpdateDTO.class));

    matchService.spectateGame(Integer.valueOf(1));

    verify(matchRepo).save(match);
}
@Test
void shouldNotifySpectatorJoined() {

    User user = new User();
    user.setId(2);
    user.setUsername("Julian");

    Match match = new Match();
    match.setId(10);
    match.setPlayers(new ArrayList<>());
    match.setSpectators(new ArrayList<>());

    LobbyUpdateDTO update = mock(LobbyUpdateDTO.class);

    when(matchRepo.findById(Integer.valueOf(10))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);

    when(lobbyService.createLobbyUpdate(match, "SPECTATOR_JOIN", "Julian"))
            .thenReturn(update);

    matchService.spectateGame(Integer.valueOf(10));

    verify(lobbyWebsocketController)
            .notifyPlayerJoined(10, update);
}
@Test
void shouldThrowWhenUserCannotSpectate() {

    User user = new User();
    user.setId(4);

    Match match = new Match();
    match.setPlayers(new ArrayList<>());
    match.setSpectators(new ArrayList<>());

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);

    doThrow(new IllegalArgumentException("Cannot spectate"))
            .when(checkers)
            .checkCanSpectateGame(match, 4);

    assertThrows(IllegalArgumentException.class,
            () -> matchService.spectateGame(Integer.valueOf(1)));

    verify(matchRepo, never()).save(any());
}
@Test
void shouldRemoveSpectatorFromMatch() {

    User user = new User();
    user.setId(1);
    user.setUsername("Julian");

    Match match = new Match();
    match.setId(1);
    match.setSpectators(new ArrayList<>(List.of(user)));

    LobbyUpdateDTO update = mock(LobbyUpdateDTO.class);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);
    when(lobbyService.createLobbyUpdate(match, "SPECTATOR_LEAVE", "Julian"))
            .thenReturn(update);

    matchService.stopSpectating(Integer.valueOf(1));

    assertTrue(match.getSpectators().isEmpty());

    verify(matchRepo).save(match);
}
@Test
void shouldSaveMatchWhenSpectatorLeaves() {

    User user = new User();
    user.setId(1);
    user.setUsername("Julian");

    Match match = new Match();
    match.setId(1);
    match.setSpectators(new ArrayList<>(List.of(user)));

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);
    when(lobbyService.createLobbyUpdate(any(), any(), any()))
            .thenReturn(mock(LobbyUpdateDTO.class));

    matchService.stopSpectating(Integer.valueOf(1));

    verify(matchRepo).save(match);
}
@Test
void shouldCreateLobbyUpdateWhenSpectatorLeaves() {

    User user = new User();
    user.setId(1);
    user.setUsername("Julian");

    Match match = new Match();
    match.setId(1);
    match.setSpectators(new ArrayList<>(List.of(user)));

    LobbyUpdateDTO update = mock(LobbyUpdateDTO.class);

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);
    when(lobbyService.createLobbyUpdate(match, "SPECTATOR_LEAVE", "Julian"))
            .thenReturn(update);

    matchService.stopSpectating(Integer.valueOf(1));

    verify(lobbyService)
            .createLobbyUpdate(match, "SPECTATOR_LEAVE", "Julian");
}
@Test
void shouldNotifySpectatorLeave() {

    User user = new User();
    user.setId(1);
    user.setUsername("Julian");

    Match match = new Match();
    match.setId(15);
    match.setSpectators(new ArrayList<>(List.of(user)));

    LobbyUpdateDTO update = mock(LobbyUpdateDTO.class);

    when(matchRepo.findById(Integer.valueOf(15))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(user);
    when(lobbyService.createLobbyUpdate(match, "SPECTATOR_LEAVE", "Julian"))
            .thenReturn(update);

    matchService.stopSpectating(Integer.valueOf(15));

    verify(lobbyWebsocketController)
            .notifyPlayerJoined(15, update);
}
@Test
void shouldDoNothingIfUserWasNotSpectator() {

    User currentUser = new User();
    currentUser.setId(2);
    currentUser.setUsername("Julian");

    User otherUser = new User();
    otherUser.setId(5);

    Match match = new Match();
    match.setId(1);
    match.setSpectators(new ArrayList<>(List.of(otherUser)));

    when(matchRepo.findById(Integer.valueOf(1))).thenReturn(Optional.of(match));
    when(userService.findCurrentUser()).thenReturn(currentUser);
    when(lobbyService.createLobbyUpdate(any(), any(), any()))
            .thenReturn(mock(LobbyUpdateDTO.class));

    matchService.stopSpectating(Integer.valueOf(1));

    assertEquals(1, match.getSpectators().size());
    assertTrue(match.getSpectators().contains(otherUser));

    verify(matchRepo).save(match);
}
}