package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import com.fasterxml.jackson.annotation.JsonBackReference;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.Authorities;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
// import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player extends User {


	
	@ManyToOne
	@JoinColumn(name = "match_id")
	@JsonBackReference
	private Match match;



    private Integer strength; 

    private Integer actionPoints; // realmente tendría que ser una función de getActionPoints no? 
        


}



