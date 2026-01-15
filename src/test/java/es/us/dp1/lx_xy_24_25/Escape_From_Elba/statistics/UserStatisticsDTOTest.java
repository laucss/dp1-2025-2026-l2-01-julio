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

        // Then
        assertNull(userStatistics.getTotalVictories());
        assertNull(userStatistics.getMatchesPlayed());
        assertNull(userStatistics.getTotalTimePlayed());
        assertNull(userStatistics.getTotalActionPoints());
        assertNull(userStatistics.getBattlesWon());
        assertNull(userStatistics.getRoomsVisited());
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

        // Then
        assertEquals(100, userStatistics.getId());
        assertEquals(7, userStatistics.getTotalVictories());
        assertEquals(20, userStatistics.getMatchesPlayed());
        assertEquals(7200, userStatistics.getTotalTimePlayed());
        assertEquals(300, userStatistics.getTotalActionPoints());
        assertEquals(15, userStatistics.getBattlesWon());
        assertEquals(50, userStatistics.getRoomsVisited());
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

        // Then
        assertEquals(0, userStatistics.getTotalVictories());
        assertEquals(0, userStatistics.getMatchesPlayed());
        assertEquals(0, userStatistics.getTotalTimePlayed());
        assertEquals(0, userStatistics.getTotalActionPoints());
        assertEquals(0, userStatistics.getBattlesWon());
        assertEquals(0, userStatistics.getRoomsVisited());
    }

    @Test
    @DisplayName("Debe manejar valores grandes en los campos")
    void testLargeValues() {
        // When
        userStatistics.setTotalVictories(1000);
        userStatistics.setMatchesPlayed(5000);
        userStatistics.setTotalTimePlayed(999999);
        userStatistics.setTotalActionPoints(99999);
        userStatistics.setBattlesWon(10000);
        userStatistics.setRoomsVisited(500);

        // Then
        assertEquals(1000, userStatistics.getTotalVictories());
        assertEquals(5000, userStatistics.getMatchesPlayed());
        assertEquals(999999, userStatistics.getTotalTimePlayed());
        assertEquals(99999, userStatistics.getTotalActionPoints());
        assertEquals(10000, userStatistics.getBattlesWon());
        assertEquals(500, userStatistics.getRoomsVisited());
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
