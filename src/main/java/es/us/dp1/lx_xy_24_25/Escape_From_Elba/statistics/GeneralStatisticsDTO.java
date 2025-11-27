package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralStatisticsDTO extends BaseEntity {
    private Double averagePlayersPerMatch;
    private Integer totalMatchesPlayed;
    // getters y setters
}
