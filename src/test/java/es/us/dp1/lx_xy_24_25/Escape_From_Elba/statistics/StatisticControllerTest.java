package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

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

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("StatisticController Tests")
class StatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticService statisticService;

    @BeforeEach
    void setUp() {
        // Setup test data
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get user statistics returns OK with data")
    void testGetUserStatistics() throws Exception {
        Integer userId = 1;
        
        when(statisticService.getTotalVictoriesByUser(userId)).thenReturn(5);
        when(statisticService.getMatchesPlayedByUser(userId)).thenReturn(10);
        when(statisticService.getTotalTimePlayedByUserFOR(userId)).thenReturn(120);
        when(statisticService.getTotalAccionPointsByUser(userId)).thenReturn(50);
        when(statisticService.getBattlesWonByUser(userId)).thenReturn(15);
        when(statisticService.getTotalRoomsVisitedByUser(userId)).thenReturn(30);

        mockMvc.perform(get("/api/v1/statistics/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVictories").value(5))
                .andExpect(jsonPath("$.matchesPlayed").value(10))
                .andExpect(jsonPath("$.totalTimePlayed").value(120))
                .andExpect(jsonPath("$.totalActionPoints").value(50))
                .andExpect(jsonPath("$.battlesWon").value(15))
                .andExpect(jsonPath("$.roomsVisited").value(30));

        verify(statisticService, times(1)).getTotalVictoriesByUser(userId);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get general statistics returns OK with data")
    void testGetGeneralStatistics() throws Exception {
        when(statisticService.getAveragePlayersPerMatch()).thenReturn(4.5);
        when(statisticService.getTotalMatchesPlayed()).thenReturn(20);
        when(statisticService.getTotalBattlesDisputed()).thenReturn(100);
        when(statisticService.getAverageRoomsVisitedPerMatch()).thenReturn(15.0);

        mockMvc.perform(get("/api/v1/statistics/general")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePlayersPerMatch").value(4.5))
                .andExpect(jsonPath("$.totalMatchesPlayed").value(20))
                .andExpect(jsonPath("$.totalBattlesDisputed").value(100))
                .andExpect(jsonPath("$.averageRoomsVisitedPerMatch").value(15.0));

        verify(statisticService, times(1)).getAveragePlayersPerMatch();
        verify(statisticService, times(1)).getTotalMatchesPlayed();
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get general statistics with zero values")
    void testGetGeneralStatisticsZeroValues() throws Exception {
        when(statisticService.getAveragePlayersPerMatch()).thenReturn(0.0);
        when(statisticService.getTotalMatchesPlayed()).thenReturn(0);
        when(statisticService.getTotalBattlesDisputed()).thenReturn(0);
        when(statisticService.getAverageRoomsVisitedPerMatch()).thenReturn(0.0);

        mockMvc.perform(get("/api/v1/statistics/general")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePlayersPerMatch").value(0.0))
                .andExpect(jsonPath("$.totalMatchesPlayed").value(0));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get user statistics with minimal values")
    void testGetUserStatisticsMinimalValues() throws Exception {
        Integer userId = 99;
        
        when(statisticService.getTotalVictoriesByUser(userId)).thenReturn(0);
        when(statisticService.getMatchesPlayedByUser(userId)).thenReturn(1);
        when(statisticService.getTotalTimePlayedByUserFOR(userId)).thenReturn(5);
        when(statisticService.getTotalAccionPointsByUser(userId)).thenReturn(null);
        when(statisticService.getBattlesWonByUser(userId)).thenReturn(0);
        when(statisticService.getTotalRoomsVisitedByUser(userId)).thenReturn(2);

        mockMvc.perform(get("/api/v1/statistics/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVictories").value(0))
                .andExpect(jsonPath("$.matchesPlayed").value(1))
                .andExpect(jsonPath("$.battlesWon").value(0));
    }
}
