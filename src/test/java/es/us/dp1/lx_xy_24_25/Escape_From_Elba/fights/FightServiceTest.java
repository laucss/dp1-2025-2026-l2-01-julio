package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightResolvedDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightResultRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.LoseAgainstNpcRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.StealCardRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchWebsocketController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.NpcRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("FightService Tests")
class FightServiceTest {

    @Mock
    private MatchService matchService;
    @Mock
    private DeckService deckService;
    @Mock
    private HandService handService;
    @Mock
    private BagService bagService;
    @Mock
    private PlayerService playerService;
    @Mock
    private MatchWebsocketController matchWebsocketController;
    @Mock
    private MatchRepository matchRepo;
    @Mock
    private PlayerRepository playerRepo;
    @Mock
    private NpcRepository npcRepository;
    @Mock
    private RoomService roomService;

    @InjectMocks
    private FightService fightService;

    private Match testMatch;
    private Player attackerPlayer;
    private Player defenderPlayer;
    private User attackerUser;
    private User defenderUser;
    private Npc testNpc;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        testMatch = new Match();
        testMatch.setId(1);
        testMatch.setPendingFights(new ArrayList<>());

        attackerUser = new User();
        attackerUser.setId(10);
        attackerUser.setUsername("attacker");

        defenderUser = new User();
        defenderUser.setId(20);
        defenderUser.setUsername("defender");

        attackerPlayer = new Player();
        attackerPlayer.setId(1);
        attackerPlayer.setUser(attackerUser);
        attackerPlayer.setStrength(3);
        attackerPlayer.setActionPoints(3);

        defenderPlayer = new Player();
        defenderPlayer.setId(2);
        defenderPlayer.setUser(defenderUser);
        defenderPlayer.setStrength(2);
        defenderPlayer.setActionPoints(3);

        testNpc = new Npc();
        testNpc.setId(5);
        testNpc.setStrength(4);
        testNpc.setIsNiallCampbell(false);

        testRoom = new Room();
        testRoom.setId(100);
    }

    @Test
    @DisplayName("Process fight resolution throw exception when match not found")
    void testProcessFightResolutionMatchNotFound() {
        FightResultRequestDTO request = new FightResultRequestDTO();
        request.setMatchId(1);
        
        when(matchRepo.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fightService.processFightResolution(request));
    }

    @Test
    @DisplayName("NPC beats player when NPC attacks and wins")
    void testNpcBeatsPlayerScenario() {
        FightResultRequestDTO request = new FightResultRequestDTO();
        request.setMatchId(1);
        request.setNpcFight(true);
        request.setNpcAttacker(true);
        request.setAttackerWins(true);
        request.setAttackerId(5);
        request.setDefenderId(1);

        when(matchRepo.findById(any())).thenReturn(Optional.of(testMatch));
        when(playerService.findById(1)).thenReturn(defenderPlayer); 
        when(npcRepository.findById(5)).thenReturn(Optional.of(testNpc));
        when(roomService.getRandomRoom()).thenReturn(testRoom);
        
        when(playerRepo.findByMatchAndRoom(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(npcRepository.findByMatchAndRoom(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        FightResolvedDTO result = fightService.processFightResolution(request);

        assertNotNull(result);
        assertEquals(FightResultType.NPC_BEATS_PLAYER, result.getFightResultType());
    }

    @Test
    @DisplayName("Player beats normal NPC successfully")
    void testPlayerBeatsNormalNpc() {
        FightResultRequestDTO request = new FightResultRequestDTO();
        request.setMatchId(1);
        request.setNpcFight(true);
        request.setNpcAttacker(false);
        request.setAttackerWins(true);
        request.setAttackerId(1);
        request.setDefenderId(5);
        request.setDefenderRoomId(100);

        Card sampleCard = new Card();
        sampleCard.setId(99);

        when(matchRepo.findById(any())).thenReturn(Optional.of(testMatch));
        when(playerService.findById(1)).thenReturn(attackerPlayer);
        when(npcRepository.findById(5)).thenReturn(Optional.of(testNpc));
        when(roomService.findById(100)).thenReturn(testRoom);
        when(roomService.getRandomRoom()).thenReturn(testRoom);
        
        when(playerRepo.findByMatchAndRoom(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(npcRepository.findByMatchAndRoom(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        when(deckService.drawCard(anyInt())).thenReturn(sampleCard);
        when(matchService.getAllCards(anyInt(), anyInt())).thenReturn(new AllCardsStatusDTO());

        FightResolvedDTO result = fightService.processFightResolution(request);

        assertNotNull(result);
        assertEquals(FightResultType.PLAYER_BEATS_NPC, result.getFightResultType());
        assertEquals(1, attackerPlayer.getBattlesWon());
    }

    @Test
    @DisplayName("Player beats Niall Campbell NPC successfully")
    void testPlayerBeatsNiallCampbell() {
        testNpc.setIsNiallCampbell(true);
        FightResultRequestDTO request = new FightResultRequestDTO();
        request.setMatchId(1);
        request.setNpcFight(true);
        request.setNpcAttacker(false);
        request.setAttackerWins(true);
        request.setAttackerId(1);
        request.setDefenderId(5);
        request.setDefenderRoomId(100);

        Card discardedCard = new Card();
        discardedCard.setId(88);

        when(matchRepo.findById(any())).thenReturn(Optional.of(testMatch));
        when(playerService.findById(1)).thenReturn(attackerPlayer);
        when(npcRepository.findById(5)).thenReturn(Optional.of(testNpc));
        when(roomService.findById(100)).thenReturn(testRoom);
        when(roomService.getRandomRoom()).thenReturn(testRoom);
        
        when(playerRepo.findByMatchAndRoom(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(npcRepository.findByMatchAndRoom(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        when(deckService.getAndRemoveLastDiscardedCard(anyInt())).thenReturn(discardedCard);
        when(matchService.getAllCards(anyInt(), anyInt())).thenReturn(new AllCardsStatusDTO());

        FightResolvedDTO result = fightService.processFightResolution(request);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Player beats another Player")
    void testPlayerBeatsPlayer() {
        FightResultRequestDTO request = new FightResultRequestDTO();
        request.setMatchId(1);
        request.setNpcFight(false);
        request.setAttackerWins(true);
        request.setAttackerId(1);
        request.setDefenderId(2);
        request.setDefenderRoomId(100);

        when(matchRepo.findById(any())).thenReturn(Optional.of(testMatch));
        when(playerService.findById(1)).thenReturn(attackerPlayer);
        when(playerService.findById(2)).thenReturn(defenderPlayer);
        when(roomService.findById(100)).thenReturn(testRoom);
        when(roomService.getRandomRoom()).thenReturn(testRoom);
        
        when(playerRepo.findByMatchAndRoom(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(npcRepository.findByMatchAndRoom(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        FightResolvedDTO result = fightService.processFightResolution(request);

        assertNotNull(result);
        assertEquals(FightResultType.PLAYER_BEATS_PLAYER, result.getFightResultType());
    }

    @Test
    @DisplayName("Player steal from Player from hand")
    void testPlayerStealFromPlayerHand() {
        StealCardRequestDTO request = new StealCardRequestDTO();
        request.setWinnerId(1);
        request.setLoserId(2);
        request.setFromWhere("hand");

        HandInGame loserHand = new HandInGame();
        Card stolenCard = new Card();
        stolenCard.setId(77);
        loserHand.setCards(new ArrayList<>(List.of(stolenCard)));

        when(playerService.findById(1)).thenReturn(attackerPlayer);
        when(playerService.findById(2)).thenReturn(defenderPlayer);
        when(handService.findPlayerHand(anyInt(), anyInt())).thenReturn(loserHand);
        when(matchService.getAllCards(anyInt(), anyInt())).thenReturn(new AllCardsStatusDTO());

        CardDTO result = fightService.playerStealFromPlayer(request, 1);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Player steal from Player from bag")
    void testPlayerStealFromPlayerBag() {
        Card targetCard = new Card();
        targetCard.setId(66);

        StealCardRequestDTO request = new StealCardRequestDTO();
        request.setWinnerId(1);
        request.setLoserId(2);
        request.setFromWhere("bag");
        request.setCard(targetCard);

        when(playerService.findById(1)).thenReturn(attackerPlayer);
        when(playerService.findById(2)).thenReturn(defenderPlayer);
        when(matchService.getAllCards(anyInt(), anyInt())).thenReturn(new AllCardsStatusDTO());

        fightService.playerStealFromPlayer(request, 1);

        verify(bagService, times(1)).removeCardFromPlayerBag(any(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Player steal from Player throws exception on invalid location")
    void testPlayerStealFromPlayerInvalidLocation() {
        StealCardRequestDTO request = new StealCardRequestDTO();
        request.setWinnerId(1);
        request.setLoserId(2);
        request.setFromWhere("wrong_place");

        when(playerService.findById(1)).thenReturn(attackerPlayer);
        when(playerService.findById(2)).thenReturn(defenderPlayer);

        assertThrows(IllegalArgumentException.class, () -> fightService.playerStealFromPlayer(request, 1));
    }

    @Test
    @DisplayName("Player loses against NPC discarding from hand")
    void testPlayerLosesAgainstNpcHand() {
        LoseAgainstNpcRequestDTO request = new LoseAgainstNpcRequestDTO(55, "hand");
        HandInGame hand = new HandInGame();
        Card targetCard = new Card();
        targetCard.setId(55);
        hand.setCards(List.of(targetCard));

        when(handService.findPlayerHand(anyInt(), anyInt())).thenReturn(hand);
        when(matchService.getAllCards(anyInt(), anyInt())).thenReturn(new AllCardsStatusDTO());

        fightService.playerLosesAgainstNpc(1, 1, request);

        verify(handService, times(1)).removeCardFromPlayerHand(any(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Player loses against NPC discarding from bag")
    void testPlayerLosesAgainstNpcBag() {
        LoseAgainstNpcRequestDTO request = new LoseAgainstNpcRequestDTO(44, "bag");
        BagInGame bag = new BagInGame();
        Card targetCard = new Card();
        targetCard.setId(44);
        bag.setCards(List.of(targetCard));

        when(bagService.findPlayerBag(anyInt(), anyInt())).thenReturn(bag);
        when(matchService.getAllCards(anyInt(), anyInt())).thenReturn(new AllCardsStatusDTO());

        fightService.playerLosesAgainstNpc(1, 1, request);

        verify(bagService, times(1)).removeCardFromPlayerBag(any(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Get possible fight in safe area returns empty")
    void testGetPossibleFightSafeArea() {
        List<PendingFight> result = fightService.getPossibleFight(1, 10, 37, false);
        
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Get possible fight adds pending fight when two players are in the same room")
    void testGetPossibleFightTwoPlayers() {
        when(matchRepo.findById(any())).thenReturn(Optional.of(testMatch));
        when(playerRepo.findByMatchAndRoom(anyInt(), anyInt())).thenReturn(List.of(attackerPlayer, defenderPlayer));
        when(npcRepository.findByMatchAndRoom(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(playerService.findByMatchIdAndUserId(anyInt(), anyInt())).thenReturn(attackerPlayer);

        List<PendingFight> result = fightService.getPossibleFight(1, 10, 100, false);

        assertNotNull(result);
        verify(matchRepo, times(1)).save(any(Match.class));
    }
}