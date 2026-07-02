package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/lobbies")
public class LobbyController {

    LobbyService ls;
    MatchService ms;

    @Autowired
    public LobbyController(LobbyService ls, MatchService ms){
        this.ls = ls;
        this.ms = ms;
    }

    @GetMapping("/private")
    public Optional<Match> getPrivateGame(@RequestParam  String code){
        return ls.getPrivateLobby(code);
    }

    @GetMapping("/{matchId}")
    public Optional<Match> getLobbyById(@PathVariable("matchId") Integer matchId) {
        return ls.getById(matchId);
    }

    @GetMapping()
    @Operation(summary = "Get public matches", description = "Get all public matches available to join.")
    public Page<Match> getPublicGames(@RequestParam MatchStatus status,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return ls.getAllPublicGamesByStatus(status,page,size);
    }

    @GetMapping("/privates")
    public List<Match> getPrivateLobbies(){
        return ls.getAllPrivateLobbies();
    }

    @PostMapping()
    @Operation(summary = "Create lobby", description = "Create a new game.")
    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<Match> createLobby(@Valid @RequestBody LobbyDTO lobbyDTO) {
        Match saved= ls.createLobby(lobbyDTO.getIsPrivate(), lobbyDTO.getName(), lobbyDTO.getMaxPlayers(), lobbyDTO.getNumNpcs());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/{matchId}/join")
    @Operation(summary = "Join public lobby", description = "Join a public lobby from a list of available lobbies.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Match> joinLobby(@Parameter(description = "Id of the lobby to join") @PathVariable Integer matchId) {
        Match joinedMatch = ls.joinLobby(matchId);

        return ResponseEntity.ok(joinedMatch);
    }
    
    @PostMapping("/join/private")
    @Operation(summary = "Join private lobby", description = "Join a private lobby using its code.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Match> joinPrivateLobby(@RequestParam String code) {
        Match joinedMatch = ls.joinPrivateLobby(code);
        return ResponseEntity.ok(joinedMatch);
    }

    @PostMapping("/{matchId}/leave")
    @Operation(summary = "Leave a lobby", description = "Leva a lobby before the game starts.")
    public ResponseEntity<Match> leaveLobby(@Parameter(description = "Id of the lobby to leave") @PathVariable Integer matchId) {
        Match leftMatch = ls.leaveLobby(matchId);
        return ResponseEntity.ok(leftMatch);
    }

    @PostMapping("/{matchId}/start")
    @Operation(summary = "Start match", description = "Start a match from a lobby.")
    public ResponseEntity<MatchDTO> startMatch(@Parameter(description = "Id of the lobby to start the match") @PathVariable Integer matchId) {
        Match startedMatch = ms.startMatch(matchId);
        return ResponseEntity.ok(new MatchDTO(startedMatch));
    }



    
}
