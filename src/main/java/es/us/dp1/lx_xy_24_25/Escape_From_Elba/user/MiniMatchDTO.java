package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import java.util.List;
import java.util.stream.Collectors;

public class MiniMatchDTO {
    private Integer id;
    private String status;
    private List<MatchPlayerDTO> players;

    public MiniMatchDTO(Match match) {
        this.id = match.getId();
        this.status = match.getStatus() != null ? match.getStatus().name() : null;
        this.players = match.getPlayers() != null 
            ? match.getPlayers().stream()
                .map(MatchPlayerDTO::new)
                .collect(Collectors.toList())
            : null;
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

    public List<MatchPlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<MatchPlayerDTO> players) {
        this.players = players;
    }

    // DTO interno para representar un jugador en una partida
    public static class MatchPlayerDTO {
        private Integer id;
        private Integer userId;
        private String username;

        public MatchPlayerDTO(es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player player) {
            this.id = player.getId();
            if (player.getUser() != null) {
                this.userId = player.getUser().getId();
                this.username = player.getUser().getUsername();
            }
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}