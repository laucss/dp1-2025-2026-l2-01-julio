package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class NotificationWebController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public void notifyNewNotification(Integer receiverId, NotificationType type) {
        messagingTemplate.convertAndSend(
            "/topic/user." + receiverId + ".notifications",
            type
        );
    }

    
}
