package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class HandService {

    /*
     * Variable que vamos a usar a modo de "almacenamiento" de las manos. Es decir, vamos a usar un map
     * en el que la clave sea el Id del jugador que posee esa "Mano" y el valor la mano en sí -> HandInGame
     * 
     * Explicación de por qué HandInGame en su clase. 
     */
    private final Map<Integer, HandInGame> activesHands = new HashMap<>(); 


    /*
     * Método que pasa la carta de la mano del jugador a su bolsa 
     * o método que quita una carta de la mano del jugador 
     */


    /*
     * Método para pasar la carta del mazo de robar a la mano del jugador que robe 
     * O método que añade una carta a la mano 
     */
    

}
