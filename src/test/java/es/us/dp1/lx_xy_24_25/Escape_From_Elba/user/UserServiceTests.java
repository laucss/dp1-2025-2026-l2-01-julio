package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

@Epic("Users & Admin Module")
@Feature("Users Management")
@Owner("DP1-tutors")
@SpringBootTest
@AutoConfigureTestDatabase
class UserServiceTests {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authService;


	@Test
	@WithMockUser(username = "player1", password = "0wn3r")
	void shouldFindCurrentUser() {
		User user = this.userService.findCurrentUser();
		assertEquals("player1", user.getUsername());
	}

	@Test
	@WithMockUser(username = "prueba")
	void shouldNotFindCorrectCurrentUser() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findCurrentUser());
	}

	@Test
	void shouldNotFindAuthenticated() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findCurrentUser());
	}

	@Test
	void shouldFindAllUsers() {
		Collection<User> users = (Collection<User>) this.userService.findAll();
		assertTrue(users.size() > 0);
	}

	@Test
	void shouldFindUsersByAuthority() {
		Collection<User> players = (Collection<User>) this.userService.findAllByAuthority("PLAYER");
		assertTrue(players.size() > 0);

		Collection<User> admins = (Collection<User>) this.userService.findAllByAuthority("ADMIN");
		assertTrue(admins.size() > 0);
	}

	@Test
	void shouldNotFindUserByIncorrectUsername() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findUser("usernotexists"));
	}

	@Test
	void shouldFindSingleUser() {
		User user = this.userService.findUser(4);
		assertEquals("player1", user.getUsername());
	}

	@Test
	void shouldNotFindSingleUserWithBadID() {
		assertThrows(ResourceNotFoundException.class, () -> this.userService.findUser(100));
	}

	@Test
	void shouldExistUser() {
		assertEquals(true, this.userService.existsUser("player1"));
	}

	@Test
	void shouldNotExistUser() {
		assertEquals(false, this.userService.existsUser("player10000"));
	}

	@Test
	void shouldExistEmail() {
		assertEquals(true, this.userService.existsEmail("player1@example.com"));
	}

	@Test
	void shouldNotExistEmail() {
		assertEquals(false, this.userService.existsEmail("player10000@example.com"));
	}

	@Test
	@Transactional
	void shouldUpdateUser() {
		int idToUpdate = 4;
		String newName = "UpdatedName";
		String newEmail = "newemail@example.com";
		
		User originalUser = this.userService.findUser(idToUpdate);
		
		UpdateUserDTO updateDTO = new UpdateUserDTO();
		updateDTO.setUsername(newName);
		updateDTO.setEmail(newEmail);
		updateDTO.setAge(25);
		updateDTO.setPassword("newPassword123");
		updateDTO.setAvatar("newAvatar.jpg");
		
		User updatedUser = userService.updateUser(idToUpdate, updateDTO);
		assertEquals(newName, updatedUser.getUsername());
		assertEquals(newEmail, updatedUser.getEmail());
		assertEquals(Integer.valueOf(25), updatedUser.getAge());
		assertEquals("newAvatar.jpg", updatedUser.getAvatar());
		
		// Verify it persisted
		User verifyUser = userService.findUser(idToUpdate);
		assertEquals(newName, verifyUser.getUsername());
		assertEquals(newEmail, verifyUser.getEmail());
	}

	@Test
	void shouldNotUpdateUserWithInvalidId() {
		UpdateUserDTO updateDTO = new UpdateUserDTO();
		updateDTO.setUsername("UpdatedName");
		
		assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(99999, updateDTO));
	}

	@Test
	@Transactional
	void shouldSaveUser() {
		int initialCount = ((Collection<User>) userService.findAll()).size();
		
		User newUser = new User();
		newUser.setUsername("brandNewUser");
		newUser.setPassword("password123");
		newUser.setAuthority(authService.findByAuthority("PLAYER"));
		
		userService.saveUser(newUser);
		assertNotNull(newUser.getId());
		
		int finalCount = ((Collection<User>) userService.findAll()).size();
		assertEquals(initialCount + 1, finalCount);
	}

	@Test
	@Transactional
	void shouldDeleteUser() {
		// Create a user to delete
		User userToDelete = new User();
		userToDelete.setUsername("userToDelete");
		userToDelete.setPassword("password123");
		userToDelete.setAuthority(authService.findByAuthority("PLAYER"));
		userService.saveUser(userToDelete);
		
		Integer userId = userToDelete.getId();
		assertNotNull(userId);
		
		// Verify it exists
		User found = userService.findUser(userId);
		assertEquals("userToDelete", found.getUsername());
		
		// Delete it
		userService.deleteUser(userId);
		
		// Verify it's deleted
		assertThrows(ResourceNotFoundException.class, () -> userService.findUser(userId));
	}

	@Test
	@Transactional
	void shouldInsertUser() {
		int count = ((Collection<User>) this.userService.findAll()).size();

		User user = new User();
		user.setUsername("Sam");
		user.setPassword("password");
		user.setAuthority(authService.findByAuthority("ADMIN"));

		this.userService.saveUser(user);
		assertNotEquals(0, user.getId().longValue());
		assertNotNull(user.getId());

		int finalCount = ((Collection<User>) this.userService.findAll()).size();
		assertEquals(count + 1, finalCount);
	}


//	@Test
//	@Transactional
//	void shouldDeleteUserWithOwner() {
//		Integer firstCount = ((Collection<User>) userService.findAll()).size();
//		User user = new User();
//		user.setUsername("Sam");
//		user.setPassword("password");
//		Authorities auth = authService.findByAuthority("OWNER");
//		user.setAuthority(auth);
//		Owner owner = new Owner();
//		owner.setAddress("Test");
//		owner.setFirstName("Test");
//		owner.setLastName("Test");
//		owner.setPlan(PricingPlan.BASIC);
//		owner.setTelephone("999999999");
//		owner.setUser(user);
//		owner.setCity("Test");
//		this.ownerService.saveOwner(owner);
//
//		Integer secondCount = ((Collection<User>) userService.findAll()).size();
//		assertEquals(firstCount + 1, secondCount);
//		userService.deleteUser(user.getId());
//		Integer lastCount = ((Collection<User>) userService.findAll()).size();
//		assertEquals(firstCount, lastCount);
//	}



//	@Test
//	@Transactional
//	void shouldDeleteUserWithVet() {
//		Integer firstCount = ((Collection<User>) userService.findAll()).size();
//		User user = new User();
//		user.setUsername("Sam");
//		user.setPassword("password");
//		Authorities auth = authService.findByAuthority("VET");
//		user.setAuthority(auth);
//		userService.saveUser(user);
//		Vet vet = new Vet();
//		vet.setFirstName("Test");
//		vet.setLastName("Test");
//		vet.setUser(user);
//		vet.setCity("Test");
//		this.vetService.saveVet(vet);
//
//		Integer secondCount = ((Collection<User>) userService.findAll()).size();
//		assertEquals(firstCount + 1, secondCount);
//		userService.deleteUser(user.getId());
//		Integer lastCount = ((Collection<User>) userService.findAll()).size();
//		assertEquals(firstCount, lastCount);
//	}

}
