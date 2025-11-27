package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
    
    @GetMapping("/{userId}/totalPoints") //muchas dudas
    public ResponseEntity<Integer> getTotalAccionPointsByUser(@PathVariable Integer userId) {
        Integer totalActionPoints = statisticService.getTotalAccionPointsByUser(userId);
        return ResponseEntity.ok(totalActionPoints);
    }

    @GetMapping("/{userId}/totalVictories")
    public ResponseEntity<Integer> getTotalVictoriesByUser(@PathVariable Integer userId) {
        Integer totalVictories = statisticService.getTotalVictoriesByUser(userId);
        return ResponseEntity.ok(totalVictories);
    }

    @GetMapping("/{userId}/matchesPlayed")
    public ResponseEntity<Integer> getMatchesPlayedByUser(@PathVariable Integer userId) {
        Integer matchesPlayed = statisticService.getMatchesPlayedByUser(userId);
        return ResponseEntity.ok(matchesPlayed);
    }

    @GetMapping("/{userId}/totalTimePlayed")
    public ResponseEntity<Integer> getTotalTimePlayedByUser(@PathVariable Integer userId) {
        Integer totalTimePlayed = statisticService.getTotalTimePlayedByUserFOR(userId);
        return ResponseEntity.ok(totalTimePlayed);
    }

    @GetMapping("/averagePlayersPerMatch")
    public ResponseEntity<Double> getAveragePlayersPerMatch() {
        Double averagePlayers = statisticService.getAveragePlayersPerMatch();
        return ResponseEntity.ok(averagePlayers);
    }

    @GetMapping("/totalMatchesPlayed")
    public ResponseEntity<Integer> getTotalMatchesPlayed() {
        Integer totalMatchesPlayed = statisticService.getTotalMatchesPlayed();
        return ResponseEntity.ok(totalMatchesPlayed);
    }
    
}
