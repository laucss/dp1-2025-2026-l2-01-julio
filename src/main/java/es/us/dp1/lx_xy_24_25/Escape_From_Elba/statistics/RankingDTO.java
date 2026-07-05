package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingDTO {

    private Integer id;
    private String username;
    private String avatar;
    private Integer totalVictories;

    public RankingDTO(Integer id, String username, String avatar, Integer totalVictories) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.totalVictories = totalVictories;
    }
}
