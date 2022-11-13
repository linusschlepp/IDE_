package app.frontend.impl;

import app.backend.ClassType;
import app.backend.CustomItem;
import app.exceptions.IDEException;
import app.frontend.api.IBox;
import app.utils.Constants;
import app.utils.FileUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;

/**
 * Default-implementation of {@link IBox}. Gets displayed, if user wants to delete a dir/ file
 * within the file/ tree-structure
 */
public class DeleteBox implements IBox {


    public final Logger LOG = LoggerFactory.getLogger(DeleteBox.class);


    private final ClassType classType;

    private final String boxText;

    private  String path;

    private final TreeItem<CustomItem> thisTreeItem;

    public DeleteBox(final TreeItem<CustomItem> treeItem) {
        this.classType = treeItem.getValue().getClassType();
        this.boxText = treeItem.getValue().getName();
        this.path = treeItem.getValue().getPath();
        this.thisTreeItem = treeItem;
    }

    public void display() {


        Stage window = new Stage();
        window.setTitle(Constants.DELETE + Constants.EMPTY_STRING + this.boxText);
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label(Constants.SURE_DELETE + Constants.EMPTY_STRING + this.boxText);
        Button button = new Button(Constants.OK);
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(button, 0, 5);
        grdPane.getChildren().addAll(label, button);
        window.setScene(new Scene(grdPane, 300, 300));
        window.show();

        button.setOnAction(e -> {
            try {
                buttonAction();
            } catch (final IDEException ex) {
                throw new RuntimeException(ex);
            }
        });
        window.close();
    }


    public void buttonAction() throws IDEException {
        LOG.info("Deleting [{}] [{}]", this.classType,
                this.boxText);

        try {
            if (this.classType.equals(ClassType.PACKAGE)) {
                FileUtils.deleteDirectory(this.path);
            } else {
                if (!this.path.contains(Constants.JAVA_FILE_EXTENSION))
                    this.path += Constants.JAVA_FILE_EXTENSION;

                if (Files.deleteIfExists(Paths.get(this.path)))
                    FileUtils.deleteFile(this.path);
            }
        } catch (IOException ex) {
            if (!(ex instanceof NoSuchFileException))
                new IDEException("[{}] [{}] could not be deleted, because it does not exist", this.classType,
                        this.boxText).throwWithLogging(LOG);
        } finally {
            this.thisTreeItem.getParent().getChildren().remove(this.thisTreeItem);
        }

        LOG.info("Successfully deleted [{}] [{}]", this.classType,
                this.boxText);

    }
}
