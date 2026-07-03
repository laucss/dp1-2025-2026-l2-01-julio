package es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/v1/invitations")
public class InvitationController {
    
    private final InvitationService invitationService;

    @Autowired
    public InvitationController(InvitationService invitationService ){
        this.invitationService = invitationService; 
    }

    @PostMapping("/invite")
    public ResponseEntity<?> sendInvite(@RequestBody @Valid InviteRequest inviteRequest) {
        InvitationMatch Invitation = invitationService.sendInvite(inviteRequest);
        return ResponseEntity.ok(Invitation);
    }

    @GetMapping
    public ResponseEntity<?> getInvitations(@RequestParam Integer receiverId, 
                                              @RequestParam(required = false) Integer senderId,
                                              @RequestParam(required = false) Integer matchId) {
        // Si se proporcionan senderId y matchId, buscar notificación específica entre esos usuarios
        if (senderId != null && matchId != null) {
            Optional<InvitationMatch> Invitation = invitationService.getInvitationBetweenUsers(senderId, receiverId, matchId);
            if (Invitation.isPresent()) {
                return ResponseEntity.ok(new InvitationMatch[]{Invitation.get()});
            } else {
                return ResponseEntity.ok(new InvitationMatch[]{});
            }
        }
        // Si solo se proporciona receiverId, obtener todas las notificaciones pendientes para ese receiver
        List<InvitationMatch> Invitations = invitationService.getPendingInvitations(receiverId);
        return ResponseEntity.ok(Invitations);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<InvitationStatus> acceptInvite(@PathVariable Integer id) {
        return ResponseEntity.ok(invitationService.acceptInvite(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectInvite(@PathVariable Integer id) {
        InvitationMatch invitationOpt = invitationService.getInvitation(id);
        invitationService.rejectInvite(invitationOpt);
        return ResponseEntity.ok("Invitación rechazada");
    }

}
