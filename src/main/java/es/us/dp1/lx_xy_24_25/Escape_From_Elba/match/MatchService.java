package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.DrawCardResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.BagNotValidException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.InvalidMovementException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.MoreThan7CardsDrawnException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.MoreThan7CardsInHand;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.NoActionPointsException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.ActionPointsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.CardsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.EscapeAttemptResultDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.LoseAgainstNpcRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchHistorialDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.StealCardRequestDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.TurnUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyWebsocketController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.NpcRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;


@Service
public class MatchService {

    private static final Integer initialCardsPerPlayer = 3; 

    DeckService deckService; 
    HandService handService; 
    BagService bagService;
    PlayerService playerService; 
    UserService userService;
    Random ran = new Random();
    LobbyWebsocketController lobbyWebsocketController;
    MatchWebsocketController matchWebsocketController;
    Checkers checkers; 
    LobbyService lobbyService;
    AbandonedMatchService abandonedMatchService;

    MatchRepository matchRepo;
    PlayerRepository playerRepo;
    RoomRepository roomRepository;
    NpcRepository npcRepository;
    RoomService roomService;
    AbandonedMatchRepository abandonedMatchRepository;

    @Autowired
    public MatchService(MatchRepository mrepo, PlayerRepository playerRepo, RoomRepository roomRepository, AbandonedMatchRepository abandonedMatchRepository, 
            RoomService roomService, DeckService deckService, HandService handService, BagService bagService, 
            PlayerService playerService, LobbyWebsocketController lobbyWebsocketController,
            MatchWebsocketController matchWebsocketController, NpcRepository npcRepository, Checkers checkers, UserService userService, LobbyService lobbyService,AbandonedMatchService abandonedMatchService) {
        this.matchRepo = mrepo;
        this.playerRepo = playerRepo;
        this.roomRepository = roomRepository;
        this.npcRepository = npcRepository;
        this.roomService = roomService;
        this.deckService = deckService;
        this.handService = handService;
        this.bagService = bagService; 
        this.playerService = playerService;
        this.lobbyWebsocketController = lobbyWebsocketController;
        this.matchWebsocketController = matchWebsocketController;
        this.checkers = checkers; 
        this.userService = userService; 
        this.lobbyService = lobbyService;
        this.abandonedMatchRepository = abandonedMatchRepository;
        this.abandonedMatchService = abandonedMatchService;
    }

    @Transactional(readOnly = true)
    public List<Match> getAllMatchs() {
        return matchRepo.findAll();
    }

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
            throw new ResourceNotFoundException("Match not found");
        return m.get();
    }

    @Transactional(readOnly=true)
    public MatchDTO getMatchDTOById(Integer matchId){
        Match m = getMatchById(matchId); 
        DeckInGame deck = deckService.findDeckById(m.getId());
        List<PlayerInGameDTO> newPlayersList = new ArrayList<>(); 
        for (Player player : m.getPlayers()){
            HandInGame hand = handService.findPlayerHand(m.getId(), player.getId()); 
            BagInGame bag = bagService.findPlayerBag(m.getId(), player.getId()); 
            newPlayersList.add(new PlayerInGameDTO(player, hand, bag)); 
        }

        return new MatchDTO(m, deck, newPlayersList);
    }


    @Transactional(readOnly = true)
    public Integer userInMatch(Integer userId) {
        return matchRepo.userInMatch(userId);
    }

    @Transactional
    public void delete(Integer id) {
        matchRepo.deleteById(id);
    }

//------------------FUNCIONES PARA EL HISTORIAL DE PARTIDAS ------------------------------------------------------

    //Para devolver el listado de todo el historial de partidas finalizadas y en curso
    @Transactional(readOnly = true) 
    public Page<Match> getFinishedAndInProgressMatches(Integer page, Integer size) {
        return matchRepo.findFinishedAndInProgress(PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "startTime")));
    }

    //Para devolver el listado de partidas en curso 
    @Transactional(readOnly = true)
    public Page<Match> getInProgressMatches(Integer page, Integer size) {
        return matchRepo.findInProgress(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime")));
    }


    //Para devolver el listado de partidas finalizadas
    @Transactional(readOnly = true)
    public Page<Match> getFinishedMatches(Integer page, Integer size) {
            return matchRepo.findFinished(PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "startTime")));
        }


        //Para devolver el listado de partidas jugadas o abandonadas por un usuario
     @Transactional(readOnly = true)
    public Page<MatchHistorialDTO> getAllMatchesByUser(Integer userId, Integer page, Integer size) {

            Page<Match> matches = matchRepo.findMatchesPlayedOrAbandonedByUser(
                userId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"))
            );

            return matches.map(match -> {

                MatchHistorialDTO dto = new MatchHistorialDTO(match);

                dto.setAbandoned(
                    abandonedMatchRepository.existsByMatchIdAndUserId(match.getId(), userId)
                );

                return dto;
            });
        }

    

    //Para devolver el listado de partidas jugadas por un usuario
    @Transactional(readOnly = true)
    public Page<Match> getMatchesPlayedByUser(Integer userId, Integer page, Integer size) {
        return matchRepo.findMatchesPlayedByUser(userId, PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "startTime")));
    }

    //Para devolver el listado de partidas creadas por un usuario 
    @Transactional(readOnly = true)
    public Page<Match> getMatchesPlayedAndCreatedByUser(Integer userId, Integer page, Integer size) {
        return matchRepo.findMatchesPlayedAndCreatedByUser(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime")));
    }

    //Para devolver el listado de partidas ganadas por un usuario 
    @Transactional(readOnly = true)
    public Page<Match> getMatchesWonByUser(Integer userId, Integer page, Integer size) {
        return matchRepo.findMatchesWonByUser(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime")));
    }

    //Para devolver el listado de partidas abandonadas por un usuario
    @Transactional(readOnly = true)
    public Page<Match> getMatchesAbandonedByUser(Integer userId, Integer page, Integer size) {
        return abandonedMatchRepository.findMatchesAbandonedByUser(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime")));
    }


//------------------------------------FUNCIONES PARA GESTIONAR LA PARTIDA---------------------------------------------------------

    //Función para inicializar un match
    @Transactional
    public Match startMatch(Integer matchId) {
        Match m = matchRepo.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        
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
            npcRepository.save(npc); // Lo guardamos en la base de datos
        } 


        // le creamos una mano y una bolsa asociadas a cada jugador 
        List<Player> playersInGame = m.getPlayers(); 
        for (Player player : playersInGame){

            player.setDiceOrder(null); // Inicializamos el valor de la tirada de dado a null
            player.setOrderInMatch(null);
            player.setActionPoints(0);
            player.setStrength(1);
            player.setCardsDrawnInTurn(0);
            
        }

        
        DeckInGame deck = initializePlayerHandCards(matchId, playersInGame); 
        m.setDeck(deck);
        m.setRoomsState(roomService.initializeRoomsForMatch(m));
        m.setCurrentTurnUserId(null);
        m.setTurnNumber(0);
        matchRepo.save(m);
        
        LobbyUpdateDTO update = lobbyService.createLobbyUpdate(m, "START", "");
        lobbyWebsocketController.notifyGameStarted(matchId, update);
        
        return m;
    }
    

    //Función para decidir el orden de los jugadores en la partida según la tirada de dados.
    @Transactional
    public Match submitDiceAndAssignOrder(Integer matchId, Integer userId, Integer diceRoll) {

        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));


        Player player = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found in this match"));
 
        //Comprobamos que el jugador no haya tirado el dado
        if (player.getDiceOrder() != null) {
            throw new IllegalArgumentException("The player has already rolled the dice.");
        }


        player.setDiceOrder(diceRoll);
        playerRepo.save(player);


        boolean allRolled = match.getPlayers().stream()
                .allMatch(p -> p.getDiceOrder() != null);

        //Cuando todos los jugadores hayan tirado el dado, asignamos el orden de turno
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


    @Transactional(rollbackFor = MoreThan7CardsInHand.class)
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

        // checkeamos que tenga menos de 7 cartas en su mano, pues sino tiene que descartar
        HandInGame currentPlayerHand = handService.findPlayerHand(matchId, currentPlayerTurn.getId()); 
        checkers.checkNoMoreThan7CardsInHand(new HandInGameDTO(currentPlayerHand));

        // si todo está correcto, le reseteamos el número de cartas robadas en el turno a cero
        currentPlayerTurn.setCardsDrawnInTurn(0); 
        playerRepo.save(currentPlayerTurn);

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

        // actualizamos sus puntos de acción por si acaso (del jugador que empieza el turno)
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
    public MatchDTO endMatch(Integer matchId, Player winner) {
        Match m = matchRepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (m.getStatus() == MatchStatus.FINISHED) {
            return new MatchDTO(m);
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

        return new MatchDTO(m);
    }


    @Transactional 
    public MatchDTO  leaveMatch(Integer matchId, Integer userId){
        Match m = matchRepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
        Player p = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found in this match"));
        User u = userService.findUser(userId);
        
        //Guardamos el orden del turno del jugador que se va
        Integer leavingPlayerOrder = p.getOrderInMatch();
        //Guardamos si el jugador que se va tiene el turno actual
        Boolean isLeavingPlayerCurrentTurn = m.getCurrentTurnUserId().equals(userId);


        abandonedMatchService.saveAbandonedMatch(u, m);
        m.getPlayers().remove(p);

        if (isLeavingPlayerCurrentTurn){
            Integer nextIndx = (leavingPlayerOrder + 1) % m.getPlayers().size();
            Player nextPlayerTurn = m.getPlayers().stream()
                .filter(pl -> pl.getOrderInMatch().equals(nextIndx))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
            //Actualizamos el id del jugador que tiene el turno actualmente en la partida
            m.setCurrentTurnUserId(nextPlayerTurn.getUser().getId());
            m.setCurrentTurnPhase(TurnPhase.DRAW);
        }

        

        //Actualizamos el orden de los jugadores que quedan en la partida 
        for (Player player : m.getPlayers()){
            //Todos los jugadores que iban después del que se va adelantan una posición
            if (player.getOrderInMatch() > leavingPlayerOrder){
                player.setOrderInMatch(player.getOrderInMatch() - 1);
                playerRepo.save(player);
            }
        }

        //Si no se llega al minimo de jugadores, se termina la partida
        if (m.getPlayers().size() < m.getMinPlayers()){
            endMatch(matchId, null);
        } else {
            matchRepo.save(m);
        }

        return new MatchDTO(m);


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
    @Transactional(rollbackFor = MoreThan7CardsDrawnException.class)
    public DrawCardResultDTO playerDrawsCardFromDeck(Integer matchId, Integer playerId){

        // buscamos al jugador para checkear sus cartas robadas en el turno
        Player player = playerRepo.findById(playerId).orElse(null); 
        
        // checkeamos que no haya robado más de 7 cartas 
        checkers.checkCardsDrawnInTurn(player);

        // si no es el caso, procedemos a robar la carta
        Card stolenCard =deckService.drawCard(matchId); 
        DeckInGame deck = deckService.findDeckById(matchId); 

        HandInGame hand = handService.addCardToPlayerHand(stolenCard, matchId, playerId);
        // actualimos el valor de los puntos de acción del jugador en la bd y de sus cartas robadas en el turno
        player.setCardsDrawnInTurn(player.getCardsDrawnInTurn() + 1);
        player.setActionPoints(player.getActionPoints() - 1);
        playerRepo.save(player);
        
        // Notificar cambios de cartas por WebSocket
        AllCardsStatusDTO playerCards = getAllCards(matchId, playerId);
        CardsUpdateDTO update = new CardsUpdateDTO(matchId, playerCards, null);
        matchWebsocketController.notifyCardsUpdate(matchId, update);

        return new DrawCardResultDTO(stolenCard, deck, hand); 
    }

    /*
     * Jugador roba una carta de recompensa (sin consumir puntos de acción)
     */
    @Transactional
    public DrawCardResultDTO playerDrawsRewardCard(Integer matchId, Integer playerId){
        Card stolenCard = deckService.drawCard(matchId);
        DeckInGame deck = deckService.findDeckById(matchId);

        HandInGame hand = handService.addCardToPlayerHand(stolenCard, matchId, playerId);

        // Notificar cambios de cartas por WebSocket
        AllCardsStatusDTO playerCards = getAllCards(matchId, playerId);
        CardsUpdateDTO update = new CardsUpdateDTO(matchId, playerCards, null);
        matchWebsocketController.notifyCardsUpdate(matchId, update);

        return new DrawCardResultDTO(stolenCard, deck, hand);
    }

    /*
     * Método que devuelve el estado de todas las cartas relacionadas con un jugador en una partida
     */

    @Transactional(readOnly = true)
    public AllCardsStatusDTO getAllCards (Integer matchId, Integer playerId){
        DeckInGameDTO deck = new DeckInGameDTO(deckService.findDeckById(matchId)); 

        HandInGameDTO hand = new HandInGameDTO(handService.findPlayerHand(matchId, playerId)); 

        BagInGameDTO bag = new BagInGameDTO(bagService.findPlayerBag(matchId, playerId));

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

    @Transactional(rollbackFor = {BagNotValidException.class, MoreThan7CardsInHand.class})
    public Integer confirmDiscardPhase(Integer matchId, AllCardsStatusDTO data ) {
        // vemos si la palabra es válida 
        Boolean validBag = bagService.checkBagIsValid(data.getBag().getCards()); 
        
        // checkeamos que el jugador no tenga más de 7 cartas en la mano y su palabra sea válida 
        checkers.checkNoMoreThan7CardsInHand(data.getHand());       
        checkers.checkWordIsValid(validBag);       

        // si la palabra de la bolsa es válida o está vacía, actualizamos todo y pasamos al siguiente turno 
        handService.update(data.getHand(), matchId, data.getPlayerId());
        bagService.update(data.getBag(), matchId, data.getPlayerId());
        deckService.update(data.getDeck(), matchId); 
        Integer nextTurnId = nextTurn(matchId).getCurrentTurnUserId(); 
        return nextTurnId; 
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
        Player winner = playerService.findById(winnerId);

        // quitamos la carta de la mano o bolsa del perdedor y se la añadimos a la mano del ganador

        if (fromWhere.equals("hand")){ 
            // Selección aleatoria de carta de la mano del perdedor
            HandInGame loserHand = handService.findPlayerHand(matchId, loserId);
            java.util.List<Card> loserHandCards = loserHand.getCards();
            if (loserHandCards == null || loserHandCards.isEmpty()) {
                throw new IllegalStateException("Loser has no cards in hand to steal");
            }
            Card randomCard = loserHandCards.get((int) Math.floor(Math.random() * loserHandCards.size()));
            handService.removeCardFromPlayerHand(randomCard, matchId, loserId);
            handService.addCardToPlayerHand(randomCard, matchId, winnerId);

        } else if (fromWhere.equals("bag")){
            bagService.removeCardFromPlayerBag(card, matchId, loserId);
            handService.addCardToPlayerHand(card, matchId, winnerId); 
        
        } else {
            throw new IllegalArgumentException("fromWhere must be 'hand' or 'bag'");
        }
        // Actualizar estadísticas de batallas
        int winnerBattlesWon = Optional.ofNullable(winner.getBattlesWon()).orElse(0);
        int winnerBattlesPlayed = Optional.ofNullable(winner.getBattlesPlayed()).orElse(0);
        int loserBattlesPlayed = Optional.ofNullable(loser.getBattlesPlayed()).orElse(0);

        winner.setBattlesWon(winnerBattlesWon + 1);
        winner.setBattlesPlayed(winnerBattlesPlayed + 1);
        loser.setBattlesPlayed(loserBattlesPlayed + 1);

        playerRepo.save(winner);
        playerRepo.save(loser);
    }

    /*
     * Método que valida y procesa el robo de una carta de un jugador perdedor
     * Valida los parámetros, encuentra la carta especificada si es necesario y ejecuta el robo
     */
    @Transactional
    public java.util.Map<String, AllCardsStatusDTO> stealCardFromPlayer(Integer matchId, Integer winnerId, Integer loserId, StealCardRequestDTO request){
        String fromWhere = request.getFromWhere();
        
        // Validar parámetro fromWhere
        if (fromWhere == null || (!fromWhere.equals("hand") && !fromWhere.equals("bag"))) {
            throw new IllegalArgumentException("fromWhere must be 'hand' or 'bag'");
        }

        // Buscar la carta si es necesario (solo para robo de bolsa)
        Card cardRef = null;
        if (request.getCardId() != null && fromWhere.equals("bag")) {
            BagInGame loserBag = bagService.findPlayerBag(matchId, loserId);
            Integer cardId = request.getCardId();
            
            cardRef = loserBag.getCards().stream()
                .filter(c -> c.getId() != null && c.getId().equals(cardId))
                .findFirst()
                .orElse(null);
            
            if (cardRef == null) {
                throw new IllegalArgumentException("Card not found in bag");
            }
        }

        // Obtener el usuario actual del turno y ejecutar el robo
        Integer currentTurnUserId = getMatchById(matchId).getCurrentTurnUserId();
        playerDrawsCardFromAnotherPlayerBag(cardRef, matchId, winnerId, loserId, fromWhere, currentTurnUserId);

        // Retornar el estado actualizado de cartas para ganador y perdedor
        AllCardsStatusDTO winnerCards = getAllCards(matchId, winnerId);
        AllCardsStatusDTO loserCards = getAllCards(matchId, loserId);
        
        return java.util.Map.of("winner", winnerCards, "loser", loserCards);
    }




    @Transactional
    public Card playerBeatsNonPlayer(Integer matchId, Integer playerId, Integer npcId){
        // Obtener el jugador y el NPC
        Player player = playerService.findById(playerId);
        Npc npc = npcRepository.findById(npcId)
                .orElseThrow(() -> new IllegalArgumentException("NPC not found"));

        // Robar carta del mazo y añadirla a la mano del jugador
        Card stolenCard = deckService.drawCard(matchId);
        handService.addCardToPlayerHand(stolenCard, matchId, playerId);

        // Actualizar la fuerza del NPC
        npc.setStrength(npc.getStrength() + 1);
        npcRepository.save(npc);

        // Actualizar estadísticas de batallas del jugador
        int battlesWon = Optional.ofNullable(player.getBattlesWon()).orElse(0);
        int battlesPlayed = Optional.ofNullable(player.getBattlesPlayed()).orElse(0);
        player.setBattlesWon(battlesWon + 1);
        player.setBattlesPlayed(battlesPlayed + 1);
        playerRepo.save(player);

        // Notificar cambios de cartas por WebSocket
        AllCardsStatusDTO playerCards = getAllCards(matchId, playerId);
        CardsUpdateDTO update = new CardsUpdateDTO(matchId, playerCards, null);
        matchWebsocketController.notifyCardsUpdate(matchId, update);

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
        // Poner a cero los puntos de acción del jugador que ha perdido
        Player player = playerService.findById(playerId);
        player.setActionPoints(0);
        playerService.save(player);

        if (fromWhere.equals("hand")){
            handService.removeCardFromPlayerHand(card, matchId, playerId);
        } else if (fromWhere.equals("bag")){
            bagService.removeCardFromPlayerBag(card, matchId, playerId); 
        } else {
            throw new IllegalArgumentException("fromWhere must be 'hand' or 'bag'");
        }

        // añadimos la carta seleccionada al mazo de descartes
        deckService.addCardToDiscardedPile(matchId, card);
        
        // Notificar cambios de cartas por WebSocket
        AllCardsStatusDTO playerCards = getAllCards(matchId, playerId);
        CardsUpdateDTO update = new CardsUpdateDTO(matchId, playerCards, null);
        matchWebsocketController.notifyCardsUpdate(matchId, update);
    }

    /*
     * Método que valida y procesa la pérdida de un jugador contra un NPC
     * Valida los parámetros, encuentra la carta especificada y llama a playerLosesAgaintsNonPlayer
     */
    @Transactional
    public AllCardsStatusDTO playerLosesAgainstNpc(Integer matchId, Integer playerId, LoseAgainstNpcRequestDTO request){
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
        } else {
            BagInGame bag = bagService.findPlayerBag(matchId, playerId);
            cardRef = bag.getCards().stream()
                .filter(c -> c.getId() != null && c.getId().equals(cardId))
                .findFirst()
                .orElse(null);
        }

        if (cardRef == null) {
            throw new IllegalArgumentException("Card not found in " + fromWhere);
        }

        // Obtener el usuario actual del turno y procesar la pérdida
        Integer currentTurnUserId = getMatchById(matchId).getCurrentTurnUserId();
        playerLosesAgaintsNonPlayer(cardRef, matchId, playerId, currentTurnUserId, fromWhere);

        // Retornar el estado actualizado de cartas
        return getAllCards(matchId, playerId);
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
    
    @Transactional
    public ActionPointsUpdateDTO consumeActionPointForUser(Integer matchId, Integer userId) {
        Player player = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado en la partida"));
        Integer currentPoints = player.getActionPoints() != null ? player.getActionPoints() : 0;
        if (currentPoints > 0) {
            player.setActionPoints(currentPoints - 1);
            playerRepo.save(player);
        }
        return new ActionPointsUpdateDTO(
            player.getId(),
            player.getUser().getId(),
            player.getUser().getUsername(),
            player.getActionPoints(),
            System.currentTimeMillis()
        );
    }

    @Transactional
    public ActionPointsUpdateDTO consumeAllActionPointForUser(Integer matchId, Integer userId) {
        Player player = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado en la partida"));
        player.setActionPoints(0);
        playerRepo.save(player);
        return new ActionPointsUpdateDTO(
            player.getId(),
            player.getUser().getId(),
            player.getUser().getUsername(),
            player.getActionPoints(),
            System.currentTimeMillis()
        );
    }


    //Función para mover un jugador de una sala a otra adyacente
    @Transactional
    public Player movePlayerToAdyacentRoom(Integer matchId, Integer userId, Integer targetRoomId) {
        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Partida no encontrada"));
        if(match.getCurrentTurnPhase() != TurnPhase.ACTIONS){
            match.setCurrentTurnPhase(TurnPhase.ACTIONS);
        }
        matchRepo.save(match);
        //Recuperar el jugador dentro del match
        Player player = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado en la partida"));
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
            .orElseThrow(() -> new ResourceNotFoundException("Sala destino no encontrada"));
        //Validar si la sala destino es adyacente
        List<Room> adjacent = currentRoom.getAdjacencyList();
        boolean canMove = adjacent.stream()
                .anyMatch(r -> r.getId().equals(targetRoom.getId()));
        if (!canMove) {
            throw new InvalidMovementException("Movimiento no permitido: la sala destino no es adyacente");
        }
        //Actualizar la sala del jugador y sus puntos de acción
        if (!targetRoom.getId().equals(currentRoom.getId())) {
            int visited = Optional.ofNullable(player.getRoomsVisited()).orElse(0);
            player.setRoomsVisited(visited + 1);
        }
        player.setRoom(targetRoom);
        if (player.getActionPoints() > 0) {
            player.setActionPoints(player.getActionPoints() - 1);
        } 

        //Guardar cambios
        return playerRepo.save(player);
    }

    @Transactional
    public Npc moveNpcToAdyacentRoom(Integer matchId, Integer npcId, Integer targetRoomId, Integer userId) {
        Match match = matchRepo.findById(matchId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));

        if(match.getCurrentTurnPhase() != TurnPhase.ACTIONS){
            match.setCurrentTurnPhase(TurnPhase.ACTIONS);
        }
        matchRepo.save(match);

        //Buscamos al npc que queremos mover de la partida
        Npc npc = npcRepository.findById(npcId)
            .orElseThrow(() -> new RuntimeException("NPC no encontrado en la partida"));

        //Obtenemos la habitación actual en la que se encuentra el npc
        Room currentRoomNpc = npc.getRoom();
        if (currentRoomNpc == null) {
            throw new RuntimeException("NPC no tiene sala asignada"); }

        //Obtenemos la sala destino a la que queremos mover al npc
        Room targetRoom = roomRepository.findById(targetRoomId)
            .orElseThrow(() -> new RuntimeException("Sala destino no encontrada"));

        //Comprobamos que el jugador tiene puntos de acción para poder mover al npc
        Player player = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado en la partida"));
        if (player.getActionPoints() <= 0) {
            throw new NoActionPointsException("Move not allowed: player has no action points left"); }

        //Comprobamos que la sala destino es adyacente a la sala actual del npc
        List<Room> adjacent = currentRoomNpc.getAdjacencyList();
        boolean canMove = adjacent.stream()
                .anyMatch(r -> r.getId().equals(targetRoom.getId()));
        if (!canMove) {
            throw new RuntimeException("Movimiento no permitido: la sala destino no es adyacente"); }

        //Actualizamos la sala del npc y los puntos de acción del jugador

        npc.setRoom(targetRoom);
        

        if (player.getActionPoints() > 0) {
            player.setActionPoints(player.getActionPoints() - 1);
            playerRepo.save(player);
        }
        //Guardamos los cambios
        return npcRepository.save(npc);
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
        Room currentRoom = player.getRoom();
        if (currentRoom == null || !targetRoom.getId().equals(currentRoom.getId())) {
            int visited = Optional.ofNullable(player.getRoomsVisited()).orElse(0);
            player.setRoomsVisited(visited + 1);
        }
        player.setRoom(targetRoom);
        player.setStrength(player.getStrength() + 1);
        
        //Guardar cambios
        return playerRepo.save(player);
    }

    public ActionPointsUpdateDTO consumeOneActionPoint(Integer matchId, Integer userId) {
        Player player = playerRepo.findByMatchAndUser(matchId, userId)
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado en la partida"));

        int current = Optional.ofNullable(player.getActionPoints()).orElse(0);
        int updated = Math.max(0, current - 1);
        player.setActionPoints(updated);
        playerRepo.save(player);

        return new ActionPointsUpdateDTO(
            player.getId(),
            player.getUser().getId(),
            player.getUser().getUsername(),
            updated,
            System.currentTimeMillis()
        );
    }
    @Transactional
    public Player movePlayerByFormingRoomName(Integer matchId, Integer userId, Integer targetRoomId) {
        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        if(match.getCurrentTurnPhase() != TurnPhase.ACTIONS){
            match.setCurrentTurnPhase(TurnPhase.ACTIONS);
        }
        matchRepo.save(match);

        Player player = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado en la partida"));
        
        if (player.getActionPoints() <= 0) {
            throw new NoActionPointsException("Move not allowed: player has no action points left");
        }

        Room targetRoom = roomRepository.findById(targetRoomId)
            .orElseThrow(() -> new RuntimeException("Sala destino no encontrada"));

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


        if (!canFormAnyWord) {
            throw new RuntimeException(
                "No se puede formar ninguna palabra del nombre de la sala '" +
                targetRoom.getName() + "' con las letras disponibles en la bolsa");
        }
                }

        // Si llegamos aquí, el movimiento es válido
        Room currentRoom = player.getRoom();
        if (currentRoom == null || !targetRoom.getId().equals(currentRoom.getId())) {
            int visited = Optional.ofNullable(player.getRoomsVisited()).orElse(0);
            player.setRoomsVisited(visited + 1);
        }
        player.setRoom(targetRoom);
        if (player.getActionPoints() > 0) {
            player.setActionPoints(player.getActionPoints() - 1);
        }

        return playerRepo.save(player);
    }


    @Transactional
    public EscapeAttemptResultDTO escapeAttempt( Integer matchId, Integer userId, Integer rolldiceResult) {
        matchRepo.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Match not found"));
        Player p = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found in this match"));

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
            endMatch(matchId, p);
            resultado.setSuccess(true);
            resultado.setWinnerUserId(p.getUser().getId());
            resultado.setDiscardRequired(false);
            return resultado;

        } else {
            //El intento de escape falla y ocurre lo mismo que si un jugador pierde contra un npc en una pelea
            List<Room> availableRooms = getAvailableRoomsForPlayer(matchId);
            if (availableRooms.isEmpty()) {
                throw new IllegalStateException("No available rooms for escape");
            }
            Room randomRoom = availableRooms.get(new Random().nextInt(availableRooms.size()));
            consumeAllActionPointForUser(matchId, userId);
            moveLoserPlayer(matchId, userId, randomRoom.getId());

            resultado.setSuccess(false);
            resultado.setDiscardRequired(true);
            return resultado;
        }
        }



    @Transactional
    public List<Room> getAvailableRoomsForPlayer(Integer matchId) {
        List<Player> players = getMatchById(matchId).getPlayers();
        List<Npc> npcs = getMatchById(matchId).getNpcs();
        List<Room> roomsOcupied = new ArrayList<>();
        for (Player p: players){
            if (!roomsOcupied.contains(p.getRoom())){
                roomsOcupied.add(p.getRoom());
            }
        }
        for (Npc n: npcs){
            if (!roomsOcupied.contains(n.getRoom())){
                roomsOcupied.add(n.getRoom());
            }
        }
        List<Room> rooms = roomRepository.findAll();
        List<Room> towers = roomService.getAllTowers();
        rooms.removeAll(towers);
        rooms.removeAll(roomsOcupied);
        return rooms;

    } 

    @Transactional
    public MatchDTO spectateGame(Integer matchId) {
        Match m = getMatchById(matchId); 
        User currentUser = userService.findCurrentUser(); 
        // checkear si esta ya en la partida como jugador o como espectador 
        if (m.getPlayers().stream().anyMatch(p -> p.getUser().getId().equals(currentUser.getId())) || m.getSpectators().contains(currentUser)){
            return new MatchDTO(m); 
        }
        // si es privado, tiene que ser amigo de todos los jugadores para poder observar
        checkers.checkCanSpectateGame(m, currentUser.getId());
        List<User> spectators = m.getSpectators(); 
        spectators.add(currentUser); 
        m.setSpectators(spectators); 
        matchRepo.save(m); 
        // Notificar a todos en el lobby que alguien se unió
        LobbyUpdateDTO update = lobbyService.createLobbyUpdate(m, "SPECTATOR_JOIN", currentUser.getUsername());
        lobbyWebsocketController.notifyPlayerJoined(m.getId(), update);
        return new MatchDTO(m); 
    }

    @Transactional
    public void stopSpectating(Integer matchId) {
        Match m = getMatchById(matchId); 
        User currentUser = userService.findCurrentUser(); 
        List<User> spectators = m.getSpectators(); 
        spectators.remove(currentUser); 
        m.setSpectators(spectators); 
        matchRepo.save(m); 
        // Notificar a todos en el lobby que alguien se unió
        LobbyUpdateDTO update = lobbyService.createLobbyUpdate(m, "SPECTATOR_LEAVE", currentUser.getUsername());
        lobbyWebsocketController.notifyPlayerJoined(m.getId(), update);
    }


}


