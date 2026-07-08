package es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications.NotificationType;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications.NotificationWebController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;


@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @InjectMocks
    private InvitationService invitationService;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchService matchService;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private Checkers checkers;

    @Mock
    private NotificationWebController notificationWebController;

    private User sender;
    private User receiver;
    private Match match;
    private InviteRequest request;

    @BeforeEach
    void setUp() {

        sender = new User();
        sender.setId(1);
        sender.setUsername("sender");

        receiver = new User();
        receiver.setId(2);
        receiver.setUsername("receiver");

        match = new Match();
        match.setId(10);

        request = new InviteRequest();
        request.setSenderId(1);
        request.setReceiverId(2);
        request.setMatchId(10);
        request.setSpectator(false);
    }

@Test
void shouldSendPlayerInvitationSuccessfully() {

    when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
    when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
    when(matchRepository.findById(match.getId())).thenReturn(Optional.of(match));

    when(invitationRepository.findBySenderIdAndReceiverIdAndStatus(
            sender.getId(),
            receiver.getId(),
            InvitationStatus.PENDING))
            .thenReturn(Optional.empty());

    when(invitationRepository.save(any(InvitationMatch.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    InvitationMatch result = invitationService.sendInvite(request);

    assertNotNull(result);
    assertEquals(sender, result.getSender());
    assertEquals(receiver, result.getReceiver());
    assertEquals(match, result.getMatch());
    assertEquals(InvitationStatus.PENDING, result.getStatus());
    assertEquals(InvitationType.INVITE, result.getType());
    assertFalse(result.isSpectator());
    assertNotNull(result.getCreatedAt());

    verify(notificationWebController)
            .notifyNewNotification(receiver.getId(), NotificationType.MATCH_INVITATION_AS_PLAYER);

    verify(invitationRepository).save(any(InvitationMatch.class));
}

@Test
void shouldSendSpectatorInvitationSuccessfully() {

    request.setSpectator(true);

    when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
    when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
    when(matchRepository.findById(match.getId())).thenReturn(Optional.of(match));

    when(invitationRepository.findBySenderIdAndReceiverIdAndStatus(
            sender.getId(),
            receiver.getId(),
            InvitationStatus.PENDING))
            .thenReturn(Optional.empty());

    when(invitationRepository.save(any(InvitationMatch.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    InvitationMatch result = invitationService.sendInvite(request);

    assertTrue(result.isSpectator());

    verify(notificationWebController)
            .notifyNewNotification(receiver.getId(), NotificationType.MATCH_INVITATION_AS_SPECTATOR);
}

@Test
void shouldThrowWhenSenderDoesNotExist() {

    when(userRepository.findById(sender.getId()))
            .thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
            () -> invitationService.sendInvite(request));

    verify(invitationRepository, never()).save(any());
}

@Test
void shouldThrowWhenReceiverDoesNotExist() {

    when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
    when(userRepository.findById(receiver.getId())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
            () -> invitationService.sendInvite(request));

    verify(invitationRepository, never()).save(any());
}

@Test
void shouldThrowWhenMatchDoesNotExist() {

    when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
    when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
    when(matchRepository.findById(match.getId())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
            () -> invitationService.sendInvite(request));

    verify(invitationRepository, never()).save(any());
}

@Test
void shouldThrowWhenPendingInvitationAlreadyExists() {

    when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
    when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
    when(matchRepository.findById(match.getId())).thenReturn(Optional.of(match));

    when(invitationRepository.findBySenderIdAndReceiverIdAndStatus(
            sender.getId(),
            receiver.getId(),
            InvitationStatus.PENDING))
            .thenReturn(Optional.of(new InvitationMatch()));

    assertThrows(IllegalArgumentException.class,
            () -> invitationService.sendInvite(request));

    verify(invitationRepository, never()).save(any());
}

@Test
void shouldSaveInvitationExactlyOnce() {

    when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
    when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
    when(matchRepository.findById(match.getId())).thenReturn(Optional.of(match));

    when(invitationRepository.findBySenderIdAndReceiverIdAndStatus(
            sender.getId(),
            receiver.getId(),
            InvitationStatus.PENDING))
            .thenReturn(Optional.empty());

    when(invitationRepository.save(any(InvitationMatch.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    invitationService.sendInvite(request);

    verify(invitationRepository, times(1)).save(any(InvitationMatch.class));
}

@Test
void shouldReturnPendingInvitations() {

    InvitationMatch invitation = new InvitationMatch();

    when(invitationRepository.findByReceiverIdAndStatus(
            receiver.getId(),
            InvitationStatus.PENDING))
            .thenReturn(List.of(invitation));

    List<InvitationMatch> result =
            invitationService.getPendingInvitations(receiver.getId());

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(invitation, result.get(0));

    verify(invitationRepository)
            .findByReceiverIdAndStatus(receiver.getId(), InvitationStatus.PENDING);
}
@Test
void shouldReturnEmptyPendingInvitations() {

    when(invitationRepository.findByReceiverIdAndStatus(
            receiver.getId(),
            InvitationStatus.PENDING))
            .thenReturn(List.of());

    List<InvitationMatch> result =
            invitationService.getPendingInvitations(receiver.getId());

    assertNotNull(result);
    assertTrue(result.isEmpty());

    verify(invitationRepository)
            .findByReceiverIdAndStatus(receiver.getId(), InvitationStatus.PENDING);
}
@Test
void shouldReturnInvitationBetweenUsers() {

    InvitationMatch invitation = new InvitationMatch();

    when(invitationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
            sender.getId(),
            receiver.getId(),
            match.getId(),
            InvitationStatus.PENDING))
            .thenReturn(Optional.of(invitation));

    Optional<InvitationMatch> result =
            invitationService.getInvitationBetweenUsers(
                    sender.getId(),
                    receiver.getId(),
                    match.getId());

    assertTrue(result.isPresent());
    assertEquals(invitation, result.get());

    verify(invitationRepository)
            .findBySenderIdAndReceiverIdAndMatchIdAndStatus(
                    sender.getId(),
                    receiver.getId(),
                    match.getId(),
                    InvitationStatus.PENDING);
}
@Test
void shouldReturnEmptyWhenInvitationBetweenUsersDoesNotExist() {

    when(invitationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
            sender.getId(),
            receiver.getId(),
            match.getId(),
            InvitationStatus.PENDING))
            .thenReturn(Optional.empty());

    Optional<InvitationMatch> result =
            invitationService.getInvitationBetweenUsers(
                    sender.getId(),
                    receiver.getId(),
                    match.getId());

    assertTrue(result.isEmpty());

    verify(invitationRepository)
            .findBySenderIdAndReceiverIdAndMatchIdAndStatus(
                    sender.getId(),
                    receiver.getId(),
                    match.getId(),
                    InvitationStatus.PENDING);
}
@Test
void shouldReturnInvitationById() {

    InvitationMatch invitation = new InvitationMatch();
    invitation.setId(1);

    when(invitationRepository.findById(1))
            .thenReturn(Optional.of(invitation));

    InvitationMatch result = invitationService.getInvitation(1);

    assertNotNull(result);
    assertEquals(invitation, result);

    verify(invitationRepository).findById(1);
}
@Test
void shouldThrowWhenInvitationDoesNotExist() {

    when(invitationRepository.findById(1))
            .thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
            () -> invitationService.getInvitation(1));

    verify(invitationRepository).findById(1);
}

@Test
void shouldAcceptPlayerInvitation() {

    InvitationMatch invitation = new InvitationMatch();
    invitation.setId(1);
    invitation.setSender(sender);
    invitation.setReceiver(receiver);
    invitation.setMatch(match);
    invitation.setStatus(InvitationStatus.PENDING);
    invitation.setSpectator(false);

    match.setPlayers(new ArrayList<>());

    when(invitationRepository.findById(1))
            .thenReturn(Optional.of(invitation));

    when(invitationRepository.save(any()))
            .thenAnswer(i -> i.getArgument(0));

    InvitationService spy = Mockito.spy(invitationService);
    doNothing().when(spy).rejectOtherInvitesForMatch(any(), any(), any());

    InvitationMatch result = spy.acceptInvite(1);

    assertEquals(InvitationStatus.ACCEPTED, result.getStatus());

    verify(lobbyService).joinLobby(match.getId());
    verify(matchService, never()).spectateGame(any());
    verify(notificationWebController)
            .notifyNewNotification(sender.getId(), NotificationType.ACCEPT_INVITATION);
    verify(invitationRepository).save(invitation);
}

@Test
void shouldAcceptSpectatorInvitation() {

    InvitationMatch invitation = new InvitationMatch();
    invitation.setId(1);
    invitation.setSender(sender);
    invitation.setReceiver(receiver);
    invitation.setMatch(match);
    invitation.setSpectator(true);

    match.setPlayers(new ArrayList<>());

    when(invitationRepository.findById(1))
            .thenReturn(Optional.of(invitation));

    when(invitationRepository.save(any()))
            .thenAnswer(i -> i.getArgument(0));

    InvitationService spy = Mockito.spy(invitationService);
    doNothing().when(spy).rejectOtherInvitesForMatch(any(), any(), any());

    InvitationMatch result = spy.acceptInvite(1);

    assertEquals(InvitationStatus.ACCEPTED, result.getStatus());

    verify(matchService).spectateGame(match.getId());
    verify(lobbyService, never()).joinLobby(any());
}

@Test
void shouldAcceptInvitationWhenPlayerAlreadyInMatch() {

    Player player = new Player();
    player.setUser(receiver);

    match.setPlayers(List.of(player));

    InvitationMatch invitation = new InvitationMatch();
    invitation.setId(1);
    invitation.setSender(sender);
    invitation.setReceiver(receiver);
    invitation.setMatch(match);

    when(invitationRepository.findById(1))
            .thenReturn(Optional.of(invitation));

    when(invitationRepository.save(any()))
            .thenAnswer(i -> i.getArgument(0));

    InvitationService spy = Mockito.spy(invitationService);
    doNothing().when(spy).rejectOtherInvitesForMatch(any(), any(), any());

    spy.acceptInvite(1);

    verify(spy, times(2))
            .rejectOtherInvitesForMatch(receiver.getId(), match.getId(), invitation.getId());
}


@Test
void shouldNotifySenderWhenInvitationAccepted() {

    InvitationMatch invitation = new InvitationMatch();
    invitation.setId(1);
    invitation.setSender(sender);
    invitation.setReceiver(receiver);
    invitation.setMatch(match);

    match.setPlayers(new ArrayList<>());

    when(invitationRepository.findById(1))
            .thenReturn(Optional.of(invitation));

    when(invitationRepository.save(any()))
            .thenAnswer(i -> i.getArgument(0));

    InvitationService spy = Mockito.spy(invitationService);
    doNothing().when(spy).rejectOtherInvitesForMatch(any(), any(), any());

    spy.acceptInvite(1);

    verify(notificationWebController)
            .notifyNewNotification(
                    sender.getId(),
                    NotificationType.ACCEPT_INVITATION);
}

@Test
void shouldRejectInvitation() {

    InvitationMatch invitation = new InvitationMatch();
    invitation.setSender(sender);
    invitation.setStatus(InvitationStatus.PENDING);

    when(invitationRepository.save(any()))
            .thenAnswer(i -> i.getArgument(0));

    InvitationMatch result = invitationService.rejectInvite(invitation);

    assertEquals(InvitationStatus.REJECTED, result.getStatus());

    verify(notificationWebController)
            .notifyNewNotification(
                    sender.getId(),
                    NotificationType.REJECT_INVITATION);

    verify(invitationRepository).save(invitation);
}

@Test
void shouldReturnSavedRejectedInvitation() {

    InvitationMatch invitation = new InvitationMatch();
    invitation.setSender(sender);

    when(invitationRepository.save(any()))
            .thenAnswer(i -> i.getArgument(0));

    InvitationMatch result = invitationService.rejectInvite(invitation);

    assertSame(invitation, result);
}

@Test
void shouldDoNothingWhenNoPendingInvitationsExist() {

    when(invitationRepository.findByReceiverIdAndMatchIdAndStatus(
            receiver.getId(),
            match.getId(),
            InvitationStatus.PENDING))
            .thenReturn(List.of());

    invitationService.rejectOtherInvitesForMatch(
            receiver.getId(),
            match.getId(),
            1);

    verify(invitationRepository, never()).save(any());
}

}
