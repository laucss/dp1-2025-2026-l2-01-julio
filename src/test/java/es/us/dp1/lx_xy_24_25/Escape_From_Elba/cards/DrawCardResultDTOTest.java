package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DrawCardResultDTO Tests")
public class DrawCardResultDTOTest {

    private Card testCard;
    private DeckInGame testDeck;
    private HandInGame testHand;

    @BeforeEach
    void setUp() {
        testCard = new Card();
        testCard.setId(1);
        testCard.setFrontImage("front.png");
        testCard.setBackImage("back.png");
        testCard.setLetter("E");

        testDeck = new DeckInGame();

        testHand = new HandInGame();
    }

    @Test
    @DisplayName("Should create DrawCardResultDTO with card, deck and hand")
    void testConstructorWithAllParameters() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);

        assertThat(dto.getCard()).isNotNull();
        assertThat(dto.getDeck()).isNotNull();
        assertThat(dto.getHand()).isNotNull();
    }

    @Test
    @DisplayName("Should convert Card to CardDTO in constructor")
    void testCardConversion() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);

        assertThat(dto.getCard()).isInstanceOf(CardDTO.class);
        assertThat(dto.getCard().getId()).isEqualTo(1);
        assertThat(dto.getCard().getFrontImage()).isEqualTo("front.png");
        assertThat(dto.getCard().getBackImage()).isEqualTo("back.png");
        assertThat(dto.getCard().getLetter()).isEqualTo("E");
    }

    @Test
    @DisplayName("Should convert DeckInGame to DeckInGameDTO in constructor")
    void testDeckConversion() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);

        assertThat(dto.getDeck()).isInstanceOf(DeckInGameDTO.class);
        assertThat(dto.getDeck().getNotDiscardedCards()).isNotNull();
        assertThat(dto.getDeck().getDiscardedCards()).isNotNull();
    }

    @Test
    @DisplayName("Should convert HandInGame to HandInGameDTO in constructor")
    void testHandConversion() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);

        assertThat(dto.getHand()).isInstanceOf(HandInGameDTO.class);
    }

    @Test
    @DisplayName("Should set and get card correctly")
    void testSetAndGetCard() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        CardDTO newCard = new CardDTO();
        newCard.setId(2);
        newCard.setLetter("L");
        
        dto.setCard(newCard);
        assertThat(dto.getCard()).isEqualTo(newCard);
        assertThat(dto.getCard().getId()).isEqualTo(2);
        assertThat(dto.getCard().getLetter()).isEqualTo("L");
    }

    @Test
    @DisplayName("Should set and get deck correctly")
    void testSetAndGetDeck() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        DeckInGameDTO newDeck = new DeckInGameDTO();
        
        dto.setDeck(newDeck);
        assertThat(dto.getDeck()).isEqualTo(newDeck);
    }

    @Test
    @DisplayName("Should set and get hand correctly")
    void testSetAndGetHand() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        HandInGameDTO newHand = new HandInGameDTO();
        
        dto.setHand(newHand);
        assertThat(dto.getHand()).isEqualTo(newHand);
    }

    @Test
    @DisplayName("Should handle card with different letters")
    void testDifferentCardLetters() {
        String[] letters = {"E", "S", "C", "A", "P", "L", "B", "I"};
        
        for (String letter : letters) {
            testCard.setLetter(letter);
            DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
            
            assertThat(dto.getCard().getLetter()).isEqualTo(letter);
        }
    }

    @Test
    @DisplayName("Should handle deck with cards")
    void testDeckWithCards() {
        Card card1 = new Card();
        card1.setId(1);
        card1.setLetter("E");
        
        Card card2 = new Card();
        card2.setId(2);
        card2.setLetter("L");
        
        testDeck.getNotDiscardedCards().add(card1);
        testDeck.getNotDiscardedCards().add(card2);
        
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        assertThat(dto.getDeck().getNotDiscardedCards()).hasSize(2);
    }

    @Test
    @DisplayName("Should handle hand with cards")
    void testHandWithCards() {
        Card card1 = new Card();
        card1.setId(1);
        card1.setLetter("E");
        
        testHand.getCards().add(card1);
        
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        assertThat(dto.getHand().getCards()).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle empty deck")
    void testEmptyDeck() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        assertThat(dto.getDeck().getNotDiscardedCards()).isEmpty();
        assertThat(dto.getDeck().getDiscardedCards()).isEmpty();
    }

    @Test
    @DisplayName("Should handle empty hand")
    void testEmptyHand() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        assertThat(dto.getHand().getCards()).isEmpty();
    }

    @Test
    @DisplayName("Should create independent instances")
    void testIndependentInstances() {
        DrawCardResultDTO dto1 = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        Card anotherCard = new Card();
        anotherCard.setId(2);
        anotherCard.setLetter("L");
        
        DrawCardResultDTO dto2 = new DrawCardResultDTO(anotherCard, testDeck, testHand);
        
        assertThat(dto1.getCard().getId()).isNotEqualTo(dto2.getCard().getId());
        assertThat(dto1.getCard().getLetter()).isNotEqualTo(dto2.getCard().getLetter());
        
        dto1.getCard().setId(99);
        assertThat(dto2.getCard().getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should update card after construction")
    void testUpdateCard() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        CardDTO originalCard = dto.getCard();
        assertThat(originalCard.getId()).isEqualTo(1);
        
        CardDTO newCard = new CardDTO();
        newCard.setId(10);
        newCard.setLetter("B");
        
        dto.setCard(newCard);
        assertThat(dto.getCard().getId()).isEqualTo(10);
        assertThat(dto.getCard().getLetter()).isEqualTo("B");
    }

    @Test
    @DisplayName("Should update deck after construction")
    void testUpdateDeck() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        DeckInGameDTO newDeck = new DeckInGameDTO();
        
        dto.setDeck(newDeck);
        assertThat(dto.getDeck()).isEqualTo(newDeck);
    }

    @Test
    @DisplayName("Should update hand after construction")
    void testUpdateHand() {
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        HandInGameDTO newHand = new HandInGameDTO();
        
        dto.setHand(newHand);
        assertThat(dto.getHand()).isEqualTo(newHand);
    }

    @Test
    @DisplayName("Should maintain card data integrity through conversion")
    void testCardDataIntegrity() {
        testCard.setId(123);
        testCard.setFrontImage("path/to/front.png");
        testCard.setBackImage("path/to/back.png");
        testCard.setLetter("E");
        
        DrawCardResultDTO dto = new DrawCardResultDTO(testCard, testDeck, testHand);
        
        assertThat(dto.getCard().getId()).isEqualTo(123);
        assertThat(dto.getCard().getFrontImage()).isEqualTo("path/to/front.png");
        assertThat(dto.getCard().getBackImage()).isEqualTo("path/to/back.png");
        assertThat(dto.getCard().getLetter()).isEqualTo("E");
    }
}
