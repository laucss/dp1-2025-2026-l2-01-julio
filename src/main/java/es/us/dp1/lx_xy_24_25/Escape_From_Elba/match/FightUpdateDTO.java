package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

public class FightUpdateDTO {
    private Integer matchId;
    private Integer attackerId;
    private String attackerUsername;
    private Integer defenderId;
    private String defenderUsername;
    private String roomName;
    private String action; // "START", "END"

    public FightUpdateDTO() {}

    public FightUpdateDTO(Integer matchId, Integer attackerId, String attackerUsername, 
                          Integer defenderId, String defenderUsername, String roomName, String action) {
        this.matchId = matchId;
        this.attackerId = attackerId;
        this.attackerUsername = attackerUsername;
        this.defenderId = defenderId;
        this.defenderUsername = defenderUsername;
        this.roomName = roomName;
        this.action = action;
    }

    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }

    public Integer getAttackerId() { return attackerId; }
    public void setAttackerId(Integer attackerId) { this.attackerId = attackerId; }

    public String getAttackerUsername() { return attackerUsername; }
    public void setAttackerUsername(String attackerUsername) { this.attackerUsername = attackerUsername; }

    public Integer getDefenderId() { return defenderId; }
    public void setDefenderId(Integer defenderId) { this.defenderId = defenderId; }

    public String getDefenderUsername() { return defenderUsername; }
    public void setDefenderUsername(String defenderUsername) { this.defenderUsername = defenderUsername; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    @Override
    public String toString() {
        return "FightUpdateDTO{" +
                "matchId=" + matchId +
                ", attackerId=" + attackerId +
                ", attackerUsername='" + attackerUsername + '\'' +
                ", defenderId=" + defenderId +
                ", defenderUsername='" + defenderUsername + '\'' +
                ", roomName='" + roomName + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
