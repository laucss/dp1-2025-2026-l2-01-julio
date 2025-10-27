package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player extends BaseEntity{

    @NotNull
    @OneToOne
    private User user; 

    // tendrá estadísticas y amigos y tal  
    @NotNull
    @ManyToOne // una partida, match tiene varios jugadores (inGame)
    // TODO: HAY QUE PONER EL JOIN COLUMN -----------------------------------------------------------
    private Match match; 
    

     /*
     * extendemos BaseEntity para que le genere un Id propio. 
     * No queremos que el PlayerInGame tenga el mismo id que el Player porque este último va a jugar a muchos juegos 
     * y el PlayerInGame es solo una representación, instancia de este en una partida concreta
     */

    @ManyToOne  // un player aparece en varias partidas y por lo tanto "tiene" varios playerInGame
    private Player player; 




    private Integer strength; 

    private Integer actionPoints; 
        
}
