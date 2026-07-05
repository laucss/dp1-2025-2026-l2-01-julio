package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.InvalidMovementException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.NoActionPointsException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DrawCardResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.ActionPointsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.EscapeAttemptResultDTO;
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
    void getMatchByIdNotFoundThrows() {
        when(matchRepo.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> matchService.getMatchById(99));
    }

    // tests de operaciones básicas

    @Test
    public void getMatchByIdThrowsExceptionWhenNotFound() {
        when(matchRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> matchService.getMatchById(1));
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

        Page<Match> result = matchService.getFinishedAndInProgressMatches(0, 5);

        assertEquals(3, result.getContent().size());
    }

    @Test
    void getMatchesPlayedByUserCallsRepo() {
        int userId = 7;
        List<Match> list = List.of(new Match());
        Page<Match> page = new PageImpl<>(list);
        when(matchRepo.findMatchesPlayedByUser(eq(userId), any(PageRequest.class))).thenReturn(page);

        Page<Match> result = matchService.getMatchesPlayedByUser(userId, 0, 3);

        assertEquals(1, result.getContent().size());
        verify(matchRepo).findMatchesPlayedByUser(eq(userId), any(PageRequest.class));
    }

    @Test
    void getMatchesWonByUserCallsRepo() {
        int userId = 13;
        List<Match> list = List.of(new Match());
        Page<Match> page = new PageImpl<>(list);
        when(matchRepo.findMatchesWonByUser(eq(userId), any(PageRequest.class))).thenReturn(page);

        Page<Match> result = matchService.getMatchesWonByUser(userId, 0, 3);

        assertEquals(1, result.getContent().size());
        verify(matchRepo).findMatchesWonByUser(eq(userId), any(PageRequest.class));
    }

    // test de startMatch


        // CORREGIR 
    /*


    // partida not found
    @Test
    void startMatchThrowsWhenMatchNotFound() {
        when(matchRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> matchService.startMatch(1));
    }

    // caso positivo todo bien
    @Test
    void startMatchSetsStatusStartTimeAndSavesMatch() {
        int matchId = 1;

        Match m = new Match();
        m.setId(matchId);
        m.setStatus(MatchStatus.WAITING);
        m.setNumNpcs(0);
        m.setPlayers(new ArrayList<>());
        m.setNpcs(new ArrayList<>());

        when(matchRepo.findById(matchId)).thenReturn(Optional.of(m));
        when(matchRepo.save(any(Match.class))).thenAnswer(i -> i.getArgument(0));

        when(roomService.initializeRoomsForMatch(m)).thenReturn(new ArrayList<>());
        doNothing().when(lobbyWebsocketController)
            .notifyGameStarted(eq(matchId), any());

        Match result = matchService.startMatch(matchId);

        assertEquals(MatchStatus.PLAYING, result.getStatus());
        assertNotNull(result.getStartTime());
        assertEquals(0, result.getTurnNumber());
        assertNull(result.getCurrentTurnUserId());

        verify(matchRepo).save(m);
    }

    // check npc bien
    @Test
    void startMatchCreatesNpcsAndMarksLastAsNiallCampbell() {
        int matchId = 2;

        Match m = new Match();
        m.setId(matchId);
        m.setNumNpcs(3);
        m.setPlayers(new ArrayList<>());
        m.setNpcs(new ArrayList<>());

        when(matchRepo.findById(matchId)).thenReturn(Optional.of(m));
        when(matchRepo.save(any(Match.class))).thenReturn(m);
        when(roomService.initializeRoomsForMatch(m)).thenReturn(new ArrayList<>());
        doNothing().when(lobbyWebsocketController)
            .notifyGameStarted(eq(matchId), any());

        when(npcRepository.save(any(Npc.class)))
            .thenAnswer(i -> i.getArgument(0));

        matchService.startMatch(matchId);

        assertEquals(3, m.getNpcs().size());

        Npc lastNpc = m.getNpcs().get(2);
        assertTrue(lastNpc.getIsNiallCampbell());

        for (Npc npc : m.getNpcs()) {
            assertEquals(1, npc.getStrength());
            assertEquals(m, npc.getMatch());
        }

        verify(npcRepository, times(3)).save(any(Npc.class));
    }

    // incia bien los players
    @Test
    void startMatchInitializesPlayersState() {
        int matchId = 3;

        Player p1 = new Player();
        Player p2 = new Player();

        Match m = new Match();
        m.setId(matchId);
        m.setNumNpcs(0);
        m.setPlayers(new ArrayList<>(List.of(p1, p2)));
        m.setNpcs(new ArrayList<>());

        when(matchRepo.findById(matchId)).thenReturn(Optional.of(m));
        when(matchRepo.save(any(Match.class))).thenReturn(m);
        when(roomService.initializeRoomsForMatch(m)).thenReturn(new ArrayList<>());
        doNothing().when(lobbyWebsocketController)
            .notifyGameStarted(eq(matchId), any());

        matchService.startMatch(matchId);

        for (Player p : m.getPlayers()) {
            assertNull(p.getDiceOrder());
            assertNull(p.getOrderInMatch());
            assertEquals(0, p.getActionPoints());
            assertEquals(1, p.getStrength());
            assertEquals(0, p.getCardsDrawnInTurn());
        }
    }


    @Test
    void startMatchInitializesDeckAndRoomsState() {
        int matchId = 4;

        Match m = new Match();
        m.setId(matchId);
        m.setNumNpcs(0);
        m.setPlayers(new ArrayList<>());
        m.setNpcs(new ArrayList<>());

        DeckInGame deck = new DeckInGame();

        when(matchRepo.findById(matchId)).thenReturn(Optional.of(m));
        when(matchRepo.save(any(Match.class))).thenReturn(m);
        when(roomService.initializeRoomsForMatch(m)).thenReturn(new ArrayList<>());
        doNothing().when(lobbyWebsocketController)
            .notifyGameStarted(eq(matchId), any());

        // método interno del servicio
        doReturn(deck)
            .when(matchService)
            .initializePlayerHandCards(matchId, m.getPlayers());

        Match result = matchService.startMatch(matchId);

        assertEquals(deck, result.getDeck());
        assertNotNull(result.getRoomsState());
    }

    @Test
    void startMatchNotifiesLobbyWebsocket() {
        int matchId = 5;

        Match m = new Match();
        m.setId(matchId);
        m.setNumNpcs(0);
        m.setPlayers(new ArrayList<>());
        m.setNpcs(new ArrayList<>());

        when(matchRepo.findById(matchId)).thenReturn(Optional.of(m));
        when(matchRepo.save(any(Match.class))).thenReturn(m);
        when(roomService.initializeRoomsForMatch(m)).thenReturn(new ArrayList<>());
        doNothing().when(lobbyWebsocketController)
            .notifyGameStarted(eq(matchId), any());

        doReturn(new DeckInGame())
            .when(matchService)
            .initializePlayerHandCards(anyInt(), anyList());

        matchService.startMatch(matchId);

        verify(lobbyWebsocketController)
            .notifyGameStarted(eq(matchId), any(LobbyUpdateDTO.class));
    }

    */

    @Test
    void endMatchSetsStatusAndWinner() {
        Match m = new Match();
        m.setId(55);
        m.setStatus(MatchStatus.PLAYING);
        m.setStartTime(LocalDateTime.now().minusMinutes(5));

        Player winner = new Player();
        winner.setId(99);

        when(matchRepo.findById(Integer.valueOf(55))).thenReturn(Optional.of(m));
        when(matchRepo.save(any(Match.class))).thenAnswer(i -> i.getArgument(0));

        matchService.endMatch(55, winner);

        assertEquals(MatchStatus.FINISHED, m.getStatus());
        assertEquals(winner, m.getWinner());
        assertNotNull(m.getEndTime());
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

        Page<Match> result = matchService.getMatchesPlayedAndCreatedByUser(userId, 0, 3);

        assertEquals(1, result.getContent().size());
        verify(matchRepo).findMatchesPlayedAndCreatedByUser(eq(userId), any(PageRequest.class));
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

    /* 
    @Test
    void getInProgressMatchesCallsRepo() {
        List<Match> list = List.of(new Match());
        when(matchRepo.findInProgress()).thenReturn(list);
        List<Match> result = matchService.getInProgressMatches();
        assertEquals(1, result.size());
        verify(matchRepo).findInProgress();
    } */

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

    /*
    @Test
    void playerBeatsNonPlayerDrawsCardAndAddsToHand() {
        int matchId = 11;
        int playerId = 12;
        Card card = new Card();
        card.setId(999);
        card.setLetter("X");
        card.setFrontImage("f");
        card.setBackImage("b");
        when(deckService.drawCard(matchId)).thenReturn(card);
        when(handService.addCardToPlayerHand(card, matchId, playerId)).thenReturn(new HandInGame());

        // ensure NPC and player exist for the interaction
        Player player = new Player(); player.setId(playerId);
        when(playerService.findById(playerId)).thenReturn(player);
        Npc npc = new Npc(); npc.setId(99); npc.setStrength(0);
        when(npcRepository.findById(Integer.valueOf(99))).thenReturn(Optional.of(npc));
        // ensure card/hand/deck/bag objects exist for getAllCards
        DeckInGame deck = new DeckInGame();
        deck.setNotDiscardedCards(new java.util.ArrayList<>());
        when(deckService.findDeckById(matchId)).thenReturn(deck);
        HandInGame hand = new HandInGame();
        hand.setCards(new java.util.ArrayList<>());
        when(handService.findPlayerHand(matchId, playerId)).thenReturn(hand);
        BagInGame bag = new BagInGame(new java.util.ArrayList<>());
        when(bagService.findPlayerBag(matchId, playerId)).thenReturn(bag);

        Card result = matchService.playerBeatsNonPlayer(matchId, playerId, 99);

        assertEquals(card, result);
        verify(deckService).drawCard(matchId);
        verify(handService).addCardToPlayerHand(card, matchId, playerId);
    }
    */

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

    /* 
    @Test
    void playerWinsNiallCampbellReturnsCardWhenPresentOrNull() {
        int matchId = 31;
        int playerId = 32;
        Card card = new Card();
        HandInGame hand = new HandInGame();

        when(deckService.getAndRemoveLastDiscardedCard(matchId)).thenReturn(card);
        when(handService.addCardToPlayerHand(card, matchId, playerId)).thenReturn(hand);

        Card r1 = matchService.playerWinsNiallCampbell(matchId, playerId);
        assertEquals(card, r1);

        when(deckService.getAndRemoveLastDiscardedCard(matchId)).thenReturn(null);
        Card r2 = matchService.playerWinsNiallCampbell(matchId, playerId);
        assertNull(r2);
    }
        */

    /*
    @Test
    void playerLosesAgaintsNonPlayerZeroesActionPointsAndMovesCardToDiscard() {
        int matchId = 41;
        int playerId = 42;
        Card card = new Card();
        Player p = new Player();
        p.setId(playerId);
        p.setActionPoints(5);

        when(playerService.findById(playerId)).thenReturn(p);
        when(handService.removeCardFromPlayerHand(card, matchId, playerId)).thenReturn(card);
        when(deckService.findDeckById(matchId)).thenReturn(new DeckInGame());
        when(handService.findPlayerHand(matchId, playerId)).thenReturn(new HandInGame());
        when(bagService.findPlayerBag(matchId, playerId)).thenReturn(new BagInGame());
        doNothing().when(deckService).addCardToDiscardedPile(matchId, card);
        when(playerService.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        matchService.playerLosesAgaintsNonPlayer(card, matchId, playerId, 99, "hand");

        assertEquals(0, p.getActionPoints());
        verify(handService).removeCardFromPlayerHand(card, matchId, playerId);
        verify(deckService).addCardToDiscardedPile(matchId, card);
        verify(playerService).save(p);
    }
    */

    @Test
    void moveLoserPlayerUpdatesRoomAndStrength() {
        int matchId = 51;
        int userId = 52;
        int roomId = 60;

        Match m = new Match(); m.setId(matchId);
        when(matchRepo.findById(Integer.valueOf(matchId))).thenReturn(Optional.of(m));

        Player p = new Player(); p.setId(70); p.setStrength(0);
        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(p));

        Room target = new Room(); target.setId(roomId);
        when(roomRepo.findById(Integer.valueOf(roomId))).thenReturn(Optional.of(target));
        when(playerRepo.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        Player res = matchService.moveLoserPlayer(matchId, userId, roomId);

        assertEquals(target, res.getRoom());
        assertEquals(Integer.valueOf(1), res.getStrength());
    }

    @Test
    void movePlayerToAdyacentRoomMovesAndConsumesActionPoint() {
        int matchId = 61; int userId = 62; int targetRoomId = 70;

        Match m = new Match(); m.setId(matchId);
        when(matchRepo.findById(Integer.valueOf(matchId))).thenReturn(Optional.of(m));

        Room current = new Room(); current.setId(1);
        Room target = new Room(); target.setId(targetRoomId);
        current.setAdjacencyList(List.of(target));

        Player p = new Player(); p.setId(100);
        p.setRoom(current);
        p.setActionPoints(2);
        User u = new User(); u.setId(userId); p.setUser(u);

        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(p));
        when(roomRepo.findById(Integer.valueOf(targetRoomId))).thenReturn(Optional.of(target));
        when(playerRepo.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        Player res = matchService.movePlayerToAdyacentRoom(matchId, userId, targetRoomId);

        assertEquals(target, res.getRoom());
        assertEquals(1, res.getActionPoints());
    }

    // la partida no existe 
    @Test
    void movePlayerToAdyacentRoomThrowsWhenMatchNotFound() {
        when(matchRepo.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> matchService.movePlayerToAdyacentRoom(1, 2, 3));
    }

    // jugador no existe 
    @Test
    void movePlayerToAdyacentRoomThrowsWhenPlayerNotFound() {
        Match match = new Match();
        match.setId(1);
        when(matchRepo.findById(1)).thenReturn(Optional.of(match));

        when(playerRepo.findByMatchAndUser(1, 2)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> matchService.movePlayerToAdyacentRoom(1, 2, 3));
    }

    // juagdor no tiene sala asignada
    @Test
    void movePlayerToAdyacentRoomThrowsWhenPlayerHasNoRoom() {
        Match match = new Match();
        match.setId(1);
        when(matchRepo.findById(1)).thenReturn(Optional.of(match));

        Player player = new Player();
        player.setRoom(null);

        when(playerRepo.findByMatchAndUser(1, 2)).thenReturn(Optional.of(player));

        assertThrows(RuntimeException.class,
            () -> matchService.movePlayerToAdyacentRoom(1, 2, 3));
    }


        // CORREGIR 
    /*
    // jugador no tiene puntos de accion
    @Test
    void movePlayerToAdyacentRoomThrowsWhenNoActionPoints() {
        int matchId = 1;
        int userId = 2;
        int targetRoomId = 3;

        Match match = new Match();
        match.setId(matchId);
        match.setCurrentTurnPhase(TurnPhase.ACTIONS);

        when(matchRepo.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepo.save(any(Match.class))).thenReturn(match);

        Room room = new Room();
        room.setId(10);
        room.setAdjacencyList(List.of());

        Player player = new Player();
        player.setRoom(room);
        player.setActionPoints(0);

        when(playerRepo.findByMatchAndUser(matchId, userId))
            .thenReturn(Optional.of(player));

        assertThrows(NoActionPointsException.class,
            () -> matchService.movePlayerToAdyacentRoom(matchId, userId, targetRoomId));
    }

    */

    
    
    // sala destiono no existe 
    @Test
    void movePlayerToAdyacentRoomThrowsWhenTargetRoomNotFound() {
        Match match = new Match();
        match.setId(1);
        when(matchRepo.findById(1)).thenReturn(Optional.of(match));

        Room currentRoom = new Room();
        currentRoom.setId(10);

        Player player = new Player();
        player.setRoom(currentRoom);
        player.setActionPoints(1);

        when(playerRepo.findByMatchAndUser(1, 2)).thenReturn(Optional.of(player));
        when(roomRepo.findById(3)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> matchService.movePlayerToAdyacentRoom(1, 2, 3));
    }


        // CORREGIR 
    /*


    // player intenta moverse a una habitacion que no es adyacente 
    @Test
    void movePlayerToAdyacentRoomThrowsIfRoomNotAdjacent() {
        int matchId = 1;
        int userId = 2;
        int targetRoomId = 7;

        Match match = new Match();
        match.setId(matchId);
        when(matchRepo.findById(matchId)).thenReturn(Optional.of(match));

        Room currentRoom = new Room();
        currentRoom.setId(1);

        Room targetRoom = new Room();
        targetRoom.setId(targetRoomId);

        // NO es adyacente
        currentRoom.setAdjacencyList(List.of());

        Player player = new Player();
        player.setRoom(currentRoom);
        player.setActionPoints(1);

        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(player));
        when(roomRepo.findById(targetRoomId)).thenReturn(Optional.of(targetRoom));

        assertThrows(InvalidMovementException.class,
            () -> matchService.movePlayerToAdyacentRoom(matchId, userId, targetRoomId));
    }

    // si el turno no esta en actions
    @Test
    void movePlayerToAdyacentRoomSetsTurnPhaseToActionsIfNotActions() {
        Match match = new Match();
        match.setId(1);
        match.setCurrentTurnPhase(TurnPhase.DRAW);
        when(matchRepo.findById(1)).thenReturn(Optional.of(match));
        when(matchRepo.save(any(Match.class))).thenReturn(match);

        Room currentRoom = new Room();
        Room targetRoom = new Room();
        targetRoom.setId(3);
        currentRoom.setAdjacencyList(List.of(targetRoom));

        Player player = new Player();
        player.setRoom(currentRoom);
        player.setActionPoints(1);

        when(playerRepo.findByMatchAndUser(1, 2)).thenReturn(Optional.of(player));
        when(roomRepo.findById(3)).thenReturn(Optional.of(targetRoom));
        when(playerRepo.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        matchService.movePlayerToAdyacentRoom(1, 2, 3);

        assertEquals(TurnPhase.ACTIONS, match.getCurrentTurnPhase());
        verify(matchRepo).save(match);
    }

    */


    @Test
    void moveNpcToAdyacentRoomMovesNpcAndConsumesPlayerActionPoint() {
        int matchId = 71; int npcId = 81; int userId = 82; int targetRoomId = 90;

        Match m = new Match(); m.setId(matchId);
        when(matchRepo.findById(Integer.valueOf(matchId))).thenReturn(Optional.of(m));

        Room current = new Room(); current.setId(2);
        Room target = new Room(); target.setId(targetRoomId);
        current.setAdjacencyList(List.of(target));

        Npc npc = new Npc(); npc.setId(npcId); npc.setRoom(current);
        when(npcRepository.findById(npcId)).thenReturn(Optional.of(npc));

        Player p = new Player(); p.setId(200); p.setActionPoints(3);
        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(p));
        when(roomRepo.findById(Integer.valueOf(targetRoomId))).thenReturn(Optional.of(target));
        when(playerRepo.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));
        when(npcRepository.save(any(Npc.class))).thenAnswer(i -> i.getArgument(0));

        Npc res = matchService.moveNpcToAdyacentRoom(matchId, npcId, targetRoomId, userId);

        assertEquals(target, res.getRoom());
        assertEquals(2, playerRepo.findByMatchAndUser(matchId, userId).get().getActionPoints());
    }

        // CORREGIR 
    /*

    @Test
    void moveNpcToAdyacentRoomThrowsWhenNpcNotFound() {
        when(npcRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> matchService.moveNpcToAdyacentRoom(1, 1, 2, 3));
    }

    */

    @Test
    void movePlayerByFormingRoomNameSucceedsWhenBagHasLetters() {
        int matchId = 101; int userId = 102; int targetRoomId = 110;

        Match m = new Match(); m.setId(matchId);
        when(matchRepo.findById(Integer.valueOf(matchId))).thenReturn(Optional.of(m));

        Room target = new Room(); target.setId(targetRoomId); target.setName("ABC");
        when(roomRepo.findById(Integer.valueOf(targetRoomId))).thenReturn(Optional.of(target));

        // bag with letters A,B,C
        Card c1 = new Card(); c1.setLetter("A");
        Card c2 = new Card(); c2.setLetter("B");
        Card c3 = new Card(); c3.setLetter("C");
        BagInGame bag = new BagInGame(List.of(c1,c2,c3));

        Player p = new Player(); p.setId(300); p.setActionPoints(2);
        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(p));
        when(bagService.findPlayerBag(matchId, p.getId())).thenReturn(bag);
        when(playerRepo.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        Player res = matchService.movePlayerByFormingRoomName(matchId, userId, targetRoomId);

        assertEquals(target, res.getRoom());
        assertEquals(1, res.getActionPoints());
    }

    @Test
    void escapeAttemptSuccessAndFailurePaths() {
        int matchId = 121; int userId = 122;

        Match m = new Match(); m.setId(matchId);
        when(matchRepo.findById(Integer.valueOf(matchId))).thenReturn(Optional.of(m));

        Room tower = new Room(); tower.setId(500);
        when(roomService.getAllTowers()).thenReturn(List.of(tower));

        Player p = new Player(); p.setId(400); User u = new User(); u.setId(userId); u.setUsername("ux"); p.setUser(u);
        p.setActionPoints(1);
        p.setRoom(tower);
        p.setStrength(5);
        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(p));

        // bag and word match
        BagInGame bag = new BagInGame(List.of(new Card()));
        when(bagService.findPlayerBag(matchId, p.getId())).thenReturn(bag);
        when(bagService.wordFromCards(any())).thenReturn("emperor");
        when(roomService.getWordOfEscapeFromTower(tower.getId())).thenReturn("emperor");

        doNothing().when(deckService).deleteDeckInGame(matchId);
        doNothing().when(handService).deleteMatchHands(matchId);
        doNothing().when(bagService).deleteMatchBags(matchId);
        when(matchRepo.save(any(Match.class))).thenAnswer(i -> i.getArgument(0));

        // success: rolldiceResult < strength
        EscapeAttemptResultDTO r1 = matchService.escapeAttempt(matchId, userId, 3);
        assertTrue(r1.isSuccess());

        // failure: rolldiceResult >= strength
        Room randomRoom = new Room(); randomRoom.setId(600);
        // make repository return both the tower and the random room; the service will remove towers and occupied rooms
        when(roomRepo.findAll()).thenReturn(new java.util.ArrayList<>(List.of(tower, randomRoom)));
        when(roomRepo.findById(Integer.valueOf(randomRoom.getId()))).thenReturn(Optional.of(randomRoom));
        p.setActionPoints(2);
        when(playerRepo.findByMatchAndUser(matchId, userId)).thenReturn(Optional.of(p));
        when(playerRepo.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        EscapeAttemptResultDTO r2 = matchService.escapeAttempt(matchId, userId, 10);
        assertFalse(r2.isSuccess());
        assertTrue(r2.isDiscardRequired());
        verify(roomRepo).findAll();
        verify(playerRepo, times(2)).save(p);
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
}