package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.AlreadyVotedException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.MoreVotesThanPlayersException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.DTOs.VoteDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.DTOs.VotingDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class VotingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

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

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testSubmitVoteSuccess() throws Exception {
        VoteDTO dto = new VoteDTO();
        dto.setPlayerId(10);
        dto.setInFavor(VoteValue.YES);

        VotingDTO votingDTO = new VotingDTO();
        votingDTO.setMatchId(1);
        votingDTO.setWeaponProposed("Sword");

        ObjectMapper mapper = new ObjectMapper();

        reset(votingService);
        when(votingService.submitVote(eq(1), any(VoteDTO.class)))
                .thenReturn(votingDTO);

        mvc.perform(post(BASE_URL + "/vote/{matchId}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isOk());

        // verify submission called once
        // Mockito.verify(votingService, times(1)).submitVote(eq(1), any(VoteDTO.class));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testSubmitVoteAlreadyVotedThrows() throws Exception {
        VoteDTO dto = new VoteDTO();
        dto.setPlayerId(10);
        dto.setInFavor(VoteValue.YES);

        ObjectMapper mapper = new ObjectMapper();

        reset(votingService);
        when(votingService.submitVote(eq(1), any(VoteDTO.class)))
                .thenThrow(new AlreadyVotedException("Player already voted"));

        mvc.perform(post(BASE_URL + "/vote/{matchId}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testSubmitVoteMoreVotesThanPlayersThrows() throws Exception {
        VoteDTO dto = new VoteDTO();
        dto.setPlayerId(10);
        dto.setInFavor(VoteValue.YES);

        ObjectMapper mapper = new ObjectMapper();

        reset(votingService);
        when(votingService.submitVote(eq(1), any(VoteDTO.class)))
                .thenThrow(new MoreVotesThanPlayersException("Too many votes"));

        mvc.perform(post(BASE_URL + "/vote/{matchId}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void testSubmitVoteNoPendingVotingThrows() throws Exception {
        VoteDTO dto = new VoteDTO();
        dto.setPlayerId(10);
        dto.setInFavor(VoteValue.YES);

        ObjectMapper mapper = new ObjectMapper();

        reset(votingService);
        when(votingService.submitVote(eq(1), any(VoteDTO.class)))
                .thenThrow(new ResourceNotFoundException("No pending voting"));

        mvc.perform(post(BASE_URL + "/vote/{matchId}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }
}
