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
    public List<ChatMessage> findChatOfMyGame() {
        var user = userService.findCurrentUser();
        if (user == null) return Collections.emptyList();

        Integer userId = user.getId();
        Optional<Player> maybePlayer = playerService.findById(userId);
        if (maybePlayer.isEmpty()) return Collections.emptyList();

        Player me = maybePlayer.get();
        if (me.getMatch() == null || me.getMatch().getId() == null) return Collections.emptyList();

        Integer matchId = me.getMatch().getId();
        List<ChatMessage> chat = chatRepository.findByMatchId(matchId);
        if (chat == null) return Collections.emptyList();

        chat.sort(Comparator.comparing(ChatMessage::getTime, Comparator.nullsLast(Comparator.naturalOrder())));
        return chat;
    }
    

    @Transactional
    public ChatMessage createChatMessage(ChatMessageDTO chatMessageDTO) {
        try {
            var user = userService.findCurrentUser();
            if (user == null) throw new IllegalStateException("Usuario no autenticado");

            Integer userId = user.getId();
            Integer matchId = chatMessageDTO.getMatchId();
            if (matchId == null) throw new IllegalArgumentException("matchId es obligatorio en el DTO");

            // playerService devuelve Optional<Player>, usar orElseThrow para obtener el Player
            Optional<Player> maybeAuthor = playerService.findByMatchIdAndUserId(matchId, userId);
            Player author = maybeAuthor.orElseThrow(() -> new IllegalStateException("Jugador no encontrado en la partida indicada"));

            Match match = author.getMatch();
            if (match == null || match.getId() == null)
                throw new IllegalStateException("El jugador no está en ninguna partida");

            ChatMessage created = new ChatMessage();
            created.setPlayer(author);
            created.setMessage(chatMessageDTO.getMessage());
            created.setDate(LocalDateTime.now());
            created.setMatch(match);

            return chatRepository.save(created);
        } catch (ResourceNotFoundException ex) {
            throw new IllegalStateException("Jugador actual no encontrado", ex);
        }
    }
} 
