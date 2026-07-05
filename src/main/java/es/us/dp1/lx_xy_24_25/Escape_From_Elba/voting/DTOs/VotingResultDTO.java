package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotingResultDTO {
    private String status;
    private String result;
    private String proposedWeapon;
    private Integer finalBonus;

    public VotingResultDTO() {
    }

    public VotingResultDTO(String status, String result, String proposedWeapon, Integer finalBonus) {
        this.status = status;
        this.result = result;
        this.proposedWeapon = proposedWeapon;
        this.finalBonus = finalBonus;
    }

    
}
