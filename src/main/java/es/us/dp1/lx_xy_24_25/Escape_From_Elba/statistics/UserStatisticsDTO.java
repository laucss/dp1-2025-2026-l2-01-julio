package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatisticsDTO extends BaseEntity {
    private Integer totalVictories = 0;
    private Integer matchesPlayed = 0;
    private Integer totalTimePlayed = 0;
    private Integer totalActionPoints = 0;
    private Integer battlesWon = 0;
    private Integer roomsVisited = 0;
    private Double winRate;
    private Double averageTimePerMatch;
    private Double averageActionPointsPerMatch;
    private Double averageRoomsVisitedPerMatch;
    private Double battlesWonPerMatch;
    private Integer totalBattlesPlayed;
    private Integer maxRoomsVisitedInMatch = 0;
    private String playerType;
    // getters y setters
}
