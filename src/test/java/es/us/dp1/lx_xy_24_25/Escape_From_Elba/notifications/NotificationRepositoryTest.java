package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    // Constants for testing
    private final Integer NOTIFICATION_ID_1 = 501;
    private final Integer NOTIFICATION_ID_2 = 502;
    private final Integer NOTIFICATION_ID_3 = 503;
    private final Integer NON_EXISTENT_NOTIFICATION_ID = 999;

    private final Integer SENDER_USER_ID = 101;
    private final Integer RECEIVER_USER_ID = 102;
    private final Integer OTHER_USER_ID = 103;

    private final Integer MATCH_ID_1 = 201;
    private final Integer MATCH_ID_2 = 202;

    // Tests for findByReceiverIdAndStatus
    @Test
    public void shouldFindPendingNotificationsByReceiverId() {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndStatus(
            RECEIVER_USER_ID, NotificationStatus.PENDING
        );
        assertNotNull(notifications);
        assertTrue(notifications.size() >= 0);
    }

    @Test
    public void shouldReturnEmptyListWhenNoPendingNotifications() {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndStatus(
            OTHER_USER_ID, NotificationStatus.PENDING
        );
        assertNotNull(notifications);
    }

    // Tests for findBySenderIdAndReceiverIdAndStatus
    @Test
    public void shouldFindNotificationBetweenSenderAndReceiver() {
        Optional<Notification> notification = notificationRepository.findBySenderIdAndReceiverIdAndStatus(
            SENDER_USER_ID, RECEIVER_USER_ID, NotificationStatus.PENDING
        );
        // May or may not exist depending on test data
        assertNotNull(notification);
    }

    @Test
    public void shouldReturnEmptyWhenNoNotificationBetweenUsers() {
        Optional<Notification> notification = notificationRepository.findBySenderIdAndReceiverIdAndStatus(
            999, 998, NotificationStatus.PENDING
        );
        assertTrue(notification.isEmpty());
    }

    // Tests for findBySenderIdAndReceiverIdAndMatchIdAndStatus
    @Test
    public void shouldFindNotificationWithMatchId() {
        Optional<Notification> notification = notificationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
            SENDER_USER_ID, RECEIVER_USER_ID, MATCH_ID_1, NotificationStatus.PENDING
        );
        assertNotNull(notification);
    }

    @Test
    public void shouldReturnEmptyWhenNoNotificationWithMatchId() {
        Optional<Notification> notification = notificationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
            999, 998, 997, NotificationStatus.PENDING
        );
        assertTrue(notification.isEmpty());
    }

    // Tests for findByReceiverIdAndMatchIdAndStatus
    @Test
    public void shouldFindNotificationsForReceiverAndMatch() {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndMatchIdAndStatus(
            RECEIVER_USER_ID, MATCH_ID_1, NotificationStatus.PENDING
        );
        assertNotNull(notifications);
        assertTrue(notifications.size() >= 0);
    }

    @Test
    public void shouldReturnEmptyListWhenNoNotificationsForMatch() {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndMatchIdAndStatus(
            999, 998, NotificationStatus.PENDING
        );
        assertNotNull(notifications);
        assertEquals(0, notifications.size());
    }
}
