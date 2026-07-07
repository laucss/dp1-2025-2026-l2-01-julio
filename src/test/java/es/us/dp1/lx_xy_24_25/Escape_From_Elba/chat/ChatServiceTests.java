package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTests {

    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        chatService = new ChatService(playerService, chatRepository, userService);
    }

    @Test
    public void findChatOfMyGameNoUserReturnsEmpty() {
        when(userService.findCurrentUser()).thenReturn(null);

        List<ChatMessage> chat = chatService.findChatOfMyGame(1);
        assertTrue(chat.isEmpty(), "Debe devolver lista vacía si no hay usuario autenticado");
    }

    @Test
    public void findChatOfMyGameUserNotInMatchReturnsEmpty() {
        User user = new User();
        user.setId(1);
        when(userService.findCurrentUser()).thenReturn(user);
        when(playerService.findOptionalPlayerByMatchIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.empty());

        List<ChatMessage> chat = chatService.findChatOfMyGame(1);
        assertTrue(chat.isEmpty(), "Debe devolver lista vacía si el usuario no es jugador de la partida");
    }

    @Test
    public void findChatOfMyGameHasMessagesReturnsSorted() {
    
        User user = new User();
        user.setId(1);
        when(userService.findCurrentUser()).thenReturn(user);

      
        Player player = new Player();
        player.setId(1);
        Match match = new Match();
        match.setId(1);
        player.setMatch(match);
        when(playerService.findOptionalPlayerByMatchIdAndUserId(match.getId(), user.getId()))
            .thenReturn(Optional.of(player));


        ChatMessage msg1 = new ChatMessage();
        msg1.setMessage("Mensaje primero");
        msg1.setDate(LocalDateTime.of(2026, 1, 5, 10, 0));
        msg1.setPlayer(player);
        msg1.setMatch(match);

        ChatMessage msg2 = new ChatMessage();
        msg2.setMessage("Mensaje segundo");
        msg2.setDate(LocalDateTime.of(2026, 1, 5, 11, 0));
        msg2.setPlayer(player);
        msg2.setMatch(match);

        when(chatRepository.findByMatchId(match.getId()))
            .thenReturn(new ArrayList<>(List.of(msg2, msg1))); 

        List<ChatMessage> chat = chatService.findChatOfMyGame(match.getId());

        assertEquals(2, chat.size());
        assertEquals(msg1, chat.get(0), "El primer mensaje debe ser el más antiguo");
        assertEquals(msg2, chat.get(1), "El segundo mensaje debe ser el más reciente");
    }


    @Test
    public void createChatMessageNoUserThrows() {
        when(userService.findCurrentUser()).thenReturn(null);
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setMessage("Hola");

        assertThrows(IllegalStateException.class, () -> chatService.createChatMessage(1, dto));
    }

    @Test
    public void createChatMessageUserNotInMatchThrows() {
        User user = new User();
        user.setId(1);
        when(userService.findCurrentUser()).thenReturn(user);
        when(playerService.findOptionalPlayerByMatchIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.empty());

        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setMessage("Hola");

        assertThrows(IllegalStateException.class, () -> chatService.createChatMessage(1, dto));
    }

    @Test
    public void createChatMessageSuccess() {
        User user = new User();
        user.setId(1);
        when(userService.findCurrentUser()).thenReturn(user);

        Player player = new Player();
        player.setId(10);
        when(playerService.findOptionalPlayerByMatchIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.of(player));

        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setMessage("Hola");

        ChatMessage savedMessage = new ChatMessage();
        savedMessage.setMessage("Hola");
        savedMessage.setPlayer(player);

        when(chatRepository.save(any(ChatMessage.class))).thenReturn(savedMessage);

        ChatMessage result = chatService.createChatMessage(1, dto);
        assertEquals("Hola", result.getMessage());
        assertEquals(player, result.getPlayer());
    }
}
