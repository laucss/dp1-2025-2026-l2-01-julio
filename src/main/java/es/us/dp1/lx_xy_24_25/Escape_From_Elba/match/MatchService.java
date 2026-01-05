package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DrawCardResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.ListCardsDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.NoActionPointsException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyWebsocketController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;


@Service
public class MatchService {

    private static final Integer initialCardsPerPlayer = 3; 

    DeckService deckService; 
    HandService handService; 
    BagService bagService;
    PlayerService playerService; 
    Random ran = new Random();
    LobbyWebsocketController lobbyWebsocketController;
    MatchWebsocketController matchWebsocketController;

    MatchRepository matchRepo;
    PlayerRepository playerRepo;
    RoomRepository roomRepository;
    RoomService roomService;

    @Autowired
    public MatchService(MatchRepository mrepo, PlayerRepository playerRepo, RoomRepository roomRepository, 
            RoomService roomService, DeckService deckService, HandService handService, BagService bagService, 
            PlayerService playerService, LobbyWebsocketController lobbyWebsocketController,
            MatchWebsocketController matchWebsocketController) {
        this.matchRepo = mrepo;
        this.playerRepo = playerRepo;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
        this.deckService = deckService;
        this.handService = handService;
        this.bagService = bagService; 
        this.playerService = playerService;
        this.lobbyWebsocketController = lobbyWebsocketController;
        this.matchWebsocketController = matchWebsocketController;
    }

    @Transactional(readOnly = true)
    public List<Match> getAllMatchs() {
        return matchRepo.findAll();
    }

    /*@Transactional(readOnly = true)  El metodo esta en LobbyService por ahora
    public Page<Match> getAllPublicLobbies(Pageable pageable) {
        return matchRepo.findPublicLobbies(pageable);
    }*/

    @Transactional(readOnly = true)
    public List<Match> getMatchsByName(String name) {
        return matchRepo.findByName(name);
    }

    @Transactional(readOnly = true) //????
    public List<Match> getRunningMatches(){
        return matchRepo.findAll();
    }

    @Transactional
    public Match save(Match m) {
        matchRepo.save(m);
        return m;
    }

    @Transactional(readOnly=true)
    public Match getMatchById(Integer matchId){
        Optional<Match> m= matchRepo.findById(matchId);
        if(!m.isPresent())
            throw new ResourceNotFoundException("Match", "id", matchId);
        return m.get();
    }

    @Transactional(readOnly = true)
    public Integer userInMatch(Integer userId) {
        return matchRepo.userInMatch(userId);
    }

    @Transactional
    public void delete(Integer id) {
        matchRepo.deleteById(id);
    }



    //Función para inicializar un match 
    //TODO: Hay que hacer que esto devuelva un MatchDTO
    @Transactional
    public Match startMatch(Integer matchId) {
        Match m = matchRepo.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Match not found"));
        
        //Cambiamos el estado de la partida a PLAYING
        m.setStatus(MatchStatus.PLAYING);

        //Inicializamos la fecha de inicio
        m.setStartTime(LocalDateTime.now());

        //Inicializamos los npcs de la partida 

        for ( int i=0; i< m.getNumNpcs(); i++){
            Npc npc = new Npc(); //Creamos el npc
            npc.setIsNiallCampbell( i== m.getNumNpcs()-1); // Si es el último npc que creamos, es Niall Campbell
            npc.setStrength(1); // El valor de la fuerza al inicio es 1
            npc.setMatch(m);// Lo asociamos a la partida
            m.getNpcs().add(npc);// Lo añadimos a la lista de npcs de la partida 
        } 


        // le creamos una mano y una bolsa asociadas a cada jugador 
        List<Player> playersInGame = m.getPlayers(); 
        for (Player player : playersInGame){

            player.setDiceOrder(null); // Inicializamos el valor de la tirada de dado a null
            player.setOrderInMatch(null);
            player.setActionPoints(0);
            player.setStrength(1);


        }

        
        DeckInGame deck = initializePlayerHandCards(matchId, playersInGame); 
        m.setDeck(deck);
        m.setRoomsState(roomService.initializeRoomsForMatch(m));
        m.setCurrentTurnUserId(null);
        m.setTurnNumber(0);
        matchRepo.save(m);
        
        LobbyUpdateDTO update = createLobbyUpdate(m, "START", "");
        lobbyWebsocketController.notifyGameStarted(matchId, update);
        
        return m;
    }
    
    @Transactional
    private LobbyUpdateDTO createLobbyUpdate(Match match, String action, String username) {
        List<LobbyUpdateDTO.PlayerLobbyDTO> players = new ArrayList<>();
        for (Player p : match.getPlayers()) {
            players.add(new LobbyUpdateDTO.PlayerLobbyDTO(
                p.getUser().getId(),
                p.getUser().getUsername(),
                p.getUser().getAvatar()
            ));
        }
        return new LobbyUpdateDTO(match.getId(), players, action, username);
    }

    //Función para decidir el orden de los jugadores en la partida según la tirada de dados.
    @Transactional
    public Match submitDiceAndAssignOrder(Integer matchId, Integer userId, Integer diceRoll) {

        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));


        Player player = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found in this match"));
 
        if (player.getDiceOrder() != null) {
            throw new IllegalArgumentException("Jugador ya ha tirado el dado");
        }


        player.setDiceOrder(diceRoll);
        playerRepo.save(player);


        boolean allRolled = match.getPlayers().stream()
                .allMatch(p -> p.getDiceOrder() != null);

        if (allRolled) {
            //  Asignar orden de turno
            List<Player> ordered = match.getPlayers().stream()
                    .sorted((a, b) -> {
                        int cmp = b.getDiceOrder() - a.getDiceOrder(); // dado mayor primero
                        if (cmp == 0) {
                            // Desempate automático usando ID
                            return a.getId().compareTo(b.getId());
                        }
                        return cmp;
                    })
                    .toList();

            for (int i = 0; i < ordered.size(); i++) {
                ordered.get(i).setOrderInMatch(i);
                playerRepo.save(ordered.get(i));
            }

            match.setCurrentTurnUserId(ordered.get(0).getUser().getId());
            match.setTurnNumber(1);
            match.setCurrentTurnPhase(TurnPhase.DRAW);
            matchRepo.save(match);
            
            // Notificar a todos los jugadores que el turno ha comenzado
            TurnUpdateDTO turnUpdate = new TurnUpdateDTO(
                matchId,
                ordered.get(0).getUser().getId(),
                ordered.get(0).getUser().getUsername(),
                1,
                TurnPhase.DRAW.toString()
            );
            matchWebsocketController.notifyTurnUpdate(matchId, turnUpdate);
        }

        return match;
    }


    @Transactional
    public Match nextTurn(Integer matchId) {
        Match m = matchRepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
        //Obtenemos el id del user del jugador que tiene el turno actualmente
        Integer currenUserTurnId = m.getCurrentTurnUserId();
        //Buscamos el jugador correspondiente
        Player currentPlayerTurn = m.getPlayers().stream()
                .filter(p -> p.getUser().getId().equals(currenUserTurnId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        //Obtenemos el indice de orden del jugador actual
        Integer currentIndx = currentPlayerTurn.getOrderInMatch();
        //Calculamos el indice del siguiente jugador
        Integer nextIndx = (currentIndx + 1) % m.getPlayers().size();
        //Buscamos el siguiente jugador por su orden en la partida
        Player nextPlayerTurn = m.getPlayers().stream()
                .filter(p -> p.getOrderInMatch().equals(nextIndx))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        //Actualizamos el id del jugador que tiene el turno actualmente en la partida
        m.setCurrentTurnUserId(nextPlayerTurn.getUser().getId());
        m.setCurrentTurnPhase(TurnPhase.DRAW);

        // actualizamos sus puntos de acción por si acaso 
        playerService.getPlayerActionPoints(matchId, nextPlayerTurn.getId()); 

        // actualizamos el estado de la partida 
        matchRepo.save(m);
        
        // Notificar a todos los jugadores el cambio de turno
        TurnUpdateDTO turnUpdate = new TurnUpdateDTO(
            matchId,
            nextPlayerTurn.getUser().getId(),
            nextPlayerTurn.getUser().getUsername(),
            m.getTurnNumber(),
            TurnPhase.DRAW.toString()
        );
        matchWebsocketController.notifyTurnUpdate(matchId, turnUpdate);
        
        return m;
 
    }


    @Transactional
    public Match endMatch(Integer matchId, Player winner) {
        Match m = matchRepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (m.getStatus() == MatchStatus.FINISHED) {
            return m;
        }

        //Cambiamos estado a FINISHED
        m.setStatus(MatchStatus.FINISHED);

        // Guuardardamos hora de fin y el ganador
        m.setEndTime(LocalDateTime.now());
        m.setWinner(winner);

        if (m.getStartTime() != null) {
            long durationSeconds = java.time.Duration.between(m.getStartTime(), m.getEndTime()).toSeconds();
        }

        deleteMatchCards(matchId); 

        matchRepo.save(m);

        return m;
    }


    @Transactional
    public void deleteMatchCards(Integer matchId){
        deckService.deleteDeckInGame(matchId);
        handService.deleteMatchHands(matchId);
        bagService.deleteMatchBags(matchId);
    }



    
    // -------------------------------------------- METODOS RELACIONADOS CON LAS CARTAS ----------------------------------------------------------------------



    /*
     * Jugador roba una carta del mazo de robar
     */
    @Transactional
    public DrawCardResultDTO playerDrawsCardFromDeck(Integer matchId, Integer playerId){
        Card stolenCard =deckService.drawCard(matchId); 
        DeckInGame deck = deckService.findDeckById(matchId); 

        HandInGame hand = handService.addCardToPlayerHand(stolenCard, matchId, playerId);
        // actualimos el valor de los puntos de acción del jugador en la bd 
        playerService.removePlayerActionPoint(matchId, playerId);

        return new DrawCardResultDTO(stolenCard, deck, hand); 
    }

    /*
     * Método que devuelve el estado de todas las cartas relacionadas con un jugador en una partida
     */

    @Transactional(readOnly = true)
    public AllCardsStatusDTO getAllCards (Integer matchId, Integer playerId){
        DeckInGameDTO deck = new DeckInGameDTO(deckService.findDeckById(matchId)); 

        HandInGameDTO hand = new HandInGameDTO(handService.findPlayerHand(matchId, playerId)); 

        ListCardsDTO bag = new ListCardsDTO(bagService.findPlayerBag(matchId, playerId));

        return new AllCardsStatusDTO(hand, bag, deck, playerId); 
    }

    /*
     * Inicializa las manos de los jugadores y el mazo de cartas para una partida
     * También actualiza / inicializa el valor de los puntos de acción de cada jugador
     */
    @Transactional
    public DeckInGame initializePlayerHandCards (Integer matchId, List<Player> players){
        DeckInGame deck = deckService.initializeDeck(matchId); 
        for (Player player : players) {
            handService.createPlayerHand(matchId, player.getId());
            bagService.createPlayerbag(matchId, player.getId());
        }
        for (int i = 0; i < initialCardsPerPlayer; i++) {
            for (Player player : players) {
                    // Sacamos la carta del mazo y se la damos al jugador
                    Card card = deck.getNotDiscardedCards().remove(0); // carta del tope del deck
                    handService.addCardToPlayerHand(card, matchId, player.getId());
                    playerService.getPlayerActionPoints(matchId, player.getId()); // actualizamos / incializamos los puntos de acción del jugador
            }
        }
        return deck;     

    }


    // ------------------------------------------- FUNCIONES LLEVADAS A CABO EN LAS PELEAS -------------------------------------------------------------------

    /*
     * JUGADOR GANA A JUGADOR 
     * Contexto: es una pelea entre dos jugadores activos, quien gana roba una carta del perdedor
     * La carta que puede robar el ganador puede ser de la mano o de la bolsa del perdedor, si es de la bolsa la elige deliberadamente, 
     * si es de la mano es aleatoriamente  
     * 
     * la carta robada siempre va a la mano del ganador y si el perdedor es el jugador con el turno actual, pierde todos sus puntos de acción
     * 
     * fromWhere indica si la carta robada es de la mano ("hand") o de la bolsa ("bag")
     * 
     * el winnerId y el loserId son los ids de los players no de los users
     */

    @Transactional
    public void playerDrawsCardFromAnotherPlayerBag(Card card, Integer matchId, Integer winnerId, Integer loserId, String fromWhere, Integer currentTurnUserId){
        // checkeamos que ambos jugadores existen en la partida
        Player loser = playerService.findById(loserId); 
        playerService.findById(winnerId);

        // quitamos la carta de la mano o bolsa del perdedor y se la añadimos a la mano del ganador

        if (fromWhere.equals("hand")){ 
            handService.removeCardFromPlayerHand(card, matchId, loserId);
            handService.addCardToPlayerHand(card, matchId, winnerId);

        } else if (fromWhere.equals("bag")){
            bagService.removeCardFromPlayerBag(card, matchId, loserId);
            handService.addCardToPlayerHand(card, matchId, winnerId); 
        
        } else {
            throw new IllegalArgumentException("fromWhere must be 'hand' or 'bag'");
        }

        // le quitamos todos los puntos de acción al perdedor si es su turno actual
        if (loserId.equals(currentTurnUserId)){
            loser.setActionPoints(0);
            playerService.save(loser);
        }
        
    }



    /*
     * JUGADOR GANA A NO-JUGADOR 
     * Contexto: un jugador activo vence a un npc (no jugador) en una pelea
     * Resultado: el jugador roba una carta del mazo, el npc suma un punto a su fuerza
     */

    @Transactional
    public Card playerBeatsNonPlayer(Integer matchId, Integer playerId, Integer npcId){
        // TODO: hay que hacer la gestión de los npcs, services, repositorios, etc

        Card stolenCard =deckService.drawCard(matchId);
        handService.addCardToPlayerHand(stolenCard, matchId, playerId);

        // A LO MEJOR HAY QUE PASAR EL NPC NO SOLO SU ID
        // actualizar fuerza del npc
        // npc.setStrength(npc.getStrength() + 1);
        return stolenCard;
    
    }

    /*
     * JUGADOR GANA NIALL CAMPBELL
     * Contexto: un jugador activo vence a Niall Campbell en una pelea
     * Resultado: el jugador recibe la última carta descartada del mazo, si la hay 
     */

    @Transactional
    public Card playerWinsNiallCampbell(Integer matchId, Integer playerId){
        Card discardedCard = deckService.getAndRemoveLastDiscardedCard(matchId); 

        if (discardedCard != null){
             // no hay carta descartada que dar al jugador
             handService.addCardToPlayerHand(discardedCard, matchId, playerId);
             return discardedCard;
        } else {
            return null; 
        }
        
    }


    /*
     * NO-JUGADOR GANA A JUGADOR
     * Contexto: un jugador activo pierde contra un npc (no jugador) en una pelea
     * Resultado: el jugador pierde la carta que el quiera de su mano o bolsa, y pierde todos sus puntos de acción
     * 
     * fromWhere indica si la carta perdida es de la mano ("hand") o de la bolsa ("bag")
     */

    @Transactional
    public void playerLosesAgaintsNonPlayer(Card card, Integer matchId, Integer playerId, Integer currentTurnUserId, String fromWhere){
        //actualizar puntos de acción del jugador, pierde todos sus puntos de acción
        /*
        Player player = playerService.findById(playerId);
        if (player.getUser().getId() == currentTurnUserId){
            player.setActionPoints(0);  
            playerService.save(player);
        }
         */

        if (fromWhere.equals("hand")){
            handService.removeCardFromPlayerHand(card, matchId, playerId);
        } else if (fromWhere.equals("bag")){
            bagService.removeCardFromPlayerBag(card, matchId, playerId); 
        } else {
            throw new IllegalArgumentException("fromWhere must be 'hand' or 'bag'");
        }

        // añadimos la carta seleccionada al mazo de descartes
        deckService.addCardToDiscardedPile(matchId, card);
            
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    

    @Transactional
    public Player getMatchWinner(Integer matchId) {
        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (match.getStatus() != MatchStatus.FINISHED) {
            throw new IllegalStateException("Match is not finished yet");
        }

        return match.getWinner();
    }
    
    


    //Función para mover un jugador de una sala a otra adyacente
    @Transactional
    public Player movePlayerToAdyacentRoom(Integer matchId, Integer userId, Integer targetRoomId) {
        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        if(match.getCurrentTurnPhase() != TurnPhase.ACTIONS){
            match.setCurrentTurnPhase(TurnPhase.ACTIONS);
        }
        matchRepo.save(match);
        //Recuperar el jugador dentro del match
        Player player = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado en la partida"));
        Room currentRoom = player.getRoom();
        if (currentRoom == null) {
            throw new RuntimeException("Jugador no tiene sala asignada");
        }
        // checkear que el jugador tiene puntos de acción para poder moverse 
        // TODO: revisar si tengo que usar la función de playerService de getPlayerActionPoints en vez de acceder directamente al atributo
        if (player.getActionPoints() <= 0) {
            throw new NoActionPointsException("Move not allowed: player has no action points left");
        }
        //Recuperar la sala destino
        Room targetRoom = roomRepository.findById(targetRoomId)
            .orElseThrow(() -> new RuntimeException("Sala destino no encontrada"));
        //Validar si la sala destino es adyacente
        List<Room> adjacent = currentRoom.getAdjacencyList();
        boolean canMove = adjacent.stream()
                .anyMatch(r -> r.getId().equals(targetRoom.getId()));
        if (!canMove) {
            throw new RuntimeException("Movimiento no permitido: la sala destino no es adyacente");
        }
        //Actualizar la sala del jugador y sus puntos de acción
        player.setRoom(targetRoom);
        if (player.getActionPoints() > 0) {
            player.setActionPoints(player.getActionPoints() - 1);
        } 

        //Guardar cambios
        return playerRepo.save(player);
    }


    @Transactional
    public Player moveLoserPlayer(Integer matchId, Integer userId, Integer targetRoomId) {
        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        if(match.getCurrentTurnPhase() != TurnPhase.ACTIONS){
            match.setCurrentTurnPhase(TurnPhase.ACTIONS);
        }
        //Recuperar el jugador dentro del match
        Player player = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado en la partida"));
        //Recuperar la sala destino
        Room targetRoom = roomRepository.findById(targetRoomId)
            .orElseThrow(() -> new RuntimeException("Sala destino no encontrada"));
        //Actualizar la sala y fuerza del jugador
        player.setRoom(targetRoom);
        player.setStrength(player.getStrength() + 1);
        
        //Guardar cambios
        return playerRepo.save(player);
    }


}
