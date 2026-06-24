package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.EmptyWeaponException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.VotingService;

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
    
    @Mock
    private VotingService votingService;

    @BeforeEach
    void setup() {
        bagService = new BagService(checkers, playerService, dictionaryService, restTemplate, votingService);
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

    // validación de palabras 
    @Test
    void doesWordExistsLocalDictionary() {
        when(dictionaryService.containsWord("cat")).thenReturn(true);
        boolean result = bagService.doesWordExists("cat");
        assertTrue(result);
    }


    // ESTA TAMBIÉN ME DABA ERROR: CORREGIDA?
    @Test
    void doesWordExistsExternalApiSuccess() {
        when(dictionaryService.containsWord("dog")).thenReturn(false);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
            .thenReturn(new ResponseEntity<>("ok", HttpStatus.OK));

        boolean result = bagService.doesWordExists("dog");
        assertTrue(result);
    }

    
    void doesWordExistsExternalApiNotFound() {
        when(dictionaryService.containsWord("zzz")).thenReturn(false);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
            .thenThrow(HttpClientErrorException.NotFound.class);

        boolean result = bagService.doesWordExists("zzz");
        assertFalse(result);
    }

    
    @Test
    void checkBagIsValidEmptyBagReturnsTrue() {
        boolean result = bagService.checkBagIsValid(new ArrayList<>());
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

    // VALIDACIÓN DE ARMAS
    @Test
    void validateWeaponEmptyThrows() {
        BagInGameDTO bag = new BagInGameDTO();
        bag.setCards(new ArrayList<>());
        bag.setPlayerId(10);

        assertThrows(EmptyWeaponException.class, () -> bagService.validateWeapon(bag, 1));
    }

    @Test
    void isWeaponOnListReturnsTrue() {
        when(dictionaryService.isWeapon("BAR")).thenReturn(true);
        boolean result = bagService.isWeaponOnTheList("BAR");
        assertTrue(result);
    }

    @Test
    void validateWeaponThrowsIfEmpty() {
        BagInGameDTO emptyBag = new BagInGameDTO();
        emptyBag.setCards(new ArrayList<>());

        assertThrows(EmptyWeaponException.class, () -> bagService.validateWeapon(emptyBag, 1));
    }

    @Test
    void validateWeaponValidWeaponOnList() {
        BagInGameDTO bag = new BagInGameDTO();
        CardDTO c1 = new CardDTO(); c1.setLetter("B");
        CardDTO c2 = new CardDTO(); c2.setLetter("A");
        CardDTO c3 = new CardDTO(); c3.setLetter("R");
        bag.setCards(List.of(c1, c2, c3));

        when(dictionaryService.isWeapon("BAR")).thenReturn(true);

        WeaponValidationDTO result = bagService.validateWeapon(bag, 1);

        assertEquals(ValidationWeaponStatus.VALID, result.getStatus());
        assertEquals(1, result.getBonusValue());
        assertEquals("BAR", result.getWeapon());
    }

    @Test
    void validateWeaponRequiresVotingIfNotOnListButExists() {
        BagInGameDTO bag = new BagInGameDTO();
        CardDTO c1 = new CardDTO(); c1.setLetter("C");
        CardDTO c2 = new CardDTO(); c2.setLetter("A");
        bag.setCards(List.of(c1, c2));
        bag.setPlayerId(10);

        when(dictionaryService.isWeapon("CA")).thenReturn(false);
        when(dictionaryService.containsWord("CA")).thenReturn(true);

        WeaponValidationDTO result = bagService.validateWeapon(bag, 1);

        assertEquals(ValidationWeaponStatus.REQUIRES_VOTING, result.getStatus());
        assertEquals(0, result.getBonusValue());
        verify(votingService, times(1)).startVoting(1, "CA", 10);
    }

    @Test
    void validateWeaponInvalidIfNotOnListAndDoesNotExist() {
        BagInGameDTO bag = new BagInGameDTO();
        CardDTO c1 = new CardDTO(); c1.setLetter("X");
        CardDTO c2 = new CardDTO(); c2.setLetter("Y");
        bag.setCards(List.of(c1, c2));

        when(dictionaryService.isWeapon("XY")).thenReturn(false);
        when(dictionaryService.containsWord("XY")).thenReturn(false);

        WeaponValidationDTO result = bagService.validateWeapon(bag, 1);

        assertEquals(ValidationWeaponStatus.INVALID, result.getStatus());
        assertEquals(0, result.getBonusValue());
        assertEquals("XY", result.getWeapon());
    }

    @Test
    void checkProposedWeaponExistsReturnsTrueIfWordExists() {
        CardDTO c1 = new CardDTO(); c1.setLetter("S");
        CardDTO c2 = new CardDTO(); c2.setLetter("W");
        when(dictionaryService.containsWord("SW")).thenReturn(true);

        boolean result = bagService.checkProposedWeaponExists(List.of(c1, c2));

        assertTrue(result);
    }

    @Test
    void checkProposedWeaponExistsReturnsFalseIfWordDoesNotExist() {
        CardDTO c1 = new CardDTO(); c1.setLetter("X");
        CardDTO c2 = new CardDTO(); c2.setLetter("Y");
        when(dictionaryService.containsWord("XY")).thenReturn(false);

        boolean result = bagService.checkProposedWeaponExists(List.of(c1, c2));

        assertFalse(result);
    }

    // DEL UPDATE
    @Test
    void updateStoresNewBagInActivesBags() {
        // Preparamos activesBags para un match y jugador
        bagService.getActivesBags().put(1, new java.util.HashMap<>());

        CardDTO c1 = new CardDTO(); c1.setLetter("A");
        BagInGameDTO dto = new BagInGameDTO();
        dto.setCards(List.of(c1));

        bagService.update(dto, 1, 10);

        assertTrue(bagService.getActivesBags().get(1).containsKey(10));
        assertEquals("A", bagService.getActivesBags().get(1).get(10).getCards().get(0).getLetter());
    }

}
