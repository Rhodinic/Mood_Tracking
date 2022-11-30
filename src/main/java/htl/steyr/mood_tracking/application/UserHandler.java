package htl.steyr.mood_tracking.application;

import htl.steyr.mood_tracking.application.model.User;
import htl.steyr.mood_tracking.application.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Component
public class UserHandler {

    @Autowired
    UserRepository userRepository;

    public void save(User u){
        userRepository.save(u);

        try {
            Files.createDirectories(Paths.get(System.getenv("APPDATA") + "\\.moodTracking"));

            FileWriter fw = new FileWriter(System.getenv("APPDATA") + "\\.moodTracking\\user.csv");
            fw.write(u.getUsername() + ";" + u.getPassword() + ";" + u.getLocation());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser(){
        User result = new User();

        try {
            String[] fileContent = Files.readString(Paths.get(System.getenv("APPDATA") + "\\.moodTracking\\user.csv")).split(";");

            result = userRepository.findByUsernameAndPassword(fileContent[0], fileContent[1]).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Optional<User> authenticate(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public boolean isUsernameTaken(String username){
        return userRepository.findByUsername(username).isPresent();
    }
}
