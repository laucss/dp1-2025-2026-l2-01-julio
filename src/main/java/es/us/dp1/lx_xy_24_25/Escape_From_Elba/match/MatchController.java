package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.net.URI;
import java.util.List;
import java.util.Optional;


import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.ConfirmDiscardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DrawCardResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Matches", description = "API for the management of Matches")
@SecurityRequirement(name = "bearerAuth")
public class MatchController {
    MatchService ms;
    LobbyService ls;
    PlayerService ps;
    HandService handService; 
    BagService bagService; 
    DeckService deckService; 

    @Autowired
    public MatchController(MatchService ms, LobbyService ls, PlayerService ps, HandService handService, BagService bagService, DeckService deckService){
        this.ms=ms;
        this.ls=ls;
        this.ps=ps;
        this.handService=handService; 
        this.bagService=bagService; 
        this.deckService=deckService; 
    }

    @GetMapping
    public List<Match> getAllGames(@ParameterObject() @RequestParam(value="name",required = false) String name, @ParameterObject @RequestParam(value="status",required = false) MatchStatus status){
        return ms.getAllMatchs();
    }

    @GetMapping("/{matchId}")
    public Match getMatchById(@PathVariable("matchId")Integer matchId){
        Optional<Match> m=ms.getMatchById(matchId);
        if(!m.isPresent())
            throw new ResourceNotFoundException("Match", "id", matchId);
        return m.get();
    }

    @GetMapping("/lobbies/private/{matchId}")
    public Optional<Match> getPrivateGame(@ParameterObject String code){
        return ls.getPrivateLobby(code);
    }

    @GetMapping("/lobbies/{matchId}")
    public Optional<Match> getLobbyById(@PathVariable("matchId") Integer matchId) {
        return ls.getById(matchId);
    }

    @GetMapping("/lobbies")
    @Operation(summary = "Get public matches", description = "Get all public matches available to join.")
    public List<Match> getPublicGames(){
        return ls.getAllPublicLobbies();
    }

    @GetMapping("/lobbies/privates")
    public List<Match> getPrivateLobbies(){
        return ls.getAllPrivateLobbies();
    }

    @GetMapping("/{matchId}/players")
    public List<Player> getPlayersByMatchId(@PathVariable("matchId") Integer matchId) {
        return ps.getPlayersByMatchId(matchId);
    }

    @GetMapping("/user/{userId}/in")
    public Integer userInMatch(@PathVariable("userId") Integer userId) {
        return ms.userInMatch(userId);
    }

    
    @PostMapping("/lobbies")
    @Operation(summary = "Create lobby", description = "Create a new game.")
    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<Match> createLobby(@RequestBody LobbyDTO lobbyDTO) {
        Match game = new Match();
        Match saved= ls.createLobby(game, lobbyDTO.getIsPrivate(), lobbyDTO.getName(), lobbyDTO.getMaxPlayers(), lobbyDTO.getNumNpcs());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/lobbies/{matchId}/join")
    @Operation(summary = "Join public lobby", description = "Join a public lobby from a list of available lobbies.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Match> joinLobby(@Parameter(description = "Id of the lobby to join") @PathVariable Integer matchId) {
        Match joinedMatch = ls.joinLobby(matchId);

        return ResponseEntity.ok(joinedMatch);


    }
    
    @PostMapping("/lobbies/join/private")
    @Operation(summary = "Join private lobby", description = "Join a private lobby using its code.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Match> joinPrivateLobby(@RequestParam String code) {
        Match joinedMatch = ls.joinPrivateLobby(code);
        return ResponseEntity.ok(joinedMatch);
    }

    @PostMapping("/lobbies/{matchId}/leave")
    @Operation(summary = "Leave a lobby", description = "Leva a lobby before the game starts.")
    public ResponseEntity<Match> leaveLobby(@Parameter(description = "Id of the lobby to leave") @PathVariable Integer matchId) {
        Match leftMatch = ls.leaveLobby(matchId);
        return ResponseEntity.ok(leftMatch);
    }

    @PostMapping("/lobbies/{matchId}/start")
    @Operation(summary = "Start match", description = "Start a match from a lobby.")
    public ResponseEntity<Match> startMatch(@Parameter(description = "Id of the lobby to start the match") @PathVariable Integer matchId) {
        Match startedMatch = ms.startMatch(matchId);
        return ResponseEntity.ok(startedMatch);
    }

    @PostMapping("/{matchId}/submit-dice")
    @Operation(summary = "Decide order", description = "Submit dice roll to decide player order at the start of the match.")
    public ResponseEntity<Match> submitDice(@PathVariable Integer matchId, @RequestParam Integer userId, @RequestParam Integer diceRoll) {

        try {
            // Llamamos al servicio que guarda la tirada y asigna orden si todos tiraron
            Match m = ms.submitDiceAndAssignOrder(matchId, userId, diceRoll);

            // Devolvemos el jugador actualizado
            return ResponseEntity.ok(m);

        } catch (IllegalArgumentException e) {
            // Si hubo algún error (jugador no encontrado, ya tiró, etc.)
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{matchId}/next-turn")
    @Operation(summary = "Next turn", description = "Advance to the next player's turn in the match.")
    public ResponseEntity<Void> nextTurn(@PathVariable("matchId") Integer matchId) {
        ms.nextTurn(matchId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{matchId}/end")
    public ResponseEntity<Match> endMatch(@PathVariable("matchId") Integer matchId) {
        Match ended = ms.endMatch(matchId);
        return ResponseEntity.ok(ended);
    }



    @PostMapping()
    public ResponseEntity<Match> createGame(@Valid @RequestBody Match m){
        m=ms.save(m);
        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(m.getId())
                .toUri();
        return ResponseEntity.created(location).body(m);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Void> updateGame(@Valid @RequestBody Match m,@PathVariable("id")Integer id){
        Match mToUpdate=getMatchById(id);
        BeanUtils.copyProperties(m,mToUpdate, "id");
        ms.save(mToUpdate);
        return ResponseEntity.noContent().build(); 
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable("id")Integer id){
        if(getMatchById(id)!=null)
            ms.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{matchId}/discardConfirmed")
    public ResponseEntity<?> updateAfterDiscard(@PathVariable Integer matchId, @RequestBody ConfirmDiscardDTO data){
        handService.update(data.getHand(), matchId, data.getPlayerId());
        bagService.update(data.getBag(), matchId, data.getPlayerId());
        deckService.update(data.getDeck(), matchId);

        return ResponseEntity.ok().build(); 
        
    }

    @PostMapping("/{matchId}/{playerId}/drawCardFromDeck")
    public ResponseEntity<DrawCardResultDTO> drawCardFromDeck (@PathVariable Integer matchId, @PathVariable Integer playerId){
        DrawCardResultDTO result = ms.playerDrawsCardFromDeck(matchId, playerId); 

        System.out.println(result);
        return ResponseEntity.ok(result); 

    } 
}

