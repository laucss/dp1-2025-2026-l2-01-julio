package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

public class VotingResultDTO {
    private String status;
    private String result;
    private String proposedWeapon;
    private Integer finalBonus;

    public VotingResultDTO() {
    }

    public VotingResultDTO(String status, String result, String proposedWeapon, Integer finalBonus) {
        this.status = status;
        this.result = result;
        this.proposedWeapon = proposedWeapon;
        this.finalBonus = finalBonus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getProposedWeapon() {
        return proposedWeapon;
    }

    public void setProposedWeapon(String proposedWeapon) {
        this.proposedWeapon = proposedWeapon;
    }

    public Integer getFinalBonus() {
        return finalBonus;
    }

    public void setFinalBonus(Integer finalBonus) {
        this.finalBonus = finalBonus;
    }
}
