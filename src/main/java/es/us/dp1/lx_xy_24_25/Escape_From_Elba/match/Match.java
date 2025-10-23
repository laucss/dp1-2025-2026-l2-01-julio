package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.NamedEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerInGame;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class Match extends NamedEntity {
    private String code;
    private LocalDateTime start;
    private LocalDateTime finish;

    @OneToMany
    private List<PlayerInGame> players; 
}
