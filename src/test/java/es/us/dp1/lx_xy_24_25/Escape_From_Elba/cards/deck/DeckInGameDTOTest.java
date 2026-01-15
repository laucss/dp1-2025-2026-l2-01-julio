package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DeckInGameDTO Tests")
public class DeckInGameDTOTest {

    private DeckInGameDTO dto;
    private DeckInGame testDeck;

    @BeforeEach
    void setUp() {
        testDeck = new DeckInGame();
        dto = new DeckInGameDTO();
    }

    @Test
    @DisplayName("Should create empty DeckInGameDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getNotDiscardedCards()).isNull();
        assertThat(dto.getDiscardedCards()).isNull();
    }

    @Test
    @DisplayName("Should create DeckInGameDTO from empty DeckInGame")
    void testConstructorWithEmptyDeck() {
        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        assertThat(dto.getNotDiscardedCards()).isNotNull();
        assertThat(dto.getDiscardedCards()).isNotNull();
        assertThat(dto.getNotDiscardedCards()).isEmpty();
        assertThat(dto.getDiscardedCards()).isEmpty();
    }

    @Test
    @DisplayName("Should create DeckInGameDTO from DeckInGame with notDiscardedCards")
    void testConstructorWithNotDiscardedCards() {
        Card card1 = new Card();
        card1.setId(1);
        card1.setLetter("E");

        Card card2 = new Card();
        card2.setId(2);
        card2.setLetter("L");

        testDeck.getNotDiscardedCards().add(card1);
        testDeck.getNotDiscardedCards().add(card2);

        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        assertThat(dto.getNotDiscardedCards()).hasSize(2);
        assertThat(dto.getDiscardedCards()).isEmpty();
        assertThat(dto.getNotDiscardedCards().get(0).getId()).isEqualTo(1);
        assertThat(dto.getNotDiscardedCards().get(1).getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should create DeckInGameDTO from DeckInGame with discardedCards")
    void testConstructorWithDiscardedCards() {
        Card card1 = new Card();
        card1.setId(3);
        card1.setLetter("B");

        Card card2 = new Card();
        card2.setId(4);
        card2.setLetter("A");

        testDeck.getDiscardedCards().add(card1);
        testDeck.getDiscardedCards().add(card2);

        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        assertThat(dto.getDiscardedCards()).hasSize(2);
        assertThat(dto.getNotDiscardedCards()).isEmpty();
        assertThat(dto.getDiscardedCards().get(0).getId()).isEqualTo(3);
        assertThat(dto.getDiscardedCards().get(1).getId()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should create DeckInGameDTO with both notDiscarded and discarded cards")
    void testConstructorWithBothCardTypes() {
        Card notDiscarded1 = new Card();
        notDiscarded1.setId(1);
        notDiscarded1.setLetter("E");

        Card notDiscarded2 = new Card();
        notDiscarded2.setId(2);
        notDiscarded2.setLetter("S");

        Card discarded1 = new Card();
        discarded1.setId(3);
        discarded1.setLetter("C");

        testDeck.getNotDiscardedCards().add(notDiscarded1);
        testDeck.getNotDiscardedCards().add(notDiscarded2);
        testDeck.getDiscardedCards().add(discarded1);

        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        assertThat(dto.getNotDiscardedCards()).hasSize(2);
        assertThat(dto.getDiscardedCards()).hasSize(1);
    }

    @Test
    @DisplayName("Should convert Card entities to CardDTO objects")
    void testCardConversion() {
        Card card = new Card();
        card.setId(10);
        card.setLetter("P");
        card.setFrontImage("plan.png");
        card.setBackImage("back_plan.png");

        testDeck.getNotDiscardedCards().add(card);

        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        CardDTO cardDTO = dto.getNotDiscardedCards().get(0);
        assertThat(cardDTO.getId()).isEqualTo(10);
        assertThat(cardDTO.getLetter()).isEqualTo("P");
        assertThat(cardDTO.getFrontImage()).isEqualTo("plan.png");
        assertThat(cardDTO.getBackImage()).isEqualTo("back_plan.png");
    }

    @Test
    @DisplayName("Should set and get notDiscardedCards correctly")
    void testSetAndGetNotDiscardedCards() {
        List<CardDTO> cards = new ArrayList<>();
        CardDTO card = new CardDTO();
        card.setId(1);
        cards.add(card);

        dto.setNotDiscardedCards(cards);
        assertThat(dto.getNotDiscardedCards()).isEqualTo(cards);
    }

    @Test
    @DisplayName("Should set and get discardedCards correctly")
    void testSetAndGetDiscardedCards() {
        List<CardDTO> cards = new ArrayList<>();
        CardDTO card = new CardDTO();
        card.setId(2);
        cards.add(card);

        dto.setDiscardedCards(cards);
        assertThat(dto.getDiscardedCards()).isEqualTo(cards);
    }

    @Test
    @DisplayName("Should handle null notDiscardedCards correctly")
    void testHandleNullNotDiscardedCards() {
        dto.setNotDiscardedCards(null);
        assertThat(dto.getNotDiscardedCards()).isNull();
    }

    @Test
    @DisplayName("Should handle null discardedCards correctly")
    void testHandleNullDiscardedCards() {
        dto.setDiscardedCards(null);
        assertThat(dto.getDiscardedCards()).isNull();
    }

    @Test
    @DisplayName("Should update notDiscardedCards from non-null to different value")
    void testUpdateNotDiscardedCards() {
        List<CardDTO> cards1 = new ArrayList<>();
        CardDTO card1 = new CardDTO();
        card1.setId(1);
        cards1.add(card1);

        dto.setNotDiscardedCards(cards1);
        assertThat(dto.getNotDiscardedCards()).hasSize(1);

        List<CardDTO> cards2 = new ArrayList<>();
        CardDTO card2 = new CardDTO();
        card2.setId(2);
        CardDTO card3 = new CardDTO();
        card3.setId(3);
        cards2.add(card2);
        cards2.add(card3);

        dto.setNotDiscardedCards(cards2);
        assertThat(dto.getNotDiscardedCards()).hasSize(2);
    }

    @Test
    @DisplayName("Should update discardedCards from non-null to different value")
    void testUpdateDiscardedCards() {
        List<CardDTO> cards1 = new ArrayList<>();
        CardDTO card1 = new CardDTO();
        card1.setId(1);
        cards1.add(card1);

        dto.setDiscardedCards(cards1);
        assertThat(dto.getDiscardedCards()).hasSize(1);

        List<CardDTO> cards2 = new ArrayList<>();
        CardDTO card2 = new CardDTO();
        card2.setId(2);
        cards2.add(card2);

        dto.setDiscardedCards(cards2);
        assertThat(dto.getDiscardedCards()).hasSize(1);
        assertThat(dto.getDiscardedCards().get(0).getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should handle empty notDiscardedCards list")
    void testEmptyNotDiscardedCardsList() {
        dto.setNotDiscardedCards(new ArrayList<>());
        assertThat(dto.getNotDiscardedCards()).isEmpty();
    }

    @Test
    @DisplayName("Should handle empty discardedCards list")
    void testEmptyDiscardedCardsList() {
        dto.setDiscardedCards(new ArrayList<>());
        assertThat(dto.getDiscardedCards()).isEmpty();
    }

    @Test
    @DisplayName("Should create independent instances")
    void testIndependentInstances() {
        Card card1 = new Card();
        card1.setId(1);
        testDeck.getNotDiscardedCards().add(card1);

        DeckInGameDTO dto1 = new DeckInGameDTO(testDeck);

        Card card2 = new Card();
        card2.setId(2);
        DeckInGame deck2 = new DeckInGame();
        deck2.getNotDiscardedCards().add(card2);

        DeckInGameDTO dto2 = new DeckInGameDTO(deck2);

        assertThat(dto1.getNotDiscardedCards()).hasSize(1);
        assertThat(dto2.getNotDiscardedCards()).hasSize(1);
        assertThat(dto1.getNotDiscardedCards().get(0).getId()).isNotEqualTo(dto2.getNotDiscardedCards().get(0).getId());

        dto1.getNotDiscardedCards().add(new CardDTO());
        assertThat(dto2.getNotDiscardedCards()).hasSize(1);
    }

    @Test
    @DisplayName("Should maintain card order after conversion")
    void testCardOrderPreservation() {
        for (int i = 1; i <= 5; i++) {
            Card card = new Card();
            card.setId(i);
            card.setLetter("E");
            testDeck.getNotDiscardedCards().add(card);
        }

        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        for (int i = 0; i < 5; i++) {
            assertThat(dto.getNotDiscardedCards().get(i).getId()).isEqualTo(i + 1);
        }
    }

    @Test
    @DisplayName("Should handle large number of notDiscardedCards")
    void testLargeNumberOfNotDiscardedCards() {
        for (int i = 1; i <= 40; i++) {
            Card card = new Card();
            card.setId(i);
            card.setLetter("E");
            testDeck.getNotDiscardedCards().add(card);
        }

        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        assertThat(dto.getNotDiscardedCards()).hasSize(40);
    }

    @Test
    @DisplayName("Should handle large number of discardedCards")
    void testLargeNumberOfDiscardedCards() {
        for (int i = 1; i <= 20; i++) {
            Card card = new Card();
            card.setId(i + 100);
            card.setLetter("D");
            testDeck.getDiscardedCards().add(card);
        }

        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        assertThat(dto.getDiscardedCards()).hasSize(20);
    }

    @Test
    @DisplayName("Should create new lists on conversion, not reference originals")
    void testListsNotReferenced() {
        Card card = new Card();
        card.setId(1);
        testDeck.getNotDiscardedCards().add(card);

        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        assertThat(dto.getNotDiscardedCards()).isNotSameAs(testDeck.getNotDiscardedCards());
        assertThat(dto.getDiscardedCards()).isNotSameAs(testDeck.getDiscardedCards());
    }

    @Test
    @DisplayName("Should handle escape letters in notDiscardedCards")
    void testEscapeLettersInNotDiscardedCards() {
        String[] letters = {"E", "S", "C", "A", "P", "L", "B", "I"};
        
        for (int i = 0; i < letters.length; i++) {
            Card card = new Card();
            card.setId(i + 1);
            card.setLetter(letters[i]);
            testDeck.getNotDiscardedCards().add(card);
        }

        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        assertThat(dto.getNotDiscardedCards()).hasSize(8);
        for (int i = 0; i < letters.length; i++) {
            assertThat(dto.getNotDiscardedCards().get(i).getLetter()).isEqualTo(letters[i]);
        }
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

        testDeck.getNotDiscardedCards().add(card1);
        testDeck.getDiscardedCards().add(card2);

        DeckInGameDTO dto = new DeckInGameDTO(testDeck);

        assertThat(dto.getNotDiscardedCards().get(0).getId()).isEqualTo(10);
        assertThat(dto.getNotDiscardedCards().get(0).getLetter()).isEqualTo("C");
        assertThat(dto.getDiscardedCards().get(0).getId()).isEqualTo(11);
        assertThat(dto.getDiscardedCards().get(0).getLetter()).isEqualTo("A");
    }

    @Test
    @DisplayName("Should add cards to notDiscardedCards after creation")
    void testAddNotDiscardedCardsAfterCreation() {
        dto.setNotDiscardedCards(new ArrayList<>());

        CardDTO card1 = new CardDTO();
        card1.setId(1);
        dto.getNotDiscardedCards().add(card1);

        assertThat(dto.getNotDiscardedCards()).hasSize(1);

        CardDTO card2 = new CardDTO();
        card2.setId(2);
        dto.getNotDiscardedCards().add(card2);

        assertThat(dto.getNotDiscardedCards()).hasSize(2);
    }

    @Test
    @DisplayName("Should add cards to discardedCards after creation")
    void testAddDiscardedCardsAfterCreation() {
        dto.setDiscardedCards(new ArrayList<>());

        CardDTO card1 = new CardDTO();
        card1.setId(1);
        dto.getDiscardedCards().add(card1);

        assertThat(dto.getDiscardedCards()).hasSize(1);

        CardDTO card2 = new CardDTO();
        card2.setId(2);
        dto.getDiscardedCards().add(card2);

        assertThat(dto.getDiscardedCards()).hasSize(2);
    }

    @Test
    @DisplayName("Should clear notDiscardedCards list")
    void testClearNotDiscardedCardsList() {
        List<CardDTO> cards = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            CardDTO card = new CardDTO();
            card.setId(i);
            cards.add(card);
        }
        dto.setNotDiscardedCards(cards);

        assertThat(dto.getNotDiscardedCards()).hasSize(3);

        dto.getNotDiscardedCards().clear();

        assertThat(dto.getNotDiscardedCards()).isEmpty();
    }

    @Test
    @DisplayName("Should clear discardedCards list")
    void testClearDiscardedCardsList() {
        List<CardDTO> cards = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            CardDTO card = new CardDTO();
            card.setId(i);
            cards.add(card);
        }
        dto.setDiscardedCards(cards);

        assertThat(dto.getDiscardedCards()).hasSize(2);

        dto.getDiscardedCards().clear();

        assertThat(dto.getDiscardedCards()).isEmpty();
    }

    @Test
    @DisplayName("Should remove specific card from notDiscardedCards")
    void testRemoveNotDiscardedCard() {
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
        dto.setNotDiscardedCards(cards);

        assertThat(dto.getNotDiscardedCards()).hasSize(3);

        dto.getNotDiscardedCards().remove(1);

        assertThat(dto.getNotDiscardedCards()).hasSize(2);
        assertThat(dto.getNotDiscardedCards().get(0).getId()).isEqualTo(1);
        assertThat(dto.getNotDiscardedCards().get(1).getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should remove specific card from discardedCards")
    void testRemoveDiscardedCard() {
        List<CardDTO> cards = new ArrayList<>();
        CardDTO card1 = new CardDTO();
        card1.setId(1);
        CardDTO card2 = new CardDTO();
        card2.setId(2);

        cards.add(card1);
        cards.add(card2);
        dto.setDiscardedCards(cards);

        assertThat(dto.getDiscardedCards()).hasSize(2);

        dto.getDiscardedCards().remove(0);

        assertThat(dto.getDiscardedCards()).hasSize(1);
        assertThat(dto.getDiscardedCards().get(0).getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should move card from notDiscarded to discarded")
    void testMoveCardFromNotDiscardedToDiscarded() {
        CardDTO card = new CardDTO();
        card.setId(1);
        card.setLetter("E");

        dto.setNotDiscardedCards(new ArrayList<>());
        dto.setDiscardedCards(new ArrayList<>());
        
        dto.getNotDiscardedCards().add(card);
        assertThat(dto.getNotDiscardedCards()).hasSize(1);
        assertThat(dto.getDiscardedCards()).isEmpty();

        dto.getNotDiscardedCards().remove(0);
        dto.getDiscardedCards().add(card);

        assertThat(dto.getNotDiscardedCards()).isEmpty();
        assertThat(dto.getDiscardedCards()).hasSize(1);
    }
}
