package htl.steyr.mood_tracking.application;

import htl.steyr.mood_tracking.MoodTrackingApplication;
import htl.steyr.mood_tracking.handlers.EntryWriter;
import htl.steyr.mood_tracking.handlers.UserHandler;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {MoodTrackingApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EntryWriterTest {

    @Autowired
    EntryWriter entryWriter;

    @Autowired
    UserHandler userHandler;

    @Order(1)
    @Test
    void getWeather() {
        Assertions.assertDoesNotThrow(() -> {
            entryWriter.getWeather("Steyr");
        });
    }

    @Order(2)
    @Test
    void saveEntry() {
        Assertions.assertDoesNotThrow(() -> {
            entryWriter.saveEntry(1, true, 1, 1, false, userHandler.getUser());
        });
    }


}