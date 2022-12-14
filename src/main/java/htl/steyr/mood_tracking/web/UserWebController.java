package htl.steyr.mood_tracking.web;

import htl.steyr.mood_tracking.handlers.EntryWriter;
import htl.steyr.mood_tracking.handlers.UserHandler;
import htl.steyr.mood_tracking.application.model.User;
import htl.steyr.mood_tracking.application.model.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserWebController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHandler userHandler;

    @Autowired
    private EntryWriter entryWriter;

    /**
     * Logs in a user
     * Required for a lot of other API-Calls
     * @param username Username of the user to log in
     * @param password Password of the user to log in
     * @return Login success
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(String username, String password) {
        Optional<User> potUser = userHandler.authenticate(username, DigestUtils.md5Hex(password).toUpperCase());

        if(potUser.isPresent()){
            userHandler.save(potUser.get());

            return ResponseEntity.status(HttpStatus.OK).body("Login successful.");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User was not found!");
    }

    /**
     * Registers a new user
     * @param username Username of the new user to create
     * @param password Password of the user to create
     * @param location Location of the user to create
     * @return Registration success, Location not found
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(String username, String password, String location) {
        if(!userHandler.isUsernameTaken(username)){
            if(entryWriter.getWeather(location) != null){
                User newUser = new User();

                newUser.setUsername(username);
                newUser.setPassword(DigestUtils.md5Hex(password).toUpperCase());
                newUser.setLocation(location);

                userHandler.save(newUser);

                return ResponseEntity.status(HttpStatus.OK).body("Successfully registered!");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location was not found.");
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username is already taken!");
    }

    /**
     * Changes the location of the currently logged-in User
     * @param location Location to change to
     * @return Location change success, Location not found
     */
    @PostMapping("/change/location")
    public ResponseEntity<String> changeLocation(String location) {
        User user = userHandler.getUser();

        if(user != null){
            if(entryWriter.getWeather(location) != null){
                user.setLocation(location);
                userRepository.save(user);

                return ResponseEntity.status(HttpStatus.OK).body("Location changed successfully.");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location was not found.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found! Please call /user/login first.");
    }

    /**
     * Returns a List of the currently taken names
     * @return List of the currently taken names
     */
    @GetMapping("/takenNames")
    public ResponseEntity<List<String>> showTakenNames() {
        List<String> response = userRepository.findAll().stream().map(User::getUsername).collect(Collectors.toList());

        if(response.size() > 0){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


}
