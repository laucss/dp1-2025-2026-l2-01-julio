package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

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
public class ChatRepositoryTests {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesRepository authorityRepository;

    @Test
    public void findByMatchIdNoChatsReturnsEmptyList() {
        List<ChatMessage> chats = chatRepository.findByMatchId(999); 
        assertTrue(chats.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 11, 12 }) 
    public void findByMatchIdNoChatsParameterized(int matchId) {
        List<ChatMessage> chats = chatRepository.findByMatchId(matchId);
        assertTrue(chats.isEmpty());
    }

    @Test
    public void findByMatchIdSingleChatReturnsOneMessage() {
        Authorities auth = new Authorities();
        auth.setAuthority("PLAYER"); 
        authorityRepository.save(auth); 

  
        User user = new User();
        user.setUsername("Jugador1");
        user.setAuthority(auth); 
        userRepository.save(user);


        Player player = new Player();
        player.setUser(user);
        playerRepository.save(player);

        Match match = new Match();
        match.setIsPrivate(false);
        matchRepository.save(match);

        ChatMessage chat = new ChatMessage();
        chat.setMatch(match);
        chat.setPlayer(player);
        chat.setMessage("Hola mundo");
        chat.setTime(java.time.LocalDateTime.now());
        chatRepository.save(chat);

        List<ChatMessage> chats = chatRepository.findByMatchId(match.getId());
        assertEquals(1, chats.size());
        assertTrue(chats.contains(chat));
    }



    @Test
    public void findByMatchIdMultipleChatsReturnsAllMessages() {

        Authorities auth1 = new Authorities();
        auth1.setAuthority("PLAYER");
        authorityRepository.save(auth1);

        Authorities auth2 = new Authorities();
        auth2.setAuthority("PLAYER");
        authorityRepository.save(auth2);

        User user1 = new User();
        user1.setUsername("Jugador1_" + System.nanoTime()); 
        user1.setAuthority(auth1);
        userRepository.save(user1);

        Player player1 = new Player();
        player1.setUser(user1);
        playerRepository.save(player1);

        User user2 = new User();
        user2.setUsername("Jugador2_" + System.nanoTime()); 
        user2.setAuthority(auth2);
        userRepository.save(user2);

        Player player2 = new Player();
        player2.setUser(user2);
        playerRepository.save(player2);


        Match match = new Match();
        match.setIsPrivate(false);
        matchRepository.save(match);


        ChatMessage chat1 = new ChatMessage();
        chat1.setMatch(match);
        chat1.setPlayer(player1);
        chat1.setMessage("Primer mensaje");
        chat1.setTime(LocalDateTime.now().minusMinutes(1));
        chatRepository.save(chat1);

        ChatMessage chat2 = new ChatMessage();
        chat2.setMatch(match);
        chat2.setPlayer(player2);
        chat2.setMessage("Segundo mensaje");
        chat2.setTime(LocalDateTime.now());
        chatRepository.save(chat2);

        List<ChatMessage> chats = chatRepository.findByMatchId(match.getId());
        assertEquals(2, chats.size());
        assertTrue(chats.contains(chat1));
        assertTrue(chats.contains(chat2));
    }

}
