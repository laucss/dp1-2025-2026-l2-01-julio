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
    dto.setTotalVictories(statisticService.getTotalVictoriesByUser(userId));
    dto.setMatchesPlayed(statisticService.getMatchesPlayedByUser(userId));
    dto.setTotalTimePlayed(statisticService.getTotalTimePlayedByUserFOR(userId));
    Integer actionPoints = statisticService.getTotalAccionPointsByUser(userId);
    dto.setTotalActionPoints(actionPoints != null ? actionPoints : 0);
    return ResponseEntity.ok(dto);
}


    @GetMapping("/general")
    public ResponseEntity<GeneralStatisticsDTO> getGeneralStatistics() {
        GeneralStatisticsDTO Gdto = new GeneralStatisticsDTO();
        Gdto.setAveragePlayersPerMatch(statisticService.getAveragePlayersPerMatch());
        Gdto.setTotalMatchesPlayed(statisticService.getTotalMatchesPlayed());
        return ResponseEntity.ok(Gdto);
    }
    
}
