package htl.steyr.mood_tracking.web;

import htl.steyr.mood_tracking.handlers.EntryWriter;
import htl.steyr.mood_tracking.handlers.UserHandler;
import htl.steyr.mood_tracking.application.model.Entry;
import htl.steyr.mood_tracking.application.model.EntryRepository;
import htl.steyr.mood_tracking.application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/entry")
public class EntryWebController {

    @Autowired
    private EntryWriter entryWriter;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private UserHandler userHandler;

    /**
     * Adds or overwrites today Entry depending on if it has already been saved for today
     * @param mood Mood of the day (1-100)
     * @param school If there was school today (t/f)
     * @param exhaustion How exhausting school was (1-100), only required if school is true, default value 0
     * @param socialAmount Amount of social interaction today (1-100)
     * @param specialEvent If there was a special event today (t/f), e.g. birthday, Christmas
     * @return Entry saving success, user not found
     */
    @PostMapping("/add")
    public ResponseEntity<String> saveEntry(int mood, boolean school, Optional<Integer> exhaustion, int socialAmount, boolean specialEvent) {
        User user = userHandler.getUser();

        /**
         * @TODO max & min values
         */

        if(user != null){
            if(school && exhaustion.isPresent()){
                entryWriter.saveEntry(mood, true, exhaustion.get(), socialAmount, specialEvent, user);
            }else{
                entryWriter.saveEntry(mood, false, 0, socialAmount, specialEvent, user);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created today's entry.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found! Please call /user/login first.");
    }

    /**
     * Returns a List of all entries of the currently logged-in User
     * @return List of all entries of the user, not found error
     */
    @GetMapping("/list")
    public ResponseEntity<List<Entry>> listEntries() {
        User user = userHandler.getUser();

        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(entryRepository.findByUser(user));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Returns today's entry
     * @return today's entry, not found error
     */
    @GetMapping("/today")
    public ResponseEntity<Entry> todaysEntry() {
        User user = userHandler.getUser();

        if(user != null){
            Optional<Entry> e = entryRepository.findByDateAndUser(new Date(), user);

            if(e.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(e.get());
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
