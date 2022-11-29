package htl.steyr.mood_tracking.modelview;

import htl.steyr.mood_tracking.UserHandler;
import htl.steyr.mood_tracking.model.User;
import javafx.event.ActionEvent;
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

    public void saveButtonClicked(ActionEvent actionEvent) {
        User u = userHandler.getUser();
        u.setLocation(locationTextField.getText());

        userHandler.save(u);
    }
}
