package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class FriendRequestRepositoryTest {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    // -------------------------------------------------------------
    // CONSTANTES ACTUALIZADAS (IDs altos para tests)
    // -------------------------------------------------------------

    private final Integer ACCEPTED_REQUEST_ID = 202;
    private final Integer PENDING_REQUEST_ID = 201;
    private final Integer NON_EXISTENT_REQUEST_ID = 999;

    private final Integer USER_ID_NO_REQUESTS = 110;

    private final Integer USER_ID_TWO_ACCEPTED_REQUESTS = 103;
    private final Integer USER_ID_ONE_ACCEPTED_REQUEST = 102;

    private final Integer USER_ID_ONE_PENDING_REQUEST = 101;
    private final Integer USER_ID_THREE_PENDING_REQUESTS = 104;

    private final Integer USER_ID_ONE_SENT_REQUEST = 101;
    private final Integer USER_ID_TWO_SENT_REQUESTS = 102;

    private final Integer USER_ID_ONE_RECEIVED_REQUEST = 102;
    private final Integer USER_ID_TWO_RECEIVED_REQUESTS = 103;

    // -------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------

    // CORREGIR 
    /* 
    @Test
    public void shouldFindRequestPendingById() {
        Optional<FriendRequest> request = friendRequestRepository.findRequestPendingById(PENDING_REQUEST_ID);
        assertTrue(request.isPresent());
        assertEquals(PENDING_REQUEST_ID, request.get().getId());
    }
        */

    @Test
    public void shouldFindRequestPendingByIdNotPending() {
        Optional<FriendRequest> request = friendRequestRepository.findRequestPendingById(ACCEPTED_REQUEST_ID);
        assertTrue(request.isEmpty());
    }

    @Test
    public void shouldFindRequestPendingByIdNotExist() {
        Optional<FriendRequest> request = friendRequestRepository.findRequestPendingById(NON_EXISTENT_REQUEST_ID);
        assertTrue(request.isEmpty());
    }

    @Test
    public void shouldFindAllRequestsByUserIdNoRequests() {
        List<FriendRequest> requests = friendRequestRepository.findAllRequestsByUserId(USER_ID_NO_REQUESTS);
        assertNotNull(requests);
        assertEquals(0, requests.size());
    }

        // CORREGIR 
    /*
    @Test
    public void shouldFindAllRequestsByUserIdOneRequest() {
        List<FriendRequest> requests = friendRequestRepository.findAllRequestsByUserId(USER_ID_ONE_SENT_REQUEST);
        assertNotNull(requests);
        assertEquals(1, requests.size());
    }

    @Test
    public void shouldFindAllRequestsByUserIdMultipleRequests() {
        List<FriendRequest> requests = friendRequestRepository.findAllRequestsByUserId(USER_ID_TWO_SENT_REQUESTS);
        assertNotNull(requests);
        assertEquals(2, requests.size());
    }
        */

    @Test
    public void shouldFindAllRequestsForUserIdNoRequests() {
        List<FriendRequest> requests = friendRequestRepository.findAllRequestsForUserId(USER_ID_NO_REQUESTS);
        assertNotNull(requests);
        assertEquals(0, requests.size());
    }

        // CORREGIR 
    /*
    @Test
    public void shouldFindAllRequestsForUserIdMultipleRequests() {
        List<FriendRequest> requests = friendRequestRepository.findAllRequestsForUserId(USER_ID_TWO_RECEIVED_REQUESTS);
        assertNotNull(requests);
        assertEquals(2, requests.size());
    }
        */

    @Test
    public void shouldFindAllFriendsByUserIdNone() {
        List<FriendRequest> requests = friendRequestRepository.findAllFriendsByUserId(USER_ID_NO_REQUESTS);
        assertNotNull(requests);
        assertEquals(0, requests.size());
    }


        // CORREGIR 
    /*
    @Test
    public void shouldFindAllFriendsByUserIdOne() {
        List<FriendRequest> requests = friendRequestRepository.findAllFriendsByUserId(USER_ID_ONE_ACCEPTED_REQUEST);
        assertNotNull(requests);
        assertEquals(1, requests.size());
    }

    

    @Test
    public void shouldFindAllFriendsByUserIdMultiple() {
        List<FriendRequest> requests = friendRequestRepository.findAllFriendsByUserId(USER_ID_TWO_ACCEPTED_REQUESTS);
        assertNotNull(requests);
        assertEquals(2, requests.size());
    }

    @Test
    public void shouldFindPendingOrFriendsUsersOne() {
        Optional<FriendRequest> request = friendRequestRepository
                .findPendingOrFriendsUsers(USER_ID_ONE_PENDING_REQUEST, USER_ID_THREE_PENDING_REQUESTS);

        assertTrue(request.isPresent());
    }
        */

    @Test
    public void shouldFindPendingOrFriendsUsersNone() {
        Optional<FriendRequest> request = friendRequestRepository
                .findPendingOrFriendsUsers(999, USER_ID_TWO_ACCEPTED_REQUESTS);

        assertTrue(request.isEmpty());
    }
}
