package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

public class DiceTotalsUpdateDTO {
    private Integer attackerId;
    private Integer attackerTotal;
    private Integer defenderId;
    private Integer defenderTotal;
    private Long timestamp;

    public DiceTotalsUpdateDTO() {}

    public DiceTotalsUpdateDTO(Integer attackerId, Integer attackerTotal, Integer defenderId, Integer defenderTotal, Long timestamp) {
        this.attackerId = attackerId;
        this.attackerTotal = attackerTotal;
        this.defenderId = defenderId;
        this.defenderTotal = defenderTotal;
        this.timestamp = timestamp;
    }

    public Integer getAttackerId() { return attackerId; }
    public void setAttackerId(Integer attackerId) { this.attackerId = attackerId; }

    public Integer getAttackerTotal() { return attackerTotal; }
    public void setAttackerTotal(Integer attackerTotal) { this.attackerTotal = attackerTotal; }

    public Integer getDefenderId() { return defenderId; }
    public void setDefenderId(Integer defenderId) { this.defenderId = defenderId; }

    public Integer getDefenderTotal() { return defenderTotal; }
    public void setDefenderTotal(Integer defenderTotal) { this.defenderTotal = defenderTotal; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "DiceTotalsUpdateDTO{" +
                "attackerId=" + attackerId +
                ", attackerTotal=" + attackerTotal +
                ", defenderId=" + defenderId +
                ", defenderTotal=" + defenderTotal +
                ", timestamp=" + timestamp +
                '}';
    }
}
