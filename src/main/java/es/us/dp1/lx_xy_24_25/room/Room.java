package es.us.dp1.lx_xy_24_25.room;
//cambio para merge en FSS8078

import java.util.List;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "rooms")
public class Room extends BaseEntity {

    @NotNull
    private String name;

    @NotNull
    private Integer timesVisited;

    @NotNull
    private Integer blackDice;

    @NotNull
    private Integer whiteDice;
    
    @NotNull
    private Boolean playerInside;

    @NotNull
    @ManyToMany
    private List<Npc> npcsInside;

    @NotNull
    @ManyToMany
    private List<Room> adjacencyList;

    public Room(String name, Integer timesVisited, Integer blackDice, Integer whiteDice, 
        Boolean playerInside, List<Npc> npcsInside, List<Room> adjacencyList) {
        this.name = name;
        this.timesVisited = timesVisited;
        this.blackDice = blackDice;
        this.whiteDice = whiteDice;
        this.playerInside = playerInside;
        this.npcsInside = npcsInside;
        this.adjacencyList = adjacencyList;
    }
}