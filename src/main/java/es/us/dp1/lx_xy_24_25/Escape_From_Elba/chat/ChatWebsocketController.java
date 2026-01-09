package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class ChatWebsocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat") 
    public void sendMessage(ChatMessage message) {
        System.out.println("Mensaje recibido en el servidor: " + message.getMessage() + " de usuario " + message.getPlayer() + " en juego " + message.getMatch().getId());
        messagingTemplate.convertAndSend(
            "/topic/game." + message.getMatch().getId() + ".chat",
            message
        );
    }
}