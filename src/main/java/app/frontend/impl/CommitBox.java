package app.frontend.impl;

import app.exceptions.IDEException;
import app.frontend.api.IBox;
import app.utils.Constants;
import app.utils.GitUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Default-implementation of {@link IBox}. Gets displayed, if the user wants commit changes to git
 */
public class CommitBox implements IBox {

    private final Logger LOG = LoggerFactory.getLogger(CommitBox.class);

    private String commitMessage;



    //TODO: CommitMessage could also be passed via constructor, try to generalize behviour, or to move fronten behaviour behvaviour

    @Override
    public void display() {


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

        this.commitMessage = textField.getText();

        button.setOnAction( e-> {
            try {
                buttonAction();
            } catch (final IDEException ex) {
                throw new RuntimeException(ex);
            }
        });

            window.close();

    }


    /**
     * Logic is executed after button is pressed on GUI
     *
     * @throws IDEException If commit was not possible
     */
    @Override
   public  void buttonAction() throws IDEException {
        try {
            GitUtils.gitCommit(this.commitMessage);
        } catch (final IOException ex) {
            new IDEException("Commit was not possible").throwWithLogging(LOG);
        }
        LOG.info("File with Message: [{}] was committed", this.commitMessage);
    }
}
