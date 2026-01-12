package es.us.dp1.lx_xy_24_25.Escape_From_Elba.hand;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;


@ExtendWith(MockitoExtension.class)
public class HandServiceTests {

    private HandService handService;

    @Mock
    private Checkers checkers;


    @BeforeEach
    void setup() {
        handService = new HandService(checkers);
    }


    @Test
    void createPlayerHandCreatesHandIfNotExists() {
        handService.createPlayerHand(1, 10);

        Map<Integer, Map<Integer, HandInGame>> hands = handService.getActivesHands();

        assertTrue(hands.containsKey(1));
        assertTrue(hands.get(1).containsKey(10));
        assertNotNull(hands.get(1).get(10));
    }


    @Test
    void createPlayerHandDoesNotOverrideExistingHand() {
        handService.createPlayerHand(1, 10);
        HandInGame firstHand = handService.getActivesHands().get(1).get(10);

        handService.createPlayerHand(1, 10);
        HandInGame secondHand = handService.getActivesHands().get(1).get(10);

        assertSame(firstHand, secondHand, "No debe sobrescribir la mano existente");
    }


    @Test
    void deleteMatchHandsRemovesMatchEntry() {
        handService.createPlayerHand(1, 10);
        handService.deleteMatchHands(1);

        assertFalse(handService.getActivesHands().containsKey(1));
    }


    @Test
    void findPlayerHandReturnsHandIfExists() {
        handService.createPlayerHand(1, 10);

        HandInGame hand = handService.findPlayerHand(1, 10);

        assertNotNull(hand);
        assertTrue(hand.getCards().isEmpty());
    }


    @Test
    void findPlayerHandReturnsEmptyHandIfNotExists() {
        HandInGame hand = handService.findPlayerHand(99, 99);

        assertNotNull(hand);
        assertTrue(hand.getCards().isEmpty());
    }



    @Test
    void addCardToPlayerHandAddsCard() {
        handService.createPlayerHand(1, 10);
        Card card = new Card();
        card.setLetter("A");

        HandInGame hand = handService.addCardToPlayerHand(card, 1, 10);


        assertEquals(1, hand.getCards().size());
        assertEquals(card, hand.getCards().get(0));
    }


    @Test
    void removeCardFromPlayerHandReturnsNullIfCardIsNull() {
        Card result = handService.removeCardFromPlayerHand(null, 1, 10);
        assertNull(result);
    }


    @Test
    void removeCardFromPlayerHandRemovesCardByReference() {
        handService.createPlayerHand(1, 10);
        HandInGame hand = handService.findPlayerHand(1, 10);


        Card card = new Card();
        card.setLetter("B");
        hand.getCards().add(card);


        Card removed = handService.removeCardFromPlayerHand(card, 1, 10);

        assertEquals(card, removed);
        assertTrue(hand.getCards().isEmpty());
    }


    @Test
    void removeCardFromPlayerHandRemovesCardByLetter() {
        handService.createPlayerHand(1, 10);
        HandInGame hand = handService.findPlayerHand(1, 10);

        Card stored = new Card();
        stored.setLetter("C");
        hand.getCards().add(stored);

        Card incoming = new Card();
        incoming.setLetter("C");

        Card removed = handService.removeCardFromPlayerHand(incoming, 1, 10);

        assertEquals(stored, removed);
        assertTrue(hand.getCards().isEmpty());
    }


   
    @Test
    void addFewCardsToPlayerHandAddsMultipleCards() {
        handService.createPlayerHand(1, 10);
        List<Card> cardsToAdd = new ArrayList<>();
        Card c1 = new Card(); c1.setLetter("X");
        Card c2 = new Card(); c2.setLetter("Y");
        cardsToAdd.add(c1); cardsToAdd.add(c2);


        handService.addFewCardsToPlayerHand(1, 10, cardsToAdd);


        HandInGame hand = handService.findPlayerHand(1, 10);
        assertEquals(2, hand.getCards().size());
        assertTrue(hand.getCards().contains(c1));
        assertTrue(hand.getCards().contains(c2));
    }


   
    @Test
    void updateReplacesPlayerHand() {
        handService.createPlayerHand(1, 10);
        List<CardDTO> newCards = new ArrayList<>();
        CardDTO dto = new CardDTO(); dto.setLetter("Z");
        newCards.add(dto);


        HandInGameDTO handDTO = new HandInGameDTO();
        handDTO.setCards(newCards);

        handService.update(handDTO, 1, 10);

        HandInGame hand = handService.findPlayerHand(1, 10);
        assertEquals(1, hand.getCards().size());
        assertEquals("Z", hand.getCards().get(0).getLetter());
    }
}
