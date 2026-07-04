package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest.FriendRequest;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest.FriendRequestService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations.InvitationMatch;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations.InvitationService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;

@Service
public class NotificationService {

    private final InvitationService invitationService; 
    private final UserService userService; 
    private final FriendRequestService friendRequestService;

    @Autowired
    public NotificationService(InvitationService invitationService, 
        UserService userService, FriendRequestService friendRequestService) {
        this.invitationService = invitationService; 
        this.userService = userService; 
        this.friendRequestService = friendRequestService;
    }

    
    public List<Notification> getAllNotifications(){
        
        Integer userId = userService.findCurrentUser().getId(); 

        List<InvitationMatch> invitations = invitationService.getPendingInvitations(userId); 
        List<FriendRequest> friendsNotifications = friendRequestService.findFriendRequestsForUserId(userId); 

        List<Notification> notifications = new ArrayList<>(
            invitations.stream()
                .map(i -> new Notification(
                    i.getId(),
                    i.getSender(),
                    i.isSpectator()
                        ? NotificationType.MATCH_INVITATION_AS_SPECTATOR
                        : NotificationType.MATCH_INVITATION_AS_PLAYER))
                .toList()
        );

        notifications.addAll(
            friendsNotifications.stream()
                .map(f -> new Notification(
                    f.getId(),
                    f.getSender(),
                    NotificationType.FRIEND_REQUEST))
                .toList()
        );



        return notifications; 

    }
    
}
