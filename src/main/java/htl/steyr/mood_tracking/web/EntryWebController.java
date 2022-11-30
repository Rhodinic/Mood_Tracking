package htl.steyr.mood_tracking.web;

import htl.steyr.mood_tracking.application.EntryWriter;
import htl.steyr.mood_tracking.application.UserHandler;
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

    @PostMapping("/add")
    public ResponseEntity<String> saveEntry(int mood, boolean school, Optional<Integer> exhaustion, int socialAmount, boolean specialEvent) {
        User user = userHandler.getUser();

        if(user != null){
            if(school && exhaustion.isPresent()){
                entryWriter.saveEntry(mood, true, exhaustion.get(), socialAmount, specialEvent, user);
            }else{
                entryWriter.saveEntry(mood, false, 0, socialAmount, specialEvent, user);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created today's entry. (CODE 201)");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found! Please call /user/login first. (CODE 404)");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Entry>> listEntries() {
        User user = userHandler.getUser();

        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(entryRepository.findByUser(user));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

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
