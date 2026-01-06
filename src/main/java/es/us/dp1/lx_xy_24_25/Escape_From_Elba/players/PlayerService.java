package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import java.util.List;
import java.util.Optional;

import org.apache.maven.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;



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
        Optional<Player> p = playerRepository.findById(id); 
        if (p.isPresent()) {
            return p.get();
        } else {
            throw new ResourceNotFoundException("Player not found with id: " + id);
        }
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

    /*
     * Método que calcula los puntos de acción del jugador, los actualiza y los devuelve
     */
    @Transactional
    public Integer getPlayerActionPoints(Integer matchId, Integer playerId){
        Player player = findById(playerId); 
        if (player == null){
            throw new ResourceNotFoundException("Player not found"); 
        } 
        HandInGame playerHand = handService.findPlayerHand(matchId, playerId); 
        Integer totalCards = playerHand.getCards().size(); 

        if (totalCards > 7 ){
            player.setActionPoints(0); 
        } else {
            Integer points=  7 - totalCards; 
            player.setActionPoints(points);
        } 
        playerRepository.save(player); 
        return player.getActionPoints();
    }
    
    /*
     * Método para quitar un punto de acción a un jugador
     */

    @Transactional
    public void removePlayerActionPoint(Integer matchId, Integer playerId){
        Player player = playerRepository.findById(playerId).orElse(null); 
        if (player != null && player.getMatch().getId().equals(matchId)){
            Integer current_action_points = player.getActionPoints(); 
            if (current_action_points > 0){
                player.setActionPoints(current_action_points - 1); 
                playerRepository.save(player); 
            }
        }
    }




}

