package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/statistics")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Statistics", description = "The Statistics API to manage the statistics of the players.")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;
    
    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }    
    

    @GetMapping("/{userId}")
    public ResponseEntity<UserStatisticsDTO> getUserStatistics(@PathVariable Integer userId) {
        UserStatisticsDTO dto = new UserStatisticsDTO();
        Integer victories = statisticService.getTotalVictoriesByUser(userId);
        Integer matches = statisticService.getMatchesPlayedByUser(userId);
        Integer totalTime = statisticService.getTotalTimePlayedByUserFOR(userId);
        Integer actionPoints = statisticService.getTotalAccionPointsByUser(userId);
        Integer battlesWon = statisticService.getBattlesWonByUser(userId);
        Integer rooms = statisticService.getTotalRoomsVisitedByUser(userId);
        actionPoints = actionPoints != null ? actionPoints : 0;
        rooms = rooms != null ? rooms : 0;
        totalTime = totalTime != null ? totalTime : 0;
        dto.setTotalVictories(victories);
        dto.setMatchesPlayed(matches);
        dto.setTotalTimePlayed(totalTime);
        dto.setTotalActionPoints(actionPoints);
        dto.setBattlesWon(battlesWon);
        dto.setRoomsVisited(rooms);
        dto.setWinRate(matches > 0 ? (victories * 100.0) / matches : 0.0);
        dto.setAverageTimePerMatch(matches > 0 ? totalTime.doubleValue() / matches : 0.0);
        dto.setAverageActionPointsPerMatch(matches > 0 ? actionPoints.doubleValue() / matches : 0.0);
        dto.setAverageRoomsVisitedPerMatch(matches > 0 ? rooms.doubleValue() / matches : 0.0);
        dto.setBattlesWonPerMatch(matches > 0 ? dto.getBattlesWon().doubleValue() / matches : 0.0);
        Integer totalBattlesPlayed =statisticService.getBattlesPlayedByUser(userId);
        dto.setTotalBattlesPlayed(totalBattlesPlayed);
        dto.setMaxRoomsVisitedInMatch(statisticService.getMaxRoomsVisitedInMatch(userId));
        dto.setPlayerType(statisticService.getPlayerType(userId));
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/general")
    public ResponseEntity<GeneralStatisticsDTO> getGeneralStatistics() {
        GeneralStatisticsDTO Gdto = new GeneralStatisticsDTO();
        Gdto.setAveragePlayersPerMatch(statisticService.getAveragePlayersPerMatch());
        Gdto.setTotalMatchesPlayed(statisticService.getTotalMatchesPlayed());
        Gdto.setTotalBattlesDisputed(statisticService.getTotalBattlesDisputed());
        Gdto.setAverageRoomsVisitedPerMatch(statisticService.getAverageRoomsVisitedPerMatch());
        Gdto.setAverageMatchDuration(statisticService.getAverageMatchDuration());
        Gdto.setLongestMatchDuration(statisticService.getLongestMatchDuration());
        Gdto.setShortestMatchDuration(statisticService.getShortestMatchDuration());
        return ResponseEntity.ok(Gdto);
    }
    
}
