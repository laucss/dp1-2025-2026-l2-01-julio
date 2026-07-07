package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.NoActionPointsException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.FightService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.PendingFight;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightResultRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.ActionPointsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.EscapeAttemptResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.NpcLocationUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.PlayerLocationUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.NpcRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;

@Service
public class ActionsService {

    private final MatchService matchService; 
    private final FightService fightService; 
    BagService bagService;
    PlayerService playerService; 
    MatchRepository matchRepo;
    PlayerRepository playerRepo;
    RoomRepository roomRepository;
    NpcRepository npcRepository;
    Checkers checkers;
    MatchWebsocketController matchWebsocketController;
    RoomService roomService;

    @Autowired
    public ActionsService(MatchService matchService, FightService fightService, 
        BagService bagService, PlayerService playerService, MatchRepository matchRepo, PlayerRepository playerRepo,
        RoomRepository roomRepository, NpcRepository npcRepository, Checkers checkers, MatchWebsocketController matchWebsocketController, RoomService roomService) {
        this.fightService = fightService;
        this.matchService = matchService; 
        this.bagService = bagService;
        this.playerService = playerService;
        this.matchRepo = matchRepo;
        this.playerRepo = playerRepo;
        this.roomRepository = roomRepository;
        this.npcRepository = npcRepository;
        this.checkers = checkers; 
        this.matchWebsocketController = matchWebsocketController;
        this.roomService = roomService;
    }

    //Función para mover un jugador de una sala a otra adyacente
    @Transactional
    public MatchDTO movePlayerToAdyacentRoom(Integer matchId, Integer userId, Integer targetRoomId) {
        Match match = matchService.getMatchById(matchId);
        if (match.getCurrentTurnPhase() != TurnPhase.ACTIONS){
            match.setCurrentTurnPhase(TurnPhase.ACTIONS);
            matchRepo.save(match);
        }
        //Recuperar el jugador dentro del match
        Player player = playerService.findByMatchIdAndUserId(matchId, userId);
        Room currentRoom = player.getRoom();
        if (currentRoom == null) {
            throw new RuntimeException("Jugador no tiene sala asignada");
        }
        checkers.checkPlayerHasActionPoints(player);
        //Recuperar la sala destino
        Room targetRoom = roomRepository.findById(targetRoomId)
            .orElseThrow(() -> new ResourceNotFoundException("Sala destino no encontrada"));
        //Validar si la sala destino es adyacente
        checkers.checkRoomIsAdyacent(currentRoom, targetRoom); 
        //Actualizar sus puntos de acción
        if (player.getActionPoints() > 0) {
            player.setActionPoints(player.getActionPoints() - 1);
        } 
        //Guardar cambios
        playerRepo.save(player);

        // notificamos los puntos de accion por websocket 
        ActionPointsUpdateDTO actionPointsUpdate = new ActionPointsUpdateDTO(
            player.getId(),
            player.getUser().getId(),
            player.getUser().getUsername(),
            player.getActionPoints(),
            System.currentTimeMillis()
        );
        matchWebsocketController.notifyActionPointsUpdate(matchId, actionPointsUpdate);

        if (!targetRoom.getId().equals(currentRoom.getId())) {
            int visited = Optional.ofNullable(player.getRoomsVisited()).orElse(0);
            player.setRoomsVisited(visited + 1);
        }
        //Guardar cambios
        player.setRoom(targetRoom);
        playerRepo.save(player);
        PlayerLocationUpdateDTO locationUpdate = new PlayerLocationUpdateDTO(player);
        matchWebsocketController.notifyPlayerLocationUpdate(matchId, locationUpdate);

        // checkeamos si hay alguien en la sala y por ende se produce una pelea
        List<PendingFight> fights = fightService.getPossibleFight(matchId, userId, targetRoomId, false);
        if (!fights.isEmpty()){
            fightService.checkPendingFights(matchId);
        } 
        return new MatchDTO(match);
        
    }

    @Transactional
    public MatchDTO moveNpcToRoom(Integer matchId, Integer npcId, Integer targetRoomId, Integer userId) {
        Match match = matchService.getMatchById(matchId);
        if (match.getCurrentTurnPhase() != TurnPhase.ACTIONS){
            match.setCurrentTurnPhase(TurnPhase.ACTIONS);
            matchRepo.save(match);
        }
        //Buscamos al npc que queremos mover de la partida
        Npc npc = npcRepository.findById(npcId)
            .orElseThrow(() -> new RuntimeException("NPC no encontrado en la partida"));

        //Obtenemos la habitación actual en la que se encuentra el npc
        Room currentRoomNpc = npc.getRoom();
        if (currentRoomNpc == null) {
            throw new RuntimeException("NPC no tiene sala asignada"); }

        //Obtenemos la sala destino a la que queremos mover al npc
        Room targetRoom = roomService.findById(targetRoomId);

        //Comprobamos que el jugador tiene puntos de acción para poder mover al npc
        Player player = playerService.findByMatchIdAndUserId(matchId, userId);
        checkers.checkPlayerHasActionPoints(player);
        // Actualizamos la sala del npc y los puntos de acción del jugador
        player.setActionPoints(player.getActionPoints() - 1);
        playerRepo.save(player);
        npc.setRoom(targetRoom);
        npcRepository.save(npc);
        NpcLocationUpdateDTO locationNpcUpdate = new NpcLocationUpdateDTO(npc);
        matchWebsocketController.notifyNpcLocationUpdate(matchId, locationNpcUpdate);
        
        // checkeamos si hay alguien en la sala y por ende se produce una pelea
        List<PendingFight> fights = fightService.getPossibleFight(matchId, npcId, targetRoomId, true);
        if (!fights.isEmpty()){ 
            fightService.checkPendingFights(matchId);
        } 

        return new MatchDTO(match);
    }

    @Transactional
    public MatchDTO movePlayerByFormingRoomName(Integer matchId, Integer userId, Integer targetRoomId) {
        Match match = matchService.getMatchById(matchId);
        if(match.getCurrentTurnPhase() != TurnPhase.ACTIONS){
            match.setCurrentTurnPhase(TurnPhase.ACTIONS);
            matchRepo.save(match);
        }
        
        Player player = playerService.findByMatchIdAndUserId(matchId, userId);
        checkers.checkPlayerHasActionPoints(player);
        Room targetRoom = roomService.findById(targetRoomId);

        // Obtener la bolsa del jugador y sus letras
        BagInGame playerBag = bagService.findPlayerBag(matchId, player.getId());
        if (playerBag == null || playerBag.getCards().isEmpty()) {
            throw new RuntimeException("El jugador no tiene cartas en su bolsa");
        }

        // Recopilar todas las letras de la bolsa
        String availableLetters = "";
        for (Card card : playerBag.getCards()) {
            if (card.getLetter() != null) {
                availableLetters += card.getLetter().toLowerCase();
            }
        }

        // Obtener las palabras que forman la sala destino 
        String[] roomWords = targetRoom.getName().toLowerCase().split("\\s+");
        Boolean canFormAnyWord = false;
        for (String roomWord : roomWords){
            //Hacemos una copia de las letras de nuestra bolsa
            String remaining = availableLetters;
            boolean canFormThisWord = true;

            //Recorremos cada letra de la palabra de la sala destino
            for (char c : roomWord.toCharArray()) {
                //El método indexOf devuelve -1 si no encuentra la letra en la cadena, si la encuentra devuelve la posición de la letra en la cadena
                if (remaining.indexOf(c) >= 0) {
                    //Si la letra está en la cadena, la eliminamos de la cadena para no usarla de nuevo
                    remaining = remaining.replaceFirst(String.valueOf(c), "");
                } else {
                    //Si la letra no está en la cadena, no podemos formar la palabra
                    canFormThisWord = false;
                    break;
                }
            }
            if (canFormThisWord) {
                canFormAnyWord = true;
                break;
            }
        }
        if (!canFormAnyWord) {
            throw new RuntimeException(
                "No se puede formar ninguna palabra del nombre de la sala '" +
                targetRoom.getName() + "' con las letras disponibles en la bolsa");
        }

        player.setActionPoints(player.getActionPoints() - 1);
        playerRepo.save(player);
        Room currentRoom = player.getRoom();
        if (currentRoom == null || !targetRoom.getId().equals(currentRoom.getId())) {
            int visited = Optional.ofNullable(player.getRoomsVisited()).orElse(0);
            player.setRoomsVisited(visited + 1);
        }
        player.setRoom(targetRoom);
        playerRepo.save(player);
        PlayerLocationUpdateDTO locationUpdate = new PlayerLocationUpdateDTO(player);
        matchWebsocketController.notifyPlayerLocationUpdate(matchId, locationUpdate);
        // checkeamos si hay alguien en la sala y por ende se produce una pelea
        List<PendingFight> fights = fightService.getPossibleFight(matchId, userId, targetRoomId, false);
        if (!fights.isEmpty()){ 
             fightService.checkPendingFights(matchId);
        } 

        return new MatchDTO(match);
    }

    @Transactional
    public EscapeAttemptResultDTO escapeAttempt( Integer matchId, Integer userId, Integer rolldiceResult) {
        matchRepo.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Match not found"));
        Player p = playerService.findByMatchIdAndUserId(matchId, userId);

        //Comprobamos que el jugador tenga puntos de acción para poder realizar la acción
        if (p.getActionPoints() <= 0) {
            throw new NoActionPointsException("Escape attempt not allowed: player has no action points left");
        }
        

        //Comprobamos que el jugador cumpla las condiciones para poder realizar un intento de escape 

        //Primero comprobamos que el jugador se encuentre en una de las torres
        Room playerRoom = p.getRoom();
        List<Room> towers = roomService.getAllTowers();
        boolean inTower = towers.stream().anyMatch(r -> r.equals(playerRoom));
        if (!inTower) {
            throw new IllegalArgumentException("Player is not in a tower room");
        }

        //Segundo comprobamos que el jugador tengo en su bolsa las cartas necesarias para formas la palabra de escape correspondiente a la torre
        // o las palabras “EMPEROR” o “CAMPBELL”

        //Primero obtenemos la bolsa del jugador 
        BagInGame playerBag = bagService.findPlayerBag(matchId, p.getId());
        //Obtenemos las cartas de la bolsa
        List<Card> bagCards = playerBag.getCards();
        //Convertimos la lista de cartas en una lista de DTO de cartas
        List<CardDTO> bagCardDTOs = bagCards.stream()
                .map(CardDTO::new)
                .toList();
        //Obtenemos la palabra que forman las letras de las cartas de la bolsa
        String bagWord = bagService.wordFromCards(bagCardDTOs).toLowerCase().replaceAll("\\s+", "") ;

        //Obtenemos la palabra de escape correspondiente a la torre en la que se encuentra el jugador
        String towerEscapeWord = roomService.getWordOfEscapeFromTower(playerRoom.getId()).toLowerCase().replaceAll("\\s+", "");

        //Comprobamos si la palabra de la bolsa del jugador es igual a la palabra de escape de la torre o a las palabras especiales
        boolean hasRequiredWord = bagWord.equals(towerEscapeWord) || bagWord.equals("emperor") || bagWord.equals("campbell");
        if (!hasRequiredWord) {
            throw new IllegalArgumentException("Player does not have the required word in their bag to attempt escape");
        }


        //Cuando se cumplan las condiciones realizamos el intento de escape 
        EscapeAttemptResultDTO resultado = new EscapeAttemptResultDTO();

        if(rolldiceResult < p.getStrength()){
            //El intento de escape es existoso
            matchService.endMatch(matchId, p);
            resultado.setSuccess(true);
            resultado.setWinnerUserId(p.getUser().getId());
            resultado.setDiscardRequired(false);
            return resultado;

        } else {
            //El intento de escape falla y ocurre lo mismo que si un jugador pierde contra un npc en una pelea
            matchService.consumeAllActionPointForUser(matchId, userId);
            // lo ponemos así para intentar reutilizar el método (tal y como se dice en las reglas)
            FightResultRequestDTO fight = new FightResultRequestDTO();
            fightService.playerLoses(p, matchId, fight ) ;

            resultado.setSuccess(false);
            resultado.setDiscardRequired(true);
            return resultado;
        }
        }
    
    
}
