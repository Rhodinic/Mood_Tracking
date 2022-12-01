package htl.steyr.mood_tracking.application;

import htl.steyr.mood_tracking.MoodTrackingApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxShowApplication extends Application {
    private static final String APPLICATION_TITLE = "Stimmungsabfragen Backend";

    private static ConfigurableApplicationContext springContext;
    private Parent root;

    @Override
    public void init() throws Exception {
        String[] args = new String[0];

        /*
         * Get an "instance" of the spring framework to enable all spring-specific
         * features (dependency injection, autoloading, java persistance api, ...).
         */
        springContext =
                new SpringApplicationBuilder().
                        sources(MoodTrackingApplication.class).
                        run(args);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("show.fxml"));

        /*
         * Set spring as the factory (when controllers were generated).
         * So spring is able to do its magic.
         */
        fxmlLoader.setControllerFactory(springContext::getBean);
        root = fxmlLoader.load();
        //Controller c = fxmlLoader.getController();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(APPLICATION_TITLE);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    @Override
    public void stop() {
        springContext.stop();
    }
}