package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics.achievements;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AchievementRestController Tests")
class AchievementRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AchievementService achievementService;

    @Autowired
    private ObjectMapper objectMapper;

    private Achievement testAchievement;

    @BeforeEach
    void setUp() {
        testAchievement = new Achievement();
        testAchievement.setId(1);
        testAchievement.setDescription("Win your first match");
        testAchievement.setMetric(Metric.VICTORIES);
        testAchievement.setThreshold(1.0);
        testAchievement.setTier(TierType.FACIL);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find all achievements")
    void testFindAll() throws Exception {
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(testAchievement);
        
        when(achievementService.getAchievements()).thenReturn(achievements);
        
        mockMvc.perform(get("/api/v1/achievements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].tier", is("FACIL")));
        
        verify(achievementService, times(1)).getAchievements();
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find all achievements empty list")
    void testFindAllEmpty() throws Exception {
        List<Achievement> achievements = new ArrayList<>();
        
        when(achievementService.getAchievements()).thenReturn(achievements);
        
        mockMvc.perform(get("/api/v1/achievements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find achievement by id")
    void testFindAchievement() throws Exception {
        when(achievementService.getById(1)).thenReturn(testAchievement);
        
        mockMvc.perform(get("/api/v1/achievements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Win your first match")))
                .andExpect(jsonPath("$.metric", is("VICTORIES")))
                .andExpect(jsonPath("$.threshold", is(1.0)))
                .andExpect(jsonPath("$.tier", is("FACIL")));
        
        verify(achievementService, times(1)).getById(1);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find achievement by id not found")
    void testFindAchievementNotFound() throws Exception {
        when(achievementService.getById(99)).thenReturn(null);
        
        mockMvc.perform(get("/api/v1/achievements/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Create achievement")
    void testCreateAchievement() throws Exception {
        when(achievementService.saveAchievement(any())).thenReturn(testAchievement);
        
        Achievement result = achievementService.saveAchievement(testAchievement);
        
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Win your first match", result.getDescription());
        verify(achievementService, times(1)).saveAchievement(any());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Create achievement with valid description")
    void testCreateAchievementNullName() throws Exception {
        testAchievement.setDescription("Another achievement");
        
        when(achievementService.saveAchievement(any())).thenReturn(testAchievement);
        
        Achievement result = achievementService.saveAchievement(testAchievement);
        
        assertNotNull(result);
        assertEquals("Another achievement", result.getDescription());
        verify(achievementService, times(1)).saveAchievement(any());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Update achievement")
    void testModifyAchievement() throws Exception {
        testAchievement.setDescription("Updated description");
        
        when(achievementService.saveAchievement(any())).thenReturn(testAchievement);
        
        Achievement result = achievementService.saveAchievement(testAchievement);
        
        assertNotNull(result);
        assertEquals("Updated description", result.getDescription());
        assertEquals(1, result.getId());
        verify(achievementService, times(1)).saveAchievement(any());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Delete achievement")
    void testDeleteAchievement() throws Exception {
        achievementService.deleteAchievementById(1);
        
        verify(achievementService, times(1)).deleteAchievementById(1);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find achievements by tier FACIL")
    void testFindAchievementsByTierFacil() throws Exception {
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(testAchievement);
        
        when(achievementService.getAchievementsByTier(TierType.FACIL)).thenReturn(achievements);
        
        List<Achievement> result = achievementService.getAchievementsByTier(TierType.FACIL);
        
        assertEquals(1, result.size());
        assertEquals(TierType.FACIL, result.get(0).getTier());
        verify(achievementService, times(1)).getAchievementsByTier(TierType.FACIL);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find achievements by tier INTERMEDIO empty")
    void testFindAchievementsByTierIntermedio() throws Exception {
        List<Achievement> achievements = new ArrayList<>();
        
        when(achievementService.getAchievementsByTier(TierType.INTERMEDIO)).thenReturn(achievements);
        
        List<Achievement> result = achievementService.getAchievementsByTier(TierType.INTERMEDIO);
        
        assertTrue(result.isEmpty());
        verify(achievementService, times(1)).getAchievementsByTier(TierType.INTERMEDIO);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Get multiple achievements with different tiers")
    void testFindMultipleAchievements() throws Exception {
        Achievement achievement2 = new Achievement();
        achievement2.setId(2);
        achievement2.setDescription("Win 10 matches");
        achievement2.setMetric(Metric.VICTORIES);
        achievement2.setThreshold(10.0);
        achievement2.setTier(TierType.INTERMEDIO);
        
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(testAchievement);
        achievements.add(achievement2);
        
        when(achievementService.getAchievements()).thenReturn(achievements);
        
        mockMvc.perform(get("/api/v1/achievements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tier", is("FACIL")))
                .andExpect(jsonPath("$[1].tier", is("INTERMEDIO")));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Delete non-existent achievement")
    void testDeleteNonExistentAchievement() throws Exception {
        achievementService.deleteAchievementById(99);
        
        verify(achievementService, times(1)).deleteAchievementById(99);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Find all achievements as admin")
    void testFindAllAsAdmin() throws Exception {
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(testAchievement);
        
        when(achievementService.getAchievements()).thenReturn(achievements);
        
        mockMvc.perform(get("/api/v1/achievements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find achievement with all tier types")
    void testFindAchievementsAllTiers() throws Exception {
        List<Achievement> achievements = new ArrayList<>();
        
        Achievement facil = new Achievement();
        facil.setId(1);
        facil.setDescription("Facil achievement");
        facil.setMetric(Metric.VICTORIES);
        facil.setThreshold(1.0);
        facil.setTier(TierType.FACIL);
        
        Achievement intermedio = new Achievement();
        intermedio.setId(2);
        intermedio.setDescription("Intermedio achievement");
        intermedio.setMetric(Metric.VICTORIES);
        intermedio.setThreshold(5.0);
        intermedio.setTier(TierType.INTERMEDIO);
        
        Achievement dificil = new Achievement();
        dificil.setId(3);
        dificil.setDescription("Dificil achievement");
        dificil.setMetric(Metric.VICTORIES);
        dificil.setThreshold(10.0);
        dificil.setTier(TierType.DIFICIL);
        
        achievements.add(facil);
        achievements.add(intermedio);
        achievements.add(dificil);
        
        when(achievementService.getAchievements()).thenReturn(achievements);
        
        mockMvc.perform(get("/api/v1/achievements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find achievement with different metrics")
    void testFindAchievementsDifferentMetrics() throws Exception {
        Achievement achievement1 = new Achievement();
        achievement1.setId(1);
        achievement1.setDescription("Win matches");
        achievement1.setMetric(Metric.VICTORIES);
        achievement1.setThreshold(5.0);
        achievement1.setTier(TierType.FACIL);
        
        Achievement achievement2 = new Achievement();
        achievement2.setId(2);
        achievement2.setDescription("Play games");
        achievement2.setMetric(Metric.GAMES_PLAYED);
        achievement2.setThreshold(10.0);
        achievement2.setTier(TierType.FACIL);
        
        Achievement achievement3 = new Achievement();
        achievement3.setId(3);
        achievement3.setDescription("Earn points");
        achievement3.setMetric(Metric.ACTION_POINTS_EARNED);
        achievement3.setThreshold(50.0);
        achievement3.setTier(TierType.INTERMEDIO);
        
        List<Achievement> achievements = List.of(achievement1, achievement2, achievement3);
        
        when(achievementService.getAchievements()).thenReturn(achievements);
        
        mockMvc.perform(get("/api/v1/achievements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].metric", is("VICTORIES")))
                .andExpect(jsonPath("$[1].metric", is("GAMES_PLAYED")))
                .andExpect(jsonPath("$[2].metric", is("ACTION_POINTS_EARNED")));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Create achievement with different thresholds")
    void testCreateAchievementDifferentThresholds() throws Exception {
        testAchievement.setThreshold(5.0);
        
        when(achievementService.saveAchievement(any())).thenReturn(testAchievement);
        
        Achievement result = achievementService.saveAchievement(testAchievement);
        
        assertEquals(5.0, result.getThreshold());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Update achievement threshold")
    void testUpdateAchievementThreshold() throws Exception {
        testAchievement.setThreshold(10.0);
        
        when(achievementService.saveAchievement(any())).thenReturn(testAchievement);
        
        Achievement result = achievementService.saveAchievement(testAchievement);
        
        assertEquals(10.0, result.getThreshold());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Update achievement tier")
    void testUpdateAchievementTier() throws Exception {
        testAchievement.setTier(TierType.DIFICIL);
        
        when(achievementService.saveAchievement(any())).thenReturn(testAchievement);
        
        Achievement result = achievementService.saveAchievement(testAchievement);
        
        assertEquals(TierType.DIFICIL, result.getTier());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find achievements by tier DIFICIL")
    void testFindAchievementsByTierDificil() throws Exception {
        Achievement dificilAchievement = new Achievement();
        dificilAchievement.setTier(TierType.DIFICIL);
        
        List<Achievement> achievements = List.of(dificilAchievement);
        
        when(achievementService.getAchievementsByTier(TierType.DIFICIL)).thenReturn(achievements);
        
        List<Achievement> result = achievementService.getAchievementsByTier(TierType.DIFICIL);
        
        assertEquals(1, result.size());
        assertEquals(TierType.DIFICIL, result.get(0).getTier());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find multiple achievements with same tier")
    void testFindMultipleAchievementsSameTier() throws Exception {
        Achievement achievement1 = new Achievement();
        achievement1.setId(1);
        achievement1.setTier(TierType.FACIL);
        
        Achievement achievement2 = new Achievement();
        achievement2.setId(2);
        achievement2.setTier(TierType.FACIL);
        
        List<Achievement> achievements = List.of(achievement1, achievement2);
        
        when(achievementService.getAchievementsByTier(TierType.FACIL)).thenReturn(achievements);
        
        List<Achievement> result = achievementService.getAchievementsByTier(TierType.FACIL);
        
        assertEquals(2, result.size());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Create achievement with high threshold")
    void testCreateAchievementHighThreshold() throws Exception {
        testAchievement.setThreshold(100.0);
        testAchievement.setTier(TierType.DIFICIL);
        
        when(achievementService.saveAchievement(any())).thenReturn(testAchievement);
        
        Achievement result = achievementService.saveAchievement(testAchievement);
        
        assertEquals(100.0, result.getThreshold());
        assertEquals(TierType.DIFICIL, result.getTier());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Find achievement and verify all fields")
    void testFindAchievementVerifyAllFields() throws Exception {
        when(achievementService.getById(1)).thenReturn(testAchievement);
        
        mockMvc.perform(get("/api/v1/achievements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.metric").exists())
                .andExpect(jsonPath("$.threshold").exists())
                .andExpect(jsonPath("$.tier").exists());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Delete multiple achievements")
    void testDeleteMultipleAchievements() throws Exception {
        achievementService.deleteAchievementById(1);
        achievementService.deleteAchievementById(2);
        achievementService.deleteAchievementById(3);
        
        verify(achievementService, times(1)).deleteAchievementById(1);
        verify(achievementService, times(1)).deleteAchievementById(2);
        verify(achievementService, times(1)).deleteAchievementById(3);
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Update achievement metric")
    void testUpdateAchievementMetric() throws Exception {
        testAchievement.setMetric(Metric.TOTAL_PLAY_TIME);
        
        when(achievementService.saveAchievement(any())).thenReturn(testAchievement);
        
        Achievement result = achievementService.saveAchievement(testAchievement);
        
        assertEquals(Metric.TOTAL_PLAY_TIME, result.getMetric());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    @DisplayName("Create achievement with all metrics types")
    void testCreateAchievementsAllMetrics() throws Exception {
        Achievement a1 = new Achievement();
        a1.setMetric(Metric.VICTORIES);
        
        Achievement a2 = new Achievement();
        a2.setMetric(Metric.GAMES_PLAYED);
        
        Achievement a3 = new Achievement();
        a3.setMetric(Metric.ACTION_POINTS_EARNED);
        
        Achievement a4 = new Achievement();
        a4.setMetric(Metric.TOTAL_PLAY_TIME);
        
        when(achievementService.saveAchievement(any())).thenReturn(a1, a2, a3, a4);
        
        assertEquals(Metric.VICTORIES, achievementService.saveAchievement(a1).getMetric());
        assertEquals(Metric.GAMES_PLAYED, achievementService.saveAchievement(a2).getMetric());
        assertEquals(Metric.ACTION_POINTS_EARNED, achievementService.saveAchievement(a3).getMetric());
        assertEquals(Metric.TOTAL_PLAY_TIME, achievementService.saveAchievement(a4).getMetric());
    }
}
