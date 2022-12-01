package htl.steyr.mood_tracking.handlers;

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

    /**
     * @TODO Umstellung auf JSON Web-Token
     */

    @Autowired
    UserRepository userRepository;

    /**
     * Saves a given user into the repository and into the AppData Folder
     * so the user doesn't have to log in every day.
     * @param u User to save
     */
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

    /**
     * Gets the user saved in the AppData folder from the database
     * @return current User
     */
    public User getUser(){
        User result = new User();

        try {
            String[] fileContent = Files.readString(Paths.get(System.getenv("APPDATA") + "\\.moodTracking\\user.csv")).split(";");

            Optional<User> potUser = userRepository.findByUsernameAndPassword(fileContent[0], fileContent[1]);

            if(potUser.isPresent()){
                result = potUser.get();
            }else{
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Checks if the user is saved in the database by Username and Password
     * @param username Username of the User to check
     * @param password Password (md5 encrypted) of the User to check
     * @return if found the User-Object, if not found empty Object
     */
    public Optional<User> authenticate(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password);
    }

    /**
     * Checks if a given Username is already in use by another user
     * @param username Username to check
     * @return true, false
     */
    public boolean isUsernameTaken(String username){
        return userRepository.findByUsername(username).isPresent();
    }
}
