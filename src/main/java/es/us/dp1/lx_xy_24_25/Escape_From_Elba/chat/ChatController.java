package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/match/{matchId}/chat")
public class ChatController {
    
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    //Obtener todos los mensajes del chat de mi partida
    @GetMapping("/my")
    public ResponseEntity<List<ChatMessageDTO>> getMyChat(@PathVariable("matchId") Integer matchId) {
        List<ChatMessageDTO> chat = chatService.findChatOfMyGame(matchId)
            .stream()
            .map(ChatMessageDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok(chat);
    }

    //Crear un nuevo mensaje
    @PostMapping
    public ResponseEntity<ChatMessageDTO> createMessage(@PathVariable("matchId") Integer matchId, @Valid @RequestBody ChatMessageDTO dto) {
        ChatMessage created = chatService.createChatMessage(matchId, dto);
        return ResponseEntity.ok(new ChatMessageDTO(created));
    }
}


