package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByReceiverIdAndStatus(Integer receiverId, NotificationStatus status);
}
