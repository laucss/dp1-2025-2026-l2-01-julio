package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand;

import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HandInGame {

    /*
     * Vamos a usar HandInGame para guardar las cartas de la mano del jugador en memoria y no en la base de datos por:  
     * 1. Es más rápido, vamos a modificar mucho el estado de la mano del jugador y hacer tantas llamadas a la BD es más lento
     * 2. No vamos a necesitar guardar tras finalizar la partida ningún dato de la mano, por lo que no necesito usar un repository. 
     */


    private Player owner; 

    private List<Card> cards; 


    public HandInGame(Player player, List<Card> cards){
        this.owner=player; 
        this.cards= cards; 
    }
    
}
