package htl.steyr.mood_tracking;

import htl.steyr.mood_tracking.model.User;
import htl.steyr.mood_tracking.model.UserRepository;
import htl.steyr.mood_tracking.model.Weather;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class ShowController {

    public Pane contentPane;
    public Group loginGroup;
    public Group mainGroup;
    public TextField usernameTextField;
    public TextField passwordTextField;

    @Autowired
    UserHandler userHandler;

    @Autowired
    EntryWriter entryWriter;

    public void initialize() {
        loadFXML("modelview/start.fxml");
    }

    public void loginButtonClicked() {
        Optional<User> response = userHandler.authenticate(usernameTextField.getText(), DigestUtils.md5Hex(passwordTextField.getText()).toUpperCase());

        if (response.isPresent()) {
            /**
             * Program will save last logged-in User into AppData Folder.
             */
            userHandler.save(response.get());
            login();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Falsche Daten!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void registerButtonClicked() {
        User newUser = new User();
        Weather newWeather = null;

        if (!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            if (userHandler.isUsernameTaken(usernameTextField.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Benutzername existiert bereits! Bitte wählen Sie einen anderen.", ButtonType.OK);
                alert.showAndWait();

            } else {
                newUser.setUsername(usernameTextField.getText());
                newUser.setPassword(DigestUtils.md5Hex(passwordTextField.getText()).toUpperCase());

                TextInputDialog inputDialog = new TextInputDialog("Steyr");
                inputDialog.setHeaderText("Geben Sie bitte Ihren Standort ein (zBsp.: Steyr)");

                while (newWeather == null) {
                    inputDialog.showAndWait();

                    newWeather = entryWriter.getWeather(inputDialog.getEditor().getText());

                    if (newWeather == null) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Dies ist kein korrekter Standort.", ButtonType.OK);
                        alert.showAndWait();
                    }
                }

                newUser.setLocation(inputDialog.getEditor().getText());

                userHandler.save(newUser);

                login();
            }
        }
    }

    private void login() {
        loginGroup.setVisible(false);
        mainGroup.setVisible(true);
    }

    public void settingsClicked() {
        loadFXML("modelview/settings.fxml");
    }

    public void graphViewClicked() {
        loadFXML("modelview/graphView.fxml");
    }

    public void tableViewClicked() {
        loadFXML("modelview/tableView.fxml");
    }

    private void loadFXML(String file) {
        Pane newLoadedPane = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(file));

            fxmlLoader.setControllerFactory(JavaFxShowApplication.getSpringContext()::getBean);
            newLoadedPane = fxmlLoader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
