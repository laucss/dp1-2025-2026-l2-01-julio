package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;



@Service
public class BagService {

    /*
     * Variable que vamos a usar a modo de "almacenamiento" del estado de la bolsa Es decir, vamos a usar un map
     * en el que la clave sea el Id de la partida O EL OWNER AUN NO SÉ y el valor, la baraja en sí -> BagInGame (guardada en memoria)
     */
    private final Map<Integer, BagInGame> activesBags = new HashMap<>(); 

    /*
     * Valida si la palabra que forma el usuario es valida para guardarse en la bolsa
     * llamando a una api de diccionario si tiene más de letras
     * Despues, guarda y actualiza la nueva bolsa del juagador
     */

    /*
    @Transactional 
    public Boolean isValidWordForBag (String word) {

        // hay que mirar a ver cómo hacer para que tampoco acepte nombres propios 
     
        if (word.length >= 3){

            // hacer llamada a la api de free dictionary api: 
            // https://api.dictionaryapi.dev/api/v2/entries/en/<word>
            
            // basicamente hacerle una llamada a esa api con la palabra y ver si devuelve una respuesta válida

            // hay otra opción de api que es https://www.wordsapi.com/  aunque creo que es un poco menos completa pero ns 

            
            BagRepository.save(bag); 
        
            }

    }
     */



    /*
     * Método que checkea si palabra formada forma parte de la lista de armas ofrecida por el juego 
     */
    
    
}
