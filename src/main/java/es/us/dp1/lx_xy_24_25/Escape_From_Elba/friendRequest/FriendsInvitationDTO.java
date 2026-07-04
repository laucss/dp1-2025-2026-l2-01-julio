package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations.InvitationStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    // booleano que indica si el jugador al que quiere invitar está en un lobby o jugando
    private boolean isInLobby; 

    private boolean pendingInvitation;

    public FriendsInvitationDTO(User user, boolean isFriendOfAllPlayers, boolean isInLobby, boolean pendingInvitation){
        this.friend = user; 
        this.isFriendOfAllPlayers = isFriendOfAllPlayers; 
        this.isInLobby = isInLobby; 
        this.pendingInvitation = pendingInvitation;
    }

    public FriendsInvitationDTO(User user, boolean isInLobby, boolean pendingInvitation){
        this.friend = user; 
        this.isInLobby = isInLobby; 
        this.pendingInvitation = pendingInvitation;
    }
    
}
