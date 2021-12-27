package app;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Instantiates one instance of GridPaneNIO and passes it to the primaryStage
 *
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        GridPaneNIO grid = new GridPaneNIO(primaryStage);
        grid.start();


    }
}
