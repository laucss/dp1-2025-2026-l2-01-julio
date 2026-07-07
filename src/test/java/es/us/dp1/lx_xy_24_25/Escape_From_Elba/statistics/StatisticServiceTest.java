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
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatisticService Tests")
class StatisticServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MatchService matchService;

    @Mock
    private UserService userService;

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
        testUser.setAvatar("avatar.png");

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
        // Corrección: El método real llama a playerRepository.getTotalVictoriesByUser(userId)
        when(playerRepository.getTotalVictoriesByUser(1)).thenReturn(1);
        
        Integer result = statisticService.getTotalVictoriesByUser(1);
        
        assertEquals(1, result);
        verify(playerRepository, times(1)).getTotalVictoriesByUser(1);
    }

    @Test
    @DisplayName("Get total victories by user when null")
    void testGetTotalVictoriesByUserNull() {
        when(playerRepository.getTotalVictoriesByUser(1)).thenReturn(null);
        
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
        testPlayer.setMatch(testMatch);
        players.add(testPlayer);
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getTotalTimePlayedByUserFOR(1);
        
        assertEquals(120, result); // 2 horas de diferencia = 120 minutos
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

    @Test
    @DisplayName("Get battles won by user when null")
    void testGetBattlesWonByUserNull() {
        when(playerRepository.getBattlesWonByUser(1)).thenReturn(null);
        
        Integer result = statisticService.getBattlesWonByUser(1);
        
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Get total battles disputed when null")
    void testGetTotalBattlesDisputedNull() {
        when(playerRepository.getTotalBattlesDisputed()).thenReturn(null);
        
        Integer result = statisticService.getTotalBattlesDisputed();
        
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Get total action points when null")
    void testGetTotalAccionPointsByUserNull() {
        when(playerRepository.getTotalAccionPointsByUser(1)).thenReturn(null);
        
        Integer result = statisticService.getTotalAccionPointsByUser(1);
        
        assertNull(result);
    }

    @Test
    @DisplayName("Get matches played by user with empty list")
    void testGetMatchesPlayedByUserEmpty() {
        when(playerRepository.findByUserId(1)).thenReturn(new ArrayList<>());
        
        Integer result = statisticService.getMatchesPlayedByUser(1);
        
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Get total time played with null match")
    void testGetTotalTimePlayedByUserNullMatch() {
        List<Player> players = new ArrayList<>();
        Player player = new Player();
        player.setMatch(null);
        players.add(player);
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getTotalTimePlayedByUserFOR(1);
        
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Get total time played with null start or end time")
    void testGetTotalTimePlayedByUserNullTimes() {
        List<Player> players = new ArrayList<>();
        Match match = new Match();
        match.setStartTime(null);
        match.setEndTime(null);
        
        Player player = new Player();
        player.setMatch(match);
        players.add(player);
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getTotalTimePlayedByUserFOR(1);
        
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Get average rooms visited with null rooms visited")
    void testGetAverageRoomsVisitedPerMatchWithNull() {
        List<Match> matches = new ArrayList<>();
        Match match = new Match();
        Player p1 = new Player();
        p1.setRoomsVisited(null);
        Player p2 = new Player();
        p2.setRoomsVisited(10);
        match.setPlayers(List.of(p1, p2));
        matches.add(match);
        
        when(matchService.getAllMatchs()).thenReturn(matches);
        
        Double result = statisticService.getAverageRoomsVisitedPerMatch();
        
        assertEquals(10.0, result);
    }

    @Test
    @DisplayName("Get average match duration with multiple matches")
    void testGetAverageMatchDuration() {
        List<Match> matches = new ArrayList<>();
        matches.add(testMatch);

        Match match2 = new Match();
        match2.setStartTime(LocalDateTime.now().minusHours(1));
        match2.setEndTime(LocalDateTime.now());
        matches.add(match2); 

        when(matchService.getAllMatchs()).thenReturn(matches);

        Double result = statisticService.getAverageMatchDuration();

        assertEquals(90.0, result); 
    }

    @Test
    @DisplayName("Get average match duration with empty matches")
    void testGetAverageMatchDurationEmpty() {
        when(matchService.getAllMatchs()).thenReturn(new ArrayList<>());

        Double result = statisticService.getAverageMatchDuration();

        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Get battles played by user")
    void testGetBattlesPlayedByUser() {
        when(playerRepository.getBattlesPlayedByUser(1)).thenReturn(10);

        Integer result = statisticService.getBattlesPlayedByUser(1);

        assertEquals(10, result);
    }

    @Test
    @DisplayName("Get battles played by user when null")
    void testGetBattlesPlayedByUserNull() {
        when(playerRepository.getBattlesPlayedByUser(1)).thenReturn(null);

        Integer result = statisticService.getBattlesPlayedByUser(1);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Get max rooms visited in match")
    void testGetMaxRoomsVisitedInMatch() {
        List<Player> players = new ArrayList<>();
        Player p1 = new Player(); p1.setRoomsVisited(4);
        Player p2 = new Player(); p2.setRoomsVisited(12);
        Player p3 = new Player(); p3.setRoomsVisited(8);
        players.addAll(List.of(p1, p2, p3));

        when(playerRepository.findByUserId(1)).thenReturn(players);

        Integer result = statisticService.getMaxRoomsVisitedInMatch(1);

        assertEquals(12, result);
    }

    @Test
    @DisplayName("Get longest match duration")
    void testGetLongestMatchDuration() {
        List<Match> matches = new ArrayList<>();
        matches.add(testMatch);

        Match match2 = new Match();
        match2.setStartTime(LocalDateTime.now().minusHours(4));
        match2.setEndTime(LocalDateTime.now());
        matches.add(match2); 

        when(matchService.getAllMatchs()).thenReturn(matches);

        Integer result = statisticService.getLongestMatchDuration();

        assertEquals(240, result);
    }

    @Test
    @DisplayName("Get shortest match duration")
    void testGetShortestMatchDuration() {
        List<Match> matches = new ArrayList<>();
        matches.add(testMatch); 

        Match match2 = new Match();
        match2.setStartTime(LocalDateTime.now().minusHours(3));
        match2.setEndTime(LocalDateTime.now());
        matches.add(match2); 

        when(matchService.getAllMatchs()).thenReturn(matches);

        Integer result = statisticService.getShortestMatchDuration();

        assertEquals(120, result);
    }

    @Test
    @DisplayName("Get shortest match duration when empty")
    void testGetShortestMatchDurationEmpty() {
        when(matchService.getAllMatchs()).thenReturn(new ArrayList<>());

        Integer result = statisticService.getShortestMatchDuration();

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Get player type Aggressive")
    void testGetPlayerTypeAggressive() {
        when(playerRepository.getBattlesWonByUser(1)).thenReturn(10);
        when(playerRepository.getTotalRoomsVisitedByUser(1)).thenReturn(5);

        String type = statisticService.getPlayerType(1);

        assertEquals("Aggressive", type);
    }

    @Test
    @DisplayName("Get player type Explorer")
    void testGetPlayerTypeExplorer() {
        when(playerRepository.getBattlesWonByUser(1)).thenReturn(2);
        when(playerRepository.getTotalRoomsVisitedByUser(1)).thenReturn(10); 

        String type = statisticService.getPlayerType(1);

        assertEquals("Explorer", type);
    }

    @Test
    @DisplayName("Get player type Balanced")
    void testGetPlayerTypeBalanced() {
        when(playerRepository.getBattlesWonByUser(1)).thenReturn(5);
        when(playerRepository.getTotalRoomsVisitedByUser(1)).thenReturn(6);

        String type = statisticService.getPlayerType(1);

        assertEquals("Balanced", type);
    }

    @Test
    @DisplayName("Get win rate by user")
    void testGetWinRateByUser() {
        when(playerRepository.getTotalVictoriesByUser(1)).thenReturn(3);
        
        List<Player> players = List.of(testPlayer, testPlayer, testPlayer, testPlayer);
        when(playerRepository.findByUserId(1)).thenReturn(players); 

        Double rate = statisticService.getWinRateByUser(1);

        assertEquals(75.0, rate); 
    }

    @Test
    @DisplayName("Get win rate by user with zero matches played")
    void testGetWinRateByUserZeroMatches() {
        when(playerRepository.getTotalVictoriesByUser(1)).thenReturn(0);
        when(playerRepository.findByUserId(1)).thenReturn(new ArrayList<>());

        Double rate = statisticService.getWinRateByUser(1);

        assertEquals(0.0, rate);
    }

}