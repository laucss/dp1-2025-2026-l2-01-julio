package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /* Encuentra un Player por su User asociado
    @Transactional(readOnly = true)
    public Optional<Player> findByUser(User user) {
        return playerRepository.findByUser(user);
    } */


    @Transactional(readOnly = true)
    public Optional<Player> findById(Integer id) {
        return playerRepository.findById(id);
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
}

