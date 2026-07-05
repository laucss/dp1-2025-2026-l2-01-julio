package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchWebsocketController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting.DTOs.WeaponVotingDTO;

@Controller
public class BagWebsocketController {

    @Autowired
    private MatchWebsocketController matchWebsocketController;

    @MessageMapping("/match/{matchId}/weapon-voting")
    public void notifyWeaponVoting(@DestinationVariable Integer matchId, WeaponVotingDTO votingData) {
        // Notificar a todos los jugadores en el match sobre la votación
        matchWebsocketController.notifyWeaponVoting(
            matchId,
            votingData.getWeapon(),
            votingData.getProposingUserId(),
            votingData.getProposingUsername()
        );
    }
}
