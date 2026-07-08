package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DrawCardResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MoveToRoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchHistorialDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.ActionPointsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.StrengthUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MatchControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatchService matchService;

    @MockBean
    private RoomService roomService;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private MatchWebsocketController matchWebsocketController;

    private Match sampleMatch;
    private MatchDTO sampleMatchDTO;

    @BeforeEach
    public void setup() {
        sampleMatch = new Match();
        sampleMatch.setId(1);
        sampleMatch.setName("Partida de Prueba");
        sampleMatch.setCode("ABC12345");
        sampleMatch.setMinPlayers(3);
        sampleMatch.setMaxPlayers(6);
        sampleMatch.setIsPrivate(true);
        sampleMatch.setStatus(MatchStatus.WAITING);
        sampleMatchDTO = new MatchDTO(sampleMatch);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testSubmitDice() throws Exception {
        when(matchService.submitDiceAndAssignOrder(eq(1), eq(10), eq(5))).thenReturn(sampleMatch);

        mockMvc.perform(post("/api/v1/matches/1/submit-dice").with(csrf())
                        .param("userId", "10")
                        .param("diceRoll", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(sampleMatch.getCode())));

        verify(matchService, times(1)).submitDiceAndAssignOrder(1, 10, 5);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testNextTurn() throws Exception {
        Match updatedMatch = new Match();
        updatedMatch.setId(1);
        updatedMatch.setCurrentTurnUserId(42);

        when(matchService.nextTurn(1)).thenReturn(updatedMatch);

        mockMvc.perform(post("/api/v1/matches/1/next-turn").with(csrf()))
                .andExpect(status().isNoContent());

        verify(matchService, times(1)).nextTurn(1);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testEndMatch() throws Exception {
        Player winner = new Player();
        winner.setId(10);
        when(playerService.findById(10)).thenReturn(winner);
        // Ajustado para que devuelva MatchDTO que es lo común en respuestas REST modernas
        when(matchService.endMatch(1, winner)).thenReturn(sampleMatchDTO);

        mockMvc.perform(put("/api/v1/matches/1/end").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetMatchById() throws Exception {
        when(matchService.getMatchDTOById(1)).thenReturn(sampleMatchDTO);

        mockMvc.perform(get("/api/v1/matches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(sampleMatch.getCode()));
    }
  
    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetPlayersByMatchId() throws Exception {
        Player p = new Player();
        p.setId(1);
        List<Player> players = List.of(p);
        when(playerService.getPlayersByMatchId(1)).thenReturn(players);

        mockMvc.perform(get("/api/v1/matches/1/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(playerService, times(1)).getPlayersByMatchId(1);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetWinnerByMatchId() throws Exception {
        Player winner = new Player();
        winner.setId(10);
        when(matchService.getMatchWinner(1)).thenReturn(winner);

        mockMvc.perform(get("/api/v1/matches/1/winner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        verify(matchService, times(1)).getMatchWinner(1);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testUserInMatch() throws Exception {
        when(matchService.userInMatch(10)).thenReturn(1);

        mockMvc.perform(get("/api/v1/matches/user/10/in"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(matchService, times(1)).userInMatch(10);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetAdjacencyMap() throws Exception {
        Room r1 = new Room();
        r1.setId(1);
        Room r2 = new Room();
        r2.setId(2);
        
        r1.setAdjacencyList(Arrays.asList(r2));
        r2.setAdjacencyList(new ArrayList<>());

        when(roomService.findAllRooms()).thenReturn(Arrays.asList(r1, r2));

        mockMvc.perform(get("/api/v1/matches/adjacencies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.1[0]").value(2))
            .andExpect(jsonPath("$.2").isArray())
            .andExpect(jsonPath("$.2").isEmpty());

        verify(roomService, times(1)).findAllRooms();
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetActionPoints() throws Exception {
        when(playerService.getPlayerActionPoints(1, 10)).thenReturn(5);

        mockMvc.perform(get("/api/v1/matches/1/10/actionPoints"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(playerService, times(1)).getPlayerActionPoints(1, 10);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testDrawCardFromDeck() throws Exception {
        Card card = new Card();
        card.setId(1);               
        card.setFrontImage("front.png");
        card.setBackImage("back.png");   
        card.setLetter("A"); 

        DeckInGame deck = new DeckInGame(List.of(card)); 
        HandInGame hand = new HandInGame(List.of(card));
        DrawCardResultDTO result = new DrawCardResultDTO(card, deck, hand);

        when(matchService.playerDrawsCardFromDeck(1, 1)).thenReturn(result);

        mockMvc.perform(post("/api/v1/matches/1/1/drawCardFromDeck").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.card.letter").value("A"));

        verify(matchService, times(1)).playerDrawsCardFromDeck(1, 1);
    }
  
    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetAllCards() throws Exception {
        AllCardsStatusDTO dto = new AllCardsStatusDTO();
        when(matchService.getAllCards(1, 10)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/matches/1/10/getAllCards"))
                .andExpect(status().isOk());

        verify(matchService, times(1)).getAllCards(1, 10);
    }

    // =========================================================================
    // NÚCLEO DE NUEVAS PRUEBAS EXPANDIDAS PARA MÁXIMA COBERTURA DEL CONTROLLER
    // =========================================================================

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetAllGames() throws Exception {
        when(matchService.getAllMatchs()).thenReturn(List.of(sampleMatch));

        mockMvc.perform(get("/api/v1/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetMatchById_Uncommented() throws Exception {
        when(matchService.getMatchDTOById(1)).thenReturn(sampleMatchDTO);

        mockMvc.perform(get("/api/v1/matches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(sampleMatch.getCode()));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetMatchesFilters() throws Exception {
        Page<MatchHistorialDTO> emptyPage = new PageImpl<>(new ArrayList<>());
        
        when(matchService.getFinishedAndInProgressMatches(any(), any())).thenReturn(emptyPage);
        when(matchService.getFinishedMatches(any(), any())).thenReturn(emptyPage);
        when(matchService.getInProgressMatches(any(), any())).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/matches/all-Matches").param("filter", "all")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/matches/all-Matches").param("filter", "finished")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/matches/all-Matches").param("filter", "inProgress")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testHistorialsByUser() throws Exception {
        Page<MatchHistorialDTO> emptyPage = new PageImpl<>(new ArrayList<>());
        
        when(matchService.getAllMatchesByUser(any(), any(), any())).thenReturn(emptyPage);
        when(matchService.getMatchesPlayedByUser(any(), any(), any())).thenReturn(emptyPage);
        when(matchService.getMatchesPlayedAndCreatedByUser(any(), any(), any())).thenReturn(emptyPage);
        when(matchService.getMatchesWonByUser(any(), any(), any())).thenReturn(emptyPage);
        when(matchService.getMatchesAbandonedByUser(any(), any(), any())).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/matches/all-Matches/10")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/matches/matches-played/10")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/matches/matches-created/10")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/matches/matches-won/10")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/matches/matches-abandoned/10")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testFinishMatch() throws Exception {
        Player winner = new Player();
        winner.setId(10);
        when(playerService.findById(10)).thenReturn(winner);
        when(matchService.endMatch(1, winner)).thenReturn(sampleMatchDTO);

        mockMvc.perform(put("/api/v1/matches/1/finish/10").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testEndMatch_NullWinner() throws Exception {
        when(matchService.endMatch(1, null)).thenReturn(sampleMatchDTO);

        mockMvc.perform(put("/api/v1/matches/1/end").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testLeaveMatch() throws Exception {
        when(matchService.leaveMatch(1, 10)).thenReturn(sampleMatchDTO);

        mockMvc.perform(put("/api/v1/matches/1/leaveMatch").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("10"))
                .andExpect(status().isOk());
    }

    //@Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testCreateGame() throws Exception {

        Match validMatch = new Match();
        validMatch.setId(1);
        validMatch.setName("Partida Nueva");
        validMatch.setCode("ABC12345");
        validMatch.setIsPrivate(true);
        validMatch.setMinPlayers(3);
        validMatch.setMaxPlayers(6);
        validMatch.setStatus(MatchStatus.WAITING);

        validMatch.setPlayers(new ArrayList<>());
        validMatch.setNpcs(new ArrayList<>());
        validMatch.setSpectators(new ArrayList<>());
        validMatch.setNumNpcs(3);

        when(matchService.save(any(Match.class))).thenReturn(validMatch);

        mockMvc.perform(post("/api/v1/matches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMatch)))
                .andExpect(status().isCreated());

        verify(matchService, times(1)).save(any(Match.class));
    }

    //@Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testUpdateGame() throws Exception {

        Match validMatch = new Match();
        validMatch.setId(1);
        validMatch.setName("Partida Modificada");
        validMatch.setCode("ABC12345");
        validMatch.setIsPrivate(true);
        validMatch.setMinPlayers(3);
        validMatch.setMaxPlayers(6);
        validMatch.setStatus(MatchStatus.WAITING);

        validMatch.setPlayers(new ArrayList<>());
        validMatch.setNpcs(new ArrayList<>());
        validMatch.setSpectators(new ArrayList<>());
        validMatch.setNumNpcs(3);

        when(matchService.getMatchById(1)).thenReturn(validMatch);
        when(matchService.save(any(Match.class))).thenReturn(validMatch);

        mockMvc.perform(put("/api/v1/matches/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validMatch)))
                .andExpect(status().isNoContent());

        verify(matchService, times(1)).getMatchById(1);
        verify(matchService, times(1)).save(any(Match.class));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testDeleteGame() throws Exception {
        when(matchService.getMatchDTOById(1)).thenReturn(sampleMatchDTO);

        mockMvc.perform(delete("/api/v1/matches/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testConfirmDiscardPhase() throws Exception {
        AllCardsStatusDTO dto = new AllCardsStatusDTO();
        dto.setPlayerId(1);
        dto.setHand(new HandInGameDTO());
        dto.setBag(new BagInGameDTO());
        dto.setDeck(new DeckInGameDTO());

        when(matchService.confirmDiscardPhase(eq(1), any(AllCardsStatusDTO.class))).thenReturn(2);

        mockMvc.perform(put("/api/v1/matches/1/confirmDiscardPhase")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testConsumeActionPoints() throws Exception {
        ActionPointsUpdateDTO update = new ActionPointsUpdateDTO();
        when(matchService.consumeAllActionPointForUser(1, 10)).thenReturn(update);
        when(matchService.consumeOneActionPoint(1, 10)).thenReturn(update);

        mockMvc.perform(post("/api/v1/matches/1/consume-all-action-points/10").with(csrf()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/matches/1/consume-action-point/10").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testNotifyStrength() throws Exception {
        StrengthUpdateDTO strengthUpdate = new StrengthUpdateDTO();

        mockMvc.perform(post("/api/v1/matches/1/notify-strength").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(strengthUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testSpectateGame() throws Exception {
        when(matchService.spectateGame(1)).thenReturn(sampleMatchDTO);

        mockMvc.perform(post("/api/v1/matches/1/spectate").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testStopSpectating() throws Exception {
        mockMvc.perform(delete("/api/v1/matches/1/StopSpectating").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Stopped spectating game 1."));
    }
}