package es.us.dp1.lx_xy_24_25.Escape_From_Elba.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

@Epic("Users & Admin Module")
@Feature("Authorization")
@Owner("DP1-tutors")
@SpringBootTest
@Transactional
public class AuthoritiesRepositoryTests {

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Test
    public void shouldSaveAndFindByIdAuthority() {
        Authorities auth = new Authorities();
        auth.setAuthority("TESTER");
        authoritiesRepository.save(auth);

        Optional<Authorities> found = authoritiesRepository.findById(auth.getId());
        assertTrue(found.isPresent());
        assertEquals("TESTER", found.get().getAuthority());
    }

    @Test
    public void shouldFindByNameAuthority() {
        Authorities auth = new Authorities();
        auth.setAuthority("MODERATOR");
        authoritiesRepository.save(auth);

        Optional<Authorities> found = authoritiesRepository.findByName("MODERATOR");
        assertTrue(found.isPresent());
        assertEquals("MODERATOR", found.get().getAuthority());
    }

    @Test
    public void shouldFindByNamePartialMatch() {
        Authorities auth = new Authorities();
        auth.setAuthority("DEVELOPER");
        authoritiesRepository.save(auth);

        Optional<Authorities> found = authoritiesRepository.findByName("DEVELOP");
        assertTrue(found.isPresent());
        assertEquals("DEVELOPER", found.get().getAuthority());
    }

    @Test
    public void shouldReturnEmptyWhenAuthoritiesByNameNotFound() {
        Optional<Authorities> found = authoritiesRepository.findByName("NONEXISTENT_AUTH");
        assertFalse(found.isPresent());
    }

    @Test
    public void shouldFindAllAuthorities() {
        Authorities auth1 = new Authorities();
        auth1.setAuthority("AUTHORITY1");
        authoritiesRepository.save(auth1);

        Authorities auth2 = new Authorities();
        auth2.setAuthority("AUTHORITY2");
        authoritiesRepository.save(auth2);

        List<Authorities> authorities = (List<Authorities>) authoritiesRepository.findAll();
        assertTrue(authorities.size() >= 2);
    }

    @Test
    public void shouldReturnEmptyWhenByIdNotFound() {
        Optional<Authorities> found = authoritiesRepository.findById(99999);
        assertFalse(found.isPresent());
    }

    @Test
    @Transactional
    public void shouldUpdateAuthority() {
        Authorities auth = new Authorities();
        auth.setAuthority("UPDATABLE");
        authoritiesRepository.save(auth);

        auth.setAuthority("UPDATED");
        Authorities updated = authoritiesRepository.save(auth);

        Optional<Authorities> found = authoritiesRepository.findById(updated.getId());
        assertTrue(found.isPresent());
        assertEquals("UPDATED", found.get().getAuthority());
    }

    @Test
    @Transactional
    public void shouldDeleteAuthority() {
        Authorities auth = new Authorities();
        auth.setAuthority("TO_DELETE");
        authoritiesRepository.save(auth);
        Integer id = auth.getId();

        authoritiesRepository.deleteById(id);

        Optional<Authorities> found = authoritiesRepository.findById(id);
        assertFalse(found.isPresent());
    }

}
