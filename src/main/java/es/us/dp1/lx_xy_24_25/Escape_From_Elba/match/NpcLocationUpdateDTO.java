package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NpcLocationUpdateDTO {
    
    private Integer npcId;
    
    private Boolean isNiallCampbell;
    
    private RoomDTO newRoom;
    
    private Long timestamp;
    
    public NpcLocationUpdateDTO(Npc npc) {
        this.npcId = npc.getId();
        this.isNiallCampbell = npc.getIsNiallCampbell();
        this.newRoom = npc.getRoom() != null ? new RoomDTO(npc.getRoom()) : null;
        this.timestamp = System.currentTimeMillis();
    }
}
