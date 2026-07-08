package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.*;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchWebsocketController;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("FightController Tests")
class FightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FightService fightService;

    @MockBean
    private MatchWebsocketController matchWebsocketController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Resolve a fight returns OK with data")
    void testResolveFight() throws Exception {
        Integer matchId = 1;
        FightResultRequestDTO request = new FightResultRequestDTO();
        FightResolvedDTO response = new FightResolvedDTO();

        when(fightService.processFightResolution(any(FightResultRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/fights/{matchId}/fight/resolve", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(fightService, times(1)).processFightResolution(any(FightResultRequestDTO.class));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Steal card from player returns OK with card details")
    void testStealCardFromPlayer() throws Exception {
        Integer matchId = 1;
        StealCardRequestDTO request = new StealCardRequestDTO();
        CardDTO mockCard = new CardDTO();

        when(fightService.playerStealFromPlayer(any(StealCardRequestDTO.class), eq(matchId))).thenReturn(mockCard);

        mockMvc.perform(post("/api/v1/fights/{matchId}/steal-card-from-player", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(fightService, times(1)).playerStealFromPlayer(any(StealCardRequestDTO.class), eq(matchId));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Player loses against NPC returns No Content")
    void testPlayerLosesAgainstNpc() throws Exception {
        Integer matchId = 1;
        Integer playerId = 2;
        LoseAgainstNpcRequestDTO request = new LoseAgainstNpcRequestDTO(13, "hand");


        doNothing().when(fightService).playerLosesAgainstNpc(eq(matchId), eq(playerId), any(LoseAgainstNpcRequestDTO.class));

        mockMvc.perform(post("/api/v1/fights/{matchId}/{playerId}/lose-against-npc", matchId, playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(fightService, times(1)).playerLosesAgainstNpc(eq(matchId), eq(playerId), any(LoseAgainstNpcRequestDTO.class));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Notify fight broadcast returns OK")
    void testNotifyFight() throws Exception {
        Integer matchId = 1;
        FightUpdateDTO fightUpdate = new FightUpdateDTO();

        doNothing().when(matchWebsocketController).notifyFightUpdate(eq(matchId), any(FightUpdateDTO.class));

        mockMvc.perform(post("/api/v1/fights/{matchId}/notify-fight", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fightUpdate)))
                .andExpect(status().isOk());

        verify(matchWebsocketController, times(1)).notifyFightUpdate(eq(matchId), any(FightUpdateDTO.class));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Notify fight dice update returns OK")
    void testNotifyFightDice() throws Exception {
        Integer matchId = 1;
        FightDiceUpdateDTO diceUpdate = new FightDiceUpdateDTO();

        doNothing().when(matchWebsocketController).notifyFightDiceUpdate(eq(matchId), any(FightDiceUpdateDTO.class));

        mockMvc.perform(post("/api/v1/fights/{matchId}/notify-fight-dice", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diceUpdate)))
                .andExpect(status().isOk());

        verify(matchWebsocketController, times(1)).notifyFightDiceUpdate(eq(matchId), any(FightDiceUpdateDTO.class));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Notify dice totals updates returns OK")
    void testNotifyDiceTotals() throws Exception {
        Integer matchId = 1;
        DiceTotalsUpdateDTO totalsUpdate = new DiceTotalsUpdateDTO();

        doNothing().when(matchWebsocketController).notifyDiceTotalsUpdate(eq(matchId), any(DiceTotalsUpdateDTO.class));

        mockMvc.perform(post("/api/v1/fights/{matchId}/notify-dice-totals", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(totalsUpdate)))
                .andExpect(status().isOk());

        verify(matchWebsocketController, times(1)).notifyDiceTotalsUpdate(eq(matchId), any(DiceTotalsUpdateDTO.class));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Notify ready state update returns OK")
    void testNotifyReadyState() throws Exception {
        Integer matchId = 1;
        ReadyStateUpdateDTO readyStateUpdate = new ReadyStateUpdateDTO();

        doNothing().when(matchWebsocketController).notifyReadyStateUpdate(eq(matchId), any(ReadyStateUpdateDTO.class));

        mockMvc.perform(post("/api/v1/fights/{matchId}/notify-ready-state", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(readyStateUpdate)))
                .andExpect(status().isOk());

        verify(matchWebsocketController, times(1)).notifyReadyStateUpdate(eq(matchId), any(ReadyStateUpdateDTO.class));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Notify weapons update returns OK")
    void testNotifyFightWeapons() throws Exception {
        Integer matchId = 1;
        WeaponsUpdateDTO weaponsUpdate = new WeaponsUpdateDTO();

        doNothing().when(matchWebsocketController).notifyWeaponsUpdate(eq(matchId), any(WeaponsUpdateDTO.class));

        mockMvc.perform(post("/api/v1/fights/{matchId}/notify-fight-weapons", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weaponsUpdate)))
                .andExpect(status().isOk());

        verify(matchWebsocketController, times(1)).notifyWeaponsUpdate(eq(matchId), any(WeaponsUpdateDTO.class));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Check pending and chain fights returns OK")
    void testCheckAndTriggerChainFights() throws Exception {
        Integer matchId = 1;

        doNothing().when(fightService).checkPendingFights(matchId);

        mockMvc.perform(post("/api/v1/fights/{matchId}/check-pending-fights", matchId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(fightService, times(1)).checkPendingFights(matchId);
    }
}
