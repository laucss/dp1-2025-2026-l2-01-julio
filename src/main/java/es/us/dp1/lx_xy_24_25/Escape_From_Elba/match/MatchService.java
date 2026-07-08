package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
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
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.MoreThan7CardsDrawnException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.MoreThan7CardsInHand;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.ActionPointsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.CardsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchHistorialDTO;
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
import org.springframework.security.access.AccessDeniedException;


@Service
public class MatchService {
    private static final Integer initialCardsPerPlayer = 3; 

    private final DeckService deckService; 
    private final HandService handService; 
    private final BagService bagService;
    private final PlayerService playerService; 
    private final UserService userService;
    private final LobbyWebsocketController lobbyWebsocketController;
    private final MatchWebsocketController matchWebsocketController;
    private final Checkers checkers; 
    private final LobbyService lobbyService;
    private final AbandonedMatchService abandonedMatchService;

    private final MatchRepository matchRepo;
    private final PlayerRepository playerRepo;
    private final RoomRepository roomRepository;
    private final NpcRepository npcRepository;
    private final RoomService roomService;
    private final AbandonedMatchRepository abandonedMatchRepository;

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
            User currentUser = userService.findCurrentUser();

            boolean isPlayer = m.getPlayers().stream()
                    .anyMatch(p -> p.getUser().getId().equals(currentUser.getId()));

            boolean isSpectator = m.getSpectators().stream()
                    .anyMatch(s -> s.getId().equals(currentUser.getId()));

            if (!isPlayer && !isSpectator) {
                throw new AccessDeniedException("You are not allowed to access this match");
            }
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

    private MatchHistorialDTO toHistorialDTO(Match match) {
     return new MatchHistorialDTO(match, userService.findUser(match.getCreatorId()));
    }
    //Para devolver el listado de todo el historial de partidas finalizadas y en curso
    @Transactional(readOnly = true) 
    public Page<MatchHistorialDTO> getFinishedAndInProgressMatches(Integer page, Integer size) {
        return matchRepo.findFinishedAndInProgress(
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime")))
            .map(this::toHistorialDTO);
    }

    //Para devolver el listado de partidas en curso 
    @Transactional(readOnly = true)
    public Page<MatchHistorialDTO> getInProgressMatches(Integer page, Integer size) {
        return matchRepo.findInProgress(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime")))
            .map(this::toHistorialDTO)  ;
    }


    //Para devolver el listado de partidas finalizadas
    @Transactional(readOnly = true)
    public Page<MatchHistorialDTO> getFinishedMatches(Integer page, Integer size) {
            return matchRepo.findFinished(PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "startTime")))
                .map(this::toHistorialDTO);
        }


        //Para devolver el listado de partidas jugadas o abandonadas por un usuario
     @Transactional(readOnly = true)
    public Page<MatchHistorialDTO> getAllMatchesByUser(Integer userId, Integer page, Integer size) {

            Page<Match> matches = matchRepo.findMatchesPlayedOrAbandonedByUser(
                userId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"))
            );

            return matches.map(match -> {

            MatchHistorialDTO dto = toHistorialDTO(match);
         

            dto.setAbandoned(
                abandonedMatchRepository.existsByMatchIdAndUserId(match.getId(), userId)
            );

            return dto;
            });
        }

    

    //Para devolver el listado de partidas jugadas por un usuario
    @Transactional(readOnly = true)
    public Page<MatchHistorialDTO> getMatchesPlayedByUser(Integer userId, Integer page, Integer size) {
        return matchRepo.findMatchesPlayedByUser(userId, PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "startTime")))
            .map(this::toHistorialDTO);
    }

    //Para devolver el listado de partidas creadas por un usuario 
    @Transactional(readOnly = true)
    public Page<MatchHistorialDTO> getMatchesPlayedAndCreatedByUser(Integer userId, Integer page, Integer size) {
        return matchRepo.findMatchesPlayedAndCreatedByUser(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime")))
            .map(this::toHistorialDTO);
    }

    //Para devolver el listado de partidas ganadas por un usuario 
    @Transactional(readOnly = true)
    public Page<MatchHistorialDTO> getMatchesWonByUser(Integer userId, Integer page, Integer size) {
        return matchRepo.findMatchesWonByUser(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime")))
            .map(this::toHistorialDTO);
    }

    //Para devolver el listado de partidas abandonadas por un usuario
    @Transactional(readOnly = true)
    public Page<MatchHistorialDTO> getMatchesAbandonedByUser(Integer userId, Integer page, Integer size) {
        return abandonedMatchRepository.findMatchesAbandonedByUser(userId, PageRequest.of(page, size))
            .map(this::toHistorialDTO);
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
        if (winner != null) {
            if (!m.getPlayers().contains(winner)) {
                throw new IllegalArgumentException("Winner is not a player in this match");
            }
            m.setWinner(winner);
        } else {
            m.setWinner(null);
        }

        if (m.getStartTime() != null) {
            long durationSeconds = Duration.between(m.getStartTime(), m.getEndTime()).toSeconds();
        }

        deleteMatchCards(matchId); 

        matchRepo.save(m);
        matchWebsocketController.notifyEndMatch(matchId, new MatchDTO(m));

        return new MatchDTO(m);
    }




  @Transactional
    public MatchDTO leaveMatch(Integer matchId, Integer userId) {
          System.out.println("HE ENTRADO EN LEAVE MATCH");
        Match m = matchRepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        Player p = playerRepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found in this match"));

        User u = userService.findUser(userId);

        Integer leavingPlayerOrder = p.getOrderInMatch();
        Boolean isLeavingPlayerCurrentTurn = m.getCurrentTurnUserId().equals(userId);

        abandonedMatchService.saveAbandonedMatch(u, m);
        m.getPlayers().remove(p);

        if (isLeavingPlayerCurrentTurn) {
            Integer nextIndx = (leavingPlayerOrder + 1) % m.getPlayers().size();

            Player nextPlayerTurn = m.getPlayers().stream()
                    .filter(pl -> pl.getOrderInMatch().equals(nextIndx))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Player not found"));

            m.setCurrentTurnUserId(nextPlayerTurn.getUser().getId());
            m.setCurrentTurnPhase(TurnPhase.DRAW);
        }

        for (Player player : m.getPlayers()) {
            if (player.getOrderInMatch() > leavingPlayerOrder) {
                player.setOrderInMatch(player.getOrderInMatch() - 1);
                playerRepo.save(player);
            }
        }

        MatchDTO dto;

        if (m.getPlayers().size() < m.getMinPlayers()) {
            dto = endMatch(matchId, null);
        } else {
            matchRepo.save(m);
            dto = new MatchDTO(m);
        }

        System.out.println("ANTES DE NOTIFICAR");
        matchWebsocketController.notifyPlayerLeft(matchId, dto);

        return dto;
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


    @Transactional
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

    //Función que se va a usar cuando un jugador falla el intento de escapar
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


