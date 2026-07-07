package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserStatisticsDTO Tests")
class UserStatisticsDTOTest {

    private UserStatisticsDTO userStatistics;

    @BeforeEach
    void setUp() {
        userStatistics = new UserStatisticsDTO();
    }

    @Test
    @DisplayName("Debe crear UserStatisticsDTO con valores por defecto")
    void testDefaultConstructor() {
        // When
        UserStatisticsDTO dto = new UserStatisticsDTO();

        // Then
        assertNotNull(dto);
        assertEquals(0, dto.getTotalVictories());
        assertEquals(0, dto.getMatchesPlayed());
        assertEquals(0, dto.getTotalTimePlayed());
        assertEquals(0, dto.getTotalActionPoints());
        assertEquals(0, dto.getBattlesWon());
        assertEquals(0, dto.getRoomsVisited());
        assertEquals(0, dto.getMaxRoomsVisitedInMatch());

        assertNull(dto.getWinRate());
        assertNull(dto.getAverageTimePerMatch());
        assertNull(dto.getAverageActionPointsPerMatch());
        assertNull(dto.getAverageRoomsVisitedPerMatch());
        assertNull(dto.getBattlesWonPerMatch());
        assertNull(dto.getTotalBattlesPlayed());
        assertNull(dto.getPlayerType());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener totalVictories")
    void testSetAndGetTotalVictories() {
        // When
        userStatistics.setTotalVictories(5);

        // Then
        assertEquals(5, userStatistics.getTotalVictories());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener matchesPlayed")
    void testSetAndGetMatchesPlayed() {
        // When
        userStatistics.setMatchesPlayed(10);

        // Then
        assertEquals(10, userStatistics.getMatchesPlayed());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener totalTimePlayed")
    void testSetAndGetTotalTimePlayed() {
        // When
        userStatistics.setTotalTimePlayed(3600);

        // Then
        assertEquals(3600, userStatistics.getTotalTimePlayed());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener totalActionPoints")
    void testSetAndGetTotalActionPoints() {
        // When
        userStatistics.setTotalActionPoints(150);

        // Then
        assertEquals(150, userStatistics.getTotalActionPoints());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener battlesWon")
    void testSetAndGetBattlesWon() {
        // When
        userStatistics.setBattlesWon(8);

        // Then
        assertEquals(8, userStatistics.getBattlesWon());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener roomsVisited")
    void testSetAndGetRoomsVisited() {
        // When
        userStatistics.setRoomsVisited(25);

        // Then
        assertEquals(25, userStatistics.getRoomsVisited());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener los campos de tipo Double")
    void testSetAndGetDoubleFields() {
        // When
        userStatistics.setWinRate(65.5);
        userStatistics.setAverageTimePerMatch(450.0);
        userStatistics.setAverageActionPointsPerMatch(22.3);
        userStatistics.setAverageRoomsVisitedPerMatch(5.4);
        userStatistics.setBattlesWonPerMatch(1.8);

        // Then
        assertEquals(65.5, userStatistics.getWinRate());
        assertEquals(450.0, userStatistics.getAverageTimePerMatch());
        assertEquals(22.3, userStatistics.getAverageActionPointsPerMatch());
        assertEquals(5.4, userStatistics.getAverageRoomsVisitedPerMatch());
        assertEquals(1.8, userStatistics.getBattlesWonPerMatch());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener totalBattlesPlayed")
    void testSetAndGetTotalBattlesPlayed() {
        // When
        userStatistics.setTotalBattlesPlayed(12);

        // Then
        assertEquals(12, userStatistics.getTotalBattlesPlayed());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener maxRoomsVisitedInMatch")
    void testSetAndGetMaxRoomsVisitedInMatch() {
        // When
        userStatistics.setMaxRoomsVisitedInMatch(9);

        // Then
        assertEquals(9, userStatistics.getMaxRoomsVisitedInMatch());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener playerType")
    void testSetAndGetPlayerType() {
        // When
        userStatistics.setPlayerType("EXPLORER");

        // Then
        assertEquals("EXPLORER", userStatistics.getPlayerType());
    }

    @Test
    @DisplayName("Debe permitir establecer id heredado de BaseEntity")
    void testSetAndGetId() {
        // When
        userStatistics.setId(1);

        // Then
        assertEquals(1, userStatistics.getId());
    }

    @Test
    @DisplayName("Debe manejar valores null en todos los campos")
    void testNullValues() {
        // When
        userStatistics.setTotalVictories(null);
        userStatistics.setMatchesPlayed(null);
        userStatistics.setTotalTimePlayed(null);
        userStatistics.setTotalActionPoints(null);
        userStatistics.setBattlesWon(null);
        userStatistics.setRoomsVisited(null);
        userStatistics.setWinRate(null);
        userStatistics.setAverageTimePerMatch(null);
        userStatistics.setAverageActionPointsPerMatch(null);
        userStatistics.setAverageRoomsVisitedPerMatch(null);
        userStatistics.setBattlesWonPerMatch(null);
        userStatistics.setTotalBattlesPlayed(null);
        userStatistics.setMaxRoomsVisitedInMatch(null);
        userStatistics.setPlayerType(null);

        // Then
        assertNull(userStatistics.getTotalVictories());
        assertNull(userStatistics.getMatchesPlayed());
        assertNull(userStatistics.getTotalTimePlayed());
        assertNull(userStatistics.getTotalActionPoints());
        assertNull(userStatistics.getBattlesWon());
        assertNull(userStatistics.getRoomsVisited());
        assertNull(userStatistics.getWinRate());
        assertNull(userStatistics.getAverageTimePerMatch());
        assertNull(userStatistics.getAverageActionPointsPerMatch());
        assertNull(userStatistics.getAverageRoomsVisitedPerMatch());
        assertNull(userStatistics.getBattlesWonPerMatch());
        assertNull(userStatistics.getTotalBattlesPlayed());
        assertNull(userStatistics.getMaxRoomsVisitedInMatch());
        assertNull(userStatistics.getPlayerType());
    }

    @Test
    @DisplayName("Debe mantener coherencia entre getters y setters")
    void testGettersAndSettersConsistency() {
        // When
        userStatistics.setId(100);
        userStatistics.setTotalVictories(7);
        userStatistics.setMatchesPlayed(20);
        userStatistics.setTotalTimePlayed(7200);
        userStatistics.setTotalActionPoints(300);
        userStatistics.setBattlesWon(15);
        userStatistics.setRoomsVisited(50);
        userStatistics.setWinRate(35.0);
        userStatistics.setAverageTimePerMatch(360.0);
        userStatistics.setAverageActionPointsPerMatch(15.0);
        userStatistics.setAverageRoomsVisitedPerMatch(2.5);
        userStatistics.setBattlesWonPerMatch(0.75);
        userStatistics.setTotalBattlesPlayed(25);
        userStatistics.setMaxRoomsVisitedInMatch(8);
        userStatistics.setPlayerType("WARRIOR");

        // Then
        assertEquals(100, userStatistics.getId());
        assertEquals(7, userStatistics.getTotalVictories());
        assertEquals(20, userStatistics.getMatchesPlayed());
        assertEquals(7200, userStatistics.getTotalTimePlayed());
        assertEquals(300, userStatistics.getTotalActionPoints());
        assertEquals(15, userStatistics.getBattlesWon());
        assertEquals(50, userStatistics.getRoomsVisited());
        assertEquals(35.0, userStatistics.getWinRate());
        assertEquals(360.0, userStatistics.getAverageTimePerMatch());
        assertEquals(15.0, userStatistics.getAverageActionPointsPerMatch());
        assertEquals(2.5, userStatistics.getAverageRoomsVisitedPerMatch());
        assertEquals(0.75, userStatistics.getBattlesWonPerMatch());
        assertEquals(25, userStatistics.getTotalBattlesPlayed());
        assertEquals(8, userStatistics.getMaxRoomsVisitedInMatch());
        assertEquals("WARRIOR", userStatistics.getPlayerType());
    }

    @Test
    @DisplayName("Debe permitir modificar valores múltiples veces")
    void testMultipleModifications() {
        // First modification
        userStatistics.setTotalVictories(3);
        assertEquals(3, userStatistics.getTotalVictories());

        // Second modification
        userStatistics.setTotalVictories(5);
        assertEquals(5, userStatistics.getTotalVictories());

        // Third modification
        userStatistics.setTotalVictories(10);
        assertEquals(10, userStatistics.getTotalVictories());
    }

    @Test
    @DisplayName("Debe manejar valores cero en todos los campos")
    void testZeroValues() {
        // When
        userStatistics.setTotalVictories(0);
        userStatistics.setMatchesPlayed(0);
        userStatistics.setTotalTimePlayed(0);
        userStatistics.setTotalActionPoints(0);
        userStatistics.setBattlesWon(0);
        userStatistics.setRoomsVisited(0);
        userStatistics.setWinRate(0.0);
        userStatistics.setAverageTimePerMatch(0.0);
        userStatistics.setAverageActionPointsPerMatch(0.0);
        userStatistics.setAverageRoomsVisitedPerMatch(0.0);
        userStatistics.setBattlesWonPerMatch(0.0);
        userStatistics.setTotalBattlesPlayed(0);
        userStatistics.setMaxRoomsVisitedInMatch(0);

        // Then
        assertEquals(0, userStatistics.getTotalVictories());
        assertEquals(0, userStatistics.getMatchesPlayed());
        assertEquals(0, userStatistics.getTotalTimePlayed());
        assertEquals(0, userStatistics.getTotalActionPoints());
        assertEquals(0, userStatistics.getBattlesWon());
        assertEquals(0, userStatistics.getRoomsVisited());
        assertEquals(0.0, userStatistics.getWinRate());
        assertEquals(0.0, userStatistics.getAverageTimePerMatch());
        assertEquals(0.0, userStatistics.getAverageActionPointsPerMatch());
        assertEquals(0.0, userStatistics.getAverageRoomsVisitedPerMatch());
        assertEquals(0.0, userStatistics.getBattlesWonPerMatch());
        assertEquals(0, userStatistics.getTotalBattlesPlayed());
        assertEquals(0, userStatistics.getMaxRoomsVisitedInMatch());
    }

    @Test
    @DisplayName("Debe manejar valores grande en los campos")
    void testLargeValues() {
        // When
        userStatistics.setTotalVictories(1000);
        userStatistics.setMatchesPlayed(5000);
        userStatistics.setTotalTimePlayed(999999);
        userStatistics.setTotalActionPoints(99999);
        userStatistics.setBattlesWon(10000);
        userStatistics.setRoomsVisited(500);
        userStatistics.setWinRate(100.0);
        userStatistics.setAverageTimePerMatch(9999.9);
        userStatistics.setAverageActionPointsPerMatch(888.8);
        userStatistics.setAverageRoomsVisitedPerMatch(77.7);
        userStatistics.setBattlesWonPerMatch(66.6);
        userStatistics.setTotalBattlesPlayed(55555);
        userStatistics.setMaxRoomsVisitedInMatch(444);

        // Then
        assertEquals(1000, userStatistics.getTotalVictories());
        assertEquals(5000, userStatistics.getMatchesPlayed());
        assertEquals(999999, userStatistics.getTotalTimePlayed());
        assertEquals(99999, userStatistics.getTotalActionPoints());
        assertEquals(10000, userStatistics.getBattlesWon());
        assertEquals(500, userStatistics.getRoomsVisited());
        assertEquals(100.0, userStatistics.getWinRate());
        assertEquals(9999.9, userStatistics.getAverageTimePerMatch());
        assertEquals(888.8, userStatistics.getAverageActionPointsPerMatch());
        assertEquals(77.7, userStatistics.getAverageRoomsVisitedPerMatch());
        assertEquals(66.6, userStatistics.getBattlesWonPerMatch());
        assertEquals(55555, userStatistics.getTotalBattlesPlayed());
        assertEquals(444, userStatistics.getMaxRoomsVisitedInMatch());
    }

    @Test
    @DisplayName("Debe crear instancias independientes")
    void testIndependentInstances() {
        // Given
        UserStatisticsDTO dto1 = new UserStatisticsDTO();
        UserStatisticsDTO dto2 = new UserStatisticsDTO();

        // When
        dto1.setTotalVictories(5);
        dto2.setTotalVictories(10);

        // Then
        assertEquals(5, dto1.getTotalVictories());
        assertEquals(10, dto2.getTotalVictories());
        assertNotEquals(dto1.getTotalVictories(), dto2.getTotalVictories());
    }
}