package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService; 

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService; 

    }

    @GetMapping()
    @Operation(summary = "Get user notifications", description = "Get all friend request and match invitation of current user")
    public ResponseEntity<List<Notification>> getNotfications() {
        return new ResponseEntity<>(notificationService.getAllNotifications(), HttpStatus.OK);
    }


    
}
