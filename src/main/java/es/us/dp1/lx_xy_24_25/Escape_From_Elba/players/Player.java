package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.persistence.Entity;
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
    
}
