package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.auth.payload.response.MessageResponse;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/friendRequests")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Friends", description = "The Friends API to manage player friends and friend requests.")
public class FriendRequestRestController {

    FriendRequestService friendRequestService;
    UserService userService;
    PlayerService playerService;

    @Autowired
    public FriendRequestRestController(FriendRequestService friendRequestService, UserService userService, PlayerService playerService) {
        this.friendRequestService = friendRequestService;
        this.userService = userService;
        this.playerService = playerService;
    }

    @GetMapping("{userId}")
    @Operation(summary = "Get friends by user id", description = "Get all friends of a user by user id.")
    public ResponseEntity<List<MiniRequestDTO>> getFriendsByUserId(@PathVariable("userId") Integer userId) {
        // Usar el método que devuelve las solicitudes aceptadas (amigos)
        List<MiniRequestDTO> friends = friendRequestService.findAcceptedFriendRequestsByUserId(userId)
        .stream()
        .map(fr -> new MiniRequestDTO(fr, playerService))
        .toList();
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("{userId}/pending")
    @Operation(summary = "Get pending requests by user id", description = "Get all pending friend requests of a user by user id.")
    public ResponseEntity<List<MiniRequestDTO>> getPendingRequestsByUserId(@PathVariable("userId") Integer userId) {
        List<MiniRequestDTO> pendingRequests = friendRequestService.findFriendRequestsByUserId(userId).stream()
                .map(r -> new MiniRequestDTO(r, playerService)).toList();
        return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
    }

    @GetMapping("{userId}/received")
    @Operation(summary = "Get received requests by user id", description = "Get all received friend requests of a user by user id.")
    public ResponseEntity<List<MiniRequestDTO>> getPendingRequestsForUserId(@PathVariable("userId") Integer userId) {
        List<MiniRequestDTO> pendingRequests = friendRequestService.findFriendRequestsForUserId(userId).stream()
                .map(r -> new MiniRequestDTO(r, playerService)).toList();
        return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
    }

    @PostMapping("{receiverName}")
    @Operation(summary = "Create friend request", description = "Create a friend request from the current user to another user.")
    public ResponseEntity<MiniRequestDTO> createFriendRequest(@RequestBody Integer senderId, @PathVariable String receiverName) {
        Integer receiverId = userService.findUser(receiverName).getId();
        if (senderId == receiverId) {
            log.error("User cannot send friend request to himself: senderId={}, receiverId={}", senderId, receiverId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        MiniRequestDTO newRequest = new MiniRequestDTO(friendRequestService.sendRequest(senderId, receiverId), playerService);
        return new ResponseEntity<>(newRequest, HttpStatus.OK);
    }

    @PutMapping("accept")
    @Operation(summary = "Accept friend request", description = "Accept a friend request from another user.")
    public ResponseEntity<MiniRequestDTO> acceptFriendRequest(@RequestBody Integer friendRequestId) {
        FriendRequest friendRequestToAccept = friendRequestService.findFriendRequestPendingById(friendRequestId);
        User currentUser = userService.findCurrentUser();
        if (friendRequestToAccept.getReceiver().getId() != currentUser.getId()) {
            log.error("User cannot accept friend request that is not for him: receiverId={}, currentUserId={}",
                    friendRequestToAccept.getReceiver().getId(), currentUser.getId());
            
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        MiniRequestDTO acceptedRequest = new MiniRequestDTO(friendRequestService.acceptRequest(friendRequestToAccept), playerService);
        return new ResponseEntity<>(acceptedRequest, HttpStatus.OK);
    }

    @PutMapping("reject")
    @Operation(summary = "Reject friend request", description = "Reject a friend request from another user.")
    public ResponseEntity<MiniRequestDTO> rejectFriendRequest(@RequestBody Integer friendRequestId) {
        FriendRequest friendRequestToReject = friendRequestService.findFriendRequestPendingById(friendRequestId);
        User currentUser = userService.findCurrentUser();
        if (friendRequestToReject.getReceiver().getId() != currentUser.getId()) {
            log.error("User cannot reject friend request that is not for him: receiverId={}, currentUserId={}",
                    friendRequestToReject.getReceiver().getId(), currentUser.getId());
            
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        MiniRequestDTO rejectedRequest = new MiniRequestDTO(friendRequestService.rejectRequest(friendRequestToReject), playerService);
        return new ResponseEntity<>(rejectedRequest, HttpStatus.OK);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete friend", description = "Delete a friend from the current user.")
    public ResponseEntity<MessageResponse> delete(@RequestBody Integer friendRequestId) {
        FriendRequest friendRequestToDelete = friendRequestService.findById(friendRequestId);
        Integer currentUserId = userService.findCurrentUser().getId();
        
        if (friendRequestToDelete.getReceiver().getId() != currentUserId
            && friendRequestToDelete.getSender().getId() != currentUserId) {
            log.error("User cannot delete friend request that is not for him: receiverId={}, senderId={}, currentUserId={}",
                    friendRequestToDelete.getReceiver().getId(), friendRequestToDelete.getSender().getId(), currentUserId);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        friendRequestService.deleteFriend(friendRequestToDelete);
        return new ResponseEntity<>(new MessageResponse("Friend deleted."), HttpStatus.OK);
    }
}
