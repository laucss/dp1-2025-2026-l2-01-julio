package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.DiceTotalsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightDiceUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightResolvedDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.FightUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.ReadyStateUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs.WeaponsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.ActionPointsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.CardsUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.HandUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.MatchDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.NpcLocationUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.PlayerLocationUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.StrengthUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs.TurnUpdateDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.DTOs.WeaponVotingDTO;

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

    public void notifyNpcLocationUpdate(Integer matchId, NpcLocationUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".npc.location",
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

    public void notifyCardsUpdate(Integer matchId, CardsUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".cards",
            update
        );
    }

    public void notifyFightResolved(Integer matchId, FightResolvedDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".fight",
            update
        );
    }

    public void notifyHandUpdate(Integer matchId, Integer playerId, HandUpdateDTO update) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".hand." + playerId,
            update
        );
    }

    public void notifyWeaponVoting(Integer matchId, String weapon, Integer proposingUserId, String proposingUsername) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".weapon.voting",
            new WeaponVotingDTO(weapon, proposingUserId, proposingUsername)
        );
    }

    public void notifyWeaponVotingResult(Integer matchId, Object votingResult) {
        messagingTemplate.convertAndSend(
            "/topic/match." + matchId + ".weapon.voting.result",
            votingResult
        );
    }

    public void notifyPlayerLeft(Integer matchId, MatchDTO match) {
    System.out.println("ENVIANDO PLAYER LEFT POR WEBSOCKET");
    messagingTemplate.convertAndSend(
        "/topic/match." + matchId + ".playerLeft",
        match
    );
}   

}
