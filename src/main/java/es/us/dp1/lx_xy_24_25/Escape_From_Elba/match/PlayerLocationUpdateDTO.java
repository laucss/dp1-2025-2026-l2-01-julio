package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerLocationUpdateDTO {
    
    private Integer playerId;
    
    private Integer userId;
    
    private String username;
    
    private RoomDTO newRoom;
    
    private Long timestamp;
    
    public PlayerLocationUpdateDTO(Player player) {
        this.playerId = player.getId();
        this.userId = player.getUser().getId();
        this.username = player.getUser().getUsername();
        this.newRoom = player.getRoom() != null ? new RoomDTO(player.getRoom()) : null;
        this.timestamp = System.currentTimeMillis();
    }
}
