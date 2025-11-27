package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;

@Service
public class MatchService {

    DeckService deckService; 
    HandService handService; 
    BagService bagService; 

    MatchRepository mrepo;

    @Autowired
    public MatchService(MatchRepository mrepo, DeckService deckService, HandService handService, BagService bagService) {
        this.mrepo = mrepo;
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
    public Optional<Match> getMatchById(Integer matchId){
        return mrepo.findById(matchId);
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
            handService.createPlayerHand(matchId, player.getId());
            bagService.createPlayerbag(matchId, player.getId());
        }

        m.setDeck(deckService.initializeDeck(matchId)); 
        mrepo.save(m);
        return m;
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
    public void playerDrawsCardFromDeck(Integer matchId, Integer playerId){
        Card stolenCard =deckService.drawCard(matchId); 

        handService.addCardToPlayerHand(stolenCard, matchId, playerId);
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

    @Transactional
    public Player getMatchWinner(Integer matchId) {
        Match match = mrepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (match.getStatus() != MatchStatus.FINISHED) {
            throw new IllegalStateException("Match is not finished yet");
        }

        return match.getWinner();
    }

}
