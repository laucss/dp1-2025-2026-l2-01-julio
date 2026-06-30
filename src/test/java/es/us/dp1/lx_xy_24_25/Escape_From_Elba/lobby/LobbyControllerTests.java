package es.us.dp1.lx_xy_24_25.Escape_From_Elba.lobby;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LobbyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private MatchService matchService;

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

        mockMvc.perform(post("/api/v1/lobbies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testJoinPublicLobby() throws Exception {
        when(lobbyService.joinLobby(1)).thenReturn(sampleMatch);

        mockMvc.perform(post("/api/v1/lobbies/1/join"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(sampleMatch.getCode())));

        verify(lobbyService, times(1)).joinLobby(1);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testJoinPrivateLobby() throws Exception {
        when(lobbyService.joinPrivateLobby("ABC12345")).thenReturn(sampleMatch);

        mockMvc.perform(post("/api/v1/lobbies/join/private")
                        .param("code", "ABC12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(sampleMatch.getCode())));

        verify(lobbyService, times(1)).joinPrivateLobby("ABC12345");
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testLeaveLobby() throws Exception {
        when(lobbyService.leaveLobby(1)).thenReturn(sampleMatch);

        mockMvc.perform(post("/api/v1/lobbies/1/leave"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleMatch.getId()));

        verify(lobbyService, times(1)).leaveLobby(1);
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testStartMatch() throws Exception {
        sampleMatch.setStatus(MatchStatus.WAITING);
        when(matchService.startMatch(1)).thenReturn(sampleMatch);

        mockMvc.perform(post("/api/v1/lobbies/1/start"))
                .andExpect(status().isOk());

        verify(matchService, times(1)).startMatch(1);
    }
    
}
