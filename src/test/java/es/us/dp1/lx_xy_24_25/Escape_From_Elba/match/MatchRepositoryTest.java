package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.AuthoritiesRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserRepository;

import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class MatchRepositoryTest {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Guardar y recuperar una partida")
    void saveAndFindById() {
        Match match = new Match();
        match.setName("Test Match");
        match.setIsPrivate(false);
        match.setStatus(MatchStatus.WAITING);
        match.setMinPlayers(3);
        match.setMaxPlayers(6);

        Match saved = matchRepository.save(match);

        Optional<Match> found = matchRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Match");
        assertThat(found.get().getIsPrivate()).isFalse();
    }



    @Test
    @DisplayName("Encontrar lobbies privados por código")
    void findPrivateLobbyByCode() {
        Match privateMatch = new Match();
        privateMatch.setName("Private Lobby");
        privateMatch.setIsPrivate(true);
        privateMatch.setStatus(MatchStatus.WAITING);
        privateMatch.setCode("ABC123");
        matchRepository.save(privateMatch);

        Optional<Match> found = matchRepository.findPrivateLobbyByCode("abc123");
        assertThat(found).isPresent();
        assertThat(found.get().getCode()).isEqualTo("ABC123");
    }

    @Test
    @DisplayName("Encontrar partidas en progreso")
    void findInProgress() {
        Match match = new Match();
        match.setName("In Progress Match");
        match.setStartTime(LocalDateTime.now().minusMinutes(10));
        match.setIsPrivate(false);
        match.setEndTime(null);
        match.setStatus(MatchStatus.PLAYING);
        matchRepository.save(match);

        List<Match> inProgress = matchRepository.findInProgress();
        assertThat(inProgress).isNotEmpty();
        assertThat(inProgress).allMatch(m -> m.getEndTime() == null);
    }

    /* 
    @Test
    @DisplayName("Encontrar partidas finalizadas")
    void findFinished() {
        Match match = new Match();
        match.setName("Finished Match");
        match.setStartTime(LocalDateTime.now().minusHours(1));
        match.setIsPrivate(false);
        match.setEndTime(LocalDateTime.now());
        match.setStatus(MatchStatus.FINISHED);
        matchRepository.save(match);

        List<Match> finished = matchRepository.findFinished();
        assertThat(finished).isNotEmpty();
        assertThat(finished).allMatch(m -> m.getEndTime() != null);
    }
        */

    @Test
    @DisplayName("Verificar límite de jugadores")
    void isFullAndMinReached() {
        Match match = new Match();
        match.setName("Player Limit Test");
        match.setIsPrivate(false);
        match.setMinPlayers(3);
        match.setMaxPlayers(3);
        match.setStatus(MatchStatus.WAITING);

        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();

        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        assertThat(match.isFull()).isTrue();
        assertThat(match.isMinReached()).isTrue();
        assertThat(match.isMinNotReached()).isFalse();
    }

        @Test
    @DisplayName("Buscar partidas por nombre")
    void findByName() {
        Match match = new Match();
        match.setName("Name Test");
        match.setIsPrivate(false);
        match.setStatus(MatchStatus.WAITING);
        matchRepository.save(match);

        List<Match> result = matchRepository.findByName("Name Test");
        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(m -> m.getName().equals("Name Test"));
    }

    @Test
    @DisplayName("Buscar lobbies privados")
    void findPrivateLobbies() {
        Match privateMatch = new Match();
        privateMatch.setName("Private Lobby Test");
        privateMatch.setIsPrivate(true);
        privateMatch.setStatus(MatchStatus.WAITING);
        matchRepository.save(privateMatch);

        List<Match> privateLobbies = matchRepository.findPrivateLobbies();
        assertThat(privateLobbies).isNotEmpty();
        assertThat(privateLobbies).allMatch(m -> m.getIsPrivate() && m.getStatus() == MatchStatus.WAITING);
    }

    /* 
    @Test
    @DisplayName("Encontrar lobby donde un usuario está")
    void findLobbyWhereUserIsIn() {
        Authorities auth = new Authorities();
        auth.setAuthority("USER");
        authoritiesRepository.save(auth);

        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        User user = new User();
        user.setUsername("player_" + uniqueSuffix);
        user.setPassword("pass");
        user.setEmail("player1_" + uniqueSuffix + "@example.com");
        user.setAuthority(auth);
        userRepository.save(user);

        Match match = new Match();
        match.setName("User Lobby Match");
        match.setIsPrivate(false);
        match.setStatus(MatchStatus.WAITING);
        matchRepository.save(match); 

        Player player = new Player();
        player.setUser(user);
        match.addPlayer(player);  

        matchRepository.save(match);

        Optional<Match> found = matchRepository.findLobbyWhereUserIsIn(user.getId());
        assertThat(found).isPresent();

        }

    @Test
    @DisplayName("Encontrar partidas listas para empezar")
    void findReadyToStart() {
        Match match = new Match();
        match.setName("Ready Match");
        match.setIsPrivate(false);
        match.setMinPlayers(3);
        match.setMaxPlayers(3);
        match.setStatus(MatchStatus.WAITING);

        Authorities auth = new Authorities();
        auth.setAuthority("USER");
        authoritiesRepository.save(auth);

        String suffix = String.valueOf(System.currentTimeMillis());
        User user = new User();
        user.setUsername("player_" + suffix);
        user.setPassword("pass");
        user.setEmail("player_" + suffix + "@example.com");
        user.setAuthority(auth);
        userRepository.save(user);

        Player player = new Player();
        player.setUser(user);
        match.addPlayer(player);

        matchRepository.save(match);

        List<Match> readyMatches = matchRepository.findReadyToStart();
        assertThat(readyMatches).isNotEmpty();
        assertThat(readyMatches).allMatch(m -> m.isMinReached() && m.getStartTime() == null);
    }


    @Test
    @DisplayName("Encontrar partidas jugables")
    void findPlayable() {
        Match match = new Match();
        match.setName("Playable Match");
        match.setIsPrivate(false);
        match.setMinPlayers(3);
        match.setMaxPlayers(3);
        match.setStatus(MatchStatus.WAITING);

       
        
        Authorities auth = new Authorities();
        auth.setAuthority("USER");
        authoritiesRepository.save(auth);

        String suffix = String.valueOf(System.currentTimeMillis());
        User user = new User();
        user.setUsername("player_" + suffix);
        user.setPassword("pass");
        user.setEmail("player_" + suffix + "@example.com");
        user.setAuthority(auth);
        userRepository.save(user);

        Player player = new Player();
        player.setUser(user);
        match.addPlayer(player);

        matchRepository.save(match);

        List<Match> playable = matchRepository.findPlayable();
        assertThat(playable).isNotEmpty();
        assertThat(playable).allMatch(m -> m.isMinReached() || m.getStartTime() != null);
    }

    @Test
    @DisplayName("Encontrar ID de la partida donde un usuario está")
    void userInMatch() {
        Authorities auth = new Authorities();
        auth.setAuthority("USER");
        authoritiesRepository.save(auth);

        String suffix = String.valueOf(System.currentTimeMillis());
        User user = new User();
        user.setUsername("player_" + suffix);
        user.setPassword("pass");
        user.setEmail("player_" + suffix + "@example.com");
        user.setAuthority(auth);
        userRepository.save(user);

        Match match = new Match();
        match.setName("User Match " + suffix);
        match.setIsPrivate(false);
        match.setStatus(MatchStatus.WAITING);

        Player player = new Player();
        player.setUser(user);
        match.addPlayer(player);

        matchRepository.save(match);

        Integer matchId = matchRepository.userInMatch(user.getId());
        assertThat(matchId).isEqualTo(match.getId());
    }

    */
}
