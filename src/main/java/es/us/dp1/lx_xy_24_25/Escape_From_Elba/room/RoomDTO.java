package es.us.dp1.lx_xy_24_25.Escape_From_Elba.room;

import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO extends BaseEntity {
    
    @NotNull
    private String name;

    @NotNull
    private Integer blackDice;

    @NotNull
    private Integer whiteDice;

    @NotNull
    private Integer timesVisited;

    @NotNull
    private List<Room> adjacencyList;


    private List<Npc> npcsInside;

    private Player playerInside;

    public RoomDTO(Room room) {
        this.setId(room.getId());
        this.setName(room.getName());
        this.setBlackDice(room.getBlackDice());
        this.setWhiteDice(room.getWhiteDice());
        this.setAdjacencyList(room.getAdjacencyList());
        this.setTimesVisited(0);
        this.setNpcsInside(new java.util.ArrayList<>());
        this.setPlayerInside(null);
    }

    public RoomDTO(){}
}
