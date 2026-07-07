package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightResolvedDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightResultRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.LoseAgainstNpcRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.StealCardRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchWebsocketController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.ActionPointsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.CardsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.NpcLocationUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.PlayerLocationUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.StrengthUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.NpcRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;



@Service
public class FightService {

    MatchService matchService;
    DeckService deckService; 
    HandService handService; 
    BagService bagService;
    PlayerService playerService; 
    MatchWebsocketController matchWebsocketController;
    Checkers checkers; 

    MatchRepository matchRepo;
    PlayerRepository playerRepo;
    NpcRepository npcRepository;
    RoomService roomService;

    @Autowired
    public FightService(MatchRepository mrepo, PlayerRepository playerRepo,
            RoomService roomService, DeckService deckService, HandService handService, BagService bagService, 
            PlayerService playerService, MatchWebsocketController matchWebsocketController, NpcRepository npcRepository, 
            Checkers checkers, MatchService matchService) {
        this.matchRepo = mrepo;
        this.playerRepo = playerRepo;
        this.npcRepository = npcRepository;
        this.roomService = roomService;
        this.deckService = deckService;
        this.handService = handService;
        this.bagService = bagService; 
        this.playerService = playerService;
        this.matchWebsocketController = matchWebsocketController;
        this.checkers = checkers; 
        this.matchService = matchService;
    }



    @Transactional
    public FightResolvedDTO processFightResolution(FightResultRequestDTO result) {
        matchRepo.findById(result.getMatchId()).orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        FightResolvedDTO fightResult = new FightResolvedDTO();

        if (result.isNpcFight()) {
            // el npc gana al player en dos casos: 
            //      1. el npc ataca y gana como atacante 
            //      2. el player ataca pero pierde 
            if ((result.isAttackerWins() && result.isNpcAttacker()) || (!result.isAttackerWins() && !result.isNpcAttacker())) { 
                fightResult = npcBeatsPlayer(result);
            } else  { // el jugador gana al npc en el resto de situaciones
                fightResult = playerBeatsNpc(result);
            }
        } else {
            fightResult = playerBeatsPlayer(result);    
        }

        matchWebsocketController.notifyFightResolved(result.getMatchId(), fightResult);
        return fightResult;

    }

    
    @Transactional
    public FightResolvedDTO npcBeatsPlayer(FightResultRequestDTO result){
        Player player = !result.isAttackerWins() && !result.isNpcAttacker() ? playerService.findById(result.getAttackerId()) : playerService.findById(result.getDefenderId());
        
        Integer npcId = result.isAttackerWins() && result.isNpcAttacker() ? result.getAttackerId() : result.getDefenderId(); 
        npcRepository.findById(npcId).orElseThrow(() -> new ResourceNotFoundException("Npc not found")); 

        Room room = playerLoses(player, result.getMatchId(), result);


        FightResolvedDTO dto = new FightResolvedDTO(
            result.getMatchId(),
            npcId,
            player.getId(),
            player.getUser().getId(),
            room.getId(),
            FightResultType.NPC_BEATS_PLAYER
        );

        return dto;
    }

    @Transactional
    public FightResolvedDTO playerBeatsNpc(FightResultRequestDTO result){
        Player player = result.isAttackerWins() && !result.isNpcAttacker() ? playerService.findById(result.getAttackerId()) : playerService.findById(result.getDefenderId());
        
        Integer npcId = !result.isAttackerWins() && result.isNpcAttacker() ? result.getAttackerId() : result.getDefenderId(); 
        Npc loser = npcRepository.findById(npcId).orElseThrow(() -> new ResourceNotFoundException("Npc not found"));
        
        Integer matchId = result.getMatchId();

        Room room = npcLoses(loser, player, result.getMatchId(), result.getDefenderRoomId());

        // Actualizar estadísticas de batallas del jugador
        updatePlayerStatistics(player);

        FightResolvedDTO dto;

        if (loser.getIsNiallCampbell()) {
            dto = playerBeatsNiallCampbell(player, npcId, matchId, room.getId());
        } else {
            dto = playerBeatsNormalNPC(player, npcId, matchId, room.getId());
        }

        dto.setChainRoomId(room.getId());
        
        // comprobamos que si hay más npcs en la sala donde se queda el player ganador, para encadenarla
        getPossibleFight(matchId, player.getUser().getId(), result.getDefenderRoomId(), false);

        return dto;

    }

    @Transactional
    public FightResolvedDTO playerBeatsNiallCampbell(Player player, Integer npcId, Integer matchId, Integer roomId){
        Card discardedCard = deckService.getAndRemoveLastDiscardedCard(matchId);  

        if (discardedCard != null){
            // no hay carta descartada que dar al jugador
            handService.addCardToPlayerHand(discardedCard, matchId, player.getId());
        } else {
            discardedCard = new Card(); 
        }

        // Si no hay carta descartada, retorna null sin robar nada
        // DeckInGame deck = deckService.findDeckById(matchId);
        // HandInGame hand = handService.findPlayerHand(matchId, player.getId());
        // DrawCardResultDTO result = new DrawCardResultDTO(discardedCard, deck, hand);

        // Notificar por WebSocket el estado actualizado de cartas (incluye deck/discard)
        AllCardsStatusDTO playerCards = matchService.getAllCards(matchId, player.getId());
        CardsUpdateDTO update = new CardsUpdateDTO(matchId, playerCards);
        matchWebsocketController.notifyCardsUpdate(matchId, update);

        return new FightResolvedDTO(matchId, player.getUser().getId(), player.getId(), npcId, roomId, discardedCard, FightResultType.PLAYER_BEATS_NPC); 

    }

    @Transactional
    public FightResolvedDTO playerBeatsNormalNPC(Player player, Integer npcId, Integer matchId, Integer roomId){
        Card stolenCard = deckService.drawCard(matchId);
        deckService.findDeckById(matchId);

        handService.addCardToPlayerHand(stolenCard, matchId,  player.getId());

        // Notificar cambios de cartas por WebSocket
        AllCardsStatusDTO playerCards = matchService.getAllCards(matchId, player.getId());
        CardsUpdateDTO update = new CardsUpdateDTO(matchId, playerCards, null);
        matchWebsocketController.notifyCardsUpdate(matchId, update);
        
        return new FightResolvedDTO(matchId, player.getUser().getId(), player.getId(), npcId, roomId, stolenCard, FightResultType.PLAYER_BEATS_NPC);   
    }

    
    @Transactional
    public FightResolvedDTO playerBeatsPlayer(FightResultRequestDTO result){
        Player winner = result.isAttackerWins() ? playerService.findById(result.getAttackerId()) : playerService.findById(result.getDefenderId()); 
        Player loser = result.isAttackerWins() ? playerService.findById(result.getDefenderId()) : playerService.findById(result.getAttackerId()); 
      
        Room defenderRoom = roomService.findById(result.getDefenderRoomId());
        Room room = playerLoses(loser, result.getMatchId(), result);
        winner.setRoom(defenderRoom);
        playerRepo.save(winner);

        PlayerLocationUpdateDTO locationUpdate = new PlayerLocationUpdateDTO(winner);
        matchWebsocketController.notifyPlayerLocationUpdate(result.getMatchId(), locationUpdate);

        // checkeamos por si puede ser que hubieran varios npcs en la misma sala (se hace cadena)
        getPossibleFight(result.getMatchId(), winner.getUser().getId(), result.getDefenderRoomId(), false);


        // si el jugador activo (el que ataca en este caso, pierde entonces sus puntos de accion se quedan a cero)
        if (loser.getId().equals(result.getAttackerId())){
            loserLoseActionPoints(loser, result.getMatchId());
        }
        // Actualizar estadísticas de batallas del jugador
        updatePlayerStatistics(winner);

        FightResolvedDTO dto = new FightResolvedDTO(
            result.getMatchId(),
            winner.getUser().getId(),
            winner.getId(),
            loser.getId(),
            loser.getUser().getId(),
            room.getId(),
            FightResultType.PLAYER_BEATS_PLAYER
        );
        

        return dto;
    }

    
    @Transactional
    public Room playerLoses(Player loser, Integer matchId, FightResultRequestDTO request){
        loser.setStrength(Math.min(6, loser.getStrength() + 1));
        // si el jugador activo pierde contra otro player, pierde sus puntos de acción
        if (!request.isNpcFight() && request.getAttackerId().equals(loser.getId())){
            loser.setActionPoints(0);
            ActionPointsUpdateDTO actionPointsUpdate = matchService.consumeAllActionPointForUser(matchId, loser.getUser().getId());
            matchWebsocketController.notifyActionPointsUpdate(matchId, actionPointsUpdate);
        }
        Room randomRoom = roomService.getRandomRoom();
        loser.setRoom(randomRoom);
        playerRepo.save(loser);

        // vemos si se enlaza una pelea 
        getPossibleFight(matchId, loser.getUser().getId(), randomRoom.getId(), false);

        PlayerLocationUpdateDTO locationUpdate = new PlayerLocationUpdateDTO(loser);
        matchWebsocketController.notifyPlayerLocationUpdate(matchId, locationUpdate);

        StrengthUpdateDTO strengthUpdate = new StrengthUpdateDTO(
                loser.getId(),
                loser.getUser().getId(),
                loser.getUser().getUsername(),
                loser.getStrength(),
                System.currentTimeMillis()
            );
        matchWebsocketController.notifyStrengthUpdate(matchId, strengthUpdate);
       

        return randomRoom;


    }

    @Transactional
    public Room  npcLoses(Npc loser, Player winner, Integer matchId, Integer roomId){
        loser.setStrength(Math.min(6, loser.getStrength() + 1));
        Room defenderRoom = roomService.findById(roomId);
        Room randomRoom  = roomService.getRandomRoom();
        loser.setRoom(randomRoom);
        npcRepository.save(loser);
        
        winner.setRoom(defenderRoom);
        playerRepo.save(winner);

        // vemos si se enlaza una pelea 
        getPossibleFight(matchId, loser.getId(), randomRoom.getId(), true);

        PlayerLocationUpdateDTO locationPlayerUpdate = new PlayerLocationUpdateDTO(winner);
        matchWebsocketController.notifyPlayerLocationUpdate(matchId, locationPlayerUpdate);

        NpcLocationUpdateDTO locationNpcUpdate = new NpcLocationUpdateDTO(loser);
        matchWebsocketController.notifyNpcLocationUpdate(matchId, locationNpcUpdate);

        StrengthUpdateDTO strengthUpdate = new StrengthUpdateDTO(loser.getId(),loser.getStrength(),System.currentTimeMillis());
        matchWebsocketController.notifyStrengthUpdate(matchId, strengthUpdate);

        return randomRoom;
    }





    @Transactional
    public CardDTO playerStealFromPlayer(StealCardRequestDTO request, Integer matchId) {
        // checkeamos que ambos jugadores existen en la partida
        Integer loserId = playerService.findById(request.getLoserId()).getId(); 
        Player winner = playerService.findById(request.getWinnerId());

        // quitamos la carta de la mano o bolsa del perdedor y se la añadimos a la mano del ganador
        Card card = request.getCard() == null ? null : request.getCard();

        if (request.getFromWhere().equals("hand")){ 
            // Selección aleatoria de carta de la mano del perdedor
            HandInGame loserHand = handService.findPlayerHand(matchId, loserId);
            List<Card> loserHandCards = loserHand.getCards();
            if (loserHandCards == null || loserHandCards.isEmpty()) {
                return new CardDTO();
            }
            card = loserHandCards.get((int) Math.floor(Math.random() * loserHandCards.size()));
            handService.removeCardFromPlayerHand(card, matchId, loserId);
            handService.addCardToPlayerHand(card, matchId, winner.getId());

        } else if (request.getFromWhere().equals("bag")){ 
            bagService.removeCardFromPlayerBag(request.getCard(), matchId, loserId);
            handService.addCardToPlayerHand(request.getCard(), matchId, winner.getId()); 
        
        } else {
            throw new IllegalArgumentException("fromWhere must be 'hand' or 'bag'");
        }
        // Actualizar estadísticas de batallas
        updatePlayerStatistics(winner);

        AllCardsStatusDTO winnerCards = matchService.getAllCards(matchId, winner.getId());
        AllCardsStatusDTO loserCards = matchService.getAllCards(matchId, loserId);
        
        CardsUpdateDTO update = new CardsUpdateDTO(matchId, winnerCards, loserCards);
        matchWebsocketController.notifyCardsUpdate(matchId, update);
        
        return new CardDTO(card);

    }


     
    @Transactional
    public void playerLosesAgainstNpc(Integer matchId, Integer playerId, LoseAgainstNpcRequestDTO request){
        String fromWhere = request.getFromWhere();
        Integer cardId = request.getCardId();

        // Validar parámetros
        if (fromWhere == null || (!"hand".equals(fromWhere) && !"bag".equals(fromWhere))) {
            throw new IllegalArgumentException("fromWhere must be 'hand' or 'bag'");
        }
        if (cardId == null) {
            throw new IllegalArgumentException("cardId cannot be null");
        }

        // Buscar la carta en mano o bolsa según corresponda
        Card cardRef = null;
        if ("hand".equals(fromWhere)) {
            HandInGame hand = handService.findPlayerHand(matchId, playerId);
            cardRef = hand.getCards().stream()
                .filter(c -> c.getId() != null && c.getId().equals(cardId))
                .findFirst()
                .orElse(null);
            handService.removeCardFromPlayerHand(cardRef, matchId, playerId);
        } else {
            BagInGame bag = bagService.findPlayerBag(matchId, playerId);
            cardRef = bag.getCards().stream()
                .filter(c -> c.getId() != null && c.getId().equals(cardId))
                .findFirst()
                .orElse(null);
            bagService.removeCardFromPlayerBag(cardRef, matchId, playerId); 
        }

        if (cardRef == null) {
            throw new IllegalArgumentException("Card not found in " + fromWhere);
        }

        // añadimos la carta seleccionada al mazo de descartes
        deckService.addCardToDiscardedPile(matchId, cardRef);

        // Notificar cambios de cartas por WebSocket
        AllCardsStatusDTO playerCards = matchService.getAllCards(matchId, playerId);
        CardsUpdateDTO update = new CardsUpdateDTO(matchId, playerCards, null);
        matchWebsocketController.notifyCardsUpdate(matchId, update);

    }

    



    @Transactional
    public void loserLoseActionPoints(Player loser, Integer matchId){
        ActionPointsUpdateDTO actionPointsUpdate = new ActionPointsUpdateDTO(
                loser.getId(),
                loser.getUser().getId(),
                loser.getUser().getUsername(),
                loser.getActionPoints(),
                System.currentTimeMillis()
            );
            matchWebsocketController.notifyActionPointsUpdate(matchId, actionPointsUpdate);
    }

    

    /* 
    @Transactional
    public void checkAndTriggerChainFights(Integer matchId, Integer loserId, Integer roomId){
        Match match = matchRepo.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("match not found"));
        if (match.getPendingFights().isEmpty()) {
            if (roomId != 37){
                Room room = roomService.findById(roomId);

                Player movedPlayer = playerRepo.findById(loserId).orElse(null); 
                Npc movedNpc = npcRepository.findById(loserId).orElse(null);

                if (movedPlayer != null) {
                    checkForChainFightPlayer(movedPlayer, room, match);
                } else if (movedNpc != null) {
                    checkForChainFightNpc(movedNpc, room, match);
                } else {
                    throw new ResourceNotFoundException("The loser has not been found");
                }
            }
        } else {
            PendingFight pendingFight = match.getPendingFights().getFirst(); 
            FightUpdateDTO update = new FightUpdateDTO(
                matchId,
                pendingFight,
                attacker.getUser().getUsername(),
                defender.getId(),
                defender.getIsNiallCampbell() ? "Niall Campbell" : "NPC",
                room.getId(),
                "START",
                true
            );
            matchWebsocketController.notifyFightUpdate(match.getId(), update);

        }


        
    }

    @Transactional
    private List<PendingFight> checkForChainFightPlayer(Player movedPlayer, Room room, Match match) {
        // Verificar si hay jugadores y NPCs en la misma sala
        List<Player> players = playerRepo.findByMatchAndRoom(match.getId(), room.getId());
        List<Npc> npcs = npcRepository.findByMatchAndRoom(match.getId(), room.getId());
        List<PendingFight> pendingFights = new ArrayList<>(); 

        // Habitación vacía
        if (players.isEmpty() && npcs.isEmpty()) {
            return pendingFights;
        }

        // Jugador contra jugador
        if (players.size() == 2) {

            Player attacker = movedPlayer;
            Player defender = players.stream().filter(p -> !p.getId().equals(movedPlayer.getId())).findFirst().orElse(null);

            if (defender == null) {return;}

            FightUpdateDTO update = new FightUpdateDTO(
                match.getId(),
                attacker.getUser().getId(),
                attacker.getUser().getUsername(),
                defender.getUser().getId(),
                defender.getUser().getUsername(),
                room.getId(),
                "START",
                false
            );

            matchWebsocketController.notifyFightUpdate(match.getId(), update);
            return;
        }

        // Jugador contra NPC
        if (players.size() == 1 && npcs.size() == 1) {

            Player attacker = movedPlayer;
            Npc defender = npcs.get(0);

            FightUpdateDTO update = new FightUpdateDTO(
                match.getId(),
                attacker.getUser().getId(),
                attacker.getUser().getUsername(),
                defender.getId(),
                defender.getIsNiallCampbell() ? "Niall Campbell" : "NPC",
                room.getId(),
                "START",
                true
            );
            matchWebsocketController.notifyFightUpdate(match.getId(), update);

            return;
      
        }
    }

    
    @Transactional
    private void checkForChainFightNpc(Npc movedNpc, Room room, Match match) {
        List<Player> players = playerRepo.findByMatchAndRoom(match.getId(), room.getId());
        List<Npc> npcs = npcRepository.findByMatchAndRoom(match.getId(), room.getId());

        // Habitación vacía
        if (players.isEmpty() && npcs.isEmpty()) {
            return;
        }

        if (npcs.size() == 2) {
            return;
        }

        // NPC contra jugador
        if (players.size() == 1 && npcs.size() == 1) {

            Player defender = players.get(0);
            Npc attacker = movedNpc;

            FightUpdateDTO update = new FightUpdateDTO(
                match.getId(),
                attacker.getId(),
                attacker.getIsNiallCampbell() ? "Niall Campbell" : "NPC",
                defender.getUser().getId(),
                defender.getUser().getUsername(),
                room.getId(),
                "START",
                true
            );

            matchWebsocketController.notifyFightUpdate(match.getId(), update);
        }
    }
        */
    
    @Transactional
    public void updatePlayerStatistics(Player player){
        // Actualizar estadísticas de batallas del jugador
        int battlesWon = Optional.ofNullable(player.getBattlesWon()).orElse(0);
        int battlesPlayed = Optional.ofNullable(player.getBattlesPlayed()).orElse(0);
        player.setBattlesWon(battlesWon + 1);
        player.setBattlesPlayed(battlesPlayed + 1);
        playerRepo.save(player);

    }

    // movedId = id del user o npc que se mueve a esa habitacion
    // isNpc si el que se mueve a la habitacion es un npc
    @Transactional
    public List<PendingFight> getPossibleFight(Integer matchId, Integer movedId, Integer roomId, boolean isNpc) {
        // Safe Area
        if (roomId == 37) {
            return new ArrayList<>();
        }
        Match match = matchRepo.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        List<Player> players = playerRepo.findByMatchAndRoom(matchId, roomId);
        List<Npc> npcs = npcRepository.findByMatchAndRoom(matchId, roomId);

        if (players.isEmpty() && npcs.isEmpty()) {
            return match.getPendingFights();
        }

        if (isNpc) {
            npcRepository.findById(movedId).orElseThrow(() -> new ResourceNotFoundException("Npc not found"));
            // npc contra jugador 
            if (players.size() == 1 && npcs.size() == 1) {
                addPendingFight(match, movedId, players.getFirst().getUser().getId(), roomId, true, true);
            }

        } else {
            Player player = playerService.findByMatchIdAndUserId(matchId, movedId);
            // jugador contra jugador 
            if (players.size() == 2) {
                Integer defenderId = players.stream()
                    .filter(p -> !p.getId().equals(player.getId()))
                    .findFirst()
                    .orElse(null).getUser().getId();

                if (!movedId.equals(defenderId)) {
                    addPendingFight(match, movedId, defenderId, roomId, false, false);
                }
            }
            // jugador contra npc 
            if (players.size() == 1 && npcs.size() >= 1) {
                addPendingFight(match, movedId, npcs.getFirst().getId(), roomId, true, false);
            }
        }
        return match.getPendingFights();
    }

    @Transactional
    private void addPendingFight(Match match,Integer attackerId,Integer defenderId,Integer roomId,boolean isNpcFight, boolean isNpcAttacker) {
        match.getPendingFights().add(new PendingFight(match.getId(),attackerId,defenderId,roomId,isNpcFight,isNpcAttacker));
        matchRepo.save(match);
    }


    @Transactional
    public void checkPendingFights(Integer matchId) {
        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        if (match.getPendingFights().isEmpty()) {
            return;
        }
        PendingFight fight = match.getPendingFights().removeFirst();
        matchRepo.save(match);

        FightUpdateDTO update = createFightUpdate(matchId, fight);
        matchWebsocketController.notifyFightUpdate(matchId, update);
    }

    @Transactional
    private FightUpdateDTO createFightUpdate(Integer matchId, PendingFight fight) {
        String attackerName;
        String defenderName;
        if (fight.isNpcAttacker()) {
            Npc attacker = npcRepository.findById(fight.getAttackerUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Npc not found"));
            Player defender = playerService.findByMatchIdAndUserId(matchId, fight.getDefenserUserId());

            attackerName = attacker.getIsNiallCampbell() ? "Niall Campbell" : "NPC";
            defenderName = defender.getUser().getUsername();

        } else if (fight.isNpcFight() && !fight.isNpcAttacker()) {
            Player attacker = playerService.findByMatchIdAndUserId(matchId, fight.getAttackerUserId());
            Npc defender = npcRepository.findById(fight.getDefenserUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Npc not found"));

            attackerName = attacker.getUser().getUsername();
            defenderName = defender.getIsNiallCampbell() ? "Niall Campbell" : "NPC";

        } else {
            Player attacker = playerService.findByMatchIdAndUserId(matchId, fight.getAttackerUserId());
            Player defender = playerService.findByMatchIdAndUserId(matchId, fight.getDefenserUserId());

            attackerName = attacker.getUser().getUsername();
            defenderName = defender.getUser().getUsername();
        }

        return new FightUpdateDTO(
                matchId,
                fight.getAttackerUserId(),
                attackerName,
                fight.getDefenserUserId(),
                defenderName,
                fight.getRoomId(),
                "START",
                fight.isNpcFight()
        );
    }



    
}
