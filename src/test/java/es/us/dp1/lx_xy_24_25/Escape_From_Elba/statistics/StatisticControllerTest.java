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
        
        when(statisticService.getBattlesPlayedByUser(userId)).thenReturn(18);
        when(statisticService.getMaxRoomsVisitedInMatch(userId)).thenReturn(12);
        when(statisticService.getPlayerType(userId)).thenReturn("Balanced");

        mockMvc.perform(get("/api/v1/statistics/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVictories").value(5))
                .andExpect(jsonPath("$.matchesPlayed").value(10))
                .andExpect(jsonPath("$.totalTimePlayed").value(120))
                .andExpect(jsonPath("$.totalActionPoints").value(50))
                .andExpect(jsonPath("$.battlesWon").value(15))
                .andExpect(jsonPath("$.roomsVisited").value(30))
                
                .andExpect(jsonPath("$.winRate").value(50.0)) 
                .andExpect(jsonPath("$.averageTimePerMatch").value(12.0)) 
                .andExpect(jsonPath("$.averageActionPointsPerMatch").value(5.0)) 
                .andExpect(jsonPath("$.averageRoomsVisitedPerMatch").value(3.0)) 
                .andExpect(jsonPath("$.battlesWonPerMatch").value(1.5)) 
                .andExpect(jsonPath("$.totalBattlesPlayed").value(18))
                .andExpect(jsonPath("$.maxRoomsVisitedInMatch").value(12))
                .andExpect(jsonPath("$.playerType").value("Balanced"));

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
        
        
        when(statisticService.getAverageMatchDuration()).thenReturn(45.5);
        when(statisticService.getLongestMatchDuration()).thenReturn(120);
        when(statisticService.getShortestMatchDuration()).thenReturn(10);

        mockMvc.perform(get("/api/v1/statistics/general")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePlayersPerMatch").value(4.5))
                .andExpect(jsonPath("$.totalMatchesPlayed").value(20))
                .andExpect(jsonPath("$.totalBattlesDisputed").value(100))
                .andExpect(jsonPath("$.averageRoomsVisitedPerMatch").value(15.0))
            
                .andExpect(jsonPath("$.averageMatchDuration").value(45.5))
                .andExpect(jsonPath("$.longestMatchDuration").value(120))
                .andExpect(jsonPath("$.shortestMatchDuration").value(10));

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
        when(statisticService.getAverageMatchDuration()).thenReturn(0.0);
        when(statisticService.getLongestMatchDuration()).thenReturn(0);
        when(statisticService.getShortestMatchDuration()).thenReturn(0);

        mockMvc.perform(get("/api/v1/statistics/general")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePlayersPerMatch").value(0.0))
                .andExpect(jsonPath("$.totalMatchesPlayed").value(0))
                .andExpect(jsonPath("$.totalBattlesDisputed").value(0))
                .andExpect(jsonPath("$.averageRoomsVisitedPerMatch").value(0.0))
                .andExpect(jsonPath("$.averageMatchDuration").value(0.0))
                .andExpect(jsonPath("$.longestMatchDuration").value(0))
                .andExpect(jsonPath("$.shortestMatchDuration").value(0));
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
        
        when(statisticService.getBattlesPlayedByUser(userId)).thenReturn(0);
        when(statisticService.getMaxRoomsVisitedInMatch(userId)).thenReturn(0);
        when(statisticService.getPlayerType(userId)).thenReturn("Explorer");

        mockMvc.perform(get("/api/v1/statistics/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVictories").value(0))
                .andExpect(jsonPath("$.matchesPlayed").value(1))
                .andExpect(jsonPath("$.battlesWon").value(0))
                .andExpect(jsonPath("$.totalActionPoints").value(0)) 
                .andExpect(jsonPath("$.roomsVisited").value(2))
                .andExpect(jsonPath("$.winRate").value(0.0))
                .andExpect(jsonPath("$.averageActionPointsPerMatch").value(0.0));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get user statistics with maximum values")
    void testGetUserStatisticsMaxValues() throws Exception {
        Integer userId = 1;
        
        when(statisticService.getTotalVictoriesByUser(userId)).thenReturn(100);
        when(statisticService.getMatchesPlayedByUser(userId)).thenReturn(150);
        when(statisticService.getTotalTimePlayedByUserFOR(userId)).thenReturn(5000);
        when(statisticService.getTotalAccionPointsByUser(userId)).thenReturn(999);
        when(statisticService.getBattlesWonByUser(userId)).thenReturn(200);
        when(statisticService.getTotalRoomsVisitedByUser(userId)).thenReturn(500);
        
        when(statisticService.getBattlesPlayedByUser(userId)).thenReturn(300);
        when(statisticService.getMaxRoomsVisitedInMatch(userId)).thenReturn(40);
        when(statisticService.getPlayerType(userId)).thenReturn("Aggressive");

        mockMvc.perform(get("/api/v1/statistics/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVictories").value(100))
                .andExpect(jsonPath("$.matchesPlayed").value(150))
                .andExpect(jsonPath("$.totalTimePlayed").value(5000))
                .andExpect(jsonPath("$.totalActionPoints").value(999))
                .andExpect(jsonPath("$.battlesWon").value(200))
                .andExpect(jsonPath("$.roomsVisited").value(500))
                .andExpect(jsonPath("$.playerType").value("Aggressive"));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get user statistics with all zero values")
    void testGetUserStatisticsAllZeros() throws Exception {
        Integer userId = 2;
        
        when(statisticService.getTotalVictoriesByUser(userId)).thenReturn(0);
        when(statisticService.getMatchesPlayedByUser(userId)).thenReturn(0); 
        when(statisticService.getTotalTimePlayedByUserFOR(userId)).thenReturn(0);
        when(statisticService.getTotalAccionPointsByUser(userId)).thenReturn(0);
        when(statisticService.getBattlesWonByUser(userId)).thenReturn(0);
        when(statisticService.getTotalRoomsVisitedByUser(userId)).thenReturn(0);
        
        when(statisticService.getBattlesPlayedByUser(userId)).thenReturn(0);
        when(statisticService.getMaxRoomsVisitedInMatch(userId)).thenReturn(0);
        when(statisticService.getPlayerType(userId)).thenReturn("Balanced");

        mockMvc.perform(get("/api/v1/statistics/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVictories").value(0))
                .andExpect(jsonPath("$.matchesPlayed").value(0))
                .andExpect(jsonPath("$.totalTimePlayed").value(0))
                .andExpect(jsonPath("$.battlesWon").value(0))
                .andExpect(jsonPath("$.winRate").value(0.0)) 
                .andExpect(jsonPath("$.averageTimePerMatch").value(0.0))
                .andExpect(jsonPath("$.averageActionPointsPerMatch").value(0.0))
                .andExpect(jsonPath("$.averageRoomsVisitedPerMatch").value(0.0))
                .andExpect(jsonPath("$.battlesWonPerMatch").value(0.0));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Get user statistics as admin")
    void testGetUserStatisticsAsAdmin() throws Exception {
        Integer userId = 5;
        
        when(statisticService.getTotalVictoriesByUser(userId)).thenReturn(10);
        when(statisticService.getMatchesPlayedByUser(userId)).thenReturn(15);
        when(statisticService.getTotalTimePlayedByUserFOR(userId)).thenReturn(200);
        when(statisticService.getTotalAccionPointsByUser(userId)).thenReturn(75);
        when(statisticService.getBattlesWonByUser(userId)).thenReturn(20);
        when(statisticService.getTotalRoomsVisitedByUser(userId)).thenReturn(40);
        
        when(statisticService.getBattlesPlayedByUser(userId)).thenReturn(25);
        when(statisticService.getMaxRoomsVisitedInMatch(userId)).thenReturn(15);
        when(statisticService.getPlayerType(userId)).thenReturn("Balanced");

        mockMvc.perform(get("/api/v1/statistics/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVictories").value(10))
                .andExpect(jsonPath("$.matchesPlayed").value(15));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Get general statistics as admin")
    void testGetGeneralStatisticsAsAdmin() throws Exception {
        when(statisticService.getAveragePlayersPerMatch()).thenReturn(3.8);
        when(statisticService.getTotalMatchesPlayed()).thenReturn(50);
        when(statisticService.getTotalBattlesDisputed()).thenReturn(150);
        when(statisticService.getAverageRoomsVisitedPerMatch()).thenReturn(12.5);
        when(statisticService.getAverageMatchDuration()).thenReturn(30.0);
        when(statisticService.getLongestMatchDuration()).thenReturn(60);
        when(statisticService.getShortestMatchDuration()).thenReturn(15);

        mockMvc.perform(get("/api/v1/statistics/general")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePlayersPerMatch").value(3.8))
                .andExpect(jsonPath("$.totalMatchesPlayed").value(50))
                .andExpect(jsonPath("$.totalBattlesDisputed").value(150));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get general statistics with high values")
    void testGetGeneralStatisticsHighValues() throws Exception {
        when(statisticService.getAveragePlayersPerMatch()).thenReturn(6.0);
        when(statisticService.getTotalMatchesPlayed()).thenReturn(1000);
        when(statisticService.getTotalBattlesDisputed()).thenReturn(5000);
        when(statisticService.getAverageRoomsVisitedPerMatch()).thenReturn(50.0);
        when(statisticService.getAverageMatchDuration()).thenReturn(500.0);
        when(statisticService.getLongestMatchDuration()).thenReturn(1000);
        when(statisticService.getShortestMatchDuration()).thenReturn(5);

        mockMvc.perform(get("/api/v1/statistics/general")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePlayersPerMatch").value(6.0))
                .andExpect(jsonPath("$.totalMatchesPlayed").value(1000))
                .andExpect(jsonPath("$.totalBattlesDisputed").value(5000))
                .andExpect(jsonPath("$.averageRoomsVisitedPerMatch").value(50.0));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get user statistics with null action points, rooms and totalTime")
    void testGetUserStatisticsNullActionPoints() throws Exception {
        Integer userId = 10;
        
        when(statisticService.getTotalVictoriesByUser(userId)).thenReturn(3);
        when(statisticService.getMatchesPlayedByUser(userId)).thenReturn(5);
        when(statisticService.getTotalTimePlayedByUserFOR(userId)).thenReturn(null);
        when(statisticService.getTotalAccionPointsByUser(userId)).thenReturn(null);
        when(statisticService.getBattlesWonByUser(userId)).thenReturn(7);
        when(statisticService.getTotalRoomsVisitedByUser(userId)).thenReturn(null);
        
        when(statisticService.getBattlesPlayedByUser(userId)).thenReturn(10);
        when(statisticService.getMaxRoomsVisitedInMatch(userId)).thenReturn(5);
        when(statisticService.getPlayerType(userId)).thenReturn("Balanced");

        mockMvc.perform(get("/api/v1/statistics/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVictories").value(3))
                .andExpect(jsonPath("$.matchesPlayed").value(5))
                .andExpect(jsonPath("$.totalTimePlayed").value(0))
                .andExpect(jsonPath("$.totalActionPoints").value(0))
                .andExpect(jsonPath("$.roomsVisited").value(0));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Verify all service methods are called for user statistics")
    void testGetUserStatisticsVerifyAllCalls() throws Exception {
        Integer userId = 1;
        
        when(statisticService.getTotalVictoriesByUser(userId)).thenReturn(5);
        when(statisticService.getMatchesPlayedByUser(userId)).thenReturn(10);
        when(statisticService.getTotalTimePlayedByUserFOR(userId)).thenReturn(120);
        when(statisticService.getTotalAccionPointsByUser(userId)).thenReturn(50);
        when(statisticService.getBattlesWonByUser(userId)).thenReturn(15);
        when(statisticService.getTotalRoomsVisitedByUser(userId)).thenReturn(30);
        when(statisticService.getBattlesPlayedByUser(userId)).thenReturn(18);
        when(statisticService.getMaxRoomsVisitedInMatch(userId)).thenReturn(12);
        when(statisticService.getPlayerType(userId)).thenReturn("Balanced");

        mockMvc.perform(get("/api/v1/statistics/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(statisticService, times(1)).getTotalVictoriesByUser(userId);
        verify(statisticService, times(1)).getMatchesPlayedByUser(userId);
        verify(statisticService, times(1)).getTotalTimePlayedByUserFOR(userId);
        verify(statisticService, times(1)).getTotalAccionPointsByUser(userId);
        verify(statisticService, times(1)).getBattlesWonByUser(userId);
        verify(statisticService, times(1)).getTotalRoomsVisitedByUser(userId);
        verify(statisticService, times(1)).getBattlesPlayedByUser(userId);
        verify(statisticService, times(1)).getMaxRoomsVisitedInMatch(userId);
        verify(statisticService, times(1)).getPlayerType(userId);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Verify all service methods are called for general statistics")
    void testGetGeneralStatisticsVerifyAllCalls() throws Exception {
        when(statisticService.getAveragePlayersPerMatch()).thenReturn(4.0);
        when(statisticService.getTotalMatchesPlayed()).thenReturn(10);
        when(statisticService.getTotalBattlesDisputed()).thenReturn(50);
        when(statisticService.getAverageRoomsVisitedPerMatch()).thenReturn(20.0);
        when(statisticService.getAverageMatchDuration()).thenReturn(30.0);
        when(statisticService.getLongestMatchDuration()).thenReturn(60);
        when(statisticService.getShortestMatchDuration()).thenReturn(15);

        mockMvc.perform(get("/api/v1/statistics/general")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(statisticService, times(1)).getAveragePlayersPerMatch();
        verify(statisticService, times(1)).getTotalMatchesPlayed();
        verify(statisticService, times(1)).getTotalBattlesDisputed();
        verify(statisticService, times(1)).getAverageRoomsVisitedPerMatch();
        verify(statisticService, times(1)).getAverageMatchDuration();
        verify(statisticService, times(1)).getLongestMatchDuration();
        verify(statisticService, times(1)).getShortestMatchDuration();
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get general statistics with decimal averages")
    void testGetGeneralStatisticsDecimalAverages() throws Exception {
        when(statisticService.getAveragePlayersPerMatch()).thenReturn(3.7);
        when(statisticService.getTotalMatchesPlayed()).thenReturn(15);
        when(statisticService.getTotalBattlesDisputed()).thenReturn(75);
        when(statisticService.getAverageRoomsVisitedPerMatch()).thenReturn(18.3);
        when(statisticService.getAverageMatchDuration()).thenReturn(42.7);
        when(statisticService.getLongestMatchDuration()).thenReturn(90);
        when(statisticService.getShortestMatchDuration()).thenReturn(11);

        mockMvc.perform(get("/api/v1/statistics/general")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePlayersPerMatch").value(3.7))
                .andExpect(jsonPath("$.averageRoomsVisitedPerMatch").value(18.3))
                .andExpect(jsonPath("$.averageMatchDuration").value(42.7));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get ranking returns OK with empty list")
    void testGetRankingEmpty() throws Exception {
        when(statisticService.getRanking()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/statistics/ranking")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}