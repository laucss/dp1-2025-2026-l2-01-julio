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
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomRepository;

@Service
public class MatchService {

    private static final Integer initialCardsPerPlayer = 3; 

    DeckService deckService; 
    HandService handService; 
    BagService bagService; 
    Random ran = new Random();

    MatchRepository mrepo;
    PlayerRepository prepo;
    RoomRepository roomRepository;

    @Autowired
    public MatchService(MatchRepository mrepo, PlayerRepository prepo, RoomRepository roomRepository, DeckService deckService, HandService handService, BagService bagService) {
        this.mrepo = mrepo;
        this.prepo = prepo;
        this.roomRepository = roomRepository;
        this.deckService = deckService;
        this.handService = handService;
        this.bagService = bagService; 
    }

    @Transactional(readOnly = true)
    public List<Match> getAllMatchs() {
        return mrepo.findAll();
    }

    /*@Transactional(readOnly = true)  El metodo esta en LobbyService por ahora
    public Page<Match> getAllPublicLobbies(Pageable pageable) {
        return mrepo.findPublicLobbies(pageable);
    }*/

    @Transactional(readOnly = true)
    public List<Match> getMatchsByName(String name) {
        return mrepo.findByName(name);
    }

    @Transactional(readOnly = true) //????
    public List<Match> getRunningMatches(){
        return mrepo.findAll();
    }

    @Transactional
    public Match save(Match m) {
        mrepo.save(m);
        return m;
    }

    @Transactional(readOnly=true)
    public Match getMatchById(Integer matchId){
        Optional<Match> m= mrepo.findById(matchId);
        if(!m.isPresent())
            throw new ResourceNotFoundException("Match", "id", matchId);
        return m.get();
    }

    @Transactional(readOnly = true)
    public Integer userInMatch(Integer userId) {
        return mrepo.userInMatch(userId);
    }

    @Transactional
    public void delete(Integer id) {
        mrepo.deleteById(id);
    }



    //Funcion para innicializar un match 
    @Transactional
    public Match startMatch(Integer matchId) {
        Match m = mrepo.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Match not found"));
        
        //Cambiamos el estado de la partida a PLAYING
        m.setStatus(MatchStatus.PLAYING);

        //Inicializamos la fecha de inicio
        m.setStartTime(LocalDateTime.now());

        List<Room> availableRooms  = roomRepository.findAll();
        //Inicializamos los npcs de la partida 

        for ( int i=0; i< m.getNumNpcs(); i++){
            Npc npc = new Npc(); //Creamos el npc
            npc.setIsNiallCampbell( i== m.getNumNpcs()-1); // Si es el último npc que creamos, es Niall Campbell
            npc.setStrength(1); // El valor de la fuerza al inicio es 1
            npc.setMatch(m);// Lo asociamos a la partida
            m.getNpcs().add(npc);// Lo añadimos a la lista de npcs de la partida 
            //Hay que añadir la asignación de niall.  
            //Asignamos una sala aleatoria al npc
            Room randomRoom = availableRooms.remove(ran.nextInt(availableRooms.size()));
            npc.setRoom(randomRoom);
        } 


        // le creamos una mano y una bolsa asociadas a cada jugador 
        List<Player> playersInGame = m.getPlayers(); 
        for (Player player : playersInGame){

            player.setDiceOrder(null); // Inicializamos el valor de la tirada de dado a null
            player.setOrderInMatch(null);
            player.setActionPoints(0);
            player.setStrength(1);

            Room randomRoom = availableRooms.remove(ran.nextInt(availableRooms.size()));
            player.setRoom(randomRoom);
        }

        DeckInGame deck = initializePlayerHandCards(matchId, playersInGame); 

        m.setDeck(deck); 
        m.setCurrentTurnUserId(null);
        m.setTurnNumber(0);
        mrepo.save(m);
        return m;
    }

    //Función para decidir el orden de los jugadores en la partida según la tirada de dados.
    @Transactional
    public Match submitDiceAndAssignOrder(Integer matchId, Integer userId, Integer diceRoll) {

        Match match = mrepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));


        Player player = prepo.findByMatchAndUser(matchId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found in this match"));
 
        if (player.getDiceOrder() != null) {
            throw new IllegalArgumentException("Jugador ya ha tirado el dado");
        }


        player.setDiceOrder(diceRoll);
        prepo.save(player);


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
                prepo.save(ordered.get(i));
            }

            match.setCurrentTurnUserId(ordered.get(0).getUser().getId());
            match.setTurnNumber(1);
            mrepo.save(match);
        }

        return match;
    }


    @Transactional
    public void nextTurn(Integer matchId) {
        Match m = mrepo.findById(matchId)
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

        mrepo.save(m);
 
    }


    @Transactional
    public Match endMatch(Integer matchId, Player winner) {
        Match m = mrepo.findById(matchId)
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

        mrepo.save(m);

        return m;
    }



    /*
     * METODOS RELACIONADOS CON LAS CARTAS 
     */

    /*
     * Reparte las cartas iniciales a un jugador 
     * si se quiere cambiar el número de cartas repartidas inicialmente: cambiar variable en DeckService
     */

    @Transactional
    public List<Card> distributeInitialCardsToPlayer(Integer matchId, Integer playerId) {
        List<Card> cards = deckService.drawInitialCardsFromDeck(matchId); 

        handService.addFewCardsToPlayerHand(matchId, playerId, cards);

        return cards; 
    }

    /*
     * Jugador roba una carta del mazo de robar
     */
    @Transactional
    public DrawCardResultDTO playerDrawsCardFromDeck(Integer matchId, Integer playerId){
        Card stolenCard =deckService.drawCard(matchId); 
        DeckInGame deck = deckService.findDeckById(matchId); 

        HandInGame hand = handService.addCardToPlayerHand(stolenCard, matchId, playerId);
        Class<?> cd= deck.getNotDiscardedCards().getClass();
        System.out.println(cd);
        

        return new DrawCardResultDTO(stolenCard, deck, hand); 
    }

    /*
     * Jugador roba una carta de la mano o bolsa de otro jugador
     */

    @Transactional
    public void playerDrawsCardFromAnotherPlayer(Card card, Integer matchId, Integer playerIdWinner, Integer PlayerIdLoser){
    

    }

    /*
     * Jugador descarta una carta de su bolsa o mano
     * Jugador pierde contra no jugador
     */

    @Transactional
    public void playerLosesAgaintsNonPlayer(Card card, Integer matchId, Integer playerId){
    
    }

    /*
     * Jugador recibe la primera carta del mazo de descartes (si hay) tras vencer a niall
     */

    @Transactional
    public void playerWinsNiallCampbell(Integer matchId, Integer playerId){
        Card discardedCard = deckService.getAndRemoveLastDiscardedCard(matchId); 

        handService.addCardToPlayerHand(discardedCard, matchId, playerId);
    }

@Transactional(readOnly = true)
    public AllCardsStatusDTO getAllCards (Integer matchId, Integer playerId){
        DeckInGameDTO deck = new DeckInGameDTO(deckService.findDeckById(matchId)); 

        HandInGameDTO hand = new HandInGameDTO(handService.findPlayerHand(matchId, playerId)); 

        BagInGameDTO bag = new BagInGameDTO(bagService.findPlayerBag(matchId, playerId));

        return new AllCardsStatusDTO(hand, bag, deck, playerId); 
    }

    @Transactional
    public Player getMatchWinner(Integer matchId) {
        Match match = mrepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (match.getStatus() != MatchStatus.FINISHED) {
            throw new IllegalStateException("Match is not finished yet");
        }

        return match.getWinner();
    }
    
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
            }
        }
        return deck;     

    }
}
