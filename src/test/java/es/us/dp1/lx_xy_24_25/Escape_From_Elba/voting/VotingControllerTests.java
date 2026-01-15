package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
public class VotingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VotingService votingService;

    private static final String BASE_URL = "/api/v1/voting";

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetAllVotingSuccess() throws Exception {
        VotingDTO dto = new VotingDTO();
        dto.setMatchId(1);
        dto.setWeaponProposed("Knife");

        when(votingService.getVotingsByMatchId(1)).thenReturn(List.of(dto));

        mockMvc.perform(get(BASE_URL + "/{matchId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].matchId").value(1))
                .andExpect(jsonPath("$[0].weaponProposed").value("Knife"));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testGetAllVotingNotFound() throws Exception {
        when(votingService.getVotingsByMatchId(2)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(BASE_URL + "/{matchId}", 2))
                .andExpect(status().isNotFound());
    }

}
