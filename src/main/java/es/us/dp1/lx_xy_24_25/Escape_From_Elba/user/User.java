package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "appusers")
public class User extends BaseEntity {

	@Column(unique = true)
	String username;

	String password;

	@Column(unique = true)
	@Email
	String email;

	@Min(1)
	@Max(100)
	Integer age;
	
	String avatar;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "authority")
	Authorities authority;

	public Boolean hasAuthority(String auth) {
		return authority.getAuthority().equals(auth);
	}

	public Boolean hasAnyAuthority(String... authorities) {
		Boolean cond = false;
		for (String auth : authorities) {
			if (auth.equals(authority.getAuthority()))
				cond = true;
		}
		return cond;
	}

	public User toUser(){
		if (!this.authority.authority.equals("USER")){
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

	public Player toPlayer(){
		if (!this.authority.authority.equals("USER")){
			return null;
		}
		Player player = new Player();
		player.setAuthority(authority);
		player.setAvatar(avatar);
		player.setId(id);
		player.setPassword(password);
		player.setUsername(username);
		return player;
	}
}
