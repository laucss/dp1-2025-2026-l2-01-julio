package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AllCardsStatusDTO Tests")
public class AllCardsStatusDTOTest {

    private AllCardsStatusDTO dto;
    private HandInGame testHand;
    private BagInGame testBag;
    private DeckInGame testDeck;
    private HandInGameDTO testHandDTO;
    private BagInGameDTO testBagDTO;
    private DeckInGameDTO testDeckDTO;

    @BeforeEach
    void setUp() {
        testHand = new HandInGame();

        testBag = new BagInGame();

        testDeck = new DeckInGame();

        testHandDTO = new HandInGameDTO();

        testBagDTO = new BagInGameDTO();

        testDeckDTO = new DeckInGameDTO();

        dto = new AllCardsStatusDTO();
    }

    @Test
    @DisplayName("Should create empty AllCardsStatusDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getHand()).isNull();
        assertThat(dto.getBag()).isNull();
        assertThat(dto.getDeck()).isNull();
        assertThat(dto.getPlayerId()).isNull();
    }

    @Test
    @DisplayName("Should create AllCardsStatusDTO from entities")
    void testConstructorWithEntities() {
        AllCardsStatusDTO dto = new AllCardsStatusDTO(testHand, testBag, testDeck, 1);

        assertThat(dto.getHand()).isNotNull();
        assertThat(dto.getBag()).isNotNull();
        assertThat(dto.getDeck()).isNotNull();
        assertThat(dto.getPlayerId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should create AllCardsStatusDTO from DTOs")
    void testConstructorWithDTOs() {
        AllCardsStatusDTO dto = new AllCardsStatusDTO(testHandDTO, testBagDTO, testDeckDTO, 2);

        assertThat(dto.getHand()).isEqualTo(testHandDTO);
        assertThat(dto.getBag()).isEqualTo(testBagDTO);
        assertThat(dto.getDeck()).isEqualTo(testDeckDTO);
        assertThat(dto.getPlayerId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should convert entities to DTOs in constructor")
    void testEntityToDTOConversion() {
        AllCardsStatusDTO dto = new AllCardsStatusDTO(testHand, testBag, testDeck, 5);

        assertThat(dto.getHand()).isInstanceOf(HandInGameDTO.class);
        assertThat(dto.getBag()).isInstanceOf(BagInGameDTO.class);
        assertThat(dto.getDeck()).isInstanceOf(DeckInGameDTO.class);
    }

    @Test
    @DisplayName("Should set and get hand correctly")
    void testSetAndGetHand() {
        HandInGameDTO hand = new HandInGameDTO();
        dto.setHand(hand);
        assertThat(dto.getHand()).isEqualTo(hand);
    }

    @Test
    @DisplayName("Should set and get bag correctly")
    void testSetAndGetBag() {
        BagInGameDTO bag = new BagInGameDTO();
        dto.setBag(bag);
        assertThat(dto.getBag()).isEqualTo(bag);
    }

    @Test
    @DisplayName("Should set and get deck correctly")
    void testSetAndGetDeck() {
        DeckInGameDTO deck = new DeckInGameDTO();
        dto.setDeck(deck);
        assertThat(dto.getDeck()).isEqualTo(deck);
    }

    @Test
    @DisplayName("Should set and get playerId correctly")
    void testSetAndGetPlayerId() {
        dto.setPlayerId(10);
        assertThat(dto.getPlayerId()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setHand(testHandDTO);
        dto.setBag(testBagDTO);
        dto.setDeck(testDeckDTO);
        dto.setPlayerId(1);

        dto.setHand(null);
        dto.setBag(null);
        dto.setDeck(null);
        dto.setPlayerId(null);

        assertThat(dto.getHand()).isNull();
        assertThat(dto.getBag()).isNull();
        assertThat(dto.getDeck()).isNull();
        assertThat(dto.getPlayerId()).isNull();
    }

    @Test
    @DisplayName("Should update hand from non-null to different value")
    void testUpdateHand() {
        dto.setHand(testHandDTO);
        assertThat(dto.getHand()).isEqualTo(testHandDTO);

        HandInGameDTO newHand = new HandInGameDTO();
        dto.setHand(newHand);
        assertThat(dto.getHand()).isEqualTo(newHand);
    }

    @Test
    @DisplayName("Should update bag from non-null to different value")
    void testUpdateBag() {
        dto.setBag(testBagDTO);
        assertThat(dto.getBag()).isEqualTo(testBagDTO);

        BagInGameDTO newBag = new BagInGameDTO();
        dto.setBag(newBag);
        assertThat(dto.getBag()).isEqualTo(newBag);
    }

    @Test
    @DisplayName("Should update deck from non-null to different value")
    void testUpdateDeck() {
        dto.setDeck(testDeckDTO);
        assertThat(dto.getDeck()).isEqualTo(testDeckDTO);

        DeckInGameDTO newDeck = new DeckInGameDTO();
        dto.setDeck(newDeck);
        assertThat(dto.getDeck()).isEqualTo(newDeck);
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
    @DisplayName("Should handle large playerId values")
    void testLargePlayerId() {
        dto.setPlayerId(Integer.MAX_VALUE);
        assertThat(dto.getPlayerId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle hand with cards")
    void testHandWithCards() {
        Card card = new Card();
        card.setId(1);
        card.setLetter("E");
        testHand.getCards().add(card);

        AllCardsStatusDTO dto = new AllCardsStatusDTO(testHand, testBag, testDeck, 1);

        assertThat(dto.getHand().getCards()).hasSize(1);
    }

    @Test
    @DisplayName("Should handle bag with cards")
    void testBagWithCards() {
        Card card1 = new Card();
        card1.setId(1);
        Card card2 = new Card();
        card2.setId(2);
        testBag.getCards().add(card1);
        testBag.getCards().add(card2);

        AllCardsStatusDTO dto = new AllCardsStatusDTO(testHand, testBag, testDeck, 1);

        assertThat(dto.getBag().getCards()).hasSize(2);
    }

    @Test
    @DisplayName("Should handle deck with cards")
    void testDeckWithCards() {
        Card card1 = new Card();
        card1.setId(1);
        Card card2 = new Card();
        card2.setId(2);
        Card card3 = new Card();
        card3.setId(3);
        testDeck.getNotDiscardedCards().add(card1);
        testDeck.getNotDiscardedCards().add(card2);
        testDeck.getNotDiscardedCards().add(card3);

        AllCardsStatusDTO dto = new AllCardsStatusDTO(testHand, testBag, testDeck, 1);

        assertThat(dto.getDeck().getNotDiscardedCards()).hasSize(3);
    }

    @Test
    @DisplayName("Should handle empty collections")
    void testEmptyCollections() {
        AllCardsStatusDTO dto = new AllCardsStatusDTO(testHand, testBag, testDeck, 1);

        assertThat(dto.getHand().getCards()).isEmpty();
        assertThat(dto.getBag().getCards()).isEmpty();
        assertThat(dto.getDeck().getNotDiscardedCards()).isEmpty();
        assertThat(dto.getDeck().getDiscardedCards()).isEmpty();
    }

    @Test
    @DisplayName("Should create independent instances with entity constructor")
    void testIndependentInstancesWithEntities() {
        AllCardsStatusDTO dto1 = new AllCardsStatusDTO(testHand, testBag, testDeck, 1);
        AllCardsStatusDTO dto2 = new AllCardsStatusDTO(testHand, testBag, testDeck, 2);

        assertThat(dto1.getPlayerId()).isNotEqualTo(dto2.getPlayerId());

        dto1.setPlayerId(99);
        assertThat(dto2.getPlayerId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should create independent instances with DTO constructor")
    void testIndependentInstancesWithDTOs() {
        AllCardsStatusDTO dto1 = new AllCardsStatusDTO(testHandDTO, testBagDTO, testDeckDTO, 1);
        AllCardsStatusDTO dto2 = new AllCardsStatusDTO(testHandDTO, testBagDTO, testDeckDTO, 2);

        assertThat(dto1.getPlayerId()).isNotEqualTo(dto2.getPlayerId());

        dto1.setPlayerId(99);
        assertThat(dto2.getPlayerId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should handle zero playerId value")
    void testZeroPlayerId() {
        dto.setPlayerId(0);
        assertThat(dto.getPlayerId()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should maintain data integrity through entity conversion")
    void testDataIntegrityWithEntities() {
        Card card = new Card();
        card.setId(1);
        card.setLetter("E");
        testHand.getCards().add(card);

        AllCardsStatusDTO dto = new AllCardsStatusDTO(testHand, testBag, testDeck, 123);

        assertThat(dto.getHand().getCards()).hasSize(1);
        assertThat(dto.getPlayerId()).isEqualTo(123);
    }

    @Test
    @DisplayName("Should maintain references when using DTO constructor")
    void testDTOReferencePreservation() {
        AllCardsStatusDTO dto = new AllCardsStatusDTO(testHandDTO, testBagDTO, testDeckDTO, 5);

        assertThat(dto.getHand()).isSameAs(testHandDTO);
        assertThat(dto.getBag()).isSameAs(testBagDTO);
        assertThat(dto.getDeck()).isSameAs(testDeckDTO);
    }

    @Test
    @DisplayName("Should handle multiple player scenarios")
    void testMultiplePlayers() {
        AllCardsStatusDTO dto1 = new AllCardsStatusDTO(testHand, testBag, testDeck, 1);
        AllCardsStatusDTO dto2 = new AllCardsStatusDTO(testHand, testBag, testDeck, 2);
        AllCardsStatusDTO dto3 = new AllCardsStatusDTO(testHand, testBag, testDeck, 3);

        assertThat(dto1.getPlayerId()).isEqualTo(1);
        assertThat(dto2.getPlayerId()).isEqualTo(2);
        assertThat(dto3.getPlayerId()).isEqualTo(3);
    }
}
