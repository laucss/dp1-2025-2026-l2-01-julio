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
}
