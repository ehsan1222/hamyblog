package ir.hamyblog.web;

import ir.hamyblog.entities.User;
import ir.hamyblog.model.UserRegisterIn;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private UserRegisterIn user;

    @Before
    public void before() {
        user = new UserRegisterIn("omid0", "password", "omid");
    }

    @Test
    public void register_shouldReturn2xxStatusCode() {
        final ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        final User userBody = response.getBody();
        assertNotNull(userBody);

        final ResponseEntity<User> userResponse = testRestTemplate.getForEntity("/users/omid0", User.class);
        assertEquals(userResponse.getStatusCode(), HttpStatus.OK);
        final User getBodyUserResponse = userResponse.getBody();
        assertNotNull(getBodyUserResponse);
        assertEquals(getBodyUserResponse.getFullName(), user.getFullName());
        assertEquals(getBodyUserResponse.getUsername(), user.getUsername());
        assertEquals(getBodyUserResponse.getPassword(), user.getPassword());
    }

    @Test
    public void register_shouldReturn4xxStatusCode() {
        final ResponseEntity<User> successResponse = testRestTemplate.postForEntity("/users", user, User.class);
        UserRegisterIn user2 = new UserRegisterIn("omid0", "sdapassword", "omid norozi");
        final ResponseEntity<User> conflictResponse = testRestTemplate.postForEntity("/users", user2, User.class);

        assertEquals(successResponse.getStatusCode(), HttpStatus.CREATED);
        assertEquals(conflictResponse.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    public void getUserByUsername_shouldReturn4xxStatusCode() {
        final ResponseEntity<User> userResponse = testRestTemplate.getForEntity("/users/omid0", User.class);
        assertEquals(userResponse.getStatusCode(), HttpStatus.NOT_FOUND);
    }

}