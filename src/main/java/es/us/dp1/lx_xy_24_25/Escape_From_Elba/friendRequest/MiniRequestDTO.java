package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.MiniMatchDTO;
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

    public MiniRequestDTO(FriendRequest fr, PlayerService playerService) {
        this.setId(fr.getId());
        // Buscar partida activa del sender
        MiniMatchDTO senderMatch = playerService.findByUserId(fr.getSender().getId()).stream()
            .filter(p -> p.getMatch() != null && p.getMatch().getStatus() != null && p.getMatch().getStatus().name().equals("PLAYING"))
            .map(p -> new MiniMatchDTO(p.getMatch()))
            .findFirst().orElse(null);
        this.setSender(new MiniUserDTO(fr.getSender(), senderMatch));

        // Buscar partida activa del receiver
        MiniMatchDTO receiverMatch = playerService.findByUserId(fr.getReceiver().getId()).stream()
            .filter(p -> p.getMatch() != null && p.getMatch().getStatus() != null && p.getMatch().getStatus().name().equals("PLAYING"))
            .map(p -> new MiniMatchDTO(p.getMatch()))
            .findFirst().orElse(null);
        this.setReceiver(new MiniUserDTO(fr.getReceiver(), receiverMatch));

        this.setStatus(fr.getStatus());
    }

    // Constructor de compatibilidad para usos antiguos
    public MiniRequestDTO(FriendRequest fr) {
        this(fr, null);
    }
}