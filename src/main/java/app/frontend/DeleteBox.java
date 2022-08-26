package app.frontend;

import app.backend.ClassType;
import app.backend.CustomItem;
import app.utils.Constants;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class DeleteBox {


    public static Logger LOG = LoggerFactory.getLogger(DeleteBox.class);


    public static void display(TreeItem<CustomItem> treeItem) {

        Stage window = new Stage();
        window.setTitle(Constants.DELETE + Constants.EMPTY_STRING + treeItem.getValue().getLabelText());
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label(Constants.SURE_DELETE+ Constants.EMPTY_STRING + treeItem.getValue().getLabelText());
        Button button = new Button(Constants.OK);
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(button, 0, 5);
        grdPane.getChildren().addAll(label, button);
        window.setScene(new Scene(grdPane, 300, 300));
        window.show();

        button.setOnAction(e -> {

            LOG.info("Deleting [{}] [{}]", treeItem.getValue().getClassType(),
                    treeItem.getValue().getBoxText().getText());

            try {
                if (treeItem.getValue().getClassType().equals(ClassType.PACKAGE)) {
                    Files.walk(Paths.get(treeItem.getValue().getPath())).sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                } else {
                    String tempPath = treeItem.getValue().getPath();
                    if (!tempPath.contains(Constants.JAVA_FILE_EXTENSION))
                        tempPath += Constants.JAVA_FILE_EXTENSION;

                    if (Files.deleteIfExists(Paths.get(tempPath)))
                        Files.delete(Paths.get(tempPath));
                }
            } catch (IOException ex) {
                if (!(ex instanceof NoSuchFileException))
                    LOG.error("[{}] [{}] could not be deleted, because it does not exist", treeItem.getValue().getClassType(),
                            treeItem.getValue().getBoxText().getText());
            } finally {
                treeItem.getParent().getChildren().remove(treeItem);
            }
            window.close();
        });

        LOG.info("Successfully deleted [{}] [{}]", treeItem.getValue().getClassType(),
                treeItem.getValue().getBoxText().getText());

    }
}
