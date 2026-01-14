package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EscapeAttemptResultDTO {

    private boolean success;
    private Integer winnerUserId;

    private boolean discardRequired;


    public EscapeAttemptResultDTO(){
        
    }

    public EscapeAttemptResultDTO(boolean success, Integer winnerUserId, boolean discardRequired) {
        this.success = success;
        this.winnerUserId = winnerUserId;
        this.discardRequired = discardRequired;
    }


    
}
