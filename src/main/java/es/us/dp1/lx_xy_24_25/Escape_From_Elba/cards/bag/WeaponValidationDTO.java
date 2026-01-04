package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeaponValidationDTO {
    private Boolean isValid;
    private Integer bonusValue;
    private String message;

    public WeaponValidationDTO(Boolean isValid, Integer bonusValue) {
        this.isValid = isValid;
        this.bonusValue = bonusValue;
        this.message = isValid ? "Valid weapon" : "Invalid weapon";
    }
}
