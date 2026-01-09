package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FightResolvedDTO {
    private Integer matchId;
    private Integer winnerId;
    private Integer winnerPlayerId;
    private Integer loserPlayerId;
    private String action = "RESOLVE"; // Para distinguir de otros eventos de fight

    public FightResolvedDTO() {
    }

    public FightResolvedDTO(Integer matchId, Integer winnerId, Integer winnerPlayerId, Integer loserPlayerId) {
        this.matchId = matchId;
        this.winnerId = winnerId;
        this.winnerPlayerId = winnerPlayerId;
        this.loserPlayerId = loserPlayerId;
    }
}
