package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticAvgDTO {

    @Min(0)
    private Double GAMES_PLAYED;

    @Min(0)
    private Double VICTORIES;

    @Min(0)
    private Double TOTAL_PLAY_TIME;

    @Min(0)
    private Double avgPlayers;

    @Min(0)
    private Double players;
    

    public StatisticAvgDTO(Double GAMES_PLAYED, Double VICTORIES, Double TOTAL_PLAY_TIME, Double avgPlayers, Double players) {
        this.GAMES_PLAYED = GAMES_PLAYED;
        this.VICTORIES = VICTORIES;
        this.TOTAL_PLAY_TIME = TOTAL_PLAY_TIME;
        this.avgPlayers = avgPlayers;
        this.players = players;
    }
}
