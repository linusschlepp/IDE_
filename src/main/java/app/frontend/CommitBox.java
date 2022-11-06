package app.frontend;

import app.backend.ClassType;
import app.backend.CustomItem;
import app.exceptions.IDEException;
import app.utils.Constants;
import app.utils.FrontendConstants;
import app.utils.GitUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Gets displayed, if the user wants commit changes to git
 */
public class CommitBox {

    private static final Logger LOG = LoggerFactory.getLogger(CommitBox.class);

    private CommitBox() {
        // Private-Constructor to hide implicit, default constructor
    }

    public static void display() {


        Stage window = new Stage();
        window.setTitle(Constants.COMMIT_MESSAGE);
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label(Constants.COMMIT_MESSAGE);
        TextField textField = new TextField(Constants.COMMIT_MESSAGE);
        textField.setPrefWidth(300);
        textField.setMaxWidth(300);
        Button button = new Button(Constants.OK);
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(textField, 0, 2);
        GridPane.setConstraints(button, 0, 5);
        grdPane.getChildren().addAll(label, textField, button);
        window.setScene(new Scene(grdPane, 300, 300));
        window.show();

        button.setOnAction( e-> {
            try {
                buttonAction(textField.getText());
            } catch (IDEException ex) {
                throw new RuntimeException(ex);
            }
        });

            window.close();

    }


    /**
     * Logic is executed after button is pressed on GUI
     *
     * @param commitMessage Message, used for commit
     * @throws IDEException If commit was not possible
     */
    private static void buttonAction(final String commitMessage) throws IDEException {
        try {
            GitUtils.gitCommit(commitMessage);
        } catch (IOException ex) {
            new IDEException("Commit was not possible").throwWithLogging(LOG);
        }
    }
}
