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

    @Test
    @DisplayName("Get battles won by user when null")
    void testGetBattlesWonByUserNull() {
        when(playerRepository.getBattlesWonByUser(1)).thenReturn(null);
        
        Integer result = statisticService.getBattlesWonByUser(1);
        
        assertEquals(0, result);
        verify(playerRepository, times(1)).getBattlesWonByUser(1);
    }

    @Test
    @DisplayName("Get total battles disputed when null")
    void testGetTotalBattlesDisputedNull() {
        when(playerRepository.getTotalBattlesDisputed()).thenReturn(null);
        
        Integer result = statisticService.getTotalBattlesDisputed();
        
        assertEquals(0, result);
        verify(playerRepository, times(1)).getTotalBattlesDisputed();
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
    @DisplayName("Get total time played with multiple matches")
    void testGetTotalTimePlayedByUserMultipleMatches() {
        List<Player> players = new ArrayList<>();
        
        Match match1 = new Match();
        match1.setStartTime(LocalDateTime.now().minusHours(3));
        match1.setEndTime(LocalDateTime.now().minusHours(2));
        
        Match match2 = new Match();
        match2.setStartTime(LocalDateTime.now().minusHours(1));
        match2.setEndTime(LocalDateTime.now());
        
        Player player1 = new Player();
        player1.setMatch(match1);
        
        Player player2 = new Player();
        player2.setMatch(match2);
        
        players.add(player1);
        players.add(player2);
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getTotalTimePlayedByUserFOR(1);
        
        assertNotNull(result);
        assertTrue(result >= 0);
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
    @DisplayName("Get total victories with multiple victories")
    void testGetTotalVictoriesByUserMultiple() {
        List<Player> players = new ArrayList<>();
        
        for (int i = 0; i < 3; i++) {
            Player player = new Player();
            player.setId(i + 1);
            Match match = new Match();
            match.setWinner(player);
            player.setMatch(match);
            players.add(player);
        }
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getTotalVictoriesByUser(1);
        
        assertEquals(3, result);
    }

    @Test
    @DisplayName("Get total victories with null winner")
    void testGetTotalVictoriesByUserNullWinner() {
        List<Player> players = new ArrayList<>();
        
        Player player = new Player();
        player.setId(1);
        Match match = new Match();
        match.setWinner(null);
        player.setMatch(match);
        players.add(player);
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getTotalVictoriesByUser(1);
        
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Get total victories with mixed results")
    void testGetTotalVictoriesByUserMixedResults() {
        List<Player> players = new ArrayList<>();
        
        Player winner = new Player();
        winner.setId(1);
        Match match1 = new Match();
        match1.setWinner(winner);
        winner.setMatch(match1);
        
        Player loser = new Player();
        loser.setId(1);
        Player otherWinner = new Player();
        otherWinner.setId(2);
        Match match2 = new Match();
        match2.setWinner(otherWinner);
        loser.setMatch(match2);
        
        players.add(winner);
        players.add(loser);
        
        when(playerRepository.findByUserId(1)).thenReturn(players);
        
        Integer result = statisticService.getTotalVictoriesByUser(1);
        
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Get average rooms visited with multiple matches")
    void testGetAverageRoomsVisitedPerMatchMultiple() {
        List<Match> matches = new ArrayList<>();
        
        Match match1 = new Match();
        Player p1 = new Player();
        p1.setRoomsVisited(10);
        Player p2 = new Player();
        p2.setRoomsVisited(20);
        match1.setPlayers(List.of(p1, p2));
        
        Match match2 = new Match();
        Player p3 = new Player();
        p3.setRoomsVisited(15);
        match2.setPlayers(List.of(p3));
        
        matches.add(match1);
        matches.add(match2);
        
        when(matchService.getAllMatchs()).thenReturn(matches);
        
        Double result = statisticService.getAverageRoomsVisitedPerMatch();
        
        assertEquals(22.5, result);
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
    @DisplayName("Get average players per match with one match")
    void testGetAveragePlayersPerMatchSingleMatch() {
        List<Match> matches = new ArrayList<>();
        Match match = new Match();
        match.setMaxPlayers(3);
        matches.add(match);
        
        when(matchService.getAllMatchs()).thenReturn(matches);
        
        Double result = statisticService.getAveragePlayersPerMatch();
        
        assertEquals(3.0, result);
    }

    @Test
    @DisplayName("Get average players per match with different max players")
    void testGetAveragePlayersPerMatchDifferent() {
        List<Match> matches = new ArrayList<>();
        
        Match match1 = new Match();
        match1.setMaxPlayers(2);
        
        Match match2 = new Match();
        match2.setMaxPlayers(4);
        
        Match match3 = new Match();
        match3.setMaxPlayers(6);
        
        matches.add(match1);
        matches.add(match2);
        matches.add(match3);
        
        when(matchService.getAllMatchs()).thenReturn(matches);
        
        Double result = statisticService.getAveragePlayersPerMatch();
        
        assertEquals(4.0, result);
    }
}
