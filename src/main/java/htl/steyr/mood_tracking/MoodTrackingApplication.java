package htl.steyr.mood_tracking;

import htl.steyr.mood_tracking.application.JavaFxApplication;
import htl.steyr.mood_tracking.application.JavaFxShowApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class MoodTrackingApplication {

    public static void main(String[] args) {

        /**
         * @TODO put into FXApplication
         */
        if(Arrays.asList(args).contains("show")){
            Application.launch(JavaFxShowApplication.class, args);
        }else{
            Application.launch(JavaFxApplication.class, args);
        }

    }

}
