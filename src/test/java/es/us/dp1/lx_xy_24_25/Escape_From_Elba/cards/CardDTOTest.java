package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CardDTO Tests")
public class CardDTOTest {

    private CardDTO dto;
    private Card testCard;

    @BeforeEach
    void setUp() {
        testCard = new Card();
        testCard.setId(1);
        testCard.setFrontImage("front.png");
        testCard.setBackImage("back.png");
        testCard.setLetter("E");

        dto = new CardDTO();
    }

    @Test
    @DisplayName("Should create empty CardDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getId()).isNull();
        assertThat(dto.getFrontImage()).isNull();
        assertThat(dto.getBackImage()).isNull();
        assertThat(dto.getLetter()).isNull();
    }

    @Test
    @DisplayName("Should create CardDTO from Card entity")
    void testConstructorWithCard() {
        CardDTO dto = new CardDTO(testCard);

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getFrontImage()).isEqualTo("front.png");
        assertThat(dto.getBackImage()).isEqualTo("back.png");
        assertThat(dto.getLetter()).isEqualTo("E");
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void testSetAndGetId() {
        dto.setId(10);
        assertThat(dto.getId()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should set and get frontImage correctly")
    void testSetAndGetFrontImage() {
        dto.setFrontImage("image.jpg");
        assertThat(dto.getFrontImage()).isEqualTo("image.jpg");
    }

    @Test
    @DisplayName("Should set and get backImage correctly")
    void testSetAndGetBackImage() {
        dto.setBackImage("back_image.jpg");
        assertThat(dto.getBackImage()).isEqualTo("back_image.jpg");
    }

    @Test
    @DisplayName("Should set and get letter correctly")
    void testSetAndGetLetter() {
        dto.setLetter("L");
        assertThat(dto.getLetter()).isEqualTo("L");
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setId(1);
        dto.setFrontImage("front");
        dto.setBackImage("back");
        dto.setLetter("E");

        dto.setId(null);
        dto.setFrontImage(null);
        dto.setBackImage(null);
        dto.setLetter(null);

        assertThat(dto.getId()).isNull();
        assertThat(dto.getFrontImage()).isNull();
        assertThat(dto.getBackImage()).isNull();
        assertThat(dto.getLetter()).isNull();
    }

    @Test
    @DisplayName("Should update id from non-null to different value")
    void testUpdateId() {
        dto.setId(1);
        assertThat(dto.getId()).isEqualTo(1);

        dto.setId(2);
        assertThat(dto.getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should update frontImage from non-null to different value")
    void testUpdateFrontImage() {
        dto.setFrontImage("image1.png");
        assertThat(dto.getFrontImage()).isEqualTo("image1.png");

        dto.setFrontImage("image2.png");
        assertThat(dto.getFrontImage()).isEqualTo("image2.png");
    }

    @Test
    @DisplayName("Should update backImage from non-null to different value")
    void testUpdateBackImage() {
        dto.setBackImage("back1.png");
        assertThat(dto.getBackImage()).isEqualTo("back1.png");

        dto.setBackImage("back2.png");
        assertThat(dto.getBackImage()).isEqualTo("back2.png");
    }

    @Test
    @DisplayName("Should update letter from non-null to different value")
    void testUpdateLetter() {
        dto.setLetter("A");
        assertThat(dto.getLetter()).isEqualTo("A");

        dto.setLetter("B");
        assertThat(dto.getLetter()).isEqualTo("B");
    }

    @Test
    @DisplayName("Should handle large id values")
    void testLargeId() {
        dto.setId(Integer.MAX_VALUE);
        assertThat(dto.getId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle different letter values")
    void testDifferentLetters() {
        String[] letters = {"E", "L", "B", "A", "S", "C", "P", "I"};
        
        for (String letter : letters) {
            dto.setLetter(letter);
            assertThat(dto.getLetter()).isEqualTo(letter);
        }
    }

    @Test
    @DisplayName("Should handle image paths with different formats")
    void testDifferentImageFormats() {
        dto.setFrontImage("path/to/image.png");
        assertThat(dto.getFrontImage()).isEqualTo("path/to/image.png");

        dto.setBackImage("/static/cards/back.jpg");
        assertThat(dto.getBackImage()).isEqualTo("/static/cards/back.jpg");
    }

    @Test
    @DisplayName("Should create independent instances")
    void testIndependentInstances() {
        CardDTO dto1 = new CardDTO();
        dto1.setId(1);
        dto1.setLetter("E");

        CardDTO dto2 = new CardDTO();
        dto2.setId(2);
        dto2.setLetter("L");

        assertThat(dto1.getId()).isNotEqualTo(dto2.getId());
        assertThat(dto1.getLetter()).isNotEqualTo(dto2.getLetter());

        dto1.setId(99);
        assertThat(dto2.getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should handle zero value for id")
    void testZeroId() {
        dto.setId(0);
        assertThat(dto.getId()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle empty strings for image paths")
    void testEmptyImagePaths() {
        dto.setFrontImage("");
        dto.setBackImage("");

        assertThat(dto.getFrontImage()).isEmpty();
        assertThat(dto.getBackImage()).isEmpty();
    }

    @Test
    @DisplayName("Should handle card with all escape letters")
    void testEscapeLetters() {
        testCard.setLetter("E");
        CardDTO dtoE = new CardDTO(testCard);
        assertThat(dtoE.getLetter()).isEqualTo("E");

        testCard.setLetter("S");
        CardDTO dtoS = new CardDTO(testCard);
        assertThat(dtoS.getLetter()).isEqualTo("S");

        testCard.setLetter("C");
        CardDTO dtoC = new CardDTO(testCard);
        assertThat(dtoC.getLetter()).isEqualTo("C");

        testCard.setLetter("A");
        CardDTO dtoA = new CardDTO(testCard);
        assertThat(dtoA.getLetter()).isEqualTo("A");

        testCard.setLetter("P");
        CardDTO dtoP = new CardDTO(testCard);
        assertThat(dtoP.getLetter()).isEqualTo("P");
    }
}
