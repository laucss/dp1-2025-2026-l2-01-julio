package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurnUpdateDTO {
    private Integer matchId;
    private Integer currentTurnUserId;
    private String currentTurnUsername;
    private Integer turnNumber;
    private String turnPhase;

    public TurnUpdateDTO() {}

    public TurnUpdateDTO(Integer matchId, Integer currentTurnUserId, String currentTurnUsername, 
                         Integer turnNumber, String turnPhase) {
        this.matchId = matchId;
        this.currentTurnUserId = currentTurnUserId;
        this.currentTurnUsername = currentTurnUsername;
        this.turnNumber = turnNumber;
        this.turnPhase = turnPhase;
    }

    
    @Override
    public String toString() {
        return "TurnUpdateDTO{" +
                "matchId=" + matchId +
                ", currentTurnUserId=" + currentTurnUserId +
                ", currentTurnUsername='" + currentTurnUsername + '\'' +
                ", turnNumber=" + turnNumber +
                ", turnPhase='" + turnPhase + '\'' +
                '}';
    }
}
