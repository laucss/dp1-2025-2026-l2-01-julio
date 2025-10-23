package es.us.dp1.lx_xy_24_25.Escape_From_Elba.auth.payload.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	
	// User
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;

	@NotBlank
	private String authority;

}
