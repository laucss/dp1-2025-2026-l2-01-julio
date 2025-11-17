 /*package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;

    public ChatService(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> findChatOfMyGame() {
        var user = userService.findCurrentUser();
        if (user == null) return Collections.emptyList();

        Player me = user.toPlayer();
        if (me == null || me.getMatch() == null || me.getMatch().getId() == null) return Collections.emptyList();

        List<ChatMessage> chat = chatRepository.findByMatchId(me.getMatch().getId());
        Collections.sort(chat); 
        return chat;
    }

    @Transactional
    public ChatMessage createChatMessage(ChatMessageDTO chatMessageDTO) {
        var user = userService.findCurrentUser();
        if (user == null) throw new IllegalStateException("Usuario no autenticado");

        Player player = user.toPlayer();
        if (player == null) throw new IllegalStateException("Usuario no tiene Player asociado");

        Match match = player.getMatch();
        if (match == null || match.getId() == null) throw new IllegalStateException("El jugador no está en ninguna partida");

        ChatMessage created = new ChatMessage();
        created.setPlayer(player);
        created.setMessage(chatMessageDTO.getMessage()); 
        created.setDate(LocalDateTime.now()); 
        created.setMatch(match);

        return chatRepository.save(created);
    }
} */
