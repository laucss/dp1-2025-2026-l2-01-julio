package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInGameDTO {
    
    // Jugador ID
    private Integer playerId;
    
    // Estado del jugador durante la partida
    private Integer strength;      
    
    private Integer actionPoints;       
    
    // Cartas en juego
    @JsonProperty("hand")
    private List<Card> handCards;       
    
    @JsonProperty("bag")
    private List<Card> bagCards;        
    
}
