package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;



@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final HandService handService; 

    @Autowired
    public PlayerService(PlayerRepository playerRepository, HandService handService) {
        this.playerRepository = playerRepository;
        this.handService= handService; 
    }

    /* Encuentra un Player por su User asociado
    @Transactional(readOnly = true)
    public Optional<Player> findByUser(User user) {
        return playerRepository.findByUser(user);
    } */


    @Transactional(readOnly = true)
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Player findById(Integer id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Player> findByUserId(Integer id) {
        return playerRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public Optional<Player> findByMatchIdAndUserId(Integer matchId, Integer userId) {
        return playerRepository.findByMatchAndUser(matchId, userId);
    }


    @Transactional
    public Player save(Player player) {
        return playerRepository.save(player);
    }


    @Transactional
    public void deleteById(Integer id) {
        playerRepository.deleteById(id);
    }

    @Transactional
    public List<Player> getPlayersByMatchId(Integer matchId) {
        return playerRepository.findByMatchId(matchId);
    }

    @Transactional
    public Integer getPlayerActionPoints(Integer matchId, Integer playerId){
        HandInGame playerHand = handService.findPlayerHand(matchId, playerId); 
        Integer totalCards = playerHand.getCards().size(); 

        if (totalCards > 7 ){
            return 0; 
        } else {
            return 7 - totalCards; 
        } 
    }


}

