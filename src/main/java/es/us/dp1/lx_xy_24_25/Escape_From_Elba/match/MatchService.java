package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck.DeckService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;

@Service
public class MatchService {

    DeckService deckService; 
    HandService handService; 

    MatchRepository mrepo;

    @Autowired
    public MatchService(MatchRepository mrepo) {
        this.mrepo = mrepo;
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


    /*
     * METODOS RELACIONADOS CON LAS CARTAS 
     */

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
