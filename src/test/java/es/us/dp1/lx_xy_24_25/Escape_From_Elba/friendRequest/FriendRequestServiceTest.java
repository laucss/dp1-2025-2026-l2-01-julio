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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.AlreadyCreatedException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserRepository;

public class FriendRequestServiceTest {

    @InjectMocks
    private FriendRequestService friendRequestService;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserRepository userRepository;

        @Mock
        private FriendWebsocketController friendWebsocketController;

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
    // Type: Backend unit test
    // Description: Tests that the service finds a friend request by ID.
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
    // Type: Backend unit test
    // Description: Tests that the service does not find a friend request with an incorrect ID.
    void shouldNotFindByIncorrectId() {
        when(friendRequestRepository.findById(REQUEST_NOT_EXIST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.friendRequestService.findById(REQUEST_NOT_EXIST_ID));
        verify(friendRequestRepository).findById(REQUEST_NOT_EXIST_ID);
    }

    @Test
    // Type: Backend unit test
    // Description: Tests that the service finds the pending friend request corresponding to the requested ID.
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
    // Type: Backend unit test
    // Description: Tests that the service throws when pending request with ID does not exist.
    void shouldNotFindFriendRequestByIncorrectId() {
        when(friendRequestRepository.findRequestPendingById(REQUEST_NOT_EXIST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.friendRequestService.findFriendRequestPendingById(REQUEST_NOT_EXIST_ID));
        verify(friendRequestRepository).findRequestPendingById(REQUEST_NOT_EXIST_ID);
    }

    @Test
    // Type: Backend unit test
    // Description: Tests that the service finds all friend requests sent by the requested user.
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
    // Type: Backend unit test
    // Description: Tests that the service finds all friend requests received by the requested user.
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
    // Type: Backend unit test
    // Description: Tests that the service finds all the friend requests (friends) of the requested user.
    void shouldFindAllFriendRequests() {
        List<FriendRequest> requests = List.of(new FriendRequest(), new FriendRequest());
        when(friendRequestRepository.findAllFriendsByUserId(CURRENT_USER_ID))
                .thenReturn(requests);

        List<FriendRequest> foundRequests =
                this.friendRequestService.findFriendsByUserId(CURRENT_USER_ID);

        assertEquals(requests, foundRequests);
        verify(friendRequestRepository).findAllFriendsByUserId(CURRENT_USER_ID);
    }

    @Test
    // Type: Backend unit test
    // Description: Tests that the service resolves friends (Users) correctly from friend requests.
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
                this.friendRequestService.findFriendsByPlayerId(CURRENT_USER_ID);

        assertEquals(expectedUsers, foundUsers);
        verify(friendRequestRepository).findAllFriendsByUserId(CURRENT_USER_ID);
    }

    @Test
    // Type: Backend unit test
    // Description: Tests that the service finds accepted friend requests for a user.
    void shouldFindAcceptedFriendRequestsByUserId() {
        List<FriendRequest> requests = List.of(new FriendRequest(), new FriendRequest());
        when(friendRequestRepository.findAllFriendsByUserId(CURRENT_USER_ID))
                .thenReturn(requests);

        List<FriendRequest> foundRequests =
                this.friendRequestService.findAcceptedFriendRequestsByUserId(CURRENT_USER_ID);

        assertEquals(requests, foundRequests);
        verify(friendRequestRepository).findAllFriendsByUserId(CURRENT_USER_ID);
    }

    @Test
    @Transactional
    // Type: Backend unit test
    // Description: Tests that the method returns true when the users are friends or have a pending friend request.
    void shouldReturnTrueWhenUsersAreFriendsOrHavePendingRequest() {
        when(friendRequestRepository.findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID))
                .thenReturn(Optional.of(new FriendRequest()));

        assertTrue(this.friendRequestService.isPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID));
        verify(friendRequestRepository).findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID);
    }

    @Test
    @Transactional
    // Type: Backend unit test
    // Description: Tests that the method returns false when the users are not friends or do not have a pending friend request.
    void shouldReturnFalseWhenUsersAreNotFriendsOrDoNotHavePendingRequest() {
        when(friendRequestRepository.findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID))
                .thenReturn(Optional.empty());

        assertTrue(!this.friendRequestService.isPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID));
        verify(friendRequestRepository).findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID);
    }

    @Test
    @Transactional
    // Type: Backend unit test
    // Description: Tests that the method returns a user with the requested ID.
    void shouldFindUserById() {
        User user = new User();
        user.setId(CURRENT_USER_ID);

        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(user));

        User foundUser = this.friendRequestService.findPlayerById(CURRENT_USER_ID);

        assertEquals(user, foundUser);
        verify(userRepository).findById(CURRENT_USER_ID);
    }

    @Test
    @Transactional
    // Type: Backend unit test
    // Description: Tests that the method throws an exception when the user with the requested ID does not exist.
    void shouldNotFindUserById() {
        when(userRepository.findById(USER_ID_NOT_EXIST)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.friendRequestService.findPlayerById(USER_ID_NOT_EXIST));
        verify(userRepository).findById(USER_ID_NOT_EXIST);
    }

    @Test
    @Transactional
    // Type: Backend unit test
    // Description: Tests that the service creates a friend request.
    void shouldCreateRequest() {
        User sender = new User();
        sender.setId(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(sender));

        User receiver = new User();
        receiver.setId(ANOTHER_USER_ID);
        when(userRepository.findById(ANOTHER_USER_ID)).thenReturn(Optional.of(receiver));

        when(friendRequestRepository.save(any(FriendRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FriendRequest newRequest =
                this.friendRequestService.sendRequest(CURRENT_USER_ID, ANOTHER_USER_ID);

        assertEquals(sender, newRequest.getSender());
        assertEquals(receiver, newRequest.getReceiver());
        assertTrue(newRequest.getStatus() == StatusType.PENDING);
        verify(friendRequestRepository).save(newRequest);
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(userRepository).findById(ANOTHER_USER_ID);
    }

    @Test
    @Transactional
    // Type: Backend unit test
    // Description: Tests that the service throws an exception when the users are already friends and a new friend request is requested.
    void shouldNotCreateRequestWhenUsersAreAlreadyFriends() {
        when(friendRequestRepository.findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID))
                .thenReturn(Optional.of(new FriendRequest()));

        AlreadyCreatedException exception = assertThrows(AlreadyCreatedException.class,
                () -> this.friendRequestService.sendRequest(CURRENT_USER_ID, ANOTHER_USER_ID));
        verify(friendRequestRepository).findPendingOrFriendsUsers(CURRENT_USER_ID, ANOTHER_USER_ID);
        assertTrue(exception.getMessage().contains("Friend request"));
    }

    @Test
    @Transactional
    // Type: Backend unit test
    // Description: Tests that the service accepts a friend request.
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
        assertTrue(updatedRequest.getStatus() == StatusType.ACCEPTED);
    }

    @Test
    @Transactional
    // Type: Backend unit test
    // Description: Tests that the service rejects a friend request.
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
        assertTrue(updatedRequest.getStatus() == StatusType.REJECTED);
    }

    @Test
    @Transactional
    // Type: Backend unit test
    // Description: Tests that the service deletes a friend.
    void shouldDeleteFriend() {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(REQUEST_EXIST_ACCEPTED_ID);

                User sender = new User(); sender.setId(CURRENT_USER_ID);
                User receiver = new User(); receiver.setId(ANOTHER_USER_ID);
                friendRequest.setSender(sender);
                friendRequest.setReceiver(receiver);

                this.friendRequestService.deleteFriend(friendRequest);

                verify(friendRequestRepository).deleteById(REQUEST_EXIST_ACCEPTED_ID);
    }
}
