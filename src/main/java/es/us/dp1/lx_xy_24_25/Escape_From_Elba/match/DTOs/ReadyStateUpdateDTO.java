package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

public class ReadyStateUpdateDTO {
    private Integer matchId;
    private Integer playerId;
    private String playerRole; // "ATTACKER" or "DEFENDER"
    private Boolean isReady;

    public ReadyStateUpdateDTO() {}

    public ReadyStateUpdateDTO(Integer matchId, Integer playerId, String playerRole, Boolean isReady) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.playerRole = playerRole;
        this.isReady = isReady;
    }

    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }

    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public String getPlayerRole() { return playerRole; }
    public void setPlayerRole(String playerRole) { this.playerRole = playerRole; }

    public Boolean getIsReady() { return isReady; }
    public void setIsReady(Boolean isReady) { this.isReady = isReady; }
}
