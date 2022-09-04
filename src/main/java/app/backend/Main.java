package app.backend;

import app.frontend.FrontendInit;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Instantiates one instance of GridPaneNIO and passes it to the primaryStage
 *
 * @author  Linus Schlepp
 * @version 1.0.0
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        FrontendInit grid = new FrontendInit(primaryStage);
        grid.init();


    }
}
