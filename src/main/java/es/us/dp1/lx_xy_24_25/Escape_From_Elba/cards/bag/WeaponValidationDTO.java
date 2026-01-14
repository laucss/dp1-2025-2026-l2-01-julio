package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeaponValidationDTO {
    private String weapon; 
    
    private Integer bonusValue;

    @Enumerated(EnumType.STRING)
    private ValidationWeaponStatus status;

    public WeaponValidationDTO(String weapon, Integer bonusValue, ValidationWeaponStatus status) {
        this.weapon = weapon;
        this.bonusValue = bonusValue;
        this.status = status;
    }

    public WeaponValidationDTO(){}
}
