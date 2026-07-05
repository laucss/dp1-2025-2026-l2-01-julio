package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
