package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

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

    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }

    public Integer getCurrentTurnUserId() { return currentTurnUserId; }
    public void setCurrentTurnUserId(Integer currentTurnUserId) { this.currentTurnUserId = currentTurnUserId; }

    public String getCurrentTurnUsername() { return currentTurnUsername; }
    public void setCurrentTurnUsername(String currentTurnUsername) { this.currentTurnUsername = currentTurnUsername; }

    public Integer getTurnNumber() { return turnNumber; }
    public void setTurnNumber(Integer turnNumber) { this.turnNumber = turnNumber; }

    public String getTurnPhase() { return turnPhase; }
    public void setTurnPhase(String turnPhase) { this.turnPhase = turnPhase; }

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
