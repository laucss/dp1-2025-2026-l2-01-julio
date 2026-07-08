package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.EscapeAttemptResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ActionsController Tests")
class ActionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActionsService actionsService;

    private MatchDTO sampleMatchDto;
    private EscapeAttemptResultDTO sampleEscapeDto;

    @BeforeEach
    void setUp() {
        // Inicializamos instancias básicas de los DTOs para evitar NullPointerException en la serialización de Jackson
        sampleMatchDto = new MatchDTO();
        // Si MatchDTO requiere un constructor con una entidad Match, puedes mockearlo o pasarle propiedades básicas según corresponda.
        
        sampleEscapeDto = new EscapeAttemptResultDTO();
        sampleEscapeDto.setSuccess(true);
        sampleEscapeDto.setWinnerUserId(10);
        sampleEscapeDto.setDiscardRequired(false);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Move player to adjacent room returns OK")
    void testMoveToAdjacentRoomSuccess() throws Exception {
        Integer matchId = 1;

        when(actionsService.movePlayerToAdyacentRoom(eq(matchId), eq(10), eq(5)))
                .thenReturn(sampleMatchDto);

        String jsonRequestBody = "{\"userId\": 10, \"roomId\": 5}";

        mockMvc.perform(put("/api/v1/actions/{matchId}/move", matchId)
                .with(csrf()) // Evita bloqueos de seguridad en peticiones PUT
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk());

        verify(actionsService, times(1)).movePlayerToAdyacentRoom(eq(matchId), eq(10), eq(5));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Move NPC to room returns OK")
    void testMoveNpcToRoomSuccess() throws Exception {
        Integer matchId = 1;

        when(actionsService.moveNpcToRoom(eq(matchId), eq(2), eq(20), eq(10)))
                .thenReturn(sampleMatchDto);

        String jsonRequestBody = "{\"npcId\": 2, \"roomId\": 20, \"userId\": 10}";

        mockMvc.perform(put("/api/v1/actions/{matchId}/moveNpc", matchId)
                .with(csrf()) // Evita bloqueos de seguridad en peticiones PUT
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk());

        verify(actionsService, times(1)).moveNpcToRoom(eq(matchId), eq(2), eq(20), eq(10));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Move by forming room name with letters returns OK")
    void testMoveByLettersSuccess() throws Exception {
        Integer matchId = 1;

        when(actionsService.movePlayerByFormingRoomName(eq(matchId), eq(15), eq(30)))
                .thenReturn(sampleMatchDto);

        String jsonRequestBody = "{\"userId\": 15, \"roomId\": 30}";

        mockMvc.perform(put("/api/v1/actions/{matchId}/moveByLetters", matchId)
                .with(csrf()) // Evita bloqueos de seguridad en peticiones PUT
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk());

        verify(actionsService, times(1)).movePlayerByFormingRoomName(eq(matchId), eq(15), eq(30));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Attempt escape with request params returns OK")
    void testAttemptEscapeSuccess() throws Exception {
        Integer matchId = 1;
        Integer userId = 10;
        Integer rollDice = 6;

        when(actionsService.escapeAttempt(eq(matchId), eq(userId), eq(rollDice)))
                .thenReturn(sampleEscapeDto);

        mockMvc.perform(post("/api/v1/actions/{matchId}/escape-attempt", matchId)
                .with(csrf()) // Evita bloqueos de seguridad en peticiones POST
                .param("userId", userId.toString())
                .param("rollDice", rollDice.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.winnerUserId").value(10));

        verify(actionsService, times(1)).escapeAttempt(eq(matchId), eq(userId), eq(rollDice));
    }
}