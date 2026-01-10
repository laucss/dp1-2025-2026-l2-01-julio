package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.util.List;

public class WeaponsUpdateDTO {
    private Integer matchId;
    private Integer playerId;
    private String playerRole; // "ATTACKER", "DEFENDER"
    private List<WeaponData> weapons;
    private Integer totalAttacker;
    private Integer totalDefender;

    public WeaponsUpdateDTO() {}

    public WeaponsUpdateDTO(Integer matchId, Integer playerId, String playerRole, 
                           List<WeaponData> weapons, Integer totalAttacker, Integer totalDefender) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.playerRole = playerRole;
        this.weapons = weapons;
        this.totalAttacker = totalAttacker;
        this.totalDefender = totalDefender;
    }

    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }

    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public String getPlayerRole() { return playerRole; }
    public void setPlayerRole(String playerRole) { this.playerRole = playerRole; }

    public List<WeaponData> getWeapons() { return weapons; }
    public void setWeapons(List<WeaponData> weapons) { this.weapons = weapons; }

    public Integer getTotalAttacker() { return totalAttacker; }
    public void setTotalAttacker(Integer totalAttacker) { this.totalAttacker = totalAttacker; }

    public Integer getTotalDefender() { return totalDefender; }
    public void setTotalDefender(Integer totalDefender) { this.totalDefender = totalDefender; }

    public static class WeaponData {
        private String name;
        private Integer bonus;

        public WeaponData() {}

        public WeaponData(String name, Integer bonus) {
            this.name = name;
            this.bonus = bonus;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getBonus() { return bonus; }
        public void setBonus(Integer bonus) { this.bonus = bonus; }
    }

    @Override
    public String toString() {
        return "WeaponsUpdateDTO{" +
                "matchId=" + matchId +
                ", playerId=" + playerId +
                ", playerRole='" + playerRole + '\'' +
                ", weapons=" + weapons +
                ", totalAttacker=" + totalAttacker +
                ", totalDefender=" + totalDefender +
                '}';
    }
}
