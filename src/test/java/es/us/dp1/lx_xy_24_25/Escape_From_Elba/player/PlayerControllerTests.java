package es.us.dp1.lx_xy_24_25.Escape_From_Elba.player;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTests {

    @MockBean
    private PlayerService playerService;

    @Autowired
    private MockMvc mvc;

    private static final String BASE_URL = "/api/v1/players";


    @Test
    @WithMockUser(authorities = {"PLAYER"})
    public void findAllPlayersEmptyTest() throws Exception {

        reset(playerService);
        when(playerService.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE_URL))
            .andExpect(status().isOk());

        verify(playerService, times(1)).findAll();
    }

    @Test
    @WithMockUser(authorities = {"PLAYER"})
    public void findAllPlayersReturnsPlayersTest() throws Exception {

        Player player = new Player();
        List<Player> players = List.of(player);

        reset(playerService);
        when(playerService.findAll()).thenReturn(players);

        mvc.perform(get(BASE_URL))
            .andExpect(status().isOk());

        verify(playerService, times(1)).findAll();
    }


    @Test
    @WithMockUser(authorities = {"PLAYER"})
    public void findAllByUserIdEmptyTest() throws Exception {

        reset(playerService);
        when(playerService.findByUserId(1)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE_URL + "/users/{userId}", 1))
            .andExpect(status().isOk());

        verify(playerService, times(1)).findByUserId(1);
    }

    @Test
    @WithMockUser(authorities = {"PLAYER"})
    public void findAllByUserIdReturnsPlayersTest() throws Exception {

        Player player = new Player();
        List<Player> players = List.of(player);

        reset(playerService);
        when(playerService.findByUserId(1)).thenReturn(players);

        mvc.perform(get(BASE_URL + "/users/{userId}", 1))
            .andExpect(status().isOk());

        verify(playerService, times(1)).findByUserId(1);
    }



    @Test
    @WithMockUser(authorities = {"PLAYER"})
    public void getPlayersByMatchIdEmptyTest() throws Exception {

        reset(playerService);
        when(playerService.getPlayersByMatchId(1))
            .thenReturn(Collections.emptyList());

        mvc.perform(get(BASE_URL + "/matches/{matchId}", 1))
            .andExpect(status().isOk());

        verify(playerService, times(1))
            .getPlayersByMatchId(1);
    }

    @Test
    @WithMockUser(authorities = {"PLAYER"})
    public void getPlayersByMatchIdReturnsPlayersTest() throws Exception {

        Player player = new Player();
        List<Player> players = List.of(player);

        reset(playerService);
        when(playerService.getPlayersByMatchId(1))
            .thenReturn(players);

        mvc.perform(get(BASE_URL + "/matches/{matchId}", 1))
            .andExpect(status().isOk());

        verify(playerService, times(1))
            .getPlayersByMatchId(1);
    }
}
