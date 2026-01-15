package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BagInGameDTO Tests")
public class BagInGameDTOTest {

    private BagInGameDTO dto;
    private BagInGame testBag;

    @BeforeEach
    void setUp() {
        testBag = new BagInGame();
        dto = new BagInGameDTO();
    }

    @Test
    @DisplayName("Should create empty BagInGameDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getCards()).isNull();
    }

    @Test
    @DisplayName("Should create BagInGameDTO from empty BagInGame")
    void testConstructorWithEmptyBag() {
        BagInGameDTO dto = new BagInGameDTO(testBag);

        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getCards()).isNotNull();
        assertThat(dto.getCards()).isEmpty();
    }

    @Test
    @DisplayName("Should create BagInGameDTO from BagInGame with cards")
    void testConstructorWithCards() {
        Card card1 = new Card();
        card1.setId(1);
        card1.setLetter("E");

        Card card2 = new Card();
        card2.setId(2);
        card2.setLetter("S");

        testBag.getCards().add(card1);
        testBag.getCards().add(card2);

        BagInGameDTO dto = new BagInGameDTO(testBag);

        assertThat(dto.getCards()).hasSize(2);
        assertThat(dto.getCards().get(0).getId()).isEqualTo(1);
        assertThat(dto.getCards().get(1).getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should create BagInGameDTO from BagInGame with playerId")
    void testConstructorWithPlayerId() {
        Integer playerId = 5;
        BagInGameDTO dto = new BagInGameDTO(testBag, playerId);

        assertThat(dto.getPlayerId()).isEqualTo(5);
        assertThat(dto.getCards()).isNotNull();
        assertThat(dto.getCards()).isEmpty();
    }

    @Test
    @DisplayName("Should create BagInGameDTO with both playerId and cards")
    void testConstructorWithPlayerIdAndCards() {
        Card card1 = new Card();
        card1.setId(1);
        card1.setLetter("C");

        testBag.getCards().add(card1);

        BagInGameDTO dto = new BagInGameDTO(testBag, 10);

        assertThat(dto.getPlayerId()).isEqualTo(10);
        assertThat(dto.getCards()).hasSize(1);
        assertThat(dto.getCards().get(0).getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should convert Card entities to CardDTO objects")
    void testCardConversion() {
        Card card = new Card();
        card.setId(15);
        card.setLetter("A");
        card.setFrontImage("adventure.png");
        card.setBackImage("back.png");

        testBag.getCards().add(card);

        BagInGameDTO dto = new BagInGameDTO(testBag);

        CardDTO cardDTO = dto.getCards().get(0);
        assertThat(cardDTO.getId()).isEqualTo(15);
        assertThat(cardDTO.getLetter()).isEqualTo("A");
        assertThat(cardDTO.getFrontImage()).isEqualTo("adventure.png");
        assertThat(cardDTO.getBackImage()).isEqualTo("back.png");
    }

    @Test
    @DisplayName("Should set and get playerId correctly")
    void testSetAndGetPlayerId() {
        dto.setPlayerId(7);
        assertThat(dto.getPlayerId()).isEqualTo(7);
    }

    @Test
    @DisplayName("Should set and get cards correctly")
    void testSetAndGetCards() {
        List<CardDTO> cards = new ArrayList<>();
        CardDTO card = new CardDTO();
        card.setId(1);
        cards.add(card);

        dto.setCards(cards);
        assertThat(dto.getCards()).isEqualTo(cards);
    }

    @Test
    @DisplayName("Should handle null playerId correctly")
    void testHandleNullPlayerId() {
        dto.setPlayerId(null);
        assertThat(dto.getPlayerId()).isNull();
    }

    @Test
    @DisplayName("Should handle null cards correctly")
    void testHandleNullCards() {
        dto.setCards(null);
        assertThat(dto.getCards()).isNull();
    }

    @Test
    @DisplayName("Should update playerId from non-null to different value")
    void testUpdatePlayerId() {
        dto.setPlayerId(1);
        assertThat(dto.getPlayerId()).isEqualTo(1);

        dto.setPlayerId(2);
        assertThat(dto.getPlayerId()).isEqualTo(2);
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
        testBag.getCards().add(card1);

        BagInGameDTO dto1 = new BagInGameDTO(testBag, 5);

        Card card2 = new Card();
        card2.setId(2);
        BagInGame bag2 = new BagInGame();
        bag2.getCards().add(card2);

        BagInGameDTO dto2 = new BagInGameDTO(bag2, 10);

        assertThat(dto1.getCards()).hasSize(1);
        assertThat(dto2.getCards()).hasSize(1);
        assertThat(dto1.getPlayerId()).isNotEqualTo(dto2.getPlayerId());

        dto1.getCards().add(new CardDTO());
        assertThat(dto2.getCards()).hasSize(1);
    }

    @Test
    @DisplayName("Should maintain card order after conversion")
    void testCardOrderPreservation() {
        for (int i = 1; i <= 5; i++) {
            Card card = new Card();
            card.setId(i);
            card.setLetter("E");
            testBag.getCards().add(card);
        }

        BagInGameDTO dto = new BagInGameDTO(testBag);

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
            testBag.getCards().add(card);
        }

        BagInGameDTO dto = new BagInGameDTO(testBag);

        assertThat(dto.getCards()).hasSize(50);
    }

    @Test
    @DisplayName("Should create new list on conversion, not reference original")
    void testListNotReferenced() {
        Card card = new Card();
        card.setId(1);
        testBag.getCards().add(card);

        BagInGameDTO dto = new BagInGameDTO(testBag);

        assertThat(dto.getCards()).isNotSameAs(testBag.getCards());
    }

    @Test
    @DisplayName("Should handle escape letters in cards")
    void testEscapeLettersInCards() {
        String[] letters = {"E", "S", "C", "A", "P", "L", "B", "I"};
        
        for (int i = 0; i < letters.length; i++) {
            Card card = new Card();
            card.setId(i + 1);
            card.setLetter(letters[i]);
            testBag.getCards().add(card);
        }

        BagInGameDTO dto = new BagInGameDTO(testBag);

        assertThat(dto.getCards()).hasSize(8);
        for (int i = 0; i < letters.length; i++) {
            assertThat(dto.getCards().get(i).getLetter()).isEqualTo(letters[i]);
        }
    }

    @Test
    @DisplayName("Should handle cards with full information")
    void testCardsWithFullInformation() {
        Card card1 = new Card();
        card1.setId(10);
        card1.setLetter("E");
        card1.setFrontImage("path/to/escape.png");
        card1.setBackImage("path/to/back.png");

        testBag.getCards().add(card1);

        BagInGameDTO dto = new BagInGameDTO(testBag, 3);

        assertThat(dto.getPlayerId()).isEqualTo(3);
        assertThat(dto.getCards().get(0).getId()).isEqualTo(10);
        assertThat(dto.getCards().get(0).getLetter()).isEqualTo("E");
        assertThat(dto.getCards().get(0).getFrontImage()).isEqualTo("path/to/escape.png");
    }

    @Test
    @DisplayName("Should add cards after creation")
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

    @Test
    @DisplayName("Should handle update playerId to zero")
    void testUpdatePlayerIdToZero() {
        dto.setPlayerId(5);
        assertThat(dto.getPlayerId()).isEqualTo(5);

        dto.setPlayerId(0);
        assertThat(dto.getPlayerId()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle large playerId value")
    void testLargePlayerIdValue() {
        dto.setPlayerId(999999);
        assertThat(dto.getPlayerId()).isEqualTo(999999);
    }

    @Test
    @DisplayName("Should handle multiple constructor variants sequentially")
    void testMultipleConstructorVariants() {
        // Using constructor without playerId
        BagInGameDTO dto1 = new BagInGameDTO(testBag);
        assertThat(dto1.getPlayerId()).isNull();

        // Using constructor with playerId
        BagInGameDTO dto2 = new BagInGameDTO(testBag, 5);
        assertThat(dto2.getPlayerId()).isEqualTo(5);

        // Both should have cards lists
        assertThat(dto1.getCards()).isNotNull();
        assertThat(dto2.getCards()).isNotNull();
    }

    @Test
    @DisplayName("Should transition from null cards to empty list to populated list")
    void testCardsListTransition() {
        assertThat(dto.getCards()).isNull();

        dto.setCards(new ArrayList<>());
        assertThat(dto.getCards()).isEmpty();

        CardDTO card = new CardDTO();
        card.setId(1);
        dto.getCards().add(card);

        assertThat(dto.getCards()).hasSize(1);
    }

    @Test
    @DisplayName("Should transition from null playerId to value to null")
    void testPlayerIdTransition() {
        assertThat(dto.getPlayerId()).isNull();

        dto.setPlayerId(5);
        assertThat(dto.getPlayerId()).isEqualTo(5);

        dto.setPlayerId(null);
        assertThat(dto.getPlayerId()).isNull();
    }

    @Test
    @DisplayName("Should handle constructor without playerId parameter")
    void testConstructorWithoutPlayerId() {
        Card card = new Card();
        card.setId(1);
        card.setLetter("S");
        testBag.getCards().add(card);

        BagInGameDTO dto = new BagInGameDTO(testBag);

        assertThat(dto.getCards()).hasSize(1);
        assertThat(dto.getPlayerId()).isNull();
    }

}
