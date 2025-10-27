package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;


import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "statistics")
public class Statistic extends BaseEntity{

    @Column(name="total_matches", nullable = false, columnDefinition = "int default 0")
    private Integer totalMatches=0;

    @Column(name="won_matches", nullable = false, columnDefinition = "int default 0")
    private Integer wonMatches=0;

    @Column(name="mins_played", nullable = false, columnDefinition = "int default 0")
    private Integer minsPlayed=0;

    @Column(name="avg_players", nullable = false, columnDefinition = "double default 0")
    private Double avgPlayers=0.;

    /*total batallas ganadas, maximos puntos fuerza, total batallas perdidas, maximo de armas */
}
