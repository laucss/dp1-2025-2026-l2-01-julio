package es.us.dp1.lx_xy_24_25.Escape_From_Elba.bag;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DictionaryService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;

@ExtendWith(MockitoExtension.class)
public class BagServiceTests {

    private BagService bagService;

    @Mock
    private Checkers checkers;

    @Mock
    private PlayerService playerService;

    @Mock
    private DictionaryService dictionaryService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        bagService = new BagService(checkers, playerService, dictionaryService, restTemplate);
    }

    
    @Test
    void createPlayerBagCreatesBagIfNotExists() {
        bagService.createPlayerbag(1, 10);

        Map<Integer, Map<Integer, BagInGame>> bags = bagService.getActivesBags();

        assertTrue(bags.containsKey(1));
        assertTrue(bags.get(1).containsKey(10));
        assertNotNull(bags.get(1).get(10));
    }

    @Test
    void createPlayerBagDoesNotOverrideExistingBag() {
        bagService.createPlayerbag(1, 10);
        BagInGame firstBag = bagService.getActivesBags().get(1).get(10);

        bagService.createPlayerbag(1, 10);
        BagInGame secondBag = bagService.getActivesBags().get(1).get(10);

        assertSame(firstBag, secondBag, "No debe sobrescribir la bolsa existente");
    }

    
    @Test
    void deleteMatchBagsRemovesMatchEntry() {
        bagService.createPlayerbag(1, 10);
        bagService.deleteMatchBags(1);

        assertFalse(bagService.getActivesBags().containsKey(1));
    }

    
    @Test
    void findPlayerBagReturnsBagIfExists() {
        bagService.createPlayerbag(1, 10);

        BagInGame bag = bagService.findPlayerBag(1, 10);

        assertNotNull(bag);
        assertTrue(bag.getCards().isEmpty());
    }

    @Test
    void findPlayerBagReturnsEmptyBagIfMatchNotExists() {
        BagInGame bag = bagService.findPlayerBag(999, 123); 
        assertNotNull(bag);
        assertTrue(bag.getCards().isEmpty());
    }


    
    @Test
    void removeCardFromPlayerBagNullCardReturnsNull() {
        Card result = bagService.removeCardFromPlayerBag(null, 1, 1);
        assertNull(result);
    }

    @Test
    void removeCardFromPlayerBagRemovesCardByReference() {
        bagService.createPlayerbag(1, 10);
        BagInGame bag = bagService.findPlayerBag(1, 10);

        Card card = new Card();
        card.setLetter("A");
        bag.getCards().add(card);

        Card removed = bagService.removeCardFromPlayerBag(card, 1, 10);

        assertEquals(card, removed);
        assertTrue(bag.getCards().isEmpty());
    }

    @Test
    void removeCardFromPlayerBagRemovesCardByLetter() {
        bagService.createPlayerbag(1, 10);
        BagInGame bag = bagService.findPlayerBag(1, 10);

        Card stored = new Card();
        stored.setLetter("B");
        bag.getCards().add(stored);

        Card incoming = new Card();
        incoming.setLetter("B");

        Card removed = bagService.removeCardFromPlayerBag(incoming, 1, 10);

        assertEquals(stored, removed);
        assertTrue(bag.getCards().isEmpty());
    }

    
    @Test
    void wordFromCardsBuildsCorrectWord() {
        CardDTO c1 = new CardDTO();
        c1.setLetter("C");
        CardDTO c2 = new CardDTO();
        c2.setLetter("A");
        CardDTO c3 = new CardDTO();
        c3.setLetter("T");

        String word = bagService.wordFromCards(List.of(c1, c2, c3));

        assertEquals("CAT", word);
    }

    
    @Test
    void isValidWordForBagShortWordIsAlwaysValid() {
        Boolean result = bagService.isValidWordForBag("hi");
        assertTrue(result);
    }

    @Test
    void isValidWordForBagWordInLocalDictionary() {
        when(dictionaryService.containsWord("cat")).thenReturn(true);

        Boolean result = bagService.isValidWordForBag("cat");

        assertTrue(result);
    }

    @Test
    void isValidWordForBagWordFoundInExternalApi() {
        when(dictionaryService.containsWord("dog")).thenReturn(false);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
            .thenReturn(new ResponseEntity<>("ok", HttpStatus.OK));

        Boolean result = bagService.isValidWordForBag("dog");

        assertTrue(result);
    }

    @Test
    void isValidWordForBagWordNotFoundAnywhere() {
        when(dictionaryService.containsWord("zzz")).thenReturn(false);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
            .thenThrow(HttpClientErrorException.NotFound.class);

        Boolean result = bagService.isValidWordForBag("zzz");

        assertFalse(result);
    }

    
    @Test
    void checkBagIsValidEmptyBagReturnsTrue() {
        Boolean result = bagService.checkBagIsValid(new ArrayList<>());
        assertTrue(result);
    }

    @Test
    void checkBagIsValidDelegatesToWordValidation() {
        CardDTO c1 = new CardDTO();
        c1.setLetter("C");
        CardDTO c2 = new CardDTO();
        c2.setLetter("A");
        CardDTO c3 = new CardDTO();
        c3.setLetter("T");

        when(dictionaryService.containsWord("CAT")).thenReturn(true);

        Boolean result = bagService.checkBagIsValid(List.of(c1, c2, c3));

        assertTrue(result);
    }

    
    @Test
    void isValidWeaponReturnsTrueIfWeapon() {
        when(dictionaryService.isWeapon("SWORD")).thenReturn(true);

        CardDTO c1 = new CardDTO();
        c1.setLetter("S");
        CardDTO c2 = new CardDTO();
        c2.setLetter("W");
        CardDTO c3 = new CardDTO();
        c3.setLetter("O");
        CardDTO c4 = new CardDTO();
        c4.setLetter("R");
        CardDTO c5 = new CardDTO();
        c5.setLetter("D");

        Boolean result = bagService.isValidWeapon(List.of(c1, c2, c3, c4, c5));

        assertTrue(result);
    }
}
