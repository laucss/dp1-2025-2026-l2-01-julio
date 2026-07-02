package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyWebsocketController {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Notifica a todos en el lobby cuando alguien se une
     */
    public void notifyPlayerJoined(Integer lobbyId, LobbyUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/lobby." + lobbyId + ".updates",
            update
        );
    }

    /**
     * Notifica a todos en el lobby cuando alguien se va
     */
    public void notifyPlayerLeft(Integer lobbyId, LobbyUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/lobby." + lobbyId + ".updates",
            update
        );
    }

    /**
     * Notifica a todos cuando el juego comienza
     */
    public void notifyGameStarted(Integer lobbyId, LobbyUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/lobby." + lobbyId + ".updates",
            update
        );
    }

}
