package htl.steyr.mood_tracking;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Controller {
    public Slider moodSlider;
    public TextField moodField;
    public CheckBox schoolCheckBox;
    public Group schoolExhaustionGroup;
    public Slider exhaustionSlider;
    public TextField exhaustionField;
    public Group secondHalfGroup;
    public Slider socialSlider;
    public TextField socialField;
    public CheckBox specialEventCheckBox;

    @Autowired
    public EntryWriter entryWriter;

    @Autowired
    public UserHandler userHandler;

    public void initialize(){
        moodSlider.valueProperty().addListener((observable, oldValue, newValue) -> moodField.setText(String.valueOf(newValue.intValue())));
        exhaustionSlider.valueProperty().addListener((observable, oldValue, newValue) -> exhaustionField.setText(String.valueOf(newValue.intValue())));
        socialSlider.valueProperty().addListener((observable, oldValue, newValue) -> socialField.setText(String.valueOf(newValue.intValue())));
    }

    public void schoolCheckBoxClicked(ActionEvent actionEvent) {
        if(schoolCheckBox.isSelected()){
            schoolExhaustionGroup.setVisible(true);
            secondHalfGroup.setLayoutY(secondHalfGroup.getLayoutY() + 40);
        }else {
            schoolExhaustionGroup.setVisible(false);
            secondHalfGroup.setLayoutY(secondHalfGroup.getLayoutY() - 40);
        }
    }

    public void moodFieldConfirmed(ActionEvent actionEvent) {
        moodSlider.setValue(Double.parseDouble(moodField.getText()));
    }

    public void exhaustionFieldConfirmed(ActionEvent actionEvent) {
        exhaustionSlider.setValue(Double.parseDouble(exhaustionField.getText()));
    }

    public void socialFieldConfirmed(ActionEvent actionEvent) {
        socialSlider.setValue(Double.parseDouble(socialField.getText()));
    }

    public void sendButtonClicked(ActionEvent actionEvent) {
        entryWriter.saveEntry((int) moodSlider.getValue(),
                                            schoolCheckBox.isSelected(),
                                            (int) exhaustionSlider.getValue(),
                                            (int) socialSlider.getValue(),
                                            specialEventCheckBox.isSelected(),
                                            userHandler.getUser());

        Platform.exit();
    }
}
