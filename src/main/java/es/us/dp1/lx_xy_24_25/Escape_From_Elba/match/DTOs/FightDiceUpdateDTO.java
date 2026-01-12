package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

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

    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }

    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public String getPlayerUsername() { return playerUsername; }
    public void setPlayerUsername(String playerUsername) { this.playerUsername = playerUsername; }

    public String getDiceType() { return diceType; }
    public void setDiceType(String diceType) { this.diceType = diceType; }

    public Integer getDiceValue() { return diceValue; }
    public void setDiceValue(Integer diceValue) { this.diceValue = diceValue; }

    @Override
    public String toString() {
        return "FightDiceUpdateDTO{" +
                "matchId=" + matchId +
                ", playerId=" + playerId +
                ", playerUsername='" + playerUsername + '\'' +
                ", diceType='" + diceType + '\'' +
                ", diceValue=" + diceValue +
                '}';
    }
}
