package htl.steyr.mood_tracking.web;

import htl.steyr.mood_tracking.MoodTrackingApplication;
import htl.steyr.mood_tracking.application.model.Entry;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = {MoodTrackingApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EntryWebControllerTest {

    @Autowired
    private EntryWebController entryWebController;

    @Autowired
    private UserWebController userWebController;

    int entryAmountBeforeTests;

    @BeforeAll
    void initialize(){
        userWebController.register("entryTests", "test", "Steyr");

        entryAmountBeforeTests = entryWebController.listEntries().getBody().size();
    }

    @Order(1)
    @Test
    void saveEntry() {
        Assertions.assertDoesNotThrow(() -> {
            entryWebController.saveEntry(1,false, java.util.Optional.of(1),1,false);
        });
    }

    @Order(2)
    @Test
    void todaysEntry() {
        Entry e = entryWebController.todaysEntry().getBody();

        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(1, e.getMood());
            assertFalse(e.isSchool());
            Assertions.assertEquals(0, e.getSchoolIntensity());
            Assertions.assertEquals(1, e.getAmountOfSocialInteraction());
            assertFalse(e.isSpecialEvent());
        });
    }

    @Order(3)
    @Test
    void listEntries() {
        Assertions.assertDoesNotThrow(() -> {
            for(Entry e : entryWebController.listEntries().getBody()){
                System.out.println(e.getMood());
            }

            Assertions.assertEquals(entryAmountBeforeTests + 1, entryWebController.listEntries().getBody().size());
        });
    }


}