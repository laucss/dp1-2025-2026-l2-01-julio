package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User sender;
    private User receiver;
    private Match match;
    private Notification notification;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        sender = new User();
        sender.setId(101);
        sender.setUsername("sender_user");

        receiver = new User();
        receiver.setId(102);
        receiver.setUsername("receiver_user");

        match = new Match();
        match.setId(201);

        notification = new Notification();
        notification.setId(501);
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setMatch(match);
        notification.setType(NotificationType.INVITE);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());
    }

    // Tests for sendInvite
    @Test
    public void shouldSendInviteSuccessfully() {
        when(notificationRepository.findBySenderIdAndReceiverIdAndStatus(
            sender.getId(), receiver.getId(), NotificationStatus.PENDING
        )).thenReturn(Optional.empty());
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.sendInvite(sender, receiver, match);

        assertNotNull(result);
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        assertEquals(match, result.getMatch());
        assertEquals(NotificationType.INVITE, result.getType());
        assertEquals(NotificationStatus.PENDING, result.getStatus());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    public void shouldThrowExceptionWhenInviteAlreadyExists() {
        when(notificationRepository.findBySenderIdAndReceiverIdAndStatus(
            sender.getId(), receiver.getId(), NotificationStatus.PENDING
        )).thenReturn(Optional.of(notification));

        assertThrows(IllegalArgumentException.class, () -> {
            notificationService.sendInvite(sender, receiver, match);
        });

        verify(notificationRepository, never()).save(any(Notification.class));
    }

    // Tests for getPendingNotifications
    @Test
    public void shouldGetPendingNotifications() {
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification);
        when(notificationRepository.findByReceiverIdAndStatus(
            receiver.getId(), NotificationStatus.PENDING
        )).thenReturn(notificationList);

        List<Notification> result = notificationService.getPendingNotifications(receiver.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notification, result.get(0));
    }

    @Test
    public void shouldReturnEmptyListWhenNoPendingNotifications() {
        when(notificationRepository.findByReceiverIdAndStatus(
            receiver.getId(), NotificationStatus.PENDING
        )).thenReturn(new ArrayList<>());

        List<Notification> result = notificationService.getPendingNotifications(receiver.getId());

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // Tests for getNotificationBetweenUsers
    @Test
    public void shouldGetNotificationBetweenUsers() {
        when(notificationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
            sender.getId(), receiver.getId(), match.getId(), NotificationStatus.PENDING
        )).thenReturn(Optional.of(notification));

        Optional<Notification> result = notificationService.getNotificationBetweenUsers(
            sender.getId(), receiver.getId(), match.getId()
        );

        assertTrue(result.isPresent());
        assertEquals(notification, result.get());
    }

    @Test
    public void shouldReturnEmptyWhenNoNotificationBetweenUsers() {
        when(notificationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
            sender.getId(), receiver.getId(), match.getId(), NotificationStatus.PENDING
        )).thenReturn(Optional.empty());

        Optional<Notification> result = notificationService.getNotificationBetweenUsers(
            sender.getId(), receiver.getId(), match.getId()
        );

        assertTrue(result.isEmpty());
    }

    // Tests for getNotification
    @Test
    public void shouldGetNotificationById() {
        when(notificationRepository.findById(notification.getId())).thenReturn(Optional.of(notification));

        Optional<Notification> result = notificationService.getNotification(notification.getId());

        assertTrue(result.isPresent());
        assertEquals(notification, result.get());
    }

    @Test
    public void shouldReturnEmptyWhenNotificationNotFound() {
        when(notificationRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Notification> result = notificationService.getNotification(999);

        assertTrue(result.isEmpty());
    }

    // Tests for acceptInvite
    @Test
    public void shouldAcceptInvite() {
        notification.setStatus(NotificationStatus.PENDING);
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.acceptInvite(notification);

        assertEquals(NotificationStatus.ACCEPTED, result.getStatus());
        verify(notificationRepository, times(1)).save(notification);
    }

    // Tests for rejectInvite
    @Test
    public void shouldRejectInvite() {
        notification.setStatus(NotificationStatus.PENDING);
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.rejectInvite(notification);

        assertEquals(NotificationStatus.REJECTED, result.getStatus());
        verify(notificationRepository, times(1)).save(notification);
    }

    // Tests for rejectOtherInvitesForMatch
    @Test
    public void shouldRejectOtherInvitesForMatch() {
        Notification notification1 = new Notification();
        notification1.setId(501);
        notification1.setStatus(NotificationStatus.PENDING);

        Notification notification2 = new Notification();
        notification2.setId(502);
        notification2.setStatus(NotificationStatus.PENDING);

        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification1);
        notificationList.add(notification2);

        when(notificationRepository.findByReceiverIdAndMatchIdAndStatus(
            receiver.getId(), match.getId(), NotificationStatus.PENDING
        )).thenReturn(notificationList);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        notificationService.rejectOtherInvitesForMatch(receiver.getId(), match.getId(), 501);

        verify(notificationRepository, times(1)).findByReceiverIdAndMatchIdAndStatus(
            receiver.getId(), match.getId(), NotificationStatus.PENDING
        );
    }

    @Test
    public void shouldReturnEmptyListWhenNoInvitesToReject() {
        when(notificationRepository.findByReceiverIdAndMatchIdAndStatus(
            receiver.getId(), match.getId(), NotificationStatus.PENDING
        )).thenReturn(new ArrayList<>());

        notificationService.rejectOtherInvitesForMatch(receiver.getId(), match.getId(), 501);

        verify(notificationRepository, times(1)).findByReceiverIdAndMatchIdAndStatus(
            receiver.getId(), match.getId(), NotificationStatus.PENDING
        );
    }
}
