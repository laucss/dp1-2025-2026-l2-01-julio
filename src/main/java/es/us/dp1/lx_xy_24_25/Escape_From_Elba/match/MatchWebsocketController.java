package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MatchWebsocketController {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyTurnUpdate(Integer matchId, TurnUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".turn",
            update
        );
    }

    public void notifyFightUpdate(Integer matchId, FightUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".fight",
            update
        );
    }

    public void notifyFightDiceUpdate(Integer matchId, FightDiceUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".fight.dice",
            update
        );
    }

    public void notifyPlayerLocationUpdate(Integer matchId, PlayerLocationUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".location",
            update
        );
    }

    public void notifyDiceTotalsUpdate(Integer matchId, DiceTotalsUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".fight.totals",
            update
        );
    }

    public void notifyActionPointsUpdate(Integer matchId, ActionPointsUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".actionPoints",
            update
        );
    }

    public void notifyStrengthUpdate(Integer matchId, StrengthUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".strength",
            update
        );
    }

    public void notifyReadyStateUpdate(Integer matchId, ReadyStateUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".fight.ready",
            update
        );
    }

    public void notifyWeaponsUpdate(Integer matchId, WeaponsUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".fight.weapons",
            update
        );
    }
}
