package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CardsUpdateDTO Tests")
public class CardsUpdateDTOTest {

    private CardsUpdateDTO dto;
    private AllCardsStatusDTO winnerCards;
    private AllCardsStatusDTO loserCards;

    @BeforeEach
    void setUp() {
        dto = new CardsUpdateDTO();
        winnerCards = new AllCardsStatusDTO();
        loserCards = new AllCardsStatusDTO();
    }

    @Test
    @DisplayName("Should create empty CardsUpdateDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getWinner()).isNull();
        assertThat(dto.getLoser()).isNull();
    }

    @Test
    @DisplayName("Should create CardsUpdateDTO with all fields via constructor")
    void testConstructorWithAllFields() {
        CardsUpdateDTO dto = new CardsUpdateDTO(1, winnerCards, loserCards);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getWinner()).isEqualTo(winnerCards);
        assertThat(dto.getLoser()).isEqualTo(loserCards);
    }

    @Test
    @DisplayName("Should set and get matchId correctly")
    void testSetAndGetMatchId() {
        dto.setMatchId(5);
        assertThat(dto.getMatchId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should set and get winner correctly")
    void testSetAndGetWinner() {
        AllCardsStatusDTO winner = new AllCardsStatusDTO();
        dto.setWinner(winner);
        assertThat(dto.getWinner()).isEqualTo(winner);
    }

    @Test
    @DisplayName("Should set and get loser correctly")
    void testSetAndGetLoser() {
        AllCardsStatusDTO loser = new AllCardsStatusDTO();
        dto.setLoser(loser);
        assertThat(dto.getLoser()).isEqualTo(loser);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setMatchId(1);
        dto.setWinner(winnerCards);
        dto.setLoser(loserCards);

        dto.setMatchId(null);
        dto.setWinner(null);
        dto.setLoser(null);

        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getWinner()).isNull();
        assertThat(dto.getLoser()).isNull();
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        AllCardsStatusDTO winner1 = new AllCardsStatusDTO();
        AllCardsStatusDTO loser1 = new AllCardsStatusDTO();
        AllCardsStatusDTO winner2 = new AllCardsStatusDTO();
        AllCardsStatusDTO loser2 = new AllCardsStatusDTO();

        dto.setMatchId(1);
        dto.setWinner(winner1);
        dto.setLoser(loser1);

        assertThat(dto.getMatchId()).isEqualTo(1);
        assertThat(dto.getWinner()).isEqualTo(winner1);

        dto.setMatchId(2);
        dto.setWinner(winner2);
        dto.setLoser(loser2);

        assertThat(dto.getMatchId()).isEqualTo(2);
        assertThat(dto.getWinner()).isEqualTo(winner2);
        assertThat(dto.getLoser()).isEqualTo(loser2);
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        AllCardsStatusDTO winner1 = new AllCardsStatusDTO();
        AllCardsStatusDTO loser1 = new AllCardsStatusDTO();
        AllCardsStatusDTO winner2 = new AllCardsStatusDTO();
        AllCardsStatusDTO loser2 = new AllCardsStatusDTO();

        CardsUpdateDTO dto1 = new CardsUpdateDTO(1, winner1, loser1);
        CardsUpdateDTO dto2 = new CardsUpdateDTO(2, winner2, loser2);

        assertThat(dto1.getMatchId()).isNotEqualTo(dto2.getMatchId());
        assertThat(dto1.getWinner()).isNotEqualTo(dto2.getWinner());
        assertThat(dto1.getLoser()).isNotEqualTo(dto2.getLoser());
    }

    @Test
    @DisplayName("Should handle large matchId values")
    void testLargeMatchId() {
        dto.setMatchId(Integer.MAX_VALUE);
        assertThat(dto.getMatchId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle zero matchId")
    void testZeroMatchId() {
        dto.setMatchId(0);
        assertThat(dto.getMatchId()).isZero();
    }

    @Test
    @DisplayName("Should handle negative matchId values")
    void testNegativeMatchId() {
        dto.setMatchId(-1);
        assertThat(dto.getMatchId()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should support fight result cards update")
    void testFightResultCardsUpdate() {
        AllCardsStatusDTO winner = new AllCardsStatusDTO();
        AllCardsStatusDTO loser = new AllCardsStatusDTO();

        CardsUpdateDTO dto = new CardsUpdateDTO(1, winner, loser);

        assertThat(dto.getWinner()).isNotNull();
        assertThat(dto.getLoser()).isNotNull();
        assertThat(dto.getWinner()).isNotEqualTo(dto.getLoser());
    }
}
