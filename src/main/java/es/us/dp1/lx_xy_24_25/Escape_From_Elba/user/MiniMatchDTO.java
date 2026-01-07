package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;

public class MiniMatchDTO {
    private Integer id;
    private String status;

    public MiniMatchDTO(Match match) {
        this.id = match.getId();
        this.status = match.getStatus() != null ? match.getStatus().name() : null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}