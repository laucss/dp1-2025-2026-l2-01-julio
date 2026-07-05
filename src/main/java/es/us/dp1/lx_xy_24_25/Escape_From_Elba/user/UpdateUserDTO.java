package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

    private String username;

    @Email
    private String email;

    @Min(1)
    @Max(100)
    private Integer age;

    private String password;

    private String avatar;
}