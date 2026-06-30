package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.NamedEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Getter
@Setter
@Entity
@Table(name = "Match")
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Match extends NamedEntity {

    // -------------------------------------------------------- ATRIBUTOS --------------------------------------------------------
    
    // Código
    private String code;

    // Indica si la partida es privada
    @NotNull
    private Boolean isPrivate;

    // Estado: WAITING, PLAYING, VOTING, FINISHED
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    //Indica la fase actual del turno
    @Enumerated(EnumType.STRING)
    private TurnPhase currentTurnPhase;

    private Integer currentTurnUserId;

    private Integer turnNumber;

    //Tiempos
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Creador
    //@NotNull
    private Integer creatorId;

    // Ganador
    @OneToOne(optional = true)
    @JoinColumn(name = "winner_id")
    private Player winner;

    //Máximo y mínimo de jugadores
    @Min(3)
    @Max(6)
    @NotNull
    private Integer maxPlayers = 6;

    @NotNull
    @Min(3)
    @Max(6)
    private Integer minPlayers = 3;

    //Jugadores 
    @NotNull
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>(); 

    //Indica el número de npcs que el creador quiere en la partida ( por defecto 3, 2 normales y Niall Campbell)
    private Integer numNpcs = 3;

    //Lista de Npcs 
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Npc> npcs = new ArrayList<>();


    @Transient // transient porque no se guardan en la base de datos 
    private DeckInGame deck; 

    // Habitaciones
    @Transient
    private List<RoomDTO> roomsState = new ArrayList<>();



    // -------------------------------------------------------- PROPIEDADES DERIVADAS --------------------------------------------------------

    public Duration getDuration() {
        if(startTime == null || endTime == null) return null;
        return Duration.between(startTime, endTime);
    }
    
    public Boolean isInProgress() {
		return this.getStartTime() != null && this.getEndTime() == null;
	}
    
    public Boolean isFinished() {
        return this.getEndTime() != null;
    }

    public void addPlayer(Player player) {
        if (player == null) throw new IllegalArgumentException("El jugador no puede ser nulo");
        if (isFull()) {
            throw new IllegalStateException("No se puede añadir otro jugador: la partida está llena (Máximo=" + this.maxPlayers + ")");
        }
        if (this.players == null) {
            this.players = new java.util.ArrayList<>();
        }
        this.players.add(player);
        player.setMatch(this); 
    }

    // Devuelve true si se ha alcanzado el numero máximo de jugadores
    public boolean isFull() {
        if (this.players == null) return false;
        if (this.maxPlayers == null) return false;
        return this.players.size() >= this.maxPlayers;
    }

    // Devuelve true si se ha alcanzado el número mínimo de jugadores para empezar
    public boolean isMinReached() {
        if (this.players == null) return false;
        if (this.minPlayers == null) return false;
        return this.players.size() >= this.minPlayers;
    }
    // Devuelve true si NO se ha alcanzado el número mínimo de jugadores
    public boolean isMinNotReached() {
        return !isMinReached();
    }

    // Lanza IllegalStateException si no se ha alcanzado el mínimo de jugadores
    public void ensureMinReached() {
        if (isMinNotReached()) {
            int current = (this.players == null) ? 0 : this.players.size();
            throw new IllegalStateException("No hay suficientes jugadores para comenzar (Mínimo=" 
                + this.minPlayers + ", actuales=" + current + ")");
        }
    }

    // Código que se genera al indicar que la partida es privada
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


    public List<RoomDTO> getRoomsState() {
        return roomsState;
    }

    public void setRoomsState(List<RoomDTO> roomsState) {
        this.roomsState = roomsState;
    }


    // -------------------------------------------------------- VALIDACIONES --------------------------------------------------------

    // Validación para asegurar que minplayers es menor o igual que maxplayers
    @AssertTrue(message = "minPlayers debe ser menor o igual que maxPlayers")
    private boolean isPlayerLimitsValid() {
        if (minPlayers == null || maxPlayers == null) 
            return true;
        return minPlayers <= maxPlayers;
    }



}
