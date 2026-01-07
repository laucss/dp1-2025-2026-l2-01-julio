package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;
//cambio para merge en FSS8078
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.MiniUserDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MiniUserDTO extends BaseEntity {
    @NotNull
    @Column(unique = true)
    String username;

    @Column(name = "avatar")
    private String avatar;

    // Estado del usuario (ONLINE, OFFLINE, PLAYING)
    private String status;

    // Nuevo: información de la partida activa (puede ser null)
    private MiniMatchDTO match;

    public MiniUserDTO(User user, MiniMatchDTO match) {
        this.setId(user.getId());
        this.setAvatar(user.getAvatar());
        this.setUsername(user.getUsername());
        this.setStatus(user.getStatus() != null ? user.getStatus().name() : "OFFLINE");
        this.match = match;
    }

    // Constructor de compatibilidad para usos antiguos
    public MiniUserDTO(User user) {
        this(user, null);
    }

    public MiniUserDTO() {
    }

    public MiniMatchDTO getMatch() {
        return match;
    }

    public void setMatch(MiniMatchDTO match) {
        this.match = match;
    }
}
