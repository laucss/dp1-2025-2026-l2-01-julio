package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchHistorialDTO {

    private String name;

    private  List<PlayerInGameDTO> players; 

    private Duration duration;

    private PlayerInGameDTO creator;

    private PlayerInGameDTO winner;

    private Boolean abandoned;



    public MatchHistorialDTO() {
    }



    public MatchHistorialDTO(Match match, User creator) {
        this.name = match.getName();
        this.players = match.getPlayers().stream().map(p-> new PlayerInGameDTO(p)).toList();
        this.duration = match.getDuration();
        this.abandoned = false;

        

        if (creator != null) {
        this.creator = new PlayerInGameDTO(creator);
         }

        // Buscar winner en la lista de players usando el winner
        if (match.getWinner() != null) {
            this.winner = match.getPlayers().stream()
                .filter(p -> p.getId().equals(match.getWinner().getId()))
                .findFirst()
                .map(PlayerInGameDTO::new)
                .orElse(null);
        }
    }
    
}
