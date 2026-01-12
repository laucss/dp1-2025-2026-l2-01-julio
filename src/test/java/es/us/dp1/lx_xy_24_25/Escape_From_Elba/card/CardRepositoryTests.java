package es.us.dp1.lx_xy_24_25.Escape_From_Elba.card;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardRepository;

@SpringBootTest
@Transactional
public class CardRepositoryTests {

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void findAllWhenNoCardsReturnsEmptyList() {

        cardRepository.deleteAll();

        List<Card> cards = cardRepository.findAll();
        assertTrue(cards.isEmpty()); 
    }

    @ParameterizedTest
    @ValueSource(ints = { 9999, 10000, 10001 })
    public void findByIdNonExistentReturnsEmptyOptional(int cardId) {
        Optional<Card> card = cardRepository.findById(cardId);
        assertTrue(card.isEmpty());
    }

    @Test
    public void saveCardAndFindByIdReturnsCard() {
        
        Card card = new Card();
        card.setFrontImage("front.png");
        card.setBackImage("back.png");
        card.setLetter("A");
        cardRepository.save(card);

        Optional<Card> found = cardRepository.findById(card.getId());
        assertTrue(found.isPresent());
        assertEquals(card.getFrontImage(), found.get().getFrontImage());
        assertEquals(card.getBackImage(), found.get().getBackImage());
        assertEquals(card.getLetter(), found.get().getLetter());
    }

    @Test
    public void findAllReturnsAllSavedCards() {
    
        cardRepository.deleteAll();

        Card card1 = new Card();
        card1.setFrontImage("front1.png");
        card1.setBackImage("back1.png");
        card1.setLetter("A");
        cardRepository.save(card1);

        Card card2 = new Card();
        card2.setFrontImage("front2.png");
        card2.setBackImage("back2.png");
        card2.setLetter("B");
        cardRepository.save(card2);

        List<Card> cards = cardRepository.findAll();
        assertEquals(2, cards.size());
        assertTrue(cards.contains(card1));
        assertTrue(cards.contains(card2));
    }

    @Test
    public void cloneCardReturnsEqualButDifferentObject() {
        Card card = new Card();
        card.setFrontImage("front.png");
        card.setBackImage("back.png");
        card.setLetter("C");
        cardRepository.save(card);

        Card cloned = card.getClone();
        assertThat(cloned).isNotSameAs(card);
        assertEquals(card.getFrontImage(), cloned.getFrontImage());
        assertEquals(card.getBackImage(), cloned.getBackImage());
        assertEquals(card.getLetter(), cloned.getLetter());
    }
}

