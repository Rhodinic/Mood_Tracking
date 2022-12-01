package htl.steyr.mood_tracking.application.views;

import htl.steyr.mood_tracking.handlers.EntryWriter;
import htl.steyr.mood_tracking.handlers.UserHandler;
import htl.steyr.mood_tracking.application.model.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.parser.Entity;

@Component
public class SettingsController {

    public TextField locationTextField;

    @Autowired
    UserHandler userHandler;

    @Autowired
    EntryWriter entryWriter;

    public void initialize(){
        locationTextField.setText(userHandler.getUser().getLocation());
    }

    public void saveButtonClicked() {
        /*
         * Shows error message if Location does not exist
         */
        if (entryWriter.getWeather(locationTextField.getText()) == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Dies ist kein korrekter Standort.", ButtonType.OK);
            alert.showAndWait();
        }else{
            User u = userHandler.getUser();

            u.setLocation(locationTextField.getText());

            userHandler.save(u);
        }
    }
}
