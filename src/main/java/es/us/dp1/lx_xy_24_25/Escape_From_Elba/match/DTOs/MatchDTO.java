package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.TurnPhase;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.NpcDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchDTO {

    private String code;

    //@NotNull
    private Integer creatorId;
    
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Min(3)
    @Max(6)
    @NotNull
    private Integer maxPlayers = 6;

    @NotNull
    @Min(3)
    @Max(6)
    private Integer minPlayers = 3;

    @NotNull
    private List<PlayerInGameDTO> players; 

    private Integer numNpcs;


    private List<NpcDTO> npcs = new ArrayList<>();

    private Integer currentTurnUserId;

    private Integer turnNumber;

    @Enumerated(EnumType.STRING)
    private TurnPhase currentTurnPhase;

    private List<RoomDTO> roomsState = new ArrayList<>();

    private DeckInGameDTO deck;

    private Player winner;

    @NotNull
    private Boolean isPrivate;

    public MatchDTO() {
    }

    public MatchDTO(Match match) {
        this.code = match.getCode();
        this.creatorId = match.getCreatorId();
        this.status = match.getStatus();
        this.startTime = match.getStartTime();
        this.endTime = match.getEndTime();
        this.maxPlayers = match.getMaxPlayers();
        this.minPlayers = match.getMinPlayers();
        this.players = match.getPlayers().stream().map(p-> new PlayerInGameDTO(p)).toList();
        this.numNpcs = match.getNumNpcs();
        this.npcs = match.getNpcs().stream().map(n-> new NpcDTO(n)).toList();
        this.turnNumber = match.getTurnNumber();
        this.currentTurnPhase = match.getCurrentTurnPhase();
        this.currentTurnUserId = match.getCurrentTurnUserId();
        this.roomsState = match.getRoomsState();
        //this.deck = new DeckInGameDTO(match.getDeck());
        this.winner = match.getWinner();
        this.isPrivate = match.getIsPrivate();
    }

    public MatchDTO(Match match, DeckInGame deck, List<PlayerInGameDTO> players ) {
        this.code = match.getCode();
        this.creatorId = match.getCreatorId();
        this.status = match.getStatus();
        this.startTime = match.getStartTime();
        this.endTime = match.getEndTime();
        this.maxPlayers = match.getMaxPlayers();
        this.minPlayers = match.getMinPlayers();
        this.players = players;
        this.numNpcs = match.getNumNpcs();
        this.npcs = match.getNpcs().stream().map(n-> new NpcDTO(n)).toList();
        this.turnNumber = match.getTurnNumber();
        this.currentTurnPhase = match.getCurrentTurnPhase();
        this.currentTurnUserId = match.getCurrentTurnUserId();
        this.roomsState = match.getRoomsState();
        this.deck = new DeckInGameDTO(deck);
        this.winner = match.getWinner();
        this.isPrivate = match.getIsPrivate();
    }
    
}
