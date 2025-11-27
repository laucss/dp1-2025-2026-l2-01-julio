package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;

@Service
public class ChatService {
    private final PlayerService playerService;
    private final ChatRepository chatRepository;
    private final UserService userService;

    @Autowired
    public ChatService(PlayerService playerService, ChatRepository chatRepository, UserService userService) {
        this.playerService = playerService;
        this.chatRepository = chatRepository;
        this.userService = userService;

    }
    
    @Transactional(readOnly = true)
    public List<ChatMessage> findChatOfMyGame(Integer matchId) {

        var user = userService.findCurrentUser();
        if (user == null) return Collections.emptyList();

        Optional<Player> maybePlayer = playerService.findByMatchIdAndUserId(matchId, user.getId());
        if (maybePlayer.isEmpty()) return Collections.emptyList(); 

        List<ChatMessage> chat = chatRepository.findByMatchId(matchId);
        if (chat == null) return Collections.emptyList();

        chat.sort(Comparator.comparing(ChatMessage::getTime)); 
        return chat;
    }

    

    @Transactional
    public ChatMessage createChatMessage(Integer matchId, ChatMessageDTO dto) {

        var user = userService.findCurrentUser();
        if (user == null)
            throw new IllegalStateException("Usuario no autenticado");

        Optional<Player> maybeAuthor =
                playerService.findByMatchIdAndUserId(matchId, user.getId());

        Player author = maybeAuthor.orElseThrow(
                () -> new IllegalStateException("No eres jugador de esta partida")
        );

        ChatMessage created = new ChatMessage();
        created.setPlayer(author);
        created.setMatch(author.getMatch());
        created.setMessage(dto.getMessage());
        created.setDate(LocalDateTime.now());

        return chatRepository.save(created);
    }

} 
