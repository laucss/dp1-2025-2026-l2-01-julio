package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics.achievements;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics.StatisticService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

@ExtendWith(MockitoExtension.class)
@DisplayName("AchievementService Tests")
class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private StatisticService statisticService;

    @InjectMocks
    private AchievementService achievementService;

    private User testUser;
    private Achievement testAchievement;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testAchievement = new Achievement();
        testAchievement.setId(1);
        testAchievement.setDescription("Win your first match");
        testAchievement.setMetric(Metric.VICTORIES);
        testAchievement.setThreshold(1.0);
        testAchievement.setTier(TierType.FACIL);
    }

    @Test
    @DisplayName("Get all achievements")
    void testGetAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(testAchievement);
        
        when(achievementRepository.findAll()).thenReturn(achievements);
        
        List<Achievement> result = achievementService.getAchievements();
        
        assertEquals(1, result.size());
        assertEquals("Win your first match", result.get(0).getDescription());
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get achievement by id")
    void testGetById() {
        when(achievementRepository.findById(1)).thenReturn(Optional.of(testAchievement));
        
        Achievement result = achievementService.getById(1);
        
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Win your first match", result.getDescription());
    }

    @Test
    @DisplayName("Get achievement by id not found")
    void testGetByIdNotFound() {
        when(achievementRepository.findById(99)).thenReturn(Optional.empty());
        
        Achievement result = achievementService.getById(99);
        
        assertNull(result);
    }

    @Test
    @DisplayName("Save achievement")
    void testSaveAchievement() {
        when(achievementRepository.save(testAchievement)).thenReturn(testAchievement);
        
        Achievement result = achievementService.saveAchievement(testAchievement);
        
        assertNotNull(result);
        assertEquals("Win your first match", result.getDescription());
        verify(achievementRepository, times(1)).save(testAchievement);
    }

    @Test
    @DisplayName("Delete achievement by id")
    void testDeleteAchievementById() {
        achievementService.deleteAchievementById(1);
        
        verify(achievementRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Achievement unlocked for victories metric")
    void testIsAchievementUnlockedVictories() {
        testAchievement.setMetric(Metric.VICTORIES);
        testAchievement.setThreshold(1.0);
        
        when(statisticService.getTotalVictoriesByUser(1)).thenReturn(5);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Achievement not unlocked for victories metric")
    void testIsAchievementNotUnlockedVictories() {
        testAchievement.setMetric(Metric.VICTORIES);
        testAchievement.setThreshold(10.0);
        
        when(statisticService.getTotalVictoriesByUser(1)).thenReturn(5);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Achievement unlocked for games played metric")
    void testIsAchievementUnlockedGamesPlayed() {
        testAchievement.setMetric(Metric.GAMES_PLAYED);
        testAchievement.setThreshold(5.0);
        
        when(statisticService.getMatchesPlayedByUser(1)).thenReturn(10);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Achievement not unlocked for games played metric")
    void testIsAchievementNotUnlockedGamesPlayed() {
        testAchievement.setMetric(Metric.GAMES_PLAYED);
        testAchievement.setThreshold(20.0);
        
        when(statisticService.getMatchesPlayedByUser(1)).thenReturn(10);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Achievement unlocked for total play time metric")
    void testIsAchievementUnlockedTotalPlayTime() {
        testAchievement.setMetric(Metric.TOTAL_PLAY_TIME);
        testAchievement.setThreshold(60.0);
        
        when(statisticService.getTotalTimePlayedByUserFOR(1)).thenReturn(120);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Achievement unlocked for action points earned metric")
    void testIsAchievementUnlockedActionPoints() {
        testAchievement.setMetric(Metric.ACTION_POINTS_EARNED);
        testAchievement.setThreshold(50.0);
        
        when(statisticService.getTotalAccionPointsByUser(1)).thenReturn(100);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Get achievements by tier FACIL")
    void testGetAchievementsByTierFacil() {
        List<Achievement> facilAchievements = new ArrayList<>();
        testAchievement.setTier(TierType.FACIL);
        facilAchievements.add(testAchievement);
        
        when(achievementRepository.findByTier(TierType.FACIL)).thenReturn(facilAchievements);
        
        List<Achievement> result = achievementService.getAchievementsByTier(TierType.FACIL);
        
        assertEquals(1, result.size());
        assertEquals(TierType.FACIL, result.get(0).getTier());
    }

    @Test
    @DisplayName("Get achievements by tier DIFICIL")
    void testGetAchievementsByTierDificil() {
        List<Achievement> dificilAchievements = new ArrayList<>();
        
        when(achievementRepository.findByTier(TierType.DIFICIL)).thenReturn(dificilAchievements);
        
        List<Achievement> result = achievementService.getAchievementsByTier(TierType.DIFICIL);
        
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Achievement at threshold boundary")
    void testIsAchievementUnlockedAtBoundary() {
        testAchievement.setMetric(Metric.VICTORIES);
        testAchievement.setThreshold(5.0);
        
        when(statisticService.getTotalVictoriesByUser(1)).thenReturn(5);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Achievement just below threshold")
    void testIsAchievementUnlockedJustBelowThreshold() {
        testAchievement.setMetric(Metric.VICTORIES);
        testAchievement.setThreshold(5.0);
        
        when(statisticService.getTotalVictoriesByUser(1)).thenReturn(4);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Get all achievements when empty")
    void testGetAchievementsEmpty() {
        when(achievementRepository.findAll()).thenReturn(new ArrayList<>());
        
        List<Achievement> result = achievementService.getAchievements();
        
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Get multiple achievements")
    void testGetMultipleAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        
        Achievement a1 = new Achievement();
        a1.setId(1);
        Achievement a2 = new Achievement();
        a2.setId(2);
        Achievement a3 = new Achievement();
        a3.setId(3);
        
        achievements.add(a1);
        achievements.add(a2);
        achievements.add(a3);
        
        when(achievementRepository.findAll()).thenReturn(achievements);
        
        List<Achievement> result = achievementService.getAchievements();
        
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Save multiple achievements")
    void testSaveMultipleAchievements() {
        Achievement a1 = new Achievement();
        a1.setId(1);
        Achievement a2 = new Achievement();
        a2.setId(2);
        
        when(achievementRepository.save(any(Achievement.class))).thenReturn(a1).thenReturn(a2);
        
        Achievement result1 = achievementService.saveAchievement(a1);
        Achievement result2 = achievementService.saveAchievement(a2);
        
        assertNotNull(result1);
        assertNotNull(result2);
        verify(achievementRepository, times(2)).save(any(Achievement.class));
    }

    @Test
    @DisplayName("Delete multiple achievements")
    void testDeleteMultipleAchievements() {
        achievementService.deleteAchievementById(1);
        achievementService.deleteAchievementById(2);
        achievementService.deleteAchievementById(3);
        
        verify(achievementRepository, times(1)).deleteById(1);
        verify(achievementRepository, times(1)).deleteById(2);
        verify(achievementRepository, times(1)).deleteById(3);
    }

    @Test
    @DisplayName("Achievement not unlocked for rooms visited metric")
    void testIsAchievementNotUnlockedRoomsVisited() {
        testAchievement.setMetric(Metric.ROOMS_VISITED);
        testAchievement.setThreshold(100.0);
        
        when(statisticService.getTotalRoomsVisitedByUser(1)).thenReturn(50);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Achievement unlocked for rooms visited metric")
    void testIsAchievementUnlockedRoomsVisited() {
        testAchievement.setMetric(Metric.ROOMS_VISITED);
        testAchievement.setThreshold(50.0);
        
        when(statisticService.getTotalRoomsVisitedByUser(1)).thenReturn(100);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Achievement unlocked for battles won metric")
    void testIsAchievementUnlockedBattlesWon() {
        testAchievement.setMetric(Metric.BATTLES_WON);
        testAchievement.setThreshold(10.0);
        
        when(statisticService.getBattlesWonByUser(1)).thenReturn(15);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Achievement not unlocked for battles won metric")
    void testIsAchievementNotUnlockedBattlesWon() {
        testAchievement.setMetric(Metric.BATTLES_WON);
        testAchievement.setThreshold(20.0);
        
        when(statisticService.getBattlesWonByUser(1)).thenReturn(15);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Achievement with zero threshold")
    void testIsAchievementUnlockedZeroThreshold() {
        testAchievement.setMetric(Metric.VICTORIES);
        testAchievement.setThreshold(0.0);
        
        when(statisticService.getTotalVictoriesByUser(1)).thenReturn(0);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Achievement with very high threshold")
    void testIsAchievementNotUnlockedVeryHighThreshold() {
        testAchievement.setMetric(Metric.VICTORIES);
        testAchievement.setThreshold(1000.0);
        
        when(statisticService.getTotalVictoriesByUser(1)).thenReturn(10);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Get achievements by tier with multiple tiers")
    void testGetAchievementsByMultipleTiers() {
        List<Achievement> facilList = new ArrayList<>();
        facilList.add(testAchievement);
        
        List<Achievement> intermedioList = new ArrayList<>();
        Achievement intermedio = new Achievement();
        intermedio.setTier(TierType.INTERMEDIO);
        intermedioList.add(intermedio);
        
        when(achievementRepository.findByTier(TierType.FACIL)).thenReturn(facilList);
        when(achievementRepository.findByTier(TierType.INTERMEDIO)).thenReturn(intermedioList);
        
        List<Achievement> facilResult = achievementService.getAchievementsByTier(TierType.FACIL);
        List<Achievement> intermedioResult = achievementService.getAchievementsByTier(TierType.INTERMEDIO);
        
        assertEquals(1, facilResult.size());
        assertEquals(1, intermedioResult.size());
    }

    @Test
    @DisplayName("Save achievement and verify fields")
    void testSaveAchievementVerifyFields() {
        testAchievement.setDescription("New Description");
        testAchievement.setMetric(Metric.GAMES_PLAYED);
        testAchievement.setThreshold(15.0);
        testAchievement.setTier(TierType.INTERMEDIO);
        
        when(achievementRepository.save(testAchievement)).thenReturn(testAchievement);
        
        Achievement result = achievementService.saveAchievement(testAchievement);
        
        assertEquals("New Description", result.getDescription());
        assertEquals(Metric.GAMES_PLAYED, result.getMetric());
        assertEquals(15.0, result.getThreshold());
        assertEquals(TierType.INTERMEDIO, result.getTier());
    }

    @Test
    @DisplayName("Achievement unlocked at exact threshold for total play time")
    void testIsAchievementUnlockedExactThresholdPlayTime() {
        testAchievement.setMetric(Metric.TOTAL_PLAY_TIME);
        testAchievement.setThreshold(100.0);
        
        when(statisticService.getTotalTimePlayedByUserFOR(1)).thenReturn(100);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Achievement not unlocked below threshold for action points")
    void testIsAchievementNotUnlockedActionPoints() {
        testAchievement.setMetric(Metric.ACTION_POINTS_EARNED);
        testAchievement.setThreshold(100.0);
        
        when(statisticService.getTotalAccionPointsByUser(1)).thenReturn(99);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Get achievement by id with different ids")
    void testGetByIdMultipleIds() {
        Achievement a1 = new Achievement();
        a1.setId(1);
        Achievement a2 = new Achievement();
        a2.setId(2);
        
        when(achievementRepository.findById(1)).thenReturn(Optional.of(a1));
        when(achievementRepository.findById(2)).thenReturn(Optional.of(a2));
        
        Achievement result1 = achievementService.getById(1);
        Achievement result2 = achievementService.getById(2);
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(1, result1.getId());
        assertEquals(2, result2.getId());
    }

    @Test
    @DisplayName("Achievement not unlocked for games played at boundary")
    void testIsAchievementNotUnlockedGamesPlayedBoundary() {
        testAchievement.setMetric(Metric.GAMES_PLAYED);
        testAchievement.setThreshold(10.0);
        
        when(statisticService.getMatchesPlayedByUser(1)).thenReturn(9);
        
        boolean result = achievementService.isAchievementUnlocked(testAchievement, testUser);
        
        assertFalse(result);
    }
}
