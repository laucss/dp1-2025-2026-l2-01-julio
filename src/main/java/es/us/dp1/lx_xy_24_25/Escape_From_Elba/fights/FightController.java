package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.DiceTotalsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightDiceUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightResolvedDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightResultRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.LoseAgainstNpcRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.ReadyStateUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.StealCardRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.WeaponsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchWebsocketController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/fights")
public class FightController {

    private final FightService fs;
    private final MatchWebsocketController matchWebsocketController; 

    @Autowired
    public FightController(FightService fs, MatchWebsocketController matchWebsocketController) {
        this.fs = fs;
        this.matchWebsocketController = matchWebsocketController; 
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

    @PostMapping("/{matchId}/notify-fight")
    @Operation(summary = "Notify fight", description = "Notifies all players when a fight is initiated.")
    public ResponseEntity<Void> notifyFight(@PathVariable Integer matchId, @RequestBody FightUpdateDTO fightUpdate) {
        matchWebsocketController.notifyFightUpdate(matchId, fightUpdate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{matchId}/notify-fight-dice")
    @Operation(summary = "Notify fight dice", description = "Notifies all players when a dice is rolled during a fight.")
    public ResponseEntity<Void> notifyFightDice(@PathVariable Integer matchId, @RequestBody FightDiceUpdateDTO diceUpdate) {
        matchWebsocketController.notifyFightDiceUpdate(matchId, diceUpdate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{matchId}/notify-dice-totals")
    @Operation(summary = "Notify dice totals", description = "Notifies all players when dice totals are updated during a fight.")
    public ResponseEntity<Void> notifyDiceTotals(@PathVariable Integer matchId, @RequestBody DiceTotalsUpdateDTO totalsUpdate) {
        matchWebsocketController.notifyDiceTotalsUpdate(matchId, totalsUpdate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{matchId}/notify-ready-state")
    @Operation(summary = "Notify ready state", description = "Notifies all players when a player changes their ready state during a fight.")
    public ResponseEntity<Void> notifyReadyState(@PathVariable Integer matchId, @RequestBody ReadyStateUpdateDTO readyStateUpdate) {
        matchWebsocketController.notifyReadyStateUpdate(matchId, readyStateUpdate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{matchId}/notify-fight-weapons")
    @Operation(summary = "Notify weapons update", description = "Notifies all players when a player adds or removes weapons during a fight.")
    public ResponseEntity<Void> notifyFightWeapons(@PathVariable Integer matchId, @RequestBody WeaponsUpdateDTO weaponsUpdate) {
        matchWebsocketController.notifyWeaponsUpdate(matchId, weaponsUpdate);
        return ResponseEntity.ok().build();
    }
      
    
}
