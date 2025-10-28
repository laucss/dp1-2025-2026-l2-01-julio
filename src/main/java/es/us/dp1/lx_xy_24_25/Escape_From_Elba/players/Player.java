package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
// import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player extends BaseEntity {

    /*
     * extendemos BaseEntity para que le genere un Id propio. 
     * No queremos que el PlayerInGame tenga el mismo id que el Player porque este último va a jugar a muchos juegos 
     * y el PlayerInGame es solo una representación, instancia de este en una partida concreta
     */

    @NotNull
    @OneToOne
    private User user; 

    @ManyToOne // una partida, match tiene varios jugadores (inGame)
    private Match match; 


    private Integer strength; 

    private Integer actionPoints; 
        


}



