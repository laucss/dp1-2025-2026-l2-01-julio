package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendsInvitationDTO {

    @NotNull
    private User friend; 

    // este boolean es para que si la partida es privada, se checkee si es amigo de todos en la partida
    private boolean isFriendOfAllPlayers; 

    public FriendsInvitationDTO(User user, boolean isFriendOfAllPlayers){
        this.friend = user; 
        this.isFriendOfAllPlayers = isFriendOfAllPlayers; 
    }

    public FriendsInvitationDTO(User user){
        this.friend = user; 
    }
    
}
