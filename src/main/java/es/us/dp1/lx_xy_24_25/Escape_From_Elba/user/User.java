package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;

import java.util.List;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "appusers")
public class User extends BaseEntity {

    @Column(unique = true)
    String username;

    String password;

    @Column(unique = true)
    @Email
    String email;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players;

    @Min(1)
    @Max(100)
    Integer age;
    
    String avatar;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "authority")
    Authorities authority;

    public Boolean hasAuthority(String auth) {
        return authority != null && auth != null && auth.equals(authority.getAuthority());
    }

    public Boolean hasAnyAuthority(String... authorities) {
        if (authority == null || authorities == null) return false;
        for (String auth : authorities) {
            if (auth != null && auth.equals(authority.getAuthority()))
                return true;
        }
        return false;
    }

    public User toUser(){
        if (authority == null || ! "USER".equals(authority.getAuthority())){
            return null;
        }
        User user = new User();
        user.setAuthority(authority);
        user.setAvatar(avatar);
        user.setId(id);
        user.setPassword(password);
        user.setUsername(username);
        return user;
    }
    /* 
    public Player toPlayer(){
        if (authority == null || ! "PLAYER".equals(authority.getAuthority())){
            return null;
        }
        Player player = new Player();
        player.setAuthority(authority);
        player.setAvatar(avatar);
        player.setId(id);
        player.setPassword(password);
        player.setUsername(username);
        return player;
    } */
}
