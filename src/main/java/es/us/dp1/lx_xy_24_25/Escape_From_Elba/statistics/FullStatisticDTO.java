package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

public class FullStatisticDTO {
    
    private StatisticDTO totalStatistic;

    private StatisticAvgDTO avgStatistic;

    private StatisticDTO minStatistic;

    private StatisticDTO maxStatistic;

    public FullStatisticDTO(StatisticDTO globalStatistic, StatisticAvgDTO avgStatistic, StatisticDTO minStatistic, StatisticDTO maxStatistic) {
        this.totalStatistic = globalStatistic;
        this.avgStatistic = avgStatistic;
        this.minStatistic = minStatistic;
        this.maxStatistic = maxStatistic;
    }

}
