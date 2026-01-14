package es.us.dp1.lx_xy_24_25.Escape_From_Elba.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.Authorities;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.AuthoritiesRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserRepository;

@SpringBootTest
@Transactional
public class PlayerRepositoryTests {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private AuthoritiesRepository authorityRepository;



    @Test
    public void findByIdNonExistingReturnsEmpty() {
        Optional<Player> player = playerRepository.findById(999);
        assertTrue(player.isEmpty());
    }


    @Test
    public void findByUserIdNoPlayersReturnsEmptyList() {
        List<Player> players = playerRepository.findByUserId(999);
        assertTrue(players.isEmpty());
    }

    @Test
    public void findByUserIdReturnsPlayers() {
    
        Authorities auth = authorityRepository.findById(2).orElseThrow();

       
        User user = new User();
        user.setUsername("testuser_" + System.nanoTime());
        user.setEmail("testuser_" + System.nanoTime() + "@example.com");
        user.setAuthority(auth);
        userRepository.save(user);

    
        Player player = new Player();
        player.setUser(user);
        playerRepository.save(player);


        List<Player> players = playerRepository.findByUserId(user.getId());
        assertEquals(1, players.size());
        assertThat(players.get(0).getUser().getId()).isEqualTo(user.getId());
    }




    

    @Test
    public void findByMatchAndUserNonExistingReturnsEmpty() {
        Optional<Player> player = playerRepository.findByMatchAndUser(999, 999);
        assertTrue(player.isEmpty());
    }

    @Test
    public void findByMatchAndUserReturnsPlayer() {

        Authorities auth = authorityRepository.findById(2).orElseThrow();

        User user = new User();
        user.setUsername("playerMatch_" + System.nanoTime());
        user.setEmail("playerMatch_" + System.nanoTime() + "@example.com");
        user.setAuthority(auth);
        userRepository.save(user);

        Match match = new Match();
        match.setIsPrivate(false);
        matchRepository.save(match);

        Player player = new Player();
        player.setUser(user);
        player.setMatch(match);
        playerRepository.save(player);


        Optional<Player> result = playerRepository.findByMatchAndUser(match.getId(), user.getId());

        assertTrue(result.isPresent());
        assertEquals(player.getId(), result.get().getId());
    }




    @ParameterizedTest
    @ValueSource(ints = { 100, 200, 300 })
    public void findByMatchIdNoPlayersReturnsEmpty(Integer matchId) {
        List<Player> players = playerRepository.findByMatchId(matchId);
        assertTrue(players.isEmpty());
    }



   

    @Test
    public void getTotalAccionPointsByUserReturnsSum() {
        Authorities auth = authorityRepository.findById(2).orElseThrow();

   
        User user = new User();
        user.setUsername("apUser_" + System.nanoTime());
        user.setEmail("apUser_" + System.nanoTime() + "@example.com");
        user.setAuthority(auth);
        userRepository.save(user);

        Player p1 = new Player();
        p1.setUser(user);
        p1.setActionPoints(3);
        playerRepository.save(p1);

        Player p2 = new Player();
        p2.setUser(user);
        p2.setActionPoints(5);
        playerRepository.save(p2);

        Integer total = playerRepository.getTotalAccionPointsByUser(user.getId());
        assertEquals(8, total);
    }

}
