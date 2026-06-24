package es.us.dp1.lx_xy_24_25.Escape_From_Elba.auth;


import static org.mockito.Mockito.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import es.us.dp1.lx_xy_24_25.Escape_From_Elba.auth.payload.request.SignupRequest;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.Authorities;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.AuthoritiesService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {


    private AuthService authService;


    @Mock
    private UserService userService;


    @Mock
    private AuthoritiesService authoritiesService;


    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setup() {
        authService = new AuthService(passwordEncoder, authoritiesService, userService);
    }


    @Test
    public void createUserWithAdminRoleSavesUser() {
        SignupRequest request = new SignupRequest();
        request.setUsername("adminUser");
        request.setPassword("password123");
        request.setEmail("admin@example.com");
        request.setAge(30);
        request.setAuthority("admin");


        Authorities adminRole = new Authorities();
        adminRole.setAuthority("ADMIN");


        when(authoritiesService.findByAuthority("ADMIN")).thenReturn(adminRole);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");


        authService.createUser(request);


        verify(userService).saveUser(argThat(user ->
            user.getUsername().equals("adminUser") &&
            user.getPassword().equals("encodedPassword") &&
            user.getEmail().equals("admin@example.com") &&
            user.getAge() == 30 &&
            user.getAuthority().getAuthority().equals("ADMIN")
        ));
    }


    @Test
    public void createUserWithDefaultRoleSavesUserAsPlayer() {
        SignupRequest request = new SignupRequest();
        request.setUsername("playerUser");
        request.setPassword("pass123");
        request.setEmail("player@example.com");
        request.setAge(25);
        request.setAuthority("anythingElse");


        Authorities playerRole = new Authorities();
        playerRole.setAuthority("PLAYER");


        when(authoritiesService.findByAuthority("PLAYER")).thenReturn(playerRole);
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");


        authService.createUser(request);


        verify(userService).saveUser(argThat(user ->
            user.getUsername().equals("playerUser") &&
            user.getPassword().equals("encodedPass") &&
            user.getEmail().equals("player@example.com") &&
            user.getAge() == 25 &&
            user.getAuthority().getAuthority().equals("PLAYER")
        ));
    }
}
