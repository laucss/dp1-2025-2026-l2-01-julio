package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.MiniUserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MiniRequestDTO extends BaseEntity{
    
    @NotNull
    private MiniUserDTO sender;

    @NotNull
    private MiniUserDTO receiver;

    @NotNull
    @Column(name = "requestStatus")
    @Enumerated(EnumType.STRING)
    private StatusType status;

    public MiniRequestDTO(FriendRequest fr) {
        this.setId(fr.getId());
        this.setSender(new MiniUserDTO(fr.getSender()));
        this.setReceiver(new MiniUserDTO(fr.getReceiver()));
        this.setStatus(fr.getStatus());
    }
}