package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;


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
    
    // Cartas en juego
    /* 
    @JsonProperty("hand")
    private List<Card> handCards;       
    
    @JsonProperty("bag")
    private List<Card> bagCards;  
    
    
    */

    public PlayerInGameDTO(Player player) {
        this.id = player.getId();
        this.strength = player.getStrength();
        this.actionPoints = player.getActionPoints();
        this.user= player.getUser();
        /*
        this.handCards = player.getHandCards();
        this.bagCards = player.getBagCards();
        */
    }

    public PlayerInGameDTO() {
    }
    
}
