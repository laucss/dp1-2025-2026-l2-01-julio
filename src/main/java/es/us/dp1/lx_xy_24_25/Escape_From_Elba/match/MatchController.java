package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


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

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DrawCardResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;
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
    PlayerService playerService;
    HandService handService; 
    BagService bagService; 
    DeckService deckService;
    MatchWebsocketController matchWebsocketController;
    RoomService roomService;

    @Autowired
    public MatchController(MatchService ms, LobbyService ls, PlayerService playerService, HandService handService, 
                          BagService bagService, DeckService deckService, MatchWebsocketController matchWebsocketController,
                          RoomService roomService){
        this.ms=ms;
        this.ls=ls;
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

    
    @PostMapping("/lobbies")
    @Operation(summary = "Create lobby", description = "Create a new game.")
    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<Match> createLobby(@Valid @RequestBody LobbyDTO lobbyDTO) {
        Match saved= ls.createLobby(lobbyDTO.getIsPrivate(), lobbyDTO.getName(), lobbyDTO.getMaxPlayers(), lobbyDTO.getNumNpcs());
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
    public ResponseEntity<MatchDTO> startMatch(@Parameter(description = "Id of the lobby to start the match") @PathVariable Integer matchId) {
        Match startedMatch = ms.startMatch(matchId);
        return ResponseEntity.ok(new MatchDTO(startedMatch));
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

    @PutMapping("/{matchId}/end")
    public ResponseEntity<MatchDTO> endMatch(@PathVariable("matchId") Integer matchId, @RequestBody @Valid Integer winnerId) {
        Player winner = playerService.findById(winnerId);
        Match ended = ms.endMatch(matchId,winner);
        return ResponseEntity.ok(new MatchDTO(ended));
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

    @PostMapping("/{matchId}/{playerId}/drawRewardCard")
    public ResponseEntity<DrawCardResultDTO> drawRewardCard (@PathVariable Integer matchId, @PathVariable Integer playerId){
        DrawCardResultDTO result = ms.playerDrawsRewardCard(matchId, playerId);
        return ResponseEntity.ok(result);

    }

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
    public ResponseEntity<MatchDTO> moveNpcToAdyacentRoom (@PathVariable Integer matchId, @RequestBody MoveNpcToRoomDTO data){
        ms.moveNpcToAdyacentRoom(matchId, data.getNpcId(), data.getRoomId(), data.getUserId()); 
        Match match = ms.getMatchById(matchId);
        
        return ResponseEntity.ok(new MatchDTO(match)); 
    }

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

    @GetMapping("/{matchId}/{playerId}/actionPoints")
    public ResponseEntity<Integer> getActionPoints(@PathVariable Integer matchId, @PathVariable Integer playerId) {
        Integer actionPoints = playerService.getPlayerActionPoints(matchId, playerId); 
        return ResponseEntity.ok(actionPoints);
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

    @PostMapping("/{matchId}/{winnerId}/steal-card-from/{loserId}")
    @Operation(summary = "Steal card from another player", description = "Winner steals a card from loser, either from hand (random/selected) or bag (selected). Returns updated card states for both players.")
    public ResponseEntity<Map<String, AllCardsStatusDTO>> stealCardFromPlayer(
            @PathVariable Integer matchId,
            @PathVariable Integer winnerId,
            @PathVariable Integer loserId,
            @Valid @RequestBody StealCardRequestDTO request) {

        try {
            // Basic validation of source
            String fromWhere = request.getFromWhere();
            if (fromWhere == null || (!fromWhere.equals("hand") && !fromWhere.equals("bag"))) {
                return ResponseEntity.badRequest().build();
            }

            // Build a Card reference using the complete card object when provided; null means random for 'hand'
            es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card cardRef = null;
            if (request.getCardId() != null && fromWhere.equals("bag")) {
                // Para robo de bolsa, buscar la carta completa por ID
                BagInGame loserBag = bagService.findPlayerBag(matchId, loserId);
                
                Integer cardId = request.getCardId();
                final Integer finalCardId = cardId;
                cardRef = loserBag.getCards().stream()
                    .filter(c -> c.getId() != null && c.getId().equals(finalCardId))
                    .findFirst()
                    .orElse(null);
                
                if (cardRef == null) {
                    return ResponseEntity.badRequest().body(null);
                }
            }

            // Execute steal in service layer
            Integer currentTurnUserId = ms.getMatchById(matchId).getCurrentTurnUserId();
            ms.playerDrawsCardFromAnotherPlayerBag(cardRef, matchId, winnerId, loserId, fromWhere, currentTurnUserId);

            // Return updated card states for winner and loser and notify via WS
            AllCardsStatusDTO winnerCards = ms.getAllCards(matchId, winnerId);
            AllCardsStatusDTO loserCards = ms.getAllCards(matchId, loserId);

            CardsUpdateDTO update = new CardsUpdateDTO(matchId, winnerCards, loserCards);
            matchWebsocketController.notifyCardsUpdate(matchId, update);

            return ResponseEntity.ok(Map.of("winner", winnerCards, "loser", loserCards));
        } catch (Exception e) {
            System.err.println("Error al robar carta: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{matchId}/{playerId}/lose-against-npc")
    @Operation(summary = "Player loses against NPC", description = "Player chooses a card to discard after losing to an NPC.")
    public ResponseEntity<AllCardsStatusDTO> playerLosesAgainstNpc(
            @PathVariable Integer matchId,
            @PathVariable Integer playerId,
            @Valid @RequestBody LoseAgainstNpcRequestDTO request) {
        try {
            String fromWhere = request.getFromWhere();
            Integer cardId = request.getCardId();

            if (fromWhere == null || (!"hand".equals(fromWhere) && !"bag".equals(fromWhere))) {
                return ResponseEntity.badRequest().build();
            }
            if (cardId == null) {
                return ResponseEntity.badRequest().build();
            }

            Card cardRef = null;
            if ("hand".equals(fromWhere)) {
                HandInGame hand = handService.findPlayerHand(matchId, playerId);
                cardRef = hand.getCards().stream()
                    .filter(c -> c.getId() != null && c.getId().equals(cardId))
                    .findFirst()
                    .orElse(null);
            } else {
                BagInGame bag = bagService.findPlayerBag(matchId, playerId);
                cardRef = bag.getCards().stream()
                    .filter(c -> c.getId() != null && c.getId().equals(cardId))
                    .findFirst()
                    .orElse(null);
            }

            if (cardRef == null) {
                return ResponseEntity.badRequest().build();
            }

            Integer currentTurnUserId = ms.getMatchById(matchId).getCurrentTurnUserId();
            ms.playerLosesAgaintsNonPlayer(cardRef, matchId, playerId, currentTurnUserId, fromWhere);

            AllCardsStatusDTO updatedCards = ms.getAllCards(matchId, playerId);
            CardsUpdateDTO update = new CardsUpdateDTO(matchId, updatedCards, updatedCards);
            matchWebsocketController.notifyCardsUpdate(matchId, update);

            return ResponseEntity.ok(updatedCards);
        } catch (Exception e) {
            System.err.println("Error al descartar carta tras perder contra NPC: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{matchId}/notify-fight-resolved")
    @Operation(summary = "Notify fight resolved", description = "Notifies all players when a fight is resolved and winner can steal a card.")
    public ResponseEntity<Void> notifyFightResolved(
            @PathVariable Integer matchId,
            @RequestBody FightResolvedDTO fightResolved) {
        matchWebsocketController.notifyFightResolved(matchId, fightResolved);
        return ResponseEntity.ok().build();
    }

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

}
