package app.backend;

import app.exceptions.IDEException;
import app.frontend.FrontendInit;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Instantiates one instance of {@link FrontendInit} and passes it to the primaryStage
 *
 * @author  Linus Schlepp
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IDEException {

        FrontendInit frontendInit = new FrontendInit(primaryStage);
        frontendInit.init();


    }
}
