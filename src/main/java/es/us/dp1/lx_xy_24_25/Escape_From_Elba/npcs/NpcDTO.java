package es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs;


import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NpcDTO {

    private Integer strength;

    private Boolean isNiallCampbell;



     private RoomDTO room;

    public NpcDTO(Npc npc) {
        this.strength = npc.getStrength();
        this.isNiallCampbell = npc.getIsNiallCampbell();
        this.room = npc.getRoom() != null ? new RoomDTO(npc.getRoom()) : null;
    }

    public NpcDTO() {
    }
    
}
