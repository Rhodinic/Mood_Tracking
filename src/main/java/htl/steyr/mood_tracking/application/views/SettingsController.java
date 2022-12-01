package htl.steyr.mood_tracking.application.views;

import htl.steyr.mood_tracking.handlers.UserHandler;
import htl.steyr.mood_tracking.application.model.User;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingsController {

    public TextField locationTextField;

    @Autowired
    UserHandler userHandler;

    public void initialize(){
        locationTextField.setText(userHandler.getUser().getLocation());
    }

    public void saveButtonClicked() {
        User u = userHandler.getUser();
        u.setLocation(locationTextField.getText());

        userHandler.save(u);
    }
}
