package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticWithPlayerDTO {

    private Integer GAMES_PLAYED=0;

    private Integer VICTORIES=0;

    private Integer TOTAL_PLAY_TIME=0;

    private Double avgPlayers=0.;
    

    public StatisticWithPlayerDTO(Statistic statistic, String username) {
        this.GAMES_PLAYED = statistic.getGAMES_PLAYED();
        this.VICTORIES = statistic.getVICTORIES();
        this.TOTAL_PLAY_TIME = statistic.getTOTAL_PLAY_TIME();
        this.avgPlayers = statistic.getAvgPlayers();
    }

    public StatisticWithPlayerDTO() {
    }
}
