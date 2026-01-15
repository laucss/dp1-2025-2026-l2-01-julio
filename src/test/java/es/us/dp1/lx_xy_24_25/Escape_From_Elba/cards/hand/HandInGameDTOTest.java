package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("HandInGameDTO Tests")
public class HandInGameDTOTest {

    private HandInGameDTO dto;
    private HandInGame testHand;

    @BeforeEach
    void setUp() {
        testHand = new HandInGame();
        dto = new HandInGameDTO();
    }

    @Test
    @DisplayName("Should create empty HandInGameDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getCards()).isNull();
    }

    @Test
    @DisplayName("Should create HandInGameDTO from empty HandInGame")
    void testConstructorWithEmptyHand() {
        HandInGameDTO dto = new HandInGameDTO(testHand);

        assertThat(dto.getCards()).isNotNull();
        assertThat(dto.getCards()).isEmpty();
    }

    @Test
    @DisplayName("Should create HandInGameDTO from HandInGame with single card")
    void testConstructorWithSingleCard() {
        Card card = new Card();
        card.setId(1);
        card.setLetter("E");
        card.setFrontImage("front.png");
        card.setBackImage("back.png");
        testHand.getCards().add(card);

        HandInGameDTO dto = new HandInGameDTO(testHand);

        assertThat(dto.getCards()).hasSize(1);
        assertThat(dto.getCards().get(0)).isInstanceOf(CardDTO.class);
        assertThat(dto.getCards().get(0).getId()).isEqualTo(1);
        assertThat(dto.getCards().get(0).getLetter()).isEqualTo("E");
    }

    @Test
    @DisplayName("Should create HandInGameDTO from HandInGame with multiple cards")
    void testConstructorWithMultipleCards() {
        Card card1 = new Card();
        card1.setId(1);
        card1.setLetter("E");

        Card card2 = new Card();
        card2.setId(2);
        card2.setLetter("L");

        Card card3 = new Card();
        card3.setId(3);
        card3.setLetter("B");

        testHand.getCards().add(card1);
        testHand.getCards().add(card2);
        testHand.getCards().add(card3);

        HandInGameDTO dto = new HandInGameDTO(testHand);

        assertThat(dto.getCards()).hasSize(3);
        assertThat(dto.getCards().get(0).getId()).isEqualTo(1);
        assertThat(dto.getCards().get(1).getId()).isEqualTo(2);
        assertThat(dto.getCards().get(2).getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should convert Card entities to CardDTO objects")
    void testCardConversion() {
        Card card = new Card();
        card.setId(5);
        card.setLetter("S");
        card.setFrontImage("escape.png");
        card.setBackImage("back_escape.png");
        testHand.getCards().add(card);

        HandInGameDTO dto = new HandInGameDTO(testHand);

        CardDTO cardDTO = dto.getCards().get(0);
        assertThat(cardDTO.getId()).isEqualTo(5);
        assertThat(cardDTO.getLetter()).isEqualTo("S");
        assertThat(cardDTO.getFrontImage()).isEqualTo("escape.png");
        assertThat(cardDTO.getBackImage()).isEqualTo("back_escape.png");
    }

    @Test
    @DisplayName("Should set and get cards correctly")
    void testSetAndGetCards() {
        List<CardDTO> cards = new ArrayList<>();
        CardDTO card1 = new CardDTO();
        card1.setId(1);
        cards.add(card1);

        dto.setCards(cards);
        assertThat(dto.getCards()).isEqualTo(cards);
        assertThat(dto.getCards()).hasSize(1);
    }

    @Test
    @DisplayName("Should handle null cards list correctly")
    void testHandleNullCards() {
        dto.setCards(null);
        assertThat(dto.getCards()).isNull();
    }

    @Test
    @DisplayName("Should update cards from non-null to different value")
    void testUpdateCards() {
        List<CardDTO> cards1 = new ArrayList<>();
        CardDTO card1 = new CardDTO();
        card1.setId(1);
        cards1.add(card1);

        dto.setCards(cards1);
        assertThat(dto.getCards()).hasSize(1);

        List<CardDTO> cards2 = new ArrayList<>();
        CardDTO card2 = new CardDTO();
        card2.setId(2);
        CardDTO card3 = new CardDTO();
        card3.setId(3);
        cards2.add(card2);
        cards2.add(card3);

        dto.setCards(cards2);
        assertThat(dto.getCards()).hasSize(2);
        assertThat(dto.getCards().get(0).getId()).isEqualTo(2);
        assertThat(dto.getCards().get(1).getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should handle empty cards list")
    void testEmptyCardsList() {
        dto.setCards(new ArrayList<>());
        assertThat(dto.getCards()).isEmpty();
    }

    @Test
    @DisplayName("Should create independent instances")
    void testIndependentInstances() {
        Card card1 = new Card();
        card1.setId(1);
        testHand.getCards().add(card1);

        HandInGameDTO dto1 = new HandInGameDTO(testHand);

        Card card2 = new Card();
        card2.setId(2);
        HandInGame hand2 = new HandInGame();
        hand2.getCards().add(card2);

        HandInGameDTO dto2 = new HandInGameDTO(hand2);

        assertThat(dto1.getCards()).hasSize(1);
        assertThat(dto2.getCards()).hasSize(1);
        assertThat(dto1.getCards().get(0).getId()).isNotEqualTo(dto2.getCards().get(0).getId());

        dto1.getCards().add(new CardDTO());
        assertThat(dto2.getCards()).hasSize(1);
    }

    @Test
    @DisplayName("Should handle hand with escape letters")
    void testEscapeLettersInHand() {
        String[] letters = {"E", "S", "C", "A", "P", "L", "B", "I"};
        
        for (int i = 0; i < letters.length; i++) {
            Card card = new Card();
            card.setId(i + 1);
            card.setLetter(letters[i]);
            testHand.getCards().add(card);
        }

        HandInGameDTO dto = new HandInGameDTO(testHand);

        assertThat(dto.getCards()).hasSize(8);
        for (int i = 0; i < letters.length; i++) {
            assertThat(dto.getCards().get(i).getLetter()).isEqualTo(letters[i]);
        }
    }

    @Test
    @DisplayName("Should maintain card order after conversion")
    void testCardOrderPreservation() {
        for (int i = 1; i <= 5; i++) {
            Card card = new Card();
            card.setId(i);
            card.setLetter("E");
            testHand.getCards().add(card);
        }

        HandInGameDTO dto = new HandInGameDTO(testHand);

        for (int i = 0; i < 5; i++) {
            assertThat(dto.getCards().get(i).getId()).isEqualTo(i + 1);
        }
    }

    @Test
    @DisplayName("Should handle large number of cards")
    void testLargeNumberOfCards() {
        for (int i = 1; i <= 50; i++) {
            Card card = new Card();
            card.setId(i);
            card.setLetter("E");
            testHand.getCards().add(card);
        }

        HandInGameDTO dto = new HandInGameDTO(testHand);

        assertThat(dto.getCards()).hasSize(50);
    }

    @Test
    @DisplayName("Should create new list on conversion, not reference original")
    void testListNotReferenced() {
        Card card = new Card();
        card.setId(1);
        testHand.getCards().add(card);

        HandInGameDTO dto = new HandInGameDTO(testHand);

        assertThat(dto.getCards()).isNotSameAs(testHand.getCards());
    }

    @Test
    @DisplayName("Should handle cards with full information")
    void testCardsWithFullInformation() {
        Card card1 = new Card();
        card1.setId(10);
        card1.setLetter("C");
        card1.setFrontImage("path/to/card10.png");
        card1.setBackImage("path/to/back.png");

        Card card2 = new Card();
        card2.setId(11);
        card2.setLetter("A");
        card2.setFrontImage("path/to/card11.png");
        card2.setBackImage("path/to/back.png");

        testHand.getCards().add(card1);
        testHand.getCards().add(card2);

        HandInGameDTO dto = new HandInGameDTO(testHand);

        assertThat(dto.getCards()).hasSize(2);
        assertThat(dto.getCards().get(0).getId()).isEqualTo(10);
        assertThat(dto.getCards().get(0).getLetter()).isEqualTo("C");
        assertThat(dto.getCards().get(1).getId()).isEqualTo(11);
        assertThat(dto.getCards().get(1).getLetter()).isEqualTo("A");
    }

    @Test
    @DisplayName("Should add cards to list after creation")
    void testAddCardsAfterCreation() {
        dto.setCards(new ArrayList<>());

        CardDTO card1 = new CardDTO();
        card1.setId(1);
        dto.getCards().add(card1);

        assertThat(dto.getCards()).hasSize(1);

        CardDTO card2 = new CardDTO();
        card2.setId(2);
        dto.getCards().add(card2);

        assertThat(dto.getCards()).hasSize(2);
    }

    @Test
    @DisplayName("Should clear cards list")
    void testClearCardsList() {
        List<CardDTO> cards = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            CardDTO card = new CardDTO();
            card.setId(i);
            cards.add(card);
        }
        dto.setCards(cards);

        assertThat(dto.getCards()).hasSize(3);

        dto.getCards().clear();

        assertThat(dto.getCards()).isEmpty();
    }

    @Test
    @DisplayName("Should remove specific card from list")
    void testRemoveCard() {
        List<CardDTO> cards = new ArrayList<>();
        CardDTO card1 = new CardDTO();
        card1.setId(1);
        CardDTO card2 = new CardDTO();
        card2.setId(2);
        CardDTO card3 = new CardDTO();
        card3.setId(3);

        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        dto.setCards(cards);

        assertThat(dto.getCards()).hasSize(3);

        dto.getCards().remove(1);

        assertThat(dto.getCards()).hasSize(2);
        assertThat(dto.getCards().get(0).getId()).isEqualTo(1);
        assertThat(dto.getCards().get(1).getId()).isEqualTo(3);
    }
}
