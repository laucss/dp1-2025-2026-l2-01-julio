package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;


import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerInGameDTO {
    
    // Jugador ID
    private Integer id;
    
    // Estado del jugador durante la partida
    private Integer strength;      
    
    private Integer actionPoints;
    
    private User user; 

    private RoomDTO currentRoom;
    

    private HandInGameDTO hand;       
    

    private BagInGameDTO bag;  


    public PlayerInGameDTO(Player player) {
        this.id = player.getId();
        this.strength = player.getStrength();
        this.actionPoints = player.getActionPoints();
        this.user= player.getUser();
        this.currentRoom = player.getRoom() != null ? new RoomDTO(player.getRoom()) : null;
    }


    public PlayerInGameDTO(Player player, HandInGame hand, BagInGame bag) {
        this.id = player.getId();
        this.strength = player.getStrength();
        this.actionPoints = player.getActionPoints();
        this.user= player.getUser();
        this.currentRoom = player.getRoom() != null ? new RoomDTO(player.getRoom()) : null;
        this.hand = new HandInGameDTO(hand);
        this.bag = new BagInGameDTO(bag);

    }


    public PlayerInGameDTO() {
    }
    
}
