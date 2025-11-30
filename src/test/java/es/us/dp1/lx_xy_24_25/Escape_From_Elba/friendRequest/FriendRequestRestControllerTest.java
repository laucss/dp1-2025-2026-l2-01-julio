package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.Authorities;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class FriendRequestRestControllerTest {

    private static final String BASE_URL = "/api/v1/friendRequests";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendRequestService friendRequestService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // ==== Datos de ejemplo ====

    private User user1;
    private User user2;
    private User user3;

    private FriendRequest fr1;
    private FriendRequest fr2;
    private FriendRequest fr3;

    private User sampleUser(Integer id, String username) {
        User u = new User();
        u.setId(id);
        u.setUsername(username);
        Authorities auth = new Authorities();
        auth.setAuthority("USER");
        u.setAuthority(auth);
        return u;
    }

    private FriendRequest sampleRequest(Integer id, User sender, User receiver, StatusType status) {
        FriendRequest fr = new FriendRequest();
        fr.setId(id);
        fr.setSender(sender);
        fr.setReceiver(receiver);
        fr.setStatus(status);
        return fr;
    }

    @BeforeEach
    void setup() {
        user1 = sampleUser(1, "user1");
        user2 = sampleUser(2, "user2");
        user3 = sampleUser(3, "user3");

        fr1 = sampleRequest(1, user1, user2, StatusType.ACCEPTED);
        fr2 = sampleRequest(2, user1, user3, StatusType.PENDING);
        fr3 = sampleRequest(3, user2, user1, StatusType.PENDING);
    }

    // =======================
    // GET FRIENDS
    // =======================

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void shouldGetFriendsByUserId() throws Exception {

        when(friendRequestService.findAcceptedFriendRequestsByUserId(1))
                .thenReturn(List.of(fr1));

        mockMvc.perform(get(BASE_URL + "/1").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    // =======================
    // GET PENDING BY USER
    // =======================

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void shouldGetPendingRequestsByUserId() throws Exception {

        when(friendRequestService.findFriendRequestsByUserId(1))
                .thenReturn(List.of(fr2, fr3));

        mockMvc.perform(get(BASE_URL + "/1/pending").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    // =======================
    // GET RECEIVED
    // =======================

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void shouldGetReceivedRequestsByUserId() throws Exception {

        when(friendRequestService.findFriendRequestsForUserId(1))
                .thenReturn(List.of(fr3));

        mockMvc.perform(get(BASE_URL + "/1/received").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    // =======================
    // CREATE FRIEND REQUEST
    // =======================

    @Test
    @WithMockUser(username = "user1", authorities = "USER")
    void shouldCreateFriendRequest() throws Exception {

        when(userService.findUser("user2")).thenReturn(user2);
        when(friendRequestService.sendRequest(1, 2)).thenReturn(fr2);

        String body = objectMapper.writeValueAsString(1);

        mockMvc.perform(post(BASE_URL + "/user2")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk());

        verify(friendRequestService).sendRequest(1, 2);
    }

    @Test
    @WithMockUser(username = "user1", authorities = "USER")
    void shouldNotCreateFriendRequestToSelf() throws Exception {

        when(userService.findUser("user1")).thenReturn(user1);

        String body = objectMapper.writeValueAsString(1);

        mockMvc.perform(post(BASE_URL + "/user1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());

        verify(friendRequestService, never()).sendRequest(any(), any());
    }

    // =======================
    // ACCEPT REQUEST
    // =======================

    @Test
    @WithMockUser(username = "user1", authorities = "USER")
    void shouldAcceptMyFriendRequest() throws Exception {

        fr3.setReceiver(user1);

        when(friendRequestService.findFriendRequestPendingById(3)).thenReturn(fr3);
        when(userService.findCurrentUser()).thenReturn(user1);
        when(friendRequestService.acceptRequest(fr3)).thenReturn(fr1);

        String body = objectMapper.writeValueAsString(3);

        mockMvc.perform(put(BASE_URL + "/accept")
                .with(csrf())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = "USER")
    void shouldNotAcceptOtherUsersRequest() throws Exception {

        fr3.setReceiver(user2); // receiver ≠ current user

        when(friendRequestService.findFriendRequestPendingById(3)).thenReturn(fr3);
        when(userService.findCurrentUser()).thenReturn(user1);

        String body = objectMapper.writeValueAsString(3);

        mockMvc.perform(put(BASE_URL + "/accept")
                .with(csrf())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());

        verify(friendRequestService, never()).acceptRequest(any());
    }

    // =======================
    // REJECT REQUEST
    // =======================

    @Test
    @WithMockUser(username = "user1", authorities = "USER")
    void shouldRejectMyFriendRequest() throws Exception {

        fr3.setReceiver(user1);
        FriendRequest rejected = sampleRequest(3, user2, user1, StatusType.REJECTED);

        when(friendRequestService.findFriendRequestPendingById(3)).thenReturn(fr3);
        when(userService.findCurrentUser()).thenReturn(user1);
        when(friendRequestService.rejectRequest(fr3)).thenReturn(rejected);

        String body = objectMapper.writeValueAsString(3);

        mockMvc.perform(put(BASE_URL + "/reject")
                .with(csrf())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = "USER")
    void shouldNotRejectOtherUsersRequest() throws Exception {

        fr3.setReceiver(user2);

        when(friendRequestService.findFriendRequestPendingById(3)).thenReturn(fr3);
        when(userService.findCurrentUser()).thenReturn(user1);

        String body = objectMapper.writeValueAsString(3);

        mockMvc.perform(put(BASE_URL + "/reject")
                .with(csrf())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());

        verify(friendRequestService, never()).rejectRequest(any());
    }

    // =======================
    // DELETE FRIEND
    // =======================

    @Test
    @WithMockUser(username = "user1", authorities = "USER")
    void shouldDeleteFriendAsSender() throws Exception {

        fr1.setSender(user1);

        when(friendRequestService.findById(1)).thenReturn(fr1);
        when(userService.findCurrentUser()).thenReturn(user1);
        doNothing().when(friendRequestService).deleteFriend(fr1);

        String body = objectMapper.writeValueAsString(1);

        mockMvc.perform(delete(BASE_URL)
                .with(csrf())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(friendRequestService).deleteFriend(fr1);
    }

    @Test
    @WithMockUser(username = "user1", authorities = "USER")
    void shouldDeleteFriendAsReceiver() throws Exception {

        fr1.setReceiver(user1);

        when(friendRequestService.findById(1)).thenReturn(fr1);
        when(userService.findCurrentUser()).thenReturn(user1);
        doNothing().when(friendRequestService).deleteFriend(fr1);

        String body = objectMapper.writeValueAsString(1);

        mockMvc.perform(delete(BASE_URL)
                .with(csrf())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user1", authorities = "USER")
    void shouldNotDeleteFriendNotMine() throws Exception {

        fr1.setSender(user2);
        fr1.setReceiver(user3);

        when(friendRequestService.findById(1)).thenReturn(fr1);
        when(userService.findCurrentUser()).thenReturn(user1);

        String body = objectMapper.writeValueAsString(1);

        mockMvc.perform(delete(BASE_URL)
                .with(csrf())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());

        verify(friendRequestService, never()).deleteFriend(any());
    }
}