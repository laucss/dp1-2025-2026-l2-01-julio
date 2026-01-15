package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GeneralStatisticsDTO Tests")
class GeneralStatisticsDTOTest {

    private GeneralStatisticsDTO generalStatistics;

    @BeforeEach
    void setUp() {
        generalStatistics = new GeneralStatisticsDTO();
    }

    @Test
    @DisplayName("Debe crear GeneralStatisticsDTO con constructor por defecto")
    void testDefaultConstructor() {
        // When
        GeneralStatisticsDTO dto = new GeneralStatisticsDTO();

        // Then
        assertNotNull(dto);
        assertNull(dto.getAveragePlayersPerMatch());
        assertNull(dto.getTotalMatchesPlayed());
        assertNull(dto.getTotalBattlesDisputed());
        assertNull(dto.getAverageRoomsVisitedPerMatch());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener averagePlayersPerMatch")
    void testSetAndGetAveragePlayersPerMatch() {
        // When
        generalStatistics.setAveragePlayersPerMatch(4.5);

        // Then
        assertEquals(4.5, generalStatistics.getAveragePlayersPerMatch());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener totalMatchesPlayed")
    void testSetAndGetTotalMatchesPlayed() {
        // When
        generalStatistics.setTotalMatchesPlayed(100);

        // Then
        assertEquals(100, generalStatistics.getTotalMatchesPlayed());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener totalBattlesDisputed")
    void testSetAndGetTotalBattlesDisputed() {
        // When
        generalStatistics.setTotalBattlesDisputed(250);

        // Then
        assertEquals(250, generalStatistics.getTotalBattlesDisputed());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener averageRoomsVisitedPerMatch")
    void testSetAndGetAverageRoomsVisitedPerMatch() {
        // When
        generalStatistics.setAverageRoomsVisitedPerMatch(5.75);

        // Then
        assertEquals(5.75, generalStatistics.getAverageRoomsVisitedPerMatch());
    }

    @Test
    @DisplayName("Debe permitir establecer id heredado de BaseEntity")
    void testSetAndGetId() {
        // When
        generalStatistics.setId(1);

        // Then
        assertEquals(1, generalStatistics.getId());
    }

    @Test
    @DisplayName("Debe manejar valores null en todos los campos")
    void testNullValues() {
        // When
        generalStatistics.setAveragePlayersPerMatch(null);
        generalStatistics.setTotalMatchesPlayed(null);
        generalStatistics.setTotalBattlesDisputed(null);
        generalStatistics.setAverageRoomsVisitedPerMatch(null);

        // Then
        assertNull(generalStatistics.getAveragePlayersPerMatch());
        assertNull(generalStatistics.getTotalMatchesPlayed());
        assertNull(generalStatistics.getTotalBattlesDisputed());
        assertNull(generalStatistics.getAverageRoomsVisitedPerMatch());
    }

    @Test
    @DisplayName("Debe mantener coherencia entre getters y setters")
    void testGettersAndSettersConsistency() {
        // When
        generalStatistics.setId(1);
        generalStatistics.setAveragePlayersPerMatch(4.8);
        generalStatistics.setTotalMatchesPlayed(150);
        generalStatistics.setTotalBattlesDisputed(350);
        generalStatistics.setAverageRoomsVisitedPerMatch(6.2);

        // Then
        assertEquals(1, generalStatistics.getId());
        assertEquals(4.8, generalStatistics.getAveragePlayersPerMatch());
        assertEquals(150, generalStatistics.getTotalMatchesPlayed());
        assertEquals(350, generalStatistics.getTotalBattlesDisputed());
        assertEquals(6.2, generalStatistics.getAverageRoomsVisitedPerMatch());
    }

    @Test
    @DisplayName("Debe permitir modificar valores múltiples veces")
    void testMultipleModifications() {
        // First modification
        generalStatistics.setAveragePlayersPerMatch(4.0);
        assertEquals(4.0, generalStatistics.getAveragePlayersPerMatch());

        // Second modification
        generalStatistics.setAveragePlayersPerMatch(5.0);
        assertEquals(5.0, generalStatistics.getAveragePlayersPerMatch());

        // Third modification
        generalStatistics.setAveragePlayersPerMatch(4.5);
        assertEquals(4.5, generalStatistics.getAveragePlayersPerMatch());
    }

    @Test
    @DisplayName("Debe manejar valores decimales precisos")
    void testPreciseDecimalValues() {
        // When
        generalStatistics.setAveragePlayersPerMatch(4.123456);
        generalStatistics.setAverageRoomsVisitedPerMatch(5.987654);

        // Then
        assertEquals(4.123456, generalStatistics.getAveragePlayersPerMatch());
        assertEquals(5.987654, generalStatistics.getAverageRoomsVisitedPerMatch());
    }

    @Test
    @DisplayName("Debe manejar valores enteros en campos Double")
    void testIntegerValuesInDoubleFields() {
        // When
        generalStatistics.setAveragePlayersPerMatch(4.0);
        generalStatistics.setAverageRoomsVisitedPerMatch(6.0);

        // Then
        assertEquals(4.0, generalStatistics.getAveragePlayersPerMatch());
        assertEquals(6.0, generalStatistics.getAverageRoomsVisitedPerMatch());
    }

    @Test
    @DisplayName("Debe manejar valores cero en campos numéricos")
    void testZeroValues() {
        // When
        generalStatistics.setAveragePlayersPerMatch(0.0);
        generalStatistics.setTotalMatchesPlayed(0);
        generalStatistics.setTotalBattlesDisputed(0);
        generalStatistics.setAverageRoomsVisitedPerMatch(0.0);

        // Then
        assertEquals(0.0, generalStatistics.getAveragePlayersPerMatch());
        assertEquals(0, generalStatistics.getTotalMatchesPlayed());
        assertEquals(0, generalStatistics.getTotalBattlesDisputed());
        assertEquals(0.0, generalStatistics.getAverageRoomsVisitedPerMatch());
    }

    @Test
    @DisplayName("Debe manejar valores grandes en los campos")
    void testLargeValues() {
        // When
        generalStatistics.setAveragePlayersPerMatch(999.99);
        generalStatistics.setTotalMatchesPlayed(100000);
        generalStatistics.setTotalBattlesDisputed(999999);
        generalStatistics.setAverageRoomsVisitedPerMatch(999.99);

        // Then
        assertEquals(999.99, generalStatistics.getAveragePlayersPerMatch());
        assertEquals(100000, generalStatistics.getTotalMatchesPlayed());
        assertEquals(999999, generalStatistics.getTotalBattlesDisputed());
        assertEquals(999.99, generalStatistics.getAverageRoomsVisitedPerMatch());
    }

    @Test
    @DisplayName("Debe manejar valores negativos en campos")
    void testNegativeValues() {
        // When
        generalStatistics.setAveragePlayersPerMatch(-1.5);
        generalStatistics.setTotalMatchesPlayed(-10);
        generalStatistics.setTotalBattlesDisputed(-50);
        generalStatistics.setAverageRoomsVisitedPerMatch(-2.5);

        // Then
        assertEquals(-1.5, generalStatistics.getAveragePlayersPerMatch());
        assertEquals(-10, generalStatistics.getTotalMatchesPlayed());
        assertEquals(-50, generalStatistics.getTotalBattlesDisputed());
        assertEquals(-2.5, generalStatistics.getAverageRoomsVisitedPerMatch());
    }

    @Test
    @DisplayName("Debe crear instancias independientes")
    void testIndependentInstances() {
        // Given
        GeneralStatisticsDTO dto1 = new GeneralStatisticsDTO();
        GeneralStatisticsDTO dto2 = new GeneralStatisticsDTO();

        // When
        dto1.setAveragePlayersPerMatch(4.0);
        dto2.setAveragePlayersPerMatch(5.0);

        // Then
        assertEquals(4.0, dto1.getAveragePlayersPerMatch());
        assertEquals(5.0, dto2.getAveragePlayersPerMatch());
        assertNotEquals(dto1.getAveragePlayersPerMatch(), dto2.getAveragePlayersPerMatch());
    }

    @Test
    @DisplayName("Debe mantener independencia entre campos al modificar")
    void testFieldIndependence() {
        // When
        generalStatistics.setAveragePlayersPerMatch(4.5);
        generalStatistics.setTotalMatchesPlayed(100);
        generalStatistics.setTotalBattlesDisputed(250);
        generalStatistics.setAverageRoomsVisitedPerMatch(5.75);

        // Modifying one field should not affect others
        generalStatistics.setAveragePlayersPerMatch(5.0);

        // Then
        assertEquals(5.0, generalStatistics.getAveragePlayersPerMatch());
        assertEquals(100, generalStatistics.getTotalMatchesPlayed());
        assertEquals(250, generalStatistics.getTotalBattlesDisputed());
        assertEquals(5.75, generalStatistics.getAverageRoomsVisitedPerMatch());
    }
}
