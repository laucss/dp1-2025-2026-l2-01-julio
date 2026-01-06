package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification sendInvite(User sender, User receiver, Match match) {
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setMatch(match);
        notification.setType(NotificationType.INVITE);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public List<Notification> getPendingNotifications(Integer receiverId) {
        return notificationRepository.findByReceiverIdAndStatus(receiverId, NotificationStatus.PENDING);
    }

    public Optional<Notification> getNotification(Integer id) {
        return notificationRepository.findById(id);
    }

    public Notification acceptInvite(Notification notification) {
        notification.setStatus(NotificationStatus.ACCEPTED);
        return notificationRepository.save(notification);
    }

    public Notification rejectInvite(Notification notification) {
        notification.setStatus(NotificationStatus.REJECTED);
        return notificationRepository.save(notification);
    }
}
