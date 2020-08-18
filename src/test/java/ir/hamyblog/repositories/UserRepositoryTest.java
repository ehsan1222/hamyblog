package ir.hamyblog.repositories;

import ir.hamyblog.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername_shouldReturnNullInUsernameNotExist() {
        final Optional<User> optionalUser = userRepository.findByUsername("ehsan");
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    public void findByUsername_shouldReturnSavedUser() {
        final String username = "ehsan1222";
        final String password = "password";
        final String fullName = "ehsan maddahi";
        User expectedUser = new User(username, password, fullName);
        testEntityManager.persist(expectedUser);

        final Optional<User> optionalUser = userRepository.findByUsername(username);

        assertFalse(optionalUser.isEmpty());
        assertEquals(optionalUser.get(), expectedUser);
    }

}