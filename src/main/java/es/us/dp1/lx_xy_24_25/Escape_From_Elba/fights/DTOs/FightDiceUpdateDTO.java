package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FightDiceUpdateDTO {
    private Integer matchId;
    private Integer playerId;
    private String playerUsername;
    private String diceType; // "WHITE", "BLACK"
    private Integer diceValue;

    public FightDiceUpdateDTO() {}

    public FightDiceUpdateDTO(Integer matchId, Integer playerId, String playerUsername, 
                              String diceType, Integer diceValue) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.playerUsername = playerUsername;
        this.diceType = diceType;
        this.diceValue = diceValue;
    }

    
}
