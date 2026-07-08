package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.AlreadyCreatedException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications.NotificationType;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications.NotificationWebController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserRepository;

@DisplayName("FriendRequestService Unit Tests")
public class FriendRequestServiceTest {

    @InjectMocks
    private FriendRequestService friendRequestService;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendWebsocketController friendWebsocketController;

    // FALTABA ESTE MOCK EXPLICITAMENTE PARA EL CONSTRUCTOR DEL SERVICIO
    @Mock
    private NotificationWebController notificationWebController;

    private static final Integer CURRENT_USER_ID = 4;
    private static final Integer ANOTHER_USER_ID = 5;
    private static final Integer USER_ID_NOT_EXIST = 100;
    private static final Integer REQUEST_EXIST_ACCEPTED_ID = 2;
    private static final Integer REQUEST_NOT_EXIST_ID = 80;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe encontrar una solicitud de amistad por su ID")
    void shouldFindById() {
        FriendRequest request = new FriendRequest();
        request.setId(REQUEST_EXIST_ACCEPTED_ID);

        when(friendRequestRepository.findById(REQUEST_EXIST_ACCEPTED_ID))
                .thenReturn(Optional.of(request));

        FriendRequest foundRequest = this.friendRequestService.findById(REQUEST_EXIST_ACCEPTED_ID);

        assertEquals(request, foundRequest);
        verify(friendRequestRepository).findById(REQUEST_EXIST_ACCEPTED_ID);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si la solicitud no existe por ID")
    void shouldNotFindByIncorrectId() {
        when(friendRequestRepository.findById(REQUEST_NOT_EXIST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.friendRequestService.findById(REQUEST_NOT_EXIST_ID));
        verify(friendRequestRepository).findById(REQUEST_NOT_EXIST_ID);
    }

    @Test
    @DisplayName("Debe encontrar una solicitud pendiente mediante su ID")
    void shouldFindFriendRequestById() {
        FriendRequest request = new FriendRequest();
        request.setId(REQUEST_EXIST_ACCEPTED_ID);

        when(friendRequestRepository.findRequestPendingById(REQUEST_EXIST_ACCEPTED_ID))
                .thenReturn(Optional.of(request));

        FriendRequest foundRequest =
                this.friendRequestService.findFriendRequestPendingById(REQUEST_EXIST_ACCEPTED_ID);

        assertEquals(request, foundRequest);
        verify(friendRequestRepository).findRequestPendingById(REQUEST_EXIST_ACCEPTED_ID);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si no hay solicitud pendiente con ese ID")
    void shouldNotFindFriendRequestByIncorrectId() {
        when(friendRequestRepository.findRequestPendingById(REQUEST_NOT_EXIST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.friendRequestService.findFriendRequestPendingById(REQUEST_NOT_EXIST_ID));
        verify(friendRequestRepository).findRequestPendingById(REQUEST_NOT_EXIST_ID);
    }

    @Test
    @DisplayName("Debe encontrar todas las solicitudes enviadas (PENDING) por un usuario emisor")
    void shouldFindAllSentRequests() {
        List<FriendRequest> requests = List.of(new FriendRequest(), new FriendRequest());
        when(friendRequestRepository.findAllRequestsByUserId(CURRENT_USER_ID))
                .thenReturn(requests);

        List<FriendRequest> foundRequests =
                this.friendRequestService.findFriendRequestsByUserId(CURRENT_USER_ID);

        assertEquals(requests, foundRequests);
        verify(friendRequestRepository).findAllRequestsByUserId(CURRENT_USER_ID);
    }

    @Test
    @DisplayName("Debe encontrar todas las solicitudes recibidas (PENDING) para un usuario receptor")
    void shouldFindAllReceivedRequests() {
        List<FriendRequest> requests = List.of(new FriendRequest(), new FriendRequest());
        when(friendRequestRepository.findAllRequestsForUserId(CURRENT_USER_ID))
                .thenReturn(requests);

        List<FriendRequest> foundRequests =
                this.friendRequestService.findFriendRequestsForUserId(CURRENT_USER_ID);

        assertEquals(requests, foundRequests);
        verify(friendRequestRepository).findAllRequestsForUserId(CURRENT_USER_ID);
    }

    @Test
    @DisplayName("Debe encontrar todas las solicitudes aceptadas asociadas a un ID de usuario")
    void shouldFindAllFriendRequests() {
        List<FriendRequest> requests = List.of(new FriendRequest(), new FriendRequest());
        when(friendRequestRepository.findAllFriendsByUserId(CURRENT_USER_ID))
                .thenReturn(requests);

        // CORREGIDO: El método correcto del servicio que mapea a findAllFriendsByUserId es el siguiente
        List<FriendRequest> foundRequests =
                this.friendRequestService.findAcceptedFriendRequestsByUserId(CURRENT_USER_ID);

        assertEquals(requests, foundRequests);
        verify(friendRequestRepository).findAllFriendsByUserId(CURRENT_USER_ID);
    }

    @Test
    @DisplayName("Debe resolver correctamente la lista de amigos mutuos (como objetos User)")
    void shouldFindAllFriendsAsUsers() {
        User currentUser = new User();
        currentUser.setId(CURRENT_USER_ID);
        User friend1 = new User();
        friend1.setId(ANOTHER_USER_ID);
        User friend2 = new User();
        friend2.setId(ANOTHER_USER_ID + 1);

        FriendRequest request1 = new FriendRequest();
        request1.setReceiver(currentUser);
        request1.setSender(friend1);

        FriendRequest request2 = new FriendRequest();
        request2.setReceiver(friend2);
        request2.setSender(currentUser);

        List<FriendRequest> requests = List.of(request1, request2);
        List<User> expectedUsers = List.of(friend1, friend2);

        when(friendRequestRepository.findAllFriendsByUserId(CURRENT_USER_ID))
                .thenReturn(requests);

        List<User> foundUsers =
                this.friendRequestService.findFriendsByUserId(CURRENT_USER_ID);

        assertEquals(expectedUsers, foundUsers);
        verify(friendRequestRepository).findAllFriendsByUserId(CURRENT_USER_ID);
    }

    @Test
    @DisplayName("Debe retornar verdadero si existe una relación previa o pendiente entre los dos usuarios")
    void shouldReturnTrueWhenUsersAreFriendsOrHavePendingRequest() {
        when(friendRequestRepository.findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID))
                .thenReturn(Optional.of(new FriendRequest()));

        assertTrue(this.friendRequestService.isPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID));
        verify(friendRequestRepository).findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID);
    }

    @Test
    @DisplayName("Debe retornar falso si no existe ninguna relación activa o pendiente entre ambos usuarios")
    void shouldReturnFalseWhenUsersAreNotFriendsOrDoNotHavePendingRequest() {
        when(friendRequestRepository.findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID))
                .thenReturn(Optional.empty());

        assertTrue(!this.friendRequestService.isPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID));
        verify(friendRequestRepository).findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID);
    }

    @Test
    void shouldFindUserById() {
        User user = new User();
        user.setId(CURRENT_USER_ID);

        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(user));

        User foundUser = this.friendRequestService.findPlayerById(CURRENT_USER_ID);

        assertEquals(user, foundUser);
        verify(userRepository).findById(CURRENT_USER_ID);
    }

    @Test
    void shouldNotFindUserById() {
        when(userRepository.findById(USER_ID_NOT_EXIST)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.friendRequestService.findPlayerById(USER_ID_NOT_EXIST));
        verify(userRepository).findById(USER_ID_NOT_EXIST);
    }

    @Test
    @DisplayName("Debe enviar correctamente una solicitud de amistad y lanzar notificaciones WebSocket")
    void shouldCreateRequest() {
        User sender = new User();
        sender.setId(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(sender));

        User receiver = new User();
        receiver.setId(ANOTHER_USER_ID);
        when(userRepository.findById(ANOTHER_USER_ID)).thenReturn(Optional.of(receiver));

        when(friendRequestRepository.findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID))
                .thenReturn(Optional.empty());

        when(friendRequestRepository.save(any(FriendRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FriendRequest newRequest =
                this.friendRequestService.sendRequest(CURRENT_USER_ID, ANOTHER_USER_ID);

        assertEquals(sender, newRequest.getSender());
        assertEquals(receiver, newRequest.getReceiver());
        assertEquals(StatusType.PENDING, newRequest.getStatus());
        
        verify(friendRequestRepository).save(any(FriendRequest.class));
        verify(friendWebsocketController).notifyNewFriendRequest(ANOTHER_USER_ID, newRequest);
        verify(notificationWebController).notifyNewNotification(ANOTHER_USER_ID, NotificationType.FRIEND_REQUEST);
    }

    @Test
    @DisplayName("Debe impedir el envío de solicitud si ya existe un vínculo pendiente o aceptado")
    void shouldNotCreateRequestWhenUsersAreAlreadyFriends() {
        when(friendRequestRepository.findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID))
                .thenReturn(Optional.of(new FriendRequest()));

        AlreadyCreatedException exception = assertThrows(AlreadyCreatedException.class,
                () -> this.friendRequestService.sendRequest(CURRENT_USER_ID, ANOTHER_USER_ID));
        
        verify(friendRequestRepository).findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID);
        assertTrue(exception.getMessage().contains("Friend request"));
    }

    @Test
    @DisplayName("Debe cambiar el estado a ACCEPTED y notificar vía WebSockets")
    void shouldAcceptRequest() {
        when(friendRequestRepository.save(any(FriendRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FriendRequest toUpdate = new FriendRequest();
        User sender = new User(); sender.setId(CURRENT_USER_ID);
        User receiver = new User(); receiver.setId(ANOTHER_USER_ID);
        toUpdate.setSender(sender);
        toUpdate.setReceiver(receiver);

        FriendRequest updatedRequest = this.friendRequestService.acceptRequest(toUpdate);

        verify(friendRequestRepository).save(updatedRequest);
        assertEquals(StatusType.ACCEPTED, updatedRequest.getStatus());
        verify(friendWebsocketController).notifyRequestAccepted(CURRENT_USER_ID, updatedRequest);
        verify(friendWebsocketController).notifyFriendRequestUpdate(ANOTHER_USER_ID, updatedRequest);
        verify(notificationWebController).notifyNewNotification(CURRENT_USER_ID, NotificationType.ACCEPT_FRIEND_REQUEST);
    }

    @Test
    @DisplayName("Debe cambiar el estado a REJECTED y emitir la notificación correspondiente")
    void shouldRejectRequest() {
        when(friendRequestRepository.save(any(FriendRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FriendRequest toUpdate = new FriendRequest();
        User sender = new User(); sender.setId(CURRENT_USER_ID);
        User receiver = new User(); receiver.setId(ANOTHER_USER_ID);
        toUpdate.setSender(sender);
        toUpdate.setReceiver(receiver);

        FriendRequest updatedRequest = this.friendRequestService.rejectRequest(toUpdate);

        verify(friendRequestRepository).save(updatedRequest);
        assertEquals(StatusType.REJECTED, updatedRequest.getStatus());
        verify(friendWebsocketController).notifyRequestRejected(CURRENT_USER_ID, updatedRequest);
        verify(friendWebsocketController).notifyFriendRequestUpdate(ANOTHER_USER_ID, updatedRequest);
        verify(notificationWebController).notifyNewNotification(CURRENT_USER_ID, NotificationType.REJECT_FRIEND_REQUEST);
    }

    @Test
    @DisplayName("Debe eliminar el registro de amistad y propagar la baja del canal WebSocket")
    void shouldDeleteFriend() {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(REQUEST_EXIST_ACCEPTED_ID);

        User sender = new User(); sender.setId(CURRENT_USER_ID);
        User receiver = new User(); receiver.setId(ANOTHER_USER_ID);
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);

        this.friendRequestService.deleteFriend(friendRequest);

        verify(friendRequestRepository).deleteById(REQUEST_EXIST_ACCEPTED_ID);
        verify(friendWebsocketController).notifyFriendRequestDeleted(CURRENT_USER_ID, ANOTHER_USER_ID, REQUEST_EXIST_ACCEPTED_ID);
    }
}