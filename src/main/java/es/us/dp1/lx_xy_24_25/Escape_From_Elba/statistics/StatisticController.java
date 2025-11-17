package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/statistics")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Statistics", description = "The Statistics API to manage the statistics of the players.")
public class StatisticController {

    private StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    @Operation(summary = "Get all statistics", description = "Returns all the statistics of the players.")
    public ResponseEntity<List<StatisticWithPlayerDTO>> getAllStatistics() {
        return new ResponseEntity<>(statisticService.findAllStatistics(), HttpStatus.OK);
    }

    @GetMapping("/global")
    @Operation(summary = "Get global statistics", description = "Returns the total, average, minimum and maximum statistics for multiple metrics in a global scale.")
    public ResponseEntity<FullStatisticDTO> getGlobalStatistics() {
        FullStatisticDTO globalStatistic = statisticService.getGlobalStatistics();
        return new ResponseEntity<>(globalStatistic, HttpStatus.OK);
    }
    
}
