package es.us.dp1.lx_xy_24_25.Escape_From_Elba.auth.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private Integer id;
	private String username;
	private String avatar;
	private List<String> roles;


	public JwtResponse(String accessToken, Integer id, String username, String avatar, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.roles = roles;
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "JwtResponse [token=" + token + ", type=" + type + ", id=" + id + ", username=" + username + ", avatar=" + avatar
				+ ", roles=" + roles + "]";
	}

}
