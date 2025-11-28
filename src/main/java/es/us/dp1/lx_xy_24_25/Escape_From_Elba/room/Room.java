package es.us.dp1.lx_xy_24_25.Escape_From_Elba.room;


import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "Room")
@NoArgsConstructor
public class Room extends BaseEntity {

    @NotNull
    private String name;

    @Min(1)
    @Max(6)
    private Integer blackDice;

    @Min(1)
    @Max(6)
    private Integer whiteDice;
    
    @NotNull
    @ManyToMany(targetEntity = Room.class)
    @JoinColumn(referencedColumnName = "id", name = "adjacencyList")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Room> adjacencyList;

    public Room(String name, Integer blackDice, Integer whiteDice, 
        List<Room> adjacencyList) {
        this.name = name;
        this.blackDice = blackDice;
        this.whiteDice = whiteDice;
        this.adjacencyList = adjacencyList;
    }
}