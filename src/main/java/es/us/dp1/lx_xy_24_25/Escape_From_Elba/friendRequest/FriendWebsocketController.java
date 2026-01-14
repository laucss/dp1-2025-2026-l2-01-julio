package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class FriendWebsocketController {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Notifica a un usuario sobre una nueva solicitud de amistad recibida
     * @param receiverId ID del usuario que recibe la solicitud
     * @param friendRequest La solicitud de amistad
     */
    public void notifyNewFriendRequest(Integer receiverId, FriendRequest friendRequest) {
        messagingTemplate.convertAndSend(
            "/topic/user." + receiverId + ".friendRequests",
            friendRequest
        );
    }

    /**
     * Notifica a un usuario sobre un cambio en sus solicitudes de amistad (aceptada/rechazada)
     * @param userId ID del usuario afectado
     * @param friendRequest La solicitud de amistad actualizada
     */
    public void notifyFriendRequestUpdate(Integer userId, FriendRequest friendRequest) {
        messagingTemplate.convertAndSend(
            "/topic/user." + userId + ".friendRequests.update",
            friendRequest
        );
    }

    /**
     * Notifica al remitente que su solicitud fue aceptada
     * @param senderId ID del usuario que envió la solicitud
     * @param friendRequest La solicitud de amistad aceptada
     */
    public void notifyRequestAccepted(Integer senderId, FriendRequest friendRequest) {
        messagingTemplate.convertAndSend(
            "/topic/user." + senderId + ".friendRequests.accepted",
            friendRequest
        );
    }

    /**
     * Notifica al remitente que su solicitud fue rechazada
     * @param senderId ID del usuario que envió la solicitud
     * @param friendRequest La solicitud de amistad rechazada
     */
    public void notifyRequestRejected(Integer senderId, FriendRequest friendRequest) {
        messagingTemplate.convertAndSend(
            "/topic/user." + senderId + ".friendRequests.rejected",
            friendRequest
        );
    }

    /**
     * Notifica a ambos usuarios que una amistad fue eliminada
     * @param user1Id ID del primer usuario
     * @param user2Id ID del segundo usuario
     * @param friendRequestId ID de la solicitud de amistad eliminada
     */
    public void notifyFriendRequestDeleted(Integer user1Id, Integer user2Id, Integer friendRequestId) {
        messagingTemplate.convertAndSend(
            "/topic/user." + user1Id + ".friendRequests.deleted",
            friendRequestId
        );
        messagingTemplate.convertAndSend(
            "/topic/user." + user2Id + ".friendRequests.deleted",
            friendRequestId
        );
    }
}
