package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.transaction.Transactional;

@Service
public class AbandonedMatchService {

    private final AbandonedMatchRepository abandonedMatchRepository;

    public AbandonedMatchService(AbandonedMatchRepository abandonedMatchRepository) {
        this.abandonedMatchRepository = abandonedMatchRepository;
    }


    @Transactional
    public void saveAbandonedMatch(User user, Match match) {

        AbandonedMatch abandoned = new AbandonedMatch();
        abandoned.setUser(user);
        abandoned.setMatch(match);
    

        abandonedMatchRepository.save(abandoned);
    }
    
}
