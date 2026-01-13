package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DrawCardResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MoveToRoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.Authorities;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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


    private Match sampleMatch;
    private MatchDTO sampleMatchDTO;

    @BeforeEach
    public void setup() {
        sampleMatch = new Match();
        sampleMatch.setId(1);
        sampleMatch.setCode("ABC12345");
        sampleMatch.setMinPlayers(3);
        sampleMatch.setMaxPlayers(6);
        sampleMatch.setIsPrivate(true);
        sampleMatchDTO = new MatchDTO(sampleMatch);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testCreateLobby() throws Exception {
        LobbyDTO dto = new LobbyDTO();
        dto.setIsPrivate(true);
        dto.setName("Test Lobby");
        dto.setMaxPlayers(4);
        dto.setNumNpcs(3);

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/matches/lobbies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testJoinPublicLobby() throws Exception {
        when(lobbyService.joinLobby(1)).thenReturn(sampleMatch);

        mockMvc.perform(post("/api/v1/matches/lobbies/1/join"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(sampleMatch.getCode())));

        verify(lobbyService, times(1)).joinLobby(1);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testJoinPrivateLobby() throws Exception {
        when(lobbyService.joinPrivateLobby("ABC12345")).thenReturn(sampleMatch);

        mockMvc.perform(post("/api/v1/matches/lobbies/join/private")
                        .param("code", "ABC12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(sampleMatch.getCode())));

        verify(lobbyService, times(1)).joinPrivateLobby("ABC12345");
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testSubmitDice() throws Exception {
        when(matchService.submitDiceAndAssignOrder(eq(1), eq(10), eq(5))).thenReturn(sampleMatch);

        mockMvc.perform(post("/api/v1/matches/1/submit-dice")
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

        mockMvc.perform(post("/api/v1/matches/1/next-turn"))
                .andExpect(status().isNoContent()); // 204

        verify(matchService, times(1)).nextTurn(1);
}

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testEndMatch() throws Exception {
        Player winner = new Player();
        winner.setId(10);
        when(playerService.findById(10)).thenReturn(winner);
        when(matchService.endMatch(1, winner)).thenReturn(sampleMatch);

        mockMvc.perform(put("/api/v1/matches/1/end")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(sampleMatch.getCode())));

        verify(matchService, times(1)).endMatch(1, winner);
    }

    
    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetMatchById() throws Exception {
        when(matchService.getMatchById(1)).thenReturn(sampleMatch);

        mockMvc.perform(get("/api/v1/matches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(sampleMatch.getId()));

        verify(matchService, times(1)).getMatchById(1);
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
    public void testLeaveLobby() throws Exception {
        when(lobbyService.leaveLobby(1)).thenReturn(sampleMatch);

        mockMvc.perform(post("/api/v1/matches/lobbies/1/leave"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleMatch.getId()));

        verify(lobbyService, times(1)).leaveLobby(1);
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testStartMatch() throws Exception {
        sampleMatch.setStatus(MatchStatus.WAITING);
        when(matchService.startMatch(1)).thenReturn(sampleMatch);

        mockMvc.perform(post("/api/v1/matches/lobbies/1/start"))
                .andExpect(status().isOk());

        verify(matchService, times(1)).startMatch(1);
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testMoveToAdjacentRoom() throws Exception {
        MoveToRoomDTO dto = new MoveToRoomDTO();
        dto.setUserId(10);
        dto.setRoomId(5);

        Player player = new Player();
        User user = new User();
        user.setId(10);
        player.setUser(user);
        player.setActionPoints(3);
        sampleMatch.setPlayers(List.of(player));

        when(matchService.getMatchById(1)).thenReturn(sampleMatch);

        mockMvc.perform(put("/api/v1/matches/1/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players[0].user.id").value(10));

        verify(matchService, times(1)).movePlayerToAdyacentRoom(1, 10, 5);
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

        mockMvc.perform(post("/api/v1/matches/1/1/drawCardFromDeck"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.card.letter").value("A"))
            .andExpect(jsonPath("$.deck.notDiscardedCards[0].id").value(1))
            .andExpect(jsonPath("$.hand.cards[0].id").value(1));

        verify(matchService, times(1)).playerDrawsCardFromDeck(1, 1);
    }



    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testDrawRewardCard() throws Exception {
        int matchId = 1;
        int playerId = 1;
        Card card = new Card();
        card.setId(1);
        card.setFrontImage("front.png");
        card.setBackImage("back.png");
        card.setLetter("A");

    
        DeckInGame deck = new DeckInGame(List.of(card)); 


        HandInGame hand = new HandInGame(List.of(card)); 


        DrawCardResultDTO drawRewardResult = new DrawCardResultDTO(card, deck, hand);

    
        when(matchService.playerDrawsRewardCard(matchId, playerId))
                .thenReturn(drawRewardResult);

    
        mockMvc.perform(post("/api/v1/matches/{matchId}/{playerId}/drawRewardCard", matchId, playerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.card.letter").value("A"))
            .andExpect(jsonPath("$.deck.notDiscardedCards[0].id").value(1))
            .andExpect(jsonPath("$.hand.cards[0].id").value(1));


        verify(matchService, times(1)).playerDrawsRewardCard(matchId, playerId);
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

}