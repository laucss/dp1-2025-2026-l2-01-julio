package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.auth.payload.response.MessageResponse;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DrawCardResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.ActionPointsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.EscapeAttemptResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchHistorialDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MoveNpcToRoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MoveToRoomDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.NpcLocationUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.PlayerLocationUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.StrengthUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Matches", description = "API for the management of Matches")
@SecurityRequirement(name = "bearerAuth")
public class MatchController {
    MatchService ms;
    PlayerService playerService;
    HandService handService; 
    BagService bagService; 
    DeckService deckService;
    MatchWebsocketController matchWebsocketController;
    RoomService roomService;

    @Autowired
    public MatchController(MatchService ms, PlayerService playerService, HandService handService, 
                          BagService bagService, DeckService deckService, MatchWebsocketController matchWebsocketController,
                          RoomService roomService){
        this.ms=ms;
        this.playerService=playerService;
        this.handService=handService; 
        this.bagService=bagService; 
        this.deckService=deckService;
        this.matchWebsocketController=matchWebsocketController;
        this.roomService=roomService;
    }

    @GetMapping("/adjacencies")
    @Operation(summary = "Get all adjacencies", description = "Returns a map where each room id maps to a list of adjacent room ids")
    public Map<Integer, List<Integer>> getAdjacencyMap() {
        return roomService.findAllRooms().stream()
            .collect(Collectors.toMap(
                r -> r.getId(),
                r -> r.getAdjacencyList().stream()
                    .map(adj -> adj.getId())
                    .collect(Collectors.toList())
            ));
    }

    @GetMapping
    public List<Match> getAllGames(@ParameterObject() @RequestParam(value="name",required = false) String name, @ParameterObject @RequestParam(value="status",required = false) MatchStatus status){
        return ms.getAllMatchs();
    }

    @GetMapping("/{matchId}")
    public MatchDTO getMatchById(@PathVariable("matchId")Integer matchId){
        MatchDTO m = ms.getMatchDTOById(matchId);
        return m;
    }



    @GetMapping("/all-Matches")
    public Page<MatchHistorialDTO> getMatches(@RequestParam(defaultValue = "all") String filter,@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "3") Integer size) {
         Page<MatchHistorialDTO> matches;
        switch (filter) {
            case "finished":
                matches = ms.getFinishedMatches(page, size);
                break;

            case "inProgress":
                matches = ms.getInProgressMatches(page, size);
                break;

            default:
                matches = ms.getFinishedAndInProgressMatches(page, size);
        }

        return matches;
    }

    @GetMapping("all-Matches/{userId}")
    public Page<MatchHistorialDTO> getAllMatchesByUser(@PathVariable("userId") Integer userId, @ParameterObject @RequestParam(value="page", defaultValue = "0") Integer page, @ParameterObject @RequestParam(value="size", defaultValue = "10") Integer size){
        Page<MatchHistorialDTO> userMatches = ms.getAllMatchesByUser(userId, page, size);
        return userMatches;
    }

    @GetMapping("matches-played/{userId}")
    public Page<MatchHistorialDTO> getAllMatchesPlayedByUser(@PathVariable("userId") Integer userId, @ParameterObject @RequestParam(value="page", defaultValue = "0") Integer page, @ParameterObject @RequestParam(value="size", defaultValue = "10") Integer size){
        Page<MatchHistorialDTO> userMatches = ms.getMatchesPlayedByUser(userId, page, size);
        return userMatches;
    }

    @GetMapping("matches-created/{userId}")
    public Page<MatchHistorialDTO> getAllMatchesCreatedByUser(@PathVariable("userId") Integer userId, @ParameterObject @RequestParam(value="page", defaultValue = "0") Integer page, @ParameterObject @RequestParam(value="size", defaultValue = "10") Integer size){
        Page<MatchHistorialDTO> matchesCreated = ms.getMatchesPlayedAndCreatedByUser(userId, page, size);
        return matchesCreated;
    }

    @GetMapping("matches-won/{userId}")
    public Page<MatchHistorialDTO> getAllMatchesWonByUser(@PathVariable("userId") Integer userId, @ParameterObject @RequestParam(value="page", defaultValue = "0") Integer page, @ParameterObject @RequestParam(value="size", defaultValue = "10") Integer size){
        Page<MatchHistorialDTO> matchesWon = ms.getMatchesWonByUser(userId, page, size);
        return matchesWon;
    }

    @GetMapping("matches-abandoned/{userId}")
    public Page<MatchHistorialDTO> getAllMatchesAbandonedByUser(@PathVariable("userId") Integer userId, @ParameterObject @RequestParam(value="page", defaultValue = "0") Integer page, @ParameterObject @RequestParam(value="size", defaultValue = "10") Integer size){
        Page<MatchHistorialDTO> matchesAbandoned = ms.getMatchesAbandonedByUser(userId, page, size);
        return matchesAbandoned;
    }

    @GetMapping("/{matchId}/players")
    public List<Player> getPlayersByMatchId(@PathVariable("matchId") Integer matchId) {
        return playerService.getPlayersByMatchId(matchId);
    }

    @GetMapping("/{matchId}/winner")
    public Player getWinnerByMatchId(@PathVariable("matchId") Integer matchId) {
        Player matchWinner = ms.getMatchWinner(matchId);
        return matchWinner;
    }   

    @GetMapping("/user/{userId}/in")
    public Integer userInMatch(@PathVariable("userId") Integer userId) {
        return ms.userInMatch(userId);
    }


    @PostMapping("/{matchId}/submit-dice")
    @Operation(summary = "Decide order", description = "Submit dice roll to decide player order at the start of the match.")
    public ResponseEntity<MatchDTO> submitDice(@PathVariable Integer matchId, @RequestParam Integer userId, @RequestParam Integer diceRoll) {

        try {
            // Llamamos al servicio que guarda la tirada y asigna orden si todos tiraron
            Match m = ms.submitDiceAndAssignOrder(matchId, userId, diceRoll);

            // Devolvemos el estado actualizado de la partida
            return ResponseEntity.ok(new MatchDTO(m));

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

    @PutMapping("/{matchId}/finish/{winnerId}")
    public ResponseEntity<MatchDTO> finishMatch(
            @PathVariable Integer matchId,
            @PathVariable Integer winnerId) {

        Player winner = playerService.findById(winnerId);
        return ResponseEntity.ok(ms.endMatch(matchId, winner));
    }

    @PutMapping("/{matchId}/end")
    public ResponseEntity<MatchDTO> endMatch(@PathVariable Integer matchId) {

    MatchDTO ended = ms.endMatch(matchId, null);
    return ResponseEntity.ok(ended);
}

    @PutMapping("/{matchId}/leaveMatch")
    public ResponseEntity<MatchDTO> leaveMatch(@PathVariable("matchId") Integer matchId, @RequestBody @Valid Integer userId) {
        MatchDTO match = ms.leaveMatch(matchId, userId);
        return ResponseEntity.ok(match);
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
        Match mToUpdate=ms.getMatchById(id); 
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

    @Operation(summary = "Confirm the end of discard phase", description = "Confirm the bag formed and the discarded cards to pass turn.")
    @PutMapping("/{matchId}/confirmDiscardPhase")
    public ResponseEntity<Integer> confirmDiscardPhase(@PathVariable Integer matchId, @RequestBody @Valid AllCardsStatusDTO data){
        Integer nextTurnId = ms.confirmDiscardPhase(matchId, data); 
        return ResponseEntity.ok(nextTurnId); 
        
    }

    @PostMapping("/{matchId}/{playerId}/drawCardFromDeck")
    public ResponseEntity<DrawCardResultDTO> drawCardFromDeck (@PathVariable Integer matchId, @PathVariable Integer playerId){
        DrawCardResultDTO result = ms.playerDrawsCardFromDeck(matchId, playerId); 
        return ResponseEntity.ok(result); 

    } 

    /* 
    @PostMapping("/{matchId}/{playerId}/drawRewardCard")
    public ResponseEntity<DrawCardResultDTO> drawRewardCard (@PathVariable Integer matchId, @PathVariable Integer playerId){
        DrawCardResultDTO result = ms.playerDrawsRewardCard(matchId, playerId);
        return ResponseEntity.ok(result);

    }
        */

    /* 
    @PostMapping("/{matchId}/{playerId}/playerWinsNiallCampbell")
    public ResponseEntity<DrawCardResultDTO> playerWinsNiallCampbell (@PathVariable Integer matchId, @PathVariable Integer playerId){
        Card card = ms.playerWinsNiallCampbell(matchId, playerId);
        // Si no hay carta descartada, retorna null sin robar nada
        DeckInGame deck = deckService.findDeckById(matchId);
        HandInGame hand = handService.findPlayerHand(matchId, playerId);
        DrawCardResultDTO result = new DrawCardResultDTO(card, deck, hand);

        // Notificar por WebSocket el estado actualizado de cartas (incluye deck/discard)
        AllCardsStatusDTO playerCards = ms.getAllCards(matchId, playerId);
        CardsUpdateDTO update = new CardsUpdateDTO(matchId, playerCards, playerCards);
        matchWebsocketController.notifyCardsUpdate(matchId, update);

        return ResponseEntity.ok(result);

    }
    */

    @GetMapping("/{matchId}/{playerId}/getAllCards")
    public ResponseEntity<AllCardsStatusDTO> getAllCards (@PathVariable Integer matchId, @PathVariable Integer playerId){
        AllCardsStatusDTO result = ms.getAllCards(matchId, playerId); 
        return ResponseEntity.ok(result); 
    }

    @PutMapping("/{matchId}/move")
    public ResponseEntity<MatchDTO> moveToAdyacentRoom (@PathVariable Integer matchId, @RequestBody MoveToRoomDTO data){
        ms.movePlayerToAdyacentRoom(matchId, data.getUserId(), data.getRoomId()); 
        Match match = ms.getMatchById(matchId);
        
        Player movedPlayer = match.getPlayers().stream()
            .filter(p -> p.getUser().getId().equals(data.getUserId()))
            .findFirst()
            .orElse(null);
        
        if (movedPlayer != null) {
            PlayerLocationUpdateDTO locationUpdate = new PlayerLocationUpdateDTO(movedPlayer);
            matchWebsocketController.notifyPlayerLocationUpdate(matchId, locationUpdate);
            
            ActionPointsUpdateDTO actionPointsUpdate = new ActionPointsUpdateDTO(
                movedPlayer.getId(),
                movedPlayer.getUser().getId(),
                movedPlayer.getUser().getUsername(),
                movedPlayer.getActionPoints(),
                System.currentTimeMillis()
            );
            matchWebsocketController.notifyActionPointsUpdate(matchId, actionPointsUpdate);
        }
        
        return ResponseEntity.ok(new MatchDTO(match)); 
    }

    
    @PutMapping("/{matchId}/moveNpc")
    public ResponseEntity<MatchDTO> moveNpcToRoom (@PathVariable Integer matchId, @RequestBody MoveNpcToRoomDTO data){
        ms.moveNpcToRoom(matchId, data.getNpcId(), data.getRoomId(), data.getUserId()); 
        Match match = ms.getMatchById(matchId);
        
        return ResponseEntity.ok(new MatchDTO(match)); 
    }

    /* 
    @PutMapping("/{matchId}/moveLoser")
    public ResponseEntity<MatchDTO> moveLoserToRandomRoom (@PathVariable Integer matchId, @RequestBody MoveToRoomDTO data){
        ms.moveLoserPlayer(matchId, data.getUserId(), data.getRoomId()); 
        Match match = ms.getMatchById(matchId);
        
        Player movedPlayer = match.getPlayers().stream()
            .filter(p -> p.getUser().getId().equals(data.getUserId()))
            .findFirst()
            .orElse(null);
        
        if (movedPlayer != null) {
            PlayerLocationUpdateDTO locationUpdate = new PlayerLocationUpdateDTO(movedPlayer);
            matchWebsocketController.notifyPlayerLocationUpdate(matchId, locationUpdate);
            
            ActionPointsUpdateDTO actionPointsUpdate = new ActionPointsUpdateDTO(
                movedPlayer.getId(),
                movedPlayer.getUser().getId(),
                movedPlayer.getUser().getUsername(),
                movedPlayer.getActionPoints(),
                System.currentTimeMillis()
            );
            matchWebsocketController.notifyActionPointsUpdate(matchId, actionPointsUpdate);
            
            StrengthUpdateDTO strengthUpdate = new StrengthUpdateDTO(
                movedPlayer.getId(),
                movedPlayer.getUser().getId(),
                movedPlayer.getUser().getUsername(),
                movedPlayer.getStrength(),
                System.currentTimeMillis()
            );
            matchWebsocketController.notifyStrengthUpdate(matchId, strengthUpdate);
        }
        
        return ResponseEntity.ok(new MatchDTO(match)); 
    }
        */

    @PutMapping("/{matchId}/moveByLetters")
    public ResponseEntity<MatchDTO> moveByFormingRoomName(@PathVariable Integer matchId, @RequestBody MoveToRoomDTO data) {
        ms.movePlayerByFormingRoomName(matchId, data.getUserId(), data.getRoomId());
        Match match = ms.getMatchById(matchId);
        
        Player movedPlayer = match.getPlayers().stream()
            .filter(p -> p.getUser().getId().equals(data.getUserId()))
            .findFirst()
            .orElse(null);
        
        if (movedPlayer != null) {
            PlayerLocationUpdateDTO locationUpdate = new PlayerLocationUpdateDTO(movedPlayer);
            matchWebsocketController.notifyPlayerLocationUpdate(matchId, locationUpdate);
            
            ActionPointsUpdateDTO actionPointsUpdate = new ActionPointsUpdateDTO(
                movedPlayer.getId(),
                movedPlayer.getUser().getId(),
                movedPlayer.getUser().getUsername(),
                movedPlayer.getActionPoints(),
                System.currentTimeMillis()
            );
            matchWebsocketController.notifyActionPointsUpdate(matchId, actionPointsUpdate);
        }
        
        return ResponseEntity.ok(new MatchDTO(match));
    }

    @GetMapping("/{matchId}/{playerId}/actionPoints")
    public ResponseEntity<Integer> getActionPoints(@PathVariable Integer matchId, @PathVariable Integer playerId) {
        Integer actionPoints = playerService.getPlayerActionPoints(matchId, playerId); 
        return ResponseEntity.ok(actionPoints);
    }



    @PostMapping("/{matchId}/notify-action-points")
    @Operation(summary = "Notify action points", description = "Notifies all players when action points are updated.")
    public ResponseEntity<Void> notifyActionPoints(@PathVariable Integer matchId, @RequestBody ActionPointsUpdateDTO actionPointsUpdate) {
        matchWebsocketController.notifyActionPointsUpdate(matchId, actionPointsUpdate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{matchId}/consume-all-action-points/{userId}")
    @Operation(summary = "Consume all action points", description = "Consumes all action points for a user and notifies all players.")
    public ResponseEntity<Void> consumeAllActionPoints(@PathVariable Integer matchId, @PathVariable Integer userId) {
        ActionPointsUpdateDTO actionPointsUpdate = ms.consumeAllActionPointForUser(matchId, userId);
        matchWebsocketController.notifyActionPointsUpdate(matchId, actionPointsUpdate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{matchId}/consume-action-point/{userId}")
    @Operation(summary = "Consume one action point", description = "Consumes one action point for a user and notifies all players.")
    public ResponseEntity<Void> consumeOneActionPoint(@PathVariable Integer matchId, @PathVariable Integer userId) {
        ActionPointsUpdateDTO actionPointsUpdate = ms.consumeOneActionPoint(matchId, userId);
        matchWebsocketController.notifyActionPointsUpdate(matchId, actionPointsUpdate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{matchId}/notify-strength")
    @Operation(summary = "Notify strength", description = "Notifies all players when strength is updated.")
    public ResponseEntity<Void> notifyStrength(@PathVariable Integer matchId, @RequestBody StrengthUpdateDTO strengthUpdate) {
        matchWebsocketController.notifyStrengthUpdate(matchId, strengthUpdate);
        return ResponseEntity.ok().build();
    }


    /* 
    @PostMapping("/{matchId}/{winnerId}/steal-card-from/{loserId}")
    @Operation(summary = "Steal card from another player", description = "Winner steals a card from loser, either from hand (random/selected) or bag (selected). Returns updated card states for both players.")
    public ResponseEntity<Map<String, AllCardsStatusDTO>> stealCardFromPlayer(@PathVariable Integer matchId, @PathVariable Integer winnerId,@PathVariable Integer loserId, @Valid @RequestBody StealCardRequestDTO request) {

        try {
            Map<String, AllCardsStatusDTO> cardsStatus = ms.stealCardFromPlayer(matchId, winnerId, loserId, request);
            
            AllCardsStatusDTO winnerCards = cardsStatus.get("winner");
            AllCardsStatusDTO loserCards = cardsStatus.get("loser");
            
            CardsUpdateDTO update = new CardsUpdateDTO(matchId, winnerCards, loserCards);
            matchWebsocketController.notifyCardsUpdate(matchId, update);
            
            return ResponseEntity.ok(cardsStatus);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error al robar carta: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
        */

    /* 
    @PostMapping("/{matchId}/{playerId}/lose-against-npc")
    @Operation(summary = "Player loses against NPC", description = "Player chooses a card to discard after losing to an NPC.")
    public ResponseEntity<AllCardsStatusDTO> playerLosesAgainstNpc(@PathVariable Integer matchId, @PathVariable Integer playerId, @Valid @RequestBody LoseAgainstNpcRequestDTO request) {
        try {
            AllCardsStatusDTO updatedCards = ms.playerLosesAgainstNpc(matchId, playerId, request);
            CardsUpdateDTO update = new CardsUpdateDTO(matchId, updatedCards, updatedCards);
            matchWebsocketController.notifyCardsUpdate(matchId, update);
            return ResponseEntity.ok(updatedCards);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error al descartar carta tras perder contra NPC: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
        */

    /* 
    @PostMapping("/{matchId}/notify-fight-resolved")
    @Operation(summary = "Notify fight resolved", description = "Notifies all players when a fight is resolved and winner can steal a card.")
    public ResponseEntity<Void> notifyFightResolved(
            @PathVariable Integer matchId,
            @RequestBody FightResolvedDTO fightResolved) {
        matchWebsocketController.notifyFightResolved(matchId, fightResolved);
        return ResponseEntity.ok().build();
    }
        */

    @PutMapping("/{matchId}/player-strength")
    @Operation(summary = "Update player strength", description = "Updates the strength of a player and notifies all players.")
    public ResponseEntity<MatchDTO> updatePlayerStrength(@PathVariable Integer matchId, @RequestBody Map<String, Integer> data) {
        Integer userId = data.get("userId");
        Integer newStrength = data.get("strength");
        
        Player player = playerService.findByMatchIdAndUserId(matchId, userId)
            .orElseThrow(() -> new RuntimeException("Player no encontrado"));
        player.setStrength(newStrength);
        playerService.save(player);
        
        Match match = ms.getMatchById(matchId);
        
        StrengthUpdateDTO strengthUpdate = new StrengthUpdateDTO(
            player.getId(),
            player.getUser().getId(),
            player.getUser().getUsername(),
            player.getStrength(),
            System.currentTimeMillis()
        );
        matchWebsocketController.notifyStrengthUpdate(matchId, strengthUpdate);
        
        return ResponseEntity.ok(new MatchDTO(match));
    }

    @PutMapping("/{matchId}/npc-strength")
    @Operation(summary = "Update NPC strength", description = "Updates the strength of an NPC and notifies all players.")
    public ResponseEntity<MatchDTO> updateNpcStrength(@PathVariable Integer matchId, @RequestBody Map<String, Integer> data) {
        Integer npcId = data.get("npcId");
        Integer newStrength = data.get("strength");
        
        Match match = ms.getMatchById(matchId);
        Npc npc = match.getNpcs().stream()
            .filter(n -> n.getId().equals(npcId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("NPC no encontrado"));
        
        npc.setStrength(newStrength);
        ms.save(match);
        
        return ResponseEntity.ok(new MatchDTO(match));
    }

    @PutMapping("/{matchId}/npc/location")
    @Operation(summary = "Force move NPC", description = "Moves an NPC to a specified room without consuming player actions (used for system-driven moves).")
    public ResponseEntity<MatchDTO> forceMoveNpc(@PathVariable Integer matchId, @RequestBody Map<String, Integer> data) {
        Integer npcId = data.get("npcId");
        Integer roomId = data.get("roomId");
        
        Match match = ms.getMatchById(matchId);
        Npc npc = match.getNpcs().stream()
            .filter(n -> n.getId().equals(npcId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("NPC no encontrado"));
        
        npc.setRoom(roomService.findById(roomId));
        ms.save(match);
        
        // Notify all clients about NPC location update
        NpcLocationUpdateDTO locationUpdate = new NpcLocationUpdateDTO(npc);
        matchWebsocketController.notifyNpcLocationUpdate(matchId, locationUpdate);
        
        return ResponseEntity.ok(new MatchDTO(match));
    }


    @PostMapping("/{matchId}/escape-attempt")
    public ResponseEntity<EscapeAttemptResultDTO> attemptEscape(@PathVariable Integer matchId, @RequestParam Integer userId, @RequestParam Integer rollDice){
        EscapeAttemptResultDTO result = ms.escapeAttempt(matchId, userId, rollDice);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{matchId}/spectate")
    public ResponseEntity<MatchDTO> spectateGame (@PathVariable Integer matchId) {
        MatchDTO m = ms.spectateGame(matchId);
        return ResponseEntity.ok(m); 
    }

    @DeleteMapping("/{matchId}/StopSpectating")
    public ResponseEntity<MessageResponse> stopSpectating (@PathVariable Integer matchId) {
        ms.stopSpectating(matchId);
        return new ResponseEntity<>(new MessageResponse("Stopped spectating game " + matchId + "."), HttpStatus.OK);
    }



}
