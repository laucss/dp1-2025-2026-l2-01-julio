package es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Npc extends BaseEntity{

    private Integer strength;

    private Boolean isNiallCampbell;


    @ManyToOne
    @JoinColumn(name="match_id")
    private Match match;


    //Añadir más adelante relación con room
    
}
