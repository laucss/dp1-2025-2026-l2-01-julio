package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

public class StrengthUpdateDTO {
    private Integer playerId;
    private Integer userId;
    private String username;
    private Integer strength;
    private Long timestamp;

    public StrengthUpdateDTO() {}

    public StrengthUpdateDTO(Integer playerId, Integer userId, String username, Integer strength, Long timestamp) {
        this.playerId = playerId;
        this.userId = userId;
        this.username = username;
        this.strength = strength;
        this.timestamp = timestamp;
    }

    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getStrength() { return strength; }
    public void setStrength(Integer strength) { this.strength = strength; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "StrengthUpdateDTO{" +
                "playerId=" + playerId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", strength=" + strength +
                ", timestamp=" + timestamp +
                '}';
    }
}
