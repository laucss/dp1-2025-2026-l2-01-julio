package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
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

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.NoActionPointsException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.FightService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.ActionPointsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.EscapeAttemptResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.NpcRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ActionsService Tests")
class ActionsServiceTest {

    @Mock 
    private MatchService matchService;
    @Mock 
    private FightService fightService;
    @Mock 
    private BagService bagService;
    @Mock 
    private PlayerService playerService;
    @Mock 
    private MatchRepository matchRepo;
    @Mock 
    private PlayerRepository playerRepo;
    @Mock 
    private RoomRepository roomRepository;
    @Mock 
    private NpcRepository npcRepository;
    @Mock 
    private Checkers checkers;
    @Mock 
    private MatchWebsocketController matchWebsocketController;
    @Mock 
    private RoomService roomService;

    @InjectMocks
    private ActionsService actionsService;

    private Match testMatch;
    private Player testPlayer;
    private User testUser;
    private Room currentRoom;
    private Room targetRoom;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testMatch = new Match();
        testMatch.setId(1);
        testMatch.setCurrentTurnPhase(TurnPhase.ACTIONS);

        currentRoom = new Room();
        currentRoom.setId(1);
        currentRoom.setName("Kitchen");

        targetRoom = new Room();
        targetRoom.setId(2);
        targetRoom.setName("Armory");

        testPlayer = new Player();
        testPlayer.setId(1);
        testPlayer.setUser(testUser);
        testPlayer.setMatch(testMatch);
        testPlayer.setRoom(currentRoom);
        testPlayer.setActionPoints(3);
        testPlayer.setStrength(5);
        testPlayer.setRoomsVisited(0);
    }

    @Test
    @DisplayName("Mover jugador a una sala adyacente con éxito")
    void testMovePlayerToAdyacentRoomSuccess() {
        when(matchService.getMatchById(any())).thenReturn(testMatch);
        when(playerService.findByMatchIdAndUserId(1, 1)).thenReturn(testPlayer);
        when(roomRepository.findById(any())).thenReturn(Optional.of(targetRoom));
        when(fightService.getPossibleFight(anyInt(), anyInt(), anyInt(), anyBoolean())).thenReturn(new ArrayList<>());

        MatchDTO result = actionsService.movePlayerToAdyacentRoom(1, 1, 2);

        assertNotNull(result);
        assertEquals(2, testPlayer.getActionPoints());
        assertEquals(targetRoom, testPlayer.getRoom());
        assertEquals(1, testPlayer.getRoomsVisited());
        
        verify(playerRepo, times(2)).save(testPlayer);
        verify(matchWebsocketController, times(1)).notifyActionPointsUpdate(eq(1), any(ActionPointsUpdateDTO.class));
    }

    @Test
    @DisplayName("Error al mover jugador si la sala de destino es la misma que la actual")
    void testMovePlayerToSameRoomThrowsException() {
        when(matchService.getMatchById(1)).thenReturn(testMatch);
        when(playerService.findByMatchIdAndUserId(1, 1)).thenReturn(testPlayer);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            actionsService.movePlayerToAdyacentRoom(1, 1, 1);
        });

        assertEquals("The destination room is the same as the current room of the player", exception.getMessage());
    }

    @Test
    @DisplayName("Mover un NPC a otra sala con éxito")
    void testMoveNpcToRoomSuccess() {
        Npc npc = new Npc();
        npc.setId(10);
        npc.setRoom(currentRoom);

        when(matchService.getMatchById(1)).thenReturn(testMatch);
        when(npcRepository.findById(10)).thenReturn(Optional.of(npc));
        when(roomService.findById(2)).thenReturn(targetRoom);
        when(playerService.findByMatchIdAndUserId(1, 1)).thenReturn(testPlayer);
        when(fightService.getPossibleFight(anyInt(), anyInt(), anyInt(), anyBoolean())).thenReturn(new ArrayList<>());

        MatchDTO result = actionsService.moveNpcToRoom(1, 10, 2, 1);

        assertNotNull(result);
        assertEquals(2, testPlayer.getActionPoints());
        assertEquals(targetRoom, npc.getRoom());
        
        verify(playerRepo, times(1)).save(testPlayer);
        verify(npcRepository, times(1)).save(npc);
    }

    @Test
    @DisplayName("Mover jugador formando el nombre de la sala con letras de la bolsa")
    void testMovePlayerByFormingRoomNameSuccess() {
        targetRoom.setName("ARMORY");
        
        BagInGame bag = new BagInGame();
        List<Card> cards = new ArrayList<>();
        // Añadimos suficientes letras para formar la palabra "armory"
        String[] letters = {"A", "R", "M", "O", "R", "Y"};
        for (String l : letters) {
            Card c = new Card();
            c.setLetter(l);
            cards.add(c);
        }
        bag.setCards(cards);

        when(matchService.getMatchById(1)).thenReturn(testMatch);
        when(playerService.findByMatchIdAndUserId(1, 1)).thenReturn(testPlayer);
        when(roomService.findById(2)).thenReturn(targetRoom);
        when(bagService.findPlayerBag(1, testPlayer.getId())).thenReturn(bag);
        when(fightService.getPossibleFight(anyInt(), anyInt(), anyInt(), anyBoolean())).thenReturn(new ArrayList<>());

        MatchDTO result = actionsService.movePlayerByFormingRoomName(1, 1, 2);

        assertNotNull(result);
        assertEquals(targetRoom, testPlayer.getRoom());
        assertEquals(2, testPlayer.getActionPoints());
    }

    @Test
    @DisplayName("Error al mover jugador por nombre de sala si no tiene las letras requeridas")
    void testMovePlayerByFormingRoomNameInsuficientLetters() {
        targetRoom.setName("ARMORY");
        
        BagInGame bag = new BagInGame();
        List<Card> cards = new ArrayList<>();
        Card c = new Card(); c.setLetter("Z"); cards.add(c); // Letra inútil
        bag.setCards(cards);

        when(matchService.getMatchById(1)).thenReturn(testMatch);
        when(playerService.findByMatchIdAndUserId(1, 1)).thenReturn(testPlayer);
        when(roomService.findById(2)).thenReturn(targetRoom);
        when(bagService.findPlayerBag(1, testPlayer.getId())).thenReturn(bag);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            actionsService.movePlayerByFormingRoomName(1, 1, 2);
        });

        assertTrue(exception.getMessage().contains("You cannot form any word from the destination room name"));
    }

    @Test
    @DisplayName("Intento de escape exitoso (Dado < Fuerza)")
    void testEscapeAttemptSuccess() {
        when(matchRepo.findById(any())).thenReturn(Optional.of(testMatch));
        when(playerService.findByMatchIdAndUserId(1, 1)).thenReturn(testPlayer);
        when(roomService.getAllTowers()).thenReturn(List.of(currentRoom)); // El jugador está en una torre
        
        BagInGame bag = new BagInGame();
        bag.setCards(new ArrayList<>()); 
        when(bagService.findPlayerBag(1, testPlayer.getId())).thenReturn(bag);
        when(bagService.wordFromCards(anyList())).thenReturn("EMPEROR"); // Palabra comodín válida
        when(roomService.getWordOfEscapeFromTower(currentRoom.getId())).thenReturn("TOWERWORD");

        // RolldiceResult (3) < Fuerza (5) -> Éxito
        EscapeAttemptResultDTO result = actionsService.escapeAttempt(1, 1, 3);

        assertTrue(result.isSuccess());
        assertEquals(testUser.getId(), result.getWinnerUserId());
        assertFalse(result.isDiscardRequired());
        
        verify(matchService, times(1)).endMatch(1, testPlayer);
    }

    @Test
    @DisplayName("Intento de escape fallido (Dado >= Fuerza)")
    void testEscapeAttemptFailure() {
        when(matchRepo.findById(any())).thenReturn(Optional.of(testMatch));
        when(playerService.findByMatchIdAndUserId(1, 1)).thenReturn(testPlayer);
        when(roomService.getAllTowers()).thenReturn(List.of(currentRoom));
        
        BagInGame bag = new BagInGame();
        bag.setCards(new ArrayList<>());
        when(bagService.findPlayerBag(1, testPlayer.getId())).thenReturn(bag);
        when(bagService.wordFromCards(anyList())).thenReturn("EMPEROR");
        when(roomService.getWordOfEscapeFromTower(currentRoom.getId())).thenReturn("TOWERWORD");
        when(matchService.getAvailableRoomsForPlayer(1)).thenReturn(List.of(targetRoom));

        // RolldiceResult (6) >= Fuerza (5) -> Fallo
        EscapeAttemptResultDTO result = actionsService.escapeAttempt(1, 1, 6);

        assertFalse(result.isSuccess());
        assertTrue(result.isDiscardRequired());
        
        verify(matchService, times(1)).consumeAllActionPointForUser(1, 1);
        verify(matchService, times(1)).moveLoserPlayer(eq(1), eq(1), anyInt());
    }

@Test
    @DisplayName("Error en intento de escape si el jugador no tiene puntos de acción")
    void testEscapeAttemptNoActionPoints() {
        testPlayer.setActionPoints(0);
        // Cambiado anyInt() por any() para evitar conflictos de tipo int / Integer
        when(matchRepo.findById(any())).thenReturn(Optional.of(testMatch));
        when(playerService.findByMatchIdAndUserId(anyInt(), anyInt())).thenReturn(testPlayer);

        assertThrows(NoActionPointsException.class, () -> {
            actionsService.escapeAttempt(1, 1, 3);
        });
    }

    @Test
    @DisplayName("Error en intento de escape si el jugador no está en una torre")
    void testEscapeAttemptNotInTower() {
        // Cambiado anyInt() por any() para evitar conflictos de tipo int / Integer
        when(matchRepo.findById(any())).thenReturn(Optional.of(testMatch));
        when(playerService.findByMatchIdAndUserId(anyInt(), anyInt())).thenReturn(testPlayer);
        when(roomService.getAllTowers()).thenReturn(List.of(targetRoom)); // La sala actual no está en la lista

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            actionsService.escapeAttempt(1, 1, 3);
        });

        assertEquals("Player is not in a tower room", exception.getMessage());
    }
}