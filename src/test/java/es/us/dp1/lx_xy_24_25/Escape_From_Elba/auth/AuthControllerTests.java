package es.us.dp1.lx_xy_24_25.Escape_From_Elba.auth;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.ObjectMapper;


import es.us.dp1.lx_xy_24_25.Escape_From_Elba.auth.payload.request.LoginRequest;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.auth.payload.request.SignupRequest;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration.jwt.JwtUtils;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration.services.UserDetailsImpl;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {


    private static final String BASE_URL = "/api/v1/auth";


    @Autowired
    private MockMvc mvc;


    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private AuthenticationManager authenticationManager;


    @MockBean
    private JwtUtils jwtUtils;


    @MockBean
    private UserService userService;


    @SpyBean
    private AuthService authService;


   


    @Test
    public void authenticateUserBadCredentialsTest() throws Exception {


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("wrongPassword");


        reset(authenticationManager);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("user", "wrongPassword")))
            .thenThrow(new BadCredentialsException("Bad credentials"));


        mvc.perform(post(BASE_URL + "/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest());


        verify(authenticationManager, times(1))
            .authenticate(new UsernamePasswordAuthenticationToken("user", "wrongPassword"));
    }


   


    @Test
    public void validateTokenValidTest() throws Exception {


        reset(jwtUtils);
        when(jwtUtils.validateJwtToken("validToken")).thenReturn(true);


        mvc.perform(get(BASE_URL + "/validate")
                .param("token", "validToken"))
            .andExpect(status().isOk());


        verify(jwtUtils, times(1)).validateJwtToken("validToken");
    }


    @Test
    public void validateTokenInvalidTest() throws Exception {


        reset(jwtUtils);
        when(jwtUtils.validateJwtToken("invalidToken")).thenReturn(false);


        mvc.perform(get(BASE_URL + "/validate")
                .param("token", "invalidToken"))
            .andExpect(status().isOk());


        verify(jwtUtils, times(1)).validateJwtToken("invalidToken");
    }


    @Test
    public void authenticateUserSuccessTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("password");


        es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User testUser =
                new es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User();
        testUser.setId(1);
        testUser.setUsername("user");
        testUser.setEmail("email@example.com");
        testUser.setAvatar("avatar");
        testUser.setPassword("password");

       
        es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.Authorities authority =
                new es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.Authorities();
        authority.setAuthority("ROLE_USER");

        testUser.setAuthority(authority);


        UserDetailsImpl userDetails = UserDetailsImpl.build(testUser);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        when(jwtUtils.generateJwtToken(any())).thenReturn("fake-jwt");

        mvc.perform(post(BASE_URL + "/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk());
    }



    @Test
    public void registerUserUsernameExistsTest() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("existingUser");
        signupRequest.setEmail("new@example.com");
        signupRequest.setPassword("password");

        when(userService.existsUser("existingUser")).thenReturn(true);

        mvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registerUserEmailExistsTest() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("existing@example.com");
        signupRequest.setPassword("password");

        when(userService.existsUser("newUser")).thenReturn(false);
        when(userService.existsEmail("existing@example.com")).thenReturn(true);

        mvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
            .andExpect(status().isBadRequest());
    }


}
