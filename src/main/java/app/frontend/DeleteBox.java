package app.frontend;

import app.backend.ClassType;
import app.backend.CustomItem;
import app.exceptions.IDEException;
import app.utils.Constants;
import app.utils.FrontendConstants;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

public class DeleteBox {


    public static final Logger LOG = LoggerFactory.getLogger(DeleteBox.class);

    private DeleteBox() {
        // Private-Constructor to hide implicit, default constructor
    }

    public static void display(final TreeItem<CustomItem> treeItem) {

        Stage window = new Stage();
        window.setTitle(Constants.DELETE + Constants.EMPTY_STRING + treeItem.getValue().getLabelText());
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label(Constants.SURE_DELETE + Constants.EMPTY_STRING + treeItem.getValue().getLabelText());
        Button button = new Button(Constants.OK);
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(button, 0, 5);
        grdPane.getChildren().addAll(label, button);
        window.setScene(new Scene(grdPane, 300, 300));
        window.show();

        button.setOnAction(e -> {
            try {
                buttonAction(treeItem, window);
            } catch (IDEException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    private static void buttonAction(final TreeItem<CustomItem> treeItem, final Stage window) throws IDEException {
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
                new IDEException("[{}] [{}] could not be deleted, because it does not exist", treeItem.getValue().getClassType(),
                        treeItem.getValue().getBoxText().getText()).throwWithLogging(LOG);
        } finally {
            treeItem.getParent().getChildren().remove(treeItem);
        }
        window.close();


        LOG.info("Successfully deleted [{}] [{}]", treeItem.getValue().getClassType(),
                treeItem.getValue().getBoxText().getText());

    }
}
