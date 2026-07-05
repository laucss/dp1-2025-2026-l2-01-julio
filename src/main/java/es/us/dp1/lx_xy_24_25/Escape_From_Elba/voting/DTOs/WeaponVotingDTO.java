package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeaponVotingDTO {
    private String weapon;
    private Integer proposingUserId;
    private String proposingUsername;

    // Constructor sin parámetros para deserialización JSON
    public WeaponVotingDTO() {
    }

    public WeaponVotingDTO(String weapon, Integer proposingUserId, String proposingUsername) {
        this.weapon = weapon;
        this.proposingUserId = proposingUserId;
        this.proposingUsername = proposingUsername;
    }


}
