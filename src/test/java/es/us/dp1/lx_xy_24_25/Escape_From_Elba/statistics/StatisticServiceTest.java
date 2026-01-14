package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatisticService Tests")
class StatisticServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private StatisticService statisticService;

    private User testUser;
    private Player testPlayer;
    private Match testMatch;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testPlayer = new Player();
        testPlayer.setId(1);
        testPlayer.setUser(testUser);
        testPlayer.setActionPoints(10);
        testPlayer.setBattlesWon(5);
        testPlayer.setBattlesPlayed(8);
        testPlayer.setRoomsVisited(15);

        testMatch = new Match();
        testMatch.setId(1);
        testMatch.setName("Test Match");
        testMatch.setMaxPlayers(4);
        testMatch.setStartTime(LocalDateTime.now().minusHours(2));
        testMatch.setEndTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("Get total action points by user")
    void testGetTotalAccionPointsByUser() {
        when(playerRepository.getTotalAccionPointsByUser(1)).thenReturn(10);
        
        Integer result = statisticService.getTotalAccionPointsByUser(1);
        
        assertEquals(10, result);
        verify(playerRepository, times(1)).getTotalAccionPointsByUser(1);
    }

    @Test
    @DisplayName("Get total victories by user")
    void testGetTotalVictoriesByUser() {
        List<Player> players = new ArrayList<>();
        testMatch.setWinner(testPlayer);
        testPlayer.setMatch(testMatch);
        players.add(testPlayer);
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getTotalVictoriesByUser(1);
        
        assertEquals(1, result);
        verify(playerRepository, times(1)).findByUserId(1);
    }

    @Test
    @DisplayName("Get total victories by user with no victories")
    void testGetTotalVictoriesByUserNoVictories() {
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getTotalVictoriesByUser(1);
        
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Get matches played by user")
    void testGetMatchesPlayedByUser() {
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);
        players.add(testPlayer);
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getMatchesPlayedByUser(1);
        
        assertEquals(2, result);
    }

    @Test
    @DisplayName("Get total time played by user in minutes")
    void testGetTotalTimePlayedByUserFOR() {
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getTotalTimePlayedByUserFOR(1);
        
        assertNotNull(result);
        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("Get average players per match")
    void testGetAveragePlayersPerMatch() {
        List<Match> matches = new ArrayList<>();
        matches.add(testMatch);
        matches.add(testMatch);
        
        when(matchService.getAllMatchs()).thenReturn(matches);
        
        Double result = statisticService.getAveragePlayersPerMatch();
        
        assertEquals(4.0, result);
    }

    @Test
    @DisplayName("Get average players per match with empty matches")
    void testGetAveragePlayersPerMatchEmpty() {
        when(matchService.getAllMatchs()).thenReturn(new ArrayList<>());
        
        Double result = statisticService.getAveragePlayersPerMatch();
        
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Get total matches played")
    void testGetTotalMatchesPlayed() {
        List<Match> matches = new ArrayList<>();
        matches.add(testMatch);
        matches.add(testMatch);
        
        when(matchService.getAllMatchs()).thenReturn(matches);
        
        Integer result = statisticService.getTotalMatchesPlayed();
        
        assertEquals(2, result);
    }

    @Test
    @DisplayName("Get battles won by user")
    void testGetBattlesWonByUser() {
        when(playerRepository.getBattlesWonByUser(1)).thenReturn(5);
        
        Integer result = statisticService.getBattlesWonByUser(1);
        
        assertEquals(5, result);
    }

    @Test
    @DisplayName("Get total battles disputed globally")
    void testGetTotalBattlesDisputed() {
        when(playerRepository.getTotalBattlesDisputed()).thenReturn(20);
        
        Integer result = statisticService.getTotalBattlesDisputed();
        
        assertEquals(20, result);
    }

    @Test
    @DisplayName("Get total rooms visited by user")
    void testGetTotalRoomsVisitedByUser() {
        when(playerRepository.getTotalRoomsVisitedByUser(1)).thenReturn(15);
        
        Integer result = statisticService.getTotalRoomsVisitedByUser(1);
        
        assertEquals(15, result);
    }

    @Test
    @DisplayName("Get total rooms visited by user when null")
    void testGetTotalRoomsVisitedByUserNull() {
        when(playerRepository.getTotalRoomsVisitedByUser(1)).thenReturn(null);
        
        Integer result = statisticService.getTotalRoomsVisitedByUser(1);
        
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Get average rooms visited per match")
    void testGetAverageRoomsVisitedPerMatch() {
        List<Match> matches = new ArrayList<>();
        
        Player player1 = new Player();
        player1.setRoomsVisited(10);
        Player player2 = new Player();
        player2.setRoomsVisited(12);
        
        testMatch.setPlayers(List.of(player1, player2));
        matches.add(testMatch);
        
        when(matchService.getAllMatchs()).thenReturn(matches);
        
        Double result = statisticService.getAverageRoomsVisitedPerMatch();
        
        assertEquals(22.0, result);
    }

    @Test
    @DisplayName("Get average rooms visited per match with no players")
    void testGetAverageRoomsVisitedPerMatchNoPlayers() {
        List<Match> matches = new ArrayList<>();
        testMatch.setPlayers(new ArrayList<>());
        matches.add(testMatch);
        
        when(matchService.getAllMatchs()).thenReturn(matches);
        
        Double result = statisticService.getAverageRoomsVisitedPerMatch();
        
        assertEquals(0.0, result);
    }
}
