package htl.steyr.mood_tracking.application;

import htl.steyr.mood_tracking.application.model.User;
import htl.steyr.mood_tracking.application.model.Weather;
import htl.steyr.mood_tracking.handlers.EntryWriter;
import htl.steyr.mood_tracking.handlers.UserHandler;
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
        loadFXML("start.fxml");
    }

    public void loginButtonClicked() {
        Optional<User> response = userHandler.authenticate(usernameTextField.getText(), DigestUtils.md5Hex(passwordTextField.getText()).toUpperCase());

        /*
         * Check for valid user, if not show Error
         */
        if (response.isPresent()) {
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
            /*
             * Check if Username is already taken, else show a warning
             */
            if (userHandler.isUsernameTaken(usernameTextField.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Benutzername existiert bereits! Bitte wählen Sie einen anderen.", ButtonType.OK);
                alert.showAndWait();

            } else {
                newUser.setUsername(usernameTextField.getText());
                newUser.setPassword(DigestUtils.md5Hex(passwordTextField.getText()).toUpperCase());

                TextInputDialog inputDialog = new TextInputDialog("Steyr");
                inputDialog.setHeaderText("Geben Sie bitte Ihren Standort ein (zBsp.: Steyr)");

                /*
                 * Shows the Weather-choosing dialogue until a valid location is chosen
                 */
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
        loadFXML("settings.fxml");
    }

    public void graphViewClicked() {
        loadFXML("graphView.fxml");
    }

    public void tableViewClicked() {
        loadFXML("tableView.fxml");
    }

    private void loadFXML(String file) {
        Pane newLoadedPane = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/" + file));

            fxmlLoader.setControllerFactory(JavaFxShowApplication.getSpringContext()::getBean);
            newLoadedPane = fxmlLoader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
