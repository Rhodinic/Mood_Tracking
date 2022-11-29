package htl.steyr.mood_tracking;

import htl.steyr.mood_tracking.model.User;
import htl.steyr.mood_tracking.model.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {MoodTrackingApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserHandlerTest {

    @Autowired
    UserRepository repository;

    @Autowired
    UserHandler userHandler;

    @Order(1)
    @Test
    void save() {
        /**
         * Hoffentlich kein Problem mit Appdata?
         */
        Assertions.assertDoesNotThrow(() -> {
            User u = new User();

            u.setPassword("test");
            u.setUsername("test");
            u.setLocation("test");

            userHandler.save(u);
        });
    }

    @Order(2)
    @Test
    void getUser() {
        Assertions.assertEquals(userHandler.getUser().getPassword(), "test");
        Assertions.assertEquals(userHandler.getUser().getUsername(), "test");
        Assertions.assertEquals(userHandler.getUser().getLocation(), "test");
    }

    @Order(3)
    @Test
    void authenticate() {
        assertTrue(userHandler.authenticate(userHandler.getUser().getUsername(), userHandler.getUser().getPassword()).isPresent());
    }

    @Order(4)
    @Test
    void isUsernameTaken() {
        assertTrue(userHandler.isUsernameTaken("test"));
    }
}