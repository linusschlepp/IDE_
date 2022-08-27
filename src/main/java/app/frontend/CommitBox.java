package app.frontend;

import app.utils.Constants;
import app.utils.GitUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Gets displayed, if the user wants to commit
 */
public class CommitBox {


    private final static Logger LOG = LoggerFactory.getLogger(CommitBox.class);

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
                GitUtils.gitCommit(textField.getText());
            } catch (IOException ex) {
                LOG.error("Commit was not possible");
            }
            window.close();

        });
    }
}
