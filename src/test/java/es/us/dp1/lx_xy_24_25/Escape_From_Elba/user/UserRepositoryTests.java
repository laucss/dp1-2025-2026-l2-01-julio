package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesRepository authorityRepository;

    @Test
    public void saveAndFindByIdReturnsUser() {
        Authorities auth = new Authorities();
        auth.setAuthority("USER");
        authorityRepository.save(auth);

        User user = new User();
        user.setUsername("testUser");
        user.setAuthority(auth);
        userRepository.save(user);

        Optional<User> found = userRepository.findById(user.getId());
        assertTrue(found.isPresent());
        assertEquals("testUser", found.get().getUsername());
    }

    @Test
    public void findByUsernameReturnsUser() {
        Authorities auth = new Authorities();
        auth.setAuthority("USER");
        authorityRepository.save(auth);

        User user = new User();
        user.setUsername("uniqueUser");
        user.setAuthority(auth);
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("uniqueUser");
        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"existsUser1", "existsUser2"})
    public void existsByUsernameReturnsTrue(String username) {
        Authorities auth = new Authorities();
        auth.setAuthority("USER");
        authorityRepository.save(auth);

        User user = new User();
        user.setUsername(username);
        user.setAuthority(auth);
        userRepository.save(user);

        assertTrue(userRepository.existsByUsername(username));
    }

    @Test
    public void existsByEmailReturnsTrue() {
        Authorities auth = new Authorities();
        auth.setAuthority("USER");
        authorityRepository.save(auth);

        User user = new User();
        user.setUsername("emailUser");
        user.setEmail("test@example.com");
        user.setAuthority(auth);
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("test@example.com"));
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    public void findAllByAuthorityReturnsOnlyMatchingUsers() {
        Authorities authUser = new Authorities();
        authUser.setAuthority("USER");
        authorityRepository.save(authUser);

        Authorities authAdmin = new Authorities();
        authAdmin.setAuthority("ADMIN");
        authorityRepository.save(authAdmin);

        User user1 = new User();
        user1.setUsername("user1");
        user1.setAuthority(authUser);
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setAuthority(authAdmin);
        userRepository.save(user2);

        Iterable<User> users = userRepository.findAllByAuthority("USER");
        List<User> userList = (List<User>) users;

        assertEquals(1, userList.size());
        assertEquals("user1", userList.get(0).getUsername());
    }

    /* 
    @Test
    public void findAllReturnsAllUsers() {
        Authorities auth = new Authorities();
        auth.setAuthority("USER");
        authorityRepository.save(auth);

        User user1 = new User();
        user1.setUsername("allUser1");
        user1.setAuthority(auth);
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("allUser2");
        user2.setAuthority(auth);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2).extracting(User::getUsername)
                        .containsExactlyInAnyOrder("allUser1", "allUser2");
    }*/

}
