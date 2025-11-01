package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.Authorities;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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

    String username;

	String password;

	String avatar;

    @NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "authority")
	Authorities authority;

	public Boolean hasAuthority(String auth) {
		return authority.getAuthority().equals(auth);
	}

	public Boolean hasAnyAuthority(String... authorities) {
		Boolean cond = false;
		for (String auth : authorities) {
			if (auth.equals(authority.getAuthority()))
				cond = true;
		}
		return cond;
	}


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

    private Integer actionPoints; // realmente tendría que ser una función de getActionPoints no? 
        


}



