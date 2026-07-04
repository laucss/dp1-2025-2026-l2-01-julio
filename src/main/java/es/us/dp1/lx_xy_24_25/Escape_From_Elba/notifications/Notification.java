package es.us.dp1.lx_xy_24_25.Escape_From_Elba.notifications;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification {

    private Integer id; // id de la invitacion o solictud de amistad

    private User sender;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationType type; 


    public Notification(Integer id, User sender, NotificationType type){
        this.id = id;
        this.sender = sender; 
        this.type = type;
    }
    
}
