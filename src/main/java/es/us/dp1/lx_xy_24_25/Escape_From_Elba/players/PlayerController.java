package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/players")
@Tag(name = "Players", description = "API for the management of players")
@SecurityRequirement(name = "bearerAuth")
public class PlayerController {
    

    private final PlayerService playerService;

    @Autowired
	public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
	}

    //devuelve todos los players
    @GetMapping
    public ResponseEntity<List<Player>> findAll() {
        List<Player> res = (List<Player>) playerService.findAll();
        return ResponseEntity.ok(res);
    }
    

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Player>> findAllByUser(@PathVariable Integer userId) {
        List<Player> res;
        if (userId != null) {
            res = playerService.findByUserId(userId);
        } else {
            res = (List<Player>) playerService.findAll();
        }
        return ResponseEntity.ok(res);
    }
    

    @GetMapping("/matches/{matchId}")
    public ResponseEntity<List<Player>> getPlayersByMatchId(@PathVariable Integer matchId) {
        List<Player> players = playerService.getPlayersByMatchId(matchId);
        return ResponseEntity.ok(players);
    }
}
