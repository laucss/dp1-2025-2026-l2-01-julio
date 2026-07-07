package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.EscapeAttemptResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MoveNpcToRoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MoveToRoomDTO;


@RestController
@RequestMapping("/api/v1/actions")
public class ActionsController {

    ActionsService actionsService; 

    @Autowired
    public ActionsController(ActionsService actionsService){
        this.actionsService=actionsService;
    }

    @PutMapping("/{matchId}/move")
    public ResponseEntity<MatchDTO> moveToAdyacentRoom (@PathVariable Integer matchId, @RequestBody MoveToRoomDTO data){
        MatchDTO result = actionsService.movePlayerToAdyacentRoom(matchId, data.getUserId(), data.getRoomId());
        return ResponseEntity.ok(result); 
    }

    
    @PutMapping("/{matchId}/moveNpc")
    public ResponseEntity<MatchDTO> moveNpcToRoom (@PathVariable Integer matchId, @RequestBody MoveNpcToRoomDTO data){
        MatchDTO result = actionsService.moveNpcToRoom(matchId, data.getNpcId(), data.getRoomId(), data.getUserId());
        return ResponseEntity.ok(result); 
    }

    @PutMapping("/{matchId}/moveByLetters")
    public ResponseEntity<MatchDTO> moveByFormingRoomName(@PathVariable Integer matchId, @RequestBody MoveToRoomDTO data) {
        MatchDTO result = actionsService.movePlayerByFormingRoomName(matchId, data.getUserId(), data.getRoomId());
        return ResponseEntity.ok(result); 
    }

    @PostMapping("/{matchId}/escape-attempt")
    public ResponseEntity<EscapeAttemptResultDTO> attemptEscape(@PathVariable Integer matchId, @RequestParam Integer userId, @RequestParam Integer rollDice){
        EscapeAttemptResultDTO result = actionsService.escapeAttempt(matchId, userId, rollDice);
        return ResponseEntity.ok(result);
    }

    
}
