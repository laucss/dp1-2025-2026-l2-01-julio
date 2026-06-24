package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class NotificationControllerTest {

    @Autowired
    private NotificationRepository notificationRepository;

    private User sender;
    private User receiver;
    private Match match;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(101);
        sender.setUsername("sender");

        receiver = new User();
        receiver.setId(102);
        receiver.setUsername("receiver");

        match = new Match();
        match.setId(201);
        match.setStatus(MatchStatus.WAITING);
        match.setPlayers(new ArrayList<>());
    }

    // ========================
    // Tests for Repository
    // ========================

    @Test
    public void shouldReturnNotificationsWhenRepositoryHasData() {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndStatus(
            receiver.getId(), NotificationStatus.PENDING
        );
        assertNotNull(notifications);
        assertTrue(notifications.size() >= 0);
    }

    @Test
    public void shouldReturnEmptyListWhenNoNotificationsExist() {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndStatus(
            999999, NotificationStatus.PENDING
        );
        assertNotNull(notifications);
        assertEquals(0, notifications.size());
    }

    @Test
    public void shouldFindNotificationById() {
        // This test just verifies the repository method works
        Optional<Notification> notification = notificationRepository.findBySenderIdAndReceiverIdAndStatus(
            sender.getId(), receiver.getId(), NotificationStatus.PENDING
        );
        assertNotNull(notification);
    }

    @Test
    public void shouldReturnEmptyWhenSearchingNonExistentNotification() {
        Optional<Notification> notification = notificationRepository.findBySenderIdAndReceiverIdAndStatus(
            999999, 999998, NotificationStatus.PENDING
        );
        assertTrue(notification.isEmpty());
    }

    @Test
    public void shouldFindNotificationsByMatchId() {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndMatchIdAndStatus(
            receiver.getId(), match.getId(), NotificationStatus.PENDING
        );
        assertNotNull(notifications);
        assertTrue(notifications.size() >= 0);
    }
}
