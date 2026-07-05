package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.CardsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.LoseAgainstNpcRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.StealCardRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/fights")
public class FightController {

    private final FightService fs;

    @Autowired
    public FightController(FightService fs) {
        this.fs = fs;
    }

    @PutMapping("/{matchId}/fight/resolve")
    public ResponseEntity<FightResolvedDTO> resolveFight(@PathVariable Integer matchId, @RequestBody FightResultRequestDTO request) {
        FightResolvedDTO result = fs.processFightResolution(request); 
        return ResponseEntity.ok(result);
    }

    
    @PostMapping("/{matchId}/steal-card-from-player")
    public ResponseEntity<CardDTO> stealCardFromPlayer(@PathVariable Integer matchId, @RequestBody StealCardRequestDTO request) {
        CardDTO result = fs.playerStealFromPlayer(request, matchId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{matchId}/{playerId}/lose-against-npc")
    @Operation(summary = "Player loses against NPC", description = "Player chooses a card to discard after losing to an NPC.")
    public ResponseEntity<?> playerLosesAgainstNpc(@PathVariable Integer matchId, @PathVariable Integer playerId, @Valid @RequestBody LoseAgainstNpcRequestDTO request) {  
        fs.playerLosesAgainstNpc(matchId, playerId, request);
        return ResponseEntity.noContent().build();
    }
      
    
}
