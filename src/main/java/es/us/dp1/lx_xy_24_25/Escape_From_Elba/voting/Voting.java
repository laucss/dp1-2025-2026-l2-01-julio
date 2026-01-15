package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Voting extends BaseEntity {

    private Integer matchId; 

    private String weaponProposed; 

    //private LocalDate createdAt; 

    @Enumerated(EnumType.STRING)
    private VotingStatus status;

    @Enumerated(EnumType.STRING)
    private VotingResult result;

    private Integer votesInFavor;

    private Integer votesAgainst;

    private Integer numPlayers; // será el número de jugadores de la partida-1 porque el jugador que propone el arma no cuenta 

    private Integer finalBonus; // es la bonificación que recibe al acabar la votación (1 si es aceptada, 0 si es rechazada)
    // lo pongo aquí para que la bonificación que se recibe se establezca a nivel de backend, porque si no la tenía que poner en una línea random del frontend. 

    @OneToMany(mappedBy = "voting", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Vote> votes;


    public Voting(){}

    // List<Player> players = lista de jugadores EXCEPTO quien propone el arma 
    public Voting(Integer matchId, String weaponProposed, List<Player> players){ 
        this.matchId = matchId;
        this.weaponProposed = weaponProposed;
        this.numPlayers = players.size(); 
        this.votes = players.stream().map(player -> new Vote(player.getId(), this)).toList();
    }

    public Voting(Integer matchId, String weaponProposed){ 
        this.matchId = matchId;
        this.weaponProposed = weaponProposed;
    }

    /*@Version
    private Integer version; ?????????????? */
    
}
