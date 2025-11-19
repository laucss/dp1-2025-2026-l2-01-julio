package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
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

    @Transactional
    public void delete(Integer id) {
        mrepo.deleteById(id);
    }



    //Funcion para innicializar un match 
    @Transactional
    public void startMatch(Integer matchId) {
        Match m = mrepo.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Match not found"));
        
        // le creamos una mano y una bolsa asociadas a cada jugador 
        List<Player> playersInGame = m.getPlayers(); 
        for (Player player : playersInGame){
            handService.createPlayerHand(matchId, player.getId());
            bagService.createPlayerbag(matchId, player.getId());
        }
        
        m.setStatus(MatchStatus.PLAYING);
        m.setStartTime(LocalDateTime.now());
        m.setDeck(deckService.initializeDeck(matchId));
        mrepo.save(m);
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


    
}
