package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HandUpdateDTO {
    private Integer matchId;
    private Integer playerId;
    private HandInGameDTO hand;

    public HandUpdateDTO() {
    }

    public HandUpdateDTO(Integer matchId, Integer playerId, HandInGameDTO hand) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.hand = hand;
    }
}
