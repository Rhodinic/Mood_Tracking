package htl.steyr.mood_tracking.web;

import htl.steyr.mood_tracking.MoodTrackingApplication;
import htl.steyr.mood_tracking.application.UserHandler;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {MoodTrackingApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserWebControllerTest {

    @Autowired
    private UserWebController userWebController;

    @Order(1)
    @Test
    void register(){
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals("Successfully registered!", userWebController.register("userTests", "test", "Steyr").getBody());
        });
    }

    @Order(2)
    @Test
    void login() {
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals("Login successful.", userWebController.login("userTests", "test").getBody());
        });
    }

    @Order(3)
    @Test
    void changeLocation() {
        Assertions.assertDoesNotThrow(() -> {
            /*
            userWebController.changeLocation("Linz");
            Assertions.assertEquals("Linz", userHandler.getUser().getLocation());
             */

            Assertions.assertEquals("Location changed successfully.", userWebController.changeLocation("Linz").getBody());
        });
    }

    @Order(4)
    @Test
    void showTakenNames() {
        Assertions.assertDoesNotThrow(() -> {
            assertTrue(userWebController.showTakenNames().getBody().contains("userTests"));
        });
    }
}