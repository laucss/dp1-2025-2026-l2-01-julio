package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("WeaponValidationDTO Tests")
public class WeaponValidationDTOTest {

    private WeaponValidationDTO dto;

    @BeforeEach
    void setUp() {
        dto = new WeaponValidationDTO();
    }

    @Test
    @DisplayName("Should create empty WeaponValidationDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getWeapon()).isNull();
        assertThat(dto.getBonusValue()).isNull();
        assertThat(dto.getStatus()).isNull();
    }

    @Test
    @DisplayName("Should create WeaponValidationDTO with all parameters")
    void testConstructorWithAllParameters() {
        WeaponValidationDTO dto = new WeaponValidationDTO("Sword", 5, ValidationWeaponStatus.VALID);

        assertThat(dto.getWeapon()).isEqualTo("Sword");
        assertThat(dto.getBonusValue()).isEqualTo(5);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.VALID);
    }

    @Test
    @DisplayName("Should set and get weapon correctly")
    void testSetAndGetWeapon() {
        dto.setWeapon("Pistol");
        assertThat(dto.getWeapon()).isEqualTo("Pistol");
    }

    @Test
    @DisplayName("Should set and get bonusValue correctly")
    void testSetAndGetBonusValue() {
        dto.setBonusValue(10);
        assertThat(dto.getBonusValue()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should set and get status correctly")
    void testSetAndGetStatus() {
        dto.setStatus(ValidationWeaponStatus.VALID);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.VALID);
    }

    @Test
    @DisplayName("Should handle null weapon")
    void testHandleNullWeapon() {
        dto.setWeapon(null);
        assertThat(dto.getWeapon()).isNull();
    }

    @Test
    @DisplayName("Should handle null bonusValue")
    void testHandleNullBonusValue() {
        dto.setBonusValue(null);
        assertThat(dto.getBonusValue()).isNull();
    }

    @Test
    @DisplayName("Should handle null status")
    void testHandleNullStatus() {
        dto.setStatus(null);
        assertThat(dto.getStatus()).isNull();
    }

    @Test
    @DisplayName("Should update weapon from non-null to different value")
    void testUpdateWeapon() {
        dto.setWeapon("Sword");
        assertThat(dto.getWeapon()).isEqualTo("Sword");

        dto.setWeapon("Axe");
        assertThat(dto.getWeapon()).isEqualTo("Axe");
    }

    @Test
    @DisplayName("Should update bonusValue from non-null to different value")
    void testUpdateBonusValue() {
        dto.setBonusValue(5);
        assertThat(dto.getBonusValue()).isEqualTo(5);

        dto.setBonusValue(15);
        assertThat(dto.getBonusValue()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should update status from non-null to different value")
    void testUpdateStatus() {
        dto.setStatus(ValidationWeaponStatus.VALID);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.VALID);

        dto.setStatus(ValidationWeaponStatus.INVALID);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.INVALID);
    }

    @Test
    @DisplayName("Should handle bonusValue of zero")
    void testBonusValueZero() {
        dto.setBonusValue(0);
        assertThat(dto.getBonusValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle negative bonusValue")
    void testNegativeBonusValue() {
        dto.setBonusValue(-5);
        assertThat(dto.getBonusValue()).isEqualTo(-5);
    }

    @Test
    @DisplayName("Should handle large bonusValue")
    void testLargeBonusValue() {
        dto.setBonusValue(999);
        assertThat(dto.getBonusValue()).isEqualTo(999);
    }

    @Test
    @DisplayName("Should handle empty weapon string")
    void testEmptyWeaponString() {
        dto.setWeapon("");
        assertThat(dto.getWeapon()).isEqualTo("");
    }

    @Test
    @DisplayName("Should handle weapon with spaces")
    void testWeaponWithSpaces() {
        dto.setWeapon("Long Sword");
        assertThat(dto.getWeapon()).isEqualTo("Long Sword");
    }

    @Test
    @DisplayName("Should handle weapon with special characters")
    void testWeaponWithSpecialCharacters() {
        dto.setWeapon("Sword-of-Fire");
        assertThat(dto.getWeapon()).isEqualTo("Sword-of-Fire");
    }

    @Test
    @DisplayName("Should handle all ValidationWeaponStatus values")
    void testAllValidationWeaponStatusValues() {
        for (ValidationWeaponStatus status : ValidationWeaponStatus.values()) {
            dto.setStatus(status);
            assertThat(dto.getStatus()).isEqualTo(status);
        }
    }

    @Test
    @DisplayName("Should transition from VALID to INVALID status")
    void testStatusTransitionValidToInvalid() {
        dto.setStatus(ValidationWeaponStatus.VALID);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.VALID);

        dto.setStatus(ValidationWeaponStatus.INVALID);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.INVALID);
    }

    @Test
    @DisplayName("Should transition from INVALID to VALID status")
    void testStatusTransitionInvalidToValid() {
        dto.setStatus(ValidationWeaponStatus.INVALID);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.INVALID);

        dto.setStatus(ValidationWeaponStatus.VALID);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.VALID);
    }

    @Test
    @DisplayName("Should create instance with constructor and verify all fields")
    void testConstructorInstanceVerification() {
        WeaponValidationDTO dto = new WeaponValidationDTO("Shield", 3, ValidationWeaponStatus.VALID);

        assertThat(dto).isNotNull();
        assertThat(dto.getWeapon()).isNotNull();
        assertThat(dto.getBonusValue()).isNotNull();
        assertThat(dto.getStatus()).isNotNull();
    }

    @Test
    @DisplayName("Should handle weapon names case sensitivity")
    void testWeaponNameCaseSensitivity() {
        dto.setWeapon("sword");
        assertThat(dto.getWeapon()).isEqualTo("sword");

        dto.setWeapon("SWORD");
        assertThat(dto.getWeapon()).isEqualTo("SWORD");

        dto.setWeapon("Sword");
        assertThat(dto.getWeapon()).isEqualTo("Sword");
    }

    @Test
    @DisplayName("Should handle multiple updates in sequence")
    void testMultipleUpdatesInSequence() {
        dto.setWeapon("Sword");
        dto.setBonusValue(5);
        dto.setStatus(ValidationWeaponStatus.VALID);

        assertThat(dto.getWeapon()).isEqualTo("Sword");
        assertThat(dto.getBonusValue()).isEqualTo(5);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.VALID);

        dto.setWeapon("Axe");
        dto.setBonusValue(8);
        dto.setStatus(ValidationWeaponStatus.INVALID);

        assertThat(dto.getWeapon()).isEqualTo("Axe");
        assertThat(dto.getBonusValue()).isEqualTo(8);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.INVALID);
    }

    @Test
    @DisplayName("Should handle update weapon to null and back to value")
    void testWeaponNullTransition() {
        dto.setWeapon("Sword");
        assertThat(dto.getWeapon()).isEqualTo("Sword");

        dto.setWeapon(null);
        assertThat(dto.getWeapon()).isNull();

        dto.setWeapon("Bow");
        assertThat(dto.getWeapon()).isEqualTo("Bow");
    }

    @Test
    @DisplayName("Should handle update bonusValue to null and back to value")
    void testBonusValueNullTransition() {
        dto.setBonusValue(10);
        assertThat(dto.getBonusValue()).isEqualTo(10);

        dto.setBonusValue(null);
        assertThat(dto.getBonusValue()).isNull();

        dto.setBonusValue(15);
        assertThat(dto.getBonusValue()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should handle update status to null and back to value")
    void testStatusNullTransition() {
        dto.setStatus(ValidationWeaponStatus.VALID);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.VALID);

        dto.setStatus(null);
        assertThat(dto.getStatus()).isNull();

        dto.setStatus(ValidationWeaponStatus.INVALID);
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.INVALID);
    }

    @Test
    @DisplayName("Should verify independent instances are independent")
    void testIndependentInstances() {
        WeaponValidationDTO dto1 = new WeaponValidationDTO("Sword", 5, ValidationWeaponStatus.VALID);
        WeaponValidationDTO dto2 = new WeaponValidationDTO("Axe", 8, ValidationWeaponStatus.INVALID);

        assertThat(dto1.getWeapon()).isNotEqualTo(dto2.getWeapon());
        assertThat(dto1.getBonusValue()).isNotEqualTo(dto2.getBonusValue());
        assertThat(dto1.getStatus()).isNotEqualTo(dto2.getStatus());
    }

    @Test
    @DisplayName("Should handle long weapon name")
    void testLongWeaponName() {
        String longName = "The Ultimate Legendary Sword of Fire and Ice";
        dto.setWeapon(longName);
        assertThat(dto.getWeapon()).isEqualTo(longName);
    }

    @Test
    @DisplayName("Should handle maximum bonusValue")
    void testMaximumBonusValue() {
        dto.setBonusValue(Integer.MAX_VALUE);
        assertThat(dto.getBonusValue()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle minimum bonusValue")
    void testMinimumBonusValue() {
        dto.setBonusValue(Integer.MIN_VALUE);
        assertThat(dto.getBonusValue()).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("Should constructor with null values")
    void testConstructorWithNullValues() {
        WeaponValidationDTO dto = new WeaponValidationDTO(null, null, null);

        assertThat(dto.getWeapon()).isNull();
        assertThat(dto.getBonusValue()).isNull();
        assertThat(dto.getStatus()).isNull();
    }

    @Test
    @DisplayName("Should constructor with partial null values")
    void testConstructorWithPartialNullValues() {
        WeaponValidationDTO dto = new WeaponValidationDTO("Sword", null, ValidationWeaponStatus.VALID);

        assertThat(dto.getWeapon()).isEqualTo("Sword");
        assertThat(dto.getBonusValue()).isNull();
        assertThat(dto.getStatus()).isEqualTo(ValidationWeaponStatus.VALID);
    }

    @Test
    @DisplayName("Should verify status is enum type")
    void testStatusIsEnum() {
        dto.setStatus(ValidationWeaponStatus.VALID);
        assertThat(dto.getStatus()).isInstanceOf(ValidationWeaponStatus.class);
    }

    @Test
    @DisplayName("Should create multiple independent instances via constructor")
    void testMultipleConstructorInstances() {
        WeaponValidationDTO dto1 = new WeaponValidationDTO("Sword", 5, ValidationWeaponStatus.VALID);
        WeaponValidationDTO dto2 = new WeaponValidationDTO("Axe", 8, ValidationWeaponStatus.INVALID);
        WeaponValidationDTO dto3 = new WeaponValidationDTO("Bow", 3, ValidationWeaponStatus.VALID);

        assertThat(dto1.getWeapon()).isEqualTo("Sword");
        assertThat(dto2.getWeapon()).isEqualTo("Axe");
        assertThat(dto3.getWeapon()).isEqualTo("Bow");

        // Modify one instance shouldn't affect others
        dto1.setWeapon("Lance");
        assertThat(dto2.getWeapon()).isEqualTo("Axe");
        assertThat(dto3.getWeapon()).isEqualTo("Bow");
    }

    @Test
    @DisplayName("Should handle bonusValue of one")
    void testBonusValueOne() {
        dto.setBonusValue(1);
        assertThat(dto.getBonusValue()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should handle common weapon types")
    void testCommonWeaponTypes() {
        String[] weapons = {"Sword", "Axe", "Bow", "Spear", "Hammer", "Dagger", "Staff", "Mace"};

        for (String weapon : weapons) {
            dto.setWeapon(weapon);
            assertThat(dto.getWeapon()).isEqualTo(weapon);
        }
    }
}
