package es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteRequest {

    @NotNull
    private Integer senderId;

    @NotNull
    private Integer receiverId;

    @NotNull
    private Integer matchId;

    private boolean spectator;
    
}
