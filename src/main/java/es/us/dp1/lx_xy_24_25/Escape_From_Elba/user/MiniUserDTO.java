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

        // Nuevo: estado del usuario (ONLINE, OFFLINE, PLAYING)
        private String status;

    public MiniUserDTO(User user){
        this.setId(user.getId());
        this.setAvatar(user.getAvatar());
        this.setUsername(user.getUsername());
        this.setStatus(user.getStatus() != null ? user.getStatus().name() : "OFFLINE");
    }

    public MiniUserDTO(){

    }
}
