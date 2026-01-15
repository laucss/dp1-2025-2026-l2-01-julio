package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

public class WeaponVotingDTO {
    private String weapon;
    private Integer proposingUserId;
    private String proposingUsername;

    // Constructor sin parámetros para deserialización JSON
    public WeaponVotingDTO() {
    }

    public WeaponVotingDTO(String weapon, Integer proposingUserId, String proposingUsername) {
        this.weapon = weapon;
        this.proposingUserId = proposingUserId;
        this.proposingUsername = proposingUsername;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public Integer getProposingUserId() {
        return proposingUserId;
    }

    public void setProposingUserId(Integer proposingUserId) {
        this.proposingUserId = proposingUserId;
    }

    public String getProposingUsername() {
        return proposingUsername;
    }

    public void setProposingUsername(String proposingUsername) {
        this.proposingUsername = proposingUsername;
    }
}
