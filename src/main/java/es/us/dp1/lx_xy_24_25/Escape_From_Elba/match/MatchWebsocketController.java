package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MatchWebsocketController {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Notifica a todos los jugadores cuando cambia el turno
     */
    public void notifyTurnUpdate(Integer matchId, TurnUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".turn",
            update
        );
    }

    /**
     * Notifica a todos los jugadores cuando se inicia o termina un combate
     */
    public void notifyFightUpdate(Integer matchId, FightUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".fight",
            update
        );
    }

    /**
     * Notifica a todos los jugadores las tiradas de dados durante un combate
     */
    public void notifyFightDiceUpdate(Integer matchId, FightDiceUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".fight.dice",
            update
        );
    }
}
