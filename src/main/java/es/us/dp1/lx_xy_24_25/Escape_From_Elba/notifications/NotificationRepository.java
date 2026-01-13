package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByReceiverIdAndStatus(Integer receiverId, NotificationStatus status);
    Optional<Notification> findBySenderIdAndReceiverIdAndStatus(Integer senderId, Integer receiverId, NotificationStatus status);
    Optional<Notification> findBySenderIdAndReceiverIdAndMatchIdAndStatus(Integer senderId, Integer receiverId, Integer matchId, NotificationStatus status);
    List<Notification> findByReceiverIdAndMatchIdAndStatus(Integer receiverId, Integer matchId, NotificationStatus status);
}
