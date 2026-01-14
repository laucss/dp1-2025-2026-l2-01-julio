package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @PostMapping("/invite")
    public ResponseEntity<?> sendInvite(@RequestBody InviteRequest request) {
        Optional<User> senderOpt = userRepository.findById(request.getSenderId());
        Optional<User> receiverOpt = userRepository.findById(request.getReceiverId());
        Optional<Match> matchOpt = matchRepository.findById(request.getMatchId());
        if (senderOpt.isEmpty() || receiverOpt.isEmpty() || matchOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Datos inválidos");
        }
        try {
            Notification notification = notificationService.sendInvite(senderOpt.get(), receiverOpt.get(), matchOpt.get());
            return ResponseEntity.ok(notification);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getNotifications(@RequestParam Integer receiverId, 
                                              @RequestParam(required = false) Integer senderId,
                                              @RequestParam(required = false) Integer matchId) {
        // Si se proporcionan senderId y matchId, buscar notificación específica entre esos usuarios
        if (senderId != null && matchId != null) {
            Optional<Notification> notification = notificationService.getNotificationBetweenUsers(senderId, receiverId, matchId);
            if (notification.isPresent()) {
                return ResponseEntity.ok(new Notification[]{notification.get()});
            } else {
                return ResponseEntity.ok(new Notification[]{});
            }
        }
        // Si solo se proporciona receiverId, obtener todas las notificaciones pendientes para ese receiver
        List<Notification> notifications = notificationService.getPendingNotifications(receiverId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptInvite(@PathVariable Integer id) {
        Optional<Notification> notificationOpt = notificationService.getNotification(id);
        if (notificationOpt.isEmpty()) return ResponseEntity.notFound().build();
        Notification notification = notificationOpt.get();
        Match match = notification.getMatch();
        User receiver = notification.getReceiver();
        
        if (match.getStatus() == MatchStatus.PLAYING) {
            return ResponseEntity.badRequest().body("La partida ya ha comenzado");
        }

        // Verificar si el usuario ya está en la partida
        boolean alreadyInMatch = match.getPlayers().stream()
            .anyMatch(p -> p.getUser().getId().equals(receiver.getId()));

        if (alreadyInMatch) {
            // Ya estaba unido: marcar esta invitación como aceptada y limpiar el resto pero responder 200 con la notificación
            Notification accepted = notificationService.acceptInvite(notification);
            notificationService.rejectOtherInvitesForMatch(receiver.getId(), match.getId(), id);
            return ResponseEntity.ok(accepted);
        }

        if (match.isFull()) {
            return ResponseEntity.badRequest().body("La partida está llena");
        }
        
        // Agregar al jugador a la partida
        Player player = new Player();
        player.setUser(receiver);
        match.addPlayer(player);
        playerRepository.save(player);
        matchRepository.save(match);
        
        // Aceptar esta invitación
        Notification accepted = notificationService.acceptInvite(notification);

        // Rechazar automáticamente todas las otras invitaciones pendientes para este receiver en esta partida
        notificationService.rejectOtherInvitesForMatch(receiver.getId(), match.getId(), id);

        return ResponseEntity.ok(accepted);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectInvite(@PathVariable Integer id) {
        Optional<Notification> notificationOpt = notificationService.getNotification(id);
        if (notificationOpt.isEmpty()) return ResponseEntity.notFound().build();
        notificationService.rejectInvite(notificationOpt.get());
        return ResponseEntity.ok("Invitación rechazada");
    }

    public static class InviteRequest {
        private Integer senderId;
        private Integer receiverId;
        private Integer matchId;
        public Integer getSenderId() { return senderId; }
        public void setSenderId(Integer senderId) { this.senderId = senderId; }
        public Integer getReceiverId() { return receiverId; }
        public void setReceiverId(Integer receiverId) { this.receiverId = receiverId; }
        public Integer getMatchId() { return matchId; }
        public void setMatchId(Integer matchId) { this.matchId = matchId; }
    }
}
