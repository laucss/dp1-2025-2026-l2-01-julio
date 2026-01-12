package es.us.dp1.lx_xy_24_25.Escape_From_Elba.deck;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;


@ExtendWith(MockitoExtension.class)
public class DeckServiceTests {

    private DeckService deckService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private Checkers checkers;


    @BeforeEach
    public void setup() {
        deckService = new DeckService(cardRepository, checkers);
    }


    @Test
    public void initializeDeckCreatesDeckAndStoresIt() {
        Card card1 = org.mockito.Mockito.mock(Card.class);
        Card card2 = org.mockito.Mockito.mock(Card.class);

        when(card1.getClone()).thenReturn(card1);
        when(card2.getClone()).thenReturn(card2);

        when(cardRepository.findAll()).thenReturn(List.of(card1, card2));

        DeckInGame deck = deckService.initializeDeck(1);

        assertNotNull(deck);
        assertEquals(2, deck.getNotDiscardedCards().size());
        assertTrue(deck.getDiscardedCards().isEmpty());
        assertTrue(deckService.getActivesDecks().containsKey(1));
    }


    @Test
    public void findDeckByIdExistingDeckReturnsDeck() {

        DeckInGame deck = new DeckInGame(new ArrayList<>());
        deckService.getActivesDecks().put(1, deck);

        DeckInGame found = deckService.findDeckById(1);
        assertEquals(deck, found);
    }


    @Test
    public void findDeckByIdNotExistingReturnsEmptyDeck() {
        DeckInGame found = deckService.findDeckById(99);
        assertNotNull(found);
        assertTrue(found.getNotDiscardedCards().isEmpty());
        assertTrue(found.getDiscardedCards().isEmpty());
    }

   
    @Test
    public void deleteDeckInGameRemovesDeck() {
        deckService.getActivesDecks().put(1, new DeckInGame());
        deckService.deleteDeckInGame(1);

        assertFalse(deckService.getActivesDecks().containsKey(1));
    }


    @Test
    public void drawCardRemovesLastCardFromDeck() {
        Card card = new Card();
        DeckInGame deck = new DeckInGame(new ArrayList<>(List.of(card)));
        deckService.getActivesDecks().put(1, deck);


        Card drawn = deckService.drawCard(1);
        assertEquals(card, drawn);
        assertTrue(deck.getNotDiscardedCards().isEmpty());
    }


    @Test
    public void addCardToDiscardedPileAddsCard() {
        Card card = new Card();
        DeckInGame deck = new DeckInGame(new ArrayList<>());
        deckService.getActivesDecks().put(1, deck);

        deckService.addCardToDiscardedPile(1, card);


        verify(checkers, times(1)).checkCardExists(card);
        assertEquals(1, deck.getDiscardedCards().size());
        assertEquals(card, deck.getDiscardedCards().get(0));
    }


    @Test
    public void getAndRemoveLastDiscardedCardReturnsAndRemovesCard() {

        Card card = new Card();
        DeckInGame deck = new DeckInGame(new ArrayList<>());
        deck.getDiscardedCards().add(card);
        deckService.getActivesDecks().put(1, deck);


        Card result = deckService.getAndRemoveLastDiscardedCard(1);


        assertEquals(card, result);
        assertTrue(deck.getDiscardedCards().isEmpty());
    }


    @Test
    public void getAndRemoveLastDiscardedCardEmptyReturnsNull() {
        DeckInGame deck = new DeckInGame(new ArrayList<>());
        deckService.getActivesDecks().put(1, deck);

        Card result = deckService.getAndRemoveLastDiscardedCard(1);

        assertNull(result);
    }

    @Test
    public void isEmptyReturnsTrueWhenNoCards() {
        DeckInGame deck = new DeckInGame(new ArrayList<>());
        deckService.getActivesDecks().put(1, deck);


        assertTrue(deckService.isEmpty(1));
    }


    @Test
    public void drawInitialCardsFromDeckReturnsThreeCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            cards.add(new Card());
        }


        DeckInGame deck = new DeckInGame(cards);
        deckService.getActivesDecks().put(1, deck);


        List<Card> drawn = deckService.drawInitialCardsFromDeck(1);


        assertEquals(3, drawn.size());
        assertTrue(deck.getNotDiscardedCards().isEmpty());
    }
}



