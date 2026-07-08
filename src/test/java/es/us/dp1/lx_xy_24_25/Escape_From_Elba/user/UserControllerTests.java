package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration.SecurityConfiguration;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UpdateUserDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserStatus;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

/**
 * Test class for the {@link VetController}
 */
@Epic("Users & Admin Module")
@Feature("Users Management")
@Owner("DP1-tutors")
@WebMvcTest(controllers = UserRestController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class UserControllerTests {

	private static final int TEST_USER_ID = 1;
	private static final int TEST_AUTH_ID = 1;
	private static final String BASE_URL = "/api/v1/users";

	@SuppressWarnings("unused")
	@Autowired
	private UserRestController userController;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthoritiesService authService;

	@MockBean
	private es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration.jwt.JwtUtils jwtUtils;

	@MockBean
	private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

	@MockBean
	private org.springframework.boot.web.client.RestTemplateBuilder restTemplateBuilder;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	private Authorities auth;
	private User user, logged;

	@BeforeEach
	void setup() {
		auth = new Authorities();
		auth.setId(TEST_AUTH_ID);
		auth.setAuthority("VET");

		user = new User();
		user.setId(1);
		user.setUsername("user");
		user.setPassword("password");
		user.setAuthority(auth);

		when(this.userService.findCurrentUser()).thenReturn(getUserFromDetails(
				(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
	}

	private User getUserFromDetails(UserDetails details) {
		logged = new User();
		logged.setUsername(details.getUsername());
		logged.setPassword(details.getPassword());
		Authorities aux = new Authorities();
		for (GrantedAuthority auth : details.getAuthorities()) {
			aux.setAuthority(auth.getAuthority());
		}
		logged.setAuthority(aux);
		return logged;
	}

	@Test
	@WithMockUser("admin")
	void shouldFindAll() throws Exception {
		User sara = new User();
		sara.setId(2);
		sara.setUsername("Sara");

		User juan = new User();
		juan.setId(3);
		juan.setUsername("Juan");

		when(this.userService.findAll()).thenReturn(List.of(user, sara, juan));

		mockMvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(3))
				.andExpect(jsonPath("$[?(@.id == 1)].username").value("user"))
				.andExpect(jsonPath("$[?(@.id == 2)].username").value("Sara"))
				.andExpect(jsonPath("$[?(@.id == 3)].username").value("Juan"));
	}

	@Test
	@WithMockUser("admin")
	void shouldFindAllWithAuthority() throws Exception {
		Authorities aux = new Authorities();
		aux.setId(2);
		aux.setAuthority("AUX");

		User sara = new User();
		sara.setId(2);
		sara.setUsername("Sara");
		sara.setAuthority(aux);

		User juan = new User();
		juan.setId(3);
		juan.setUsername("Juan");
		juan.setAuthority(auth);

		when(this.userService.findAllByAuthority(auth.getAuthority())).thenReturn(List.of(user, juan));

		mockMvc.perform(get(BASE_URL).param("auth", "VET")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2)).andExpect(jsonPath("$[?(@.id == 1)].username").value("user"))
				.andExpect(jsonPath("$[?(@.id == 3)].username").value("Juan"));
	}

	@Test
	@WithMockUser("admin")
	void shouldFindAllAuths() throws Exception {
		Authorities aux = new Authorities();
		aux.setId(2);
		aux.setAuthority("AUX");

		when(this.authService.findAll()).thenReturn(List.of(auth, aux));

		mockMvc.perform(get(BASE_URL + "/authorities")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2)).andExpect(jsonPath("$[?(@.id == 1)].authority").value("VET"))
				.andExpect(jsonPath("$[?(@.id == 2)].authority").value("AUX"));
	}

	@Test
	@WithMockUser("admin")
	void shouldReturnUser() throws Exception {
		when(this.userService.findUser(TEST_USER_ID)).thenReturn(user);
		mockMvc.perform(get(BASE_URL + "/{id}", TEST_USER_ID)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(TEST_USER_ID))
				.andExpect(jsonPath("$.username").value(user.getUsername()))
				.andExpect(jsonPath("$.authority.authority").value(user.getAuthority().getAuthority()));
	}

	@Test
	@WithMockUser("admin")
	void shouldReturnNotFoundUser() throws Exception {
		when(this.userService.findUser(TEST_USER_ID)).thenThrow(ResourceNotFoundException.class);
		mockMvc.perform(get(BASE_URL + "/{id}", TEST_USER_ID)).andExpect(status().isNotFound());
	}


	@Test
	@WithMockUser("admin")
	void shouldUpdateUser() throws Exception {
		UpdateUserDTO updateDTO = new UpdateUserDTO();
		updateDTO.setUsername("UPDATED");
		updateDTO.setPassword("CHANGED");

		User updatedUser = new User();
		updatedUser.setId(TEST_USER_ID);
		updatedUser.setUsername("UPDATED");
		updatedUser.setPassword("CHANGED");
		updatedUser.setAvatar("avatar.jpg");
		updatedUser.setEmail("updated@test.com");
		updatedUser.setAge(30);
		updatedUser.setAuthority(auth);

		when(this.userService.updateUser(eq(TEST_USER_ID), any(UpdateUserDTO.class))).thenReturn(updatedUser);
		when(this.jwtUtils.generateTokenFromUsername(eq("UPDATED"), any(Authorities.class))).thenReturn("newToken");

		mockMvc.perform(put(BASE_URL + "/{userId}", TEST_USER_ID)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDTO)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("UPDATED"))
			.andExpect(jsonPath("$.id").value(TEST_USER_ID));
	}

	@Test
	@WithMockUser("admin")
	void shouldReturnNotFoundUpdateUser() throws Exception {
		UpdateUserDTO updateDTO = new UpdateUserDTO();
		updateDTO.setUsername("UPDATED");
		updateDTO.setPassword("UPDATED");

		when(this.userService.updateUser(eq(TEST_USER_ID), any(UpdateUserDTO.class))).thenThrow(ResourceNotFoundException.class);

		mockMvc.perform(put(BASE_URL + "/{userId}", TEST_USER_ID).with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDTO))).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser("admin")
	void shouldDeleteOtherUser() throws Exception {
		logged.setId(2);

		when(this.userService.findUser(TEST_USER_ID)).thenReturn(user);
		doNothing().when(this.userService).deleteUser(TEST_USER_ID);

		mockMvc.perform(delete(BASE_URL + "/{id}", TEST_USER_ID).with(csrf())).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("User deleted!"));
	}

	@Test
	@WithMockUser("admin")
	void shouldNotDeleteLoggedUser() throws Exception {
		logged.setId(TEST_USER_ID);

		when(this.userService.findUser(TEST_USER_ID)).thenReturn(user);
		doNothing().when(this.userService).deleteUser(TEST_USER_ID);

		mockMvc.perform(delete(BASE_URL + "/{id}", TEST_USER_ID).with(csrf())).andExpect(status().isForbidden())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof AccessDeniedException));
	}

	@Test
	@WithMockUser("admin")
	void shouldSetUserOnline() throws Exception {
		user.setStatus(UserStatus.OFFLINE);
		logged.setStatus(UserStatus.OFFLINE);

		when(this.userService.findCurrentUser()).thenReturn(logged);
		when(this.userService.saveUser(any(User.class))).thenReturn(logged);

		mockMvc.perform(put(BASE_URL + "/status/online").with(csrf()))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser("admin")
	void shouldSetUserOffline() throws Exception {
		user.setStatus(UserStatus.ONLINE);
		logged.setStatus(UserStatus.ONLINE);

		when(this.userService.findCurrentUser()).thenReturn(logged);
		when(this.userService.saveUser(any(User.class))).thenReturn(logged);

		mockMvc.perform(put(BASE_URL + "/status/offline").with(csrf()))
				.andExpect(status().isOk());
	}

}
