package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import scala.concurrent.duration.Duration;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.NamedEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerInGame;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Entity
//@Table(name = "Game")??
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Match extends NamedEntity {
    private String code;
    
    //Tiempos
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /** estaría bien??
    public Duration getDuration() {
        if(startTime == null || endTime == null) return null;
        return Duration.between(startTime, endTime);
    }
    */

    //Máximo y mínimo de jugadores
    @Min(3)
    @Max(6)
    @NotNull
    private Integer maxPlayers = 6;

    //Usuarios
    @NotNull
    @OneToMany(cascade = CascadeType.ALL)
    private List<PlayerInGame> players; 

    /** 
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Deck deck;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Chat chat;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Board board;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL)
    private NPC npc;
    */

    //Indica si la partida es privada
    @NotNull
    private Boolean isPrivate;

    
    public Boolean isFinished() {
        return this.getEndTime() != null;
    }

    //añade jugador 
    public void addPlayer(PlayerInGame player) {
        this.players.add(player);
        player.setMatch(this); 
    }


    //En principio devuelve un jugador aleatorio de la partida para empezar (o null si no hay jugadores)
    public PlayerInGame pickRandomStartingPlayer() {
        if (players == null || players.isEmpty()) return null;
        SecureRandom rnd = new SecureRandom();
        int idx = rnd.nextInt(players.size());
        return players.get(idx);
    }

    //Código que se genera al indicar que la partida es privada
    public String generateCodeLobby() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code;

        code = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }


}
