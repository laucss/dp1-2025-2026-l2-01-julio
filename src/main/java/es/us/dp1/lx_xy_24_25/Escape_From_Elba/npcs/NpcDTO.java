package es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NpcDTO {

    private Integer strength;

    private Boolean isNiallCampbell;


    //Un npc está en una habitación
    // private RoomDTO room;

    public NpcDTO(Npc npc) {
        this.strength = npc.getStrength();
        this.isNiallCampbell = npc.getIsNiallCampbell();
        //this.room = new RoomDTO(npc.getRoom());
    }

    public NpcDTO() {
    }
    
}
