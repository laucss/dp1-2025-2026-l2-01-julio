package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.card;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DictionaryService;

public class DictionaryServiceTests {

    private DictionaryService dictionaryService;

    @BeforeEach
    void setup() throws Exception {
        dictionaryService = new DictionaryService();

        java.lang.reflect.Field dictField = DictionaryService.class.getDeclaredField("dictionary");
        dictField.setAccessible(true);
        dictField.set(dictionaryService, Set.of("cat", "dog", "bird"));

        java.lang.reflect.Field weaponsField = DictionaryService.class.getDeclaredField("weapons");
        weaponsField.setAccessible(true);
        weaponsField.set(dictionaryService, Set.of("sword", "dagger", "axe"));
    }

    
    @Test
    void containsWordReturnsTrueForExistingWord() {
        assertTrue(dictionaryService.containsWord("cat"));
        assertTrue(dictionaryService.containsWord("DOG")); 
    }

    @Test
    void containsWordReturnsFalseForNonExistingWord() {
        assertFalse(dictionaryService.containsWord("elephant"));
        assertFalse(dictionaryService.containsWord("zzz"));
    }


    @Test
    void isWeaponReturnsTrueForWeapon() {
        assertTrue(dictionaryService.isWeapon("sword"));
        assertTrue(dictionaryService.isWeapon("DAGGER")); 
    }

    @Test
    void isWeaponReturnsFalseForNonWeapon() {
        assertFalse(dictionaryService.isWeapon("cat"));
        assertFalse(dictionaryService.isWeapon("bird"));
        assertFalse(dictionaryService.isWeapon("hammer")); 
    }
}
