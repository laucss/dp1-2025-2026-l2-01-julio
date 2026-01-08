package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

public class ActionPointsUpdateDTO {
    private Integer playerId;
    private Integer userId;
    private String username;
    private Integer actionPoints;
    private Long timestamp;

    public ActionPointsUpdateDTO() {}

    public ActionPointsUpdateDTO(Integer playerId, Integer userId, String username, Integer actionPoints, Long timestamp) {
        this.playerId = playerId;
        this.userId = userId;
        this.username = username;
        this.actionPoints = actionPoints;
        this.timestamp = timestamp;
    }

    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getActionPoints() { return actionPoints; }
    public void setActionPoints(Integer actionPoints) { this.actionPoints = actionPoints; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "ActionPointsUpdateDTO{" +
                "playerId=" + playerId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", actionPoints=" + actionPoints +
                ", timestamp=" + timestamp +
                '}';
    }
}
