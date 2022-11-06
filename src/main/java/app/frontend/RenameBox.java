package app.frontend;

import app.backend.ClassType;
import app.backend.CustomItem;
import app.exceptions.IDEException;
import app.utils.Constants;
import app.utils.FileUtils;
import app.utils.FrontendConstants;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;


public class RenameBox {


    private static final Logger LOG = LoggerFactory.getLogger(RenameBox.class);

    private RenameBox() {
        // Private-Constructor to hide implicit, default constructor
    }


    public static void display(final TreeItem<CustomItem> treeItem) {


        Stage window = new Stage();
        window.setTitle("Rename " + treeItem.getValue().getLabelText());
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label("Enter new name for: " + treeItem.getValue().getLabelText());
        TextField textField = new TextField("Enter new name of " + treeItem.getValue().getClassType());
        textField.setPrefWidth(300);
        textField.setMaxWidth(300);
        Button button = new Button(Constants.OK);
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(textField, 0, 2);
        GridPane.setConstraints(button, 0, 5);
        grdPane.getChildren().addAll(label, textField, button);
        window.setScene(new Scene(grdPane, 300, 300));
        window.show();

        //button initiates renaming process
        button.setOnAction(e -> {
            try {
                buttonAction(treeItem, textField, window);
            } catch (IDEException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        });

    }

    //TODO: Check if window is really necessary as parameter
    /**
     * Logic is executed after button is pressed on GUI
     *
     * @param treeItem TreeItem to be renamed
     * @param textField TextField, where new name is entered
     * @param window Stage, where renaming process happens
     * @throws IDEException If changing the classContent fails
     */
    private static void buttonAction(final TreeItem<CustomItem> treeItem, final TextField textField, final Stage window) throws IDEException {
        try {
            // if the required TreeItem is of classType package
            if (treeItem.getValue().getClassType().equals(ClassType.PACKAGE)) {

                String oldName = treeItem.getValue().getLabelText();
                Path source = Paths.get(treeItem.getValue().getPath());

                treeItem.getValue().setBoxText(textField.getText());
                FrontendConstants.packageNameHashMap.put(textField.getText(), FrontendConstants.packageNameHashMap.remove(oldName));
                Files.move(source, source.resolveSibling(textField.getText()));

                // changes the name of the parent-package
                treeItem.getValue().setPath(treeItem.getValue().getPath().replaceAll(oldName, textField.getText()));
                //takes a recursive-approach changes the content of the children of the package and the children of the packages within in the packages
                changeClassContentRec(treeItem.getChildren(), textField.getText(), oldName);
            }
            //if the required TreeItem is of classType enum, interface or class
            else {
                String oldName = treeItem.getValue().getLabelText();
                Path source = Paths.get(treeItem.getValue().getPath().contains(Constants.JAVA_FILE_EXTENSION) ? treeItem.getValue().getPath() : treeItem.getValue().getPath() + Constants.JAVA_FILE_EXTENSION);
                //path of treeItem is getting changed to new name
                treeItem.getValue().setPath(treeItem.getValue().getPath()
                        .replaceAll(oldName, textField.getText()));
                treeItem.getValue().setBoxText(textField.getText());
                Files.move(source, source.resolveSibling(textField.getText() + Constants.JAVA_FILE_EXTENSION));
                changeClassContent(treeItem, textField.getText(), oldName);
            }
        } catch (IOException ex) {
            if (ex instanceof FileAlreadyExistsException) {
                AlertBox.display(Alert.AlertType.WARNING, "A file with the name: " + textField.getText() +
                        " already exists, please choose a different name");
            } else
                new IDEException("[{}], [{}] could not be renamed", treeItem.getValue().getClassType().getClassType(),
                        treeItem.getValue().getBoxText().getText()).throwWithLogging(LOG);
        } finally {
            window.close();
        }
    }


    /**
     * Changes classContent of files corresponding to renaming processes
     *
     * @param treeItem treeItem, of which the content is getting changed
     * @param newName  new name of the file
     * @param oldName  name which is getting replaced
     */
    private static void changeClassContent(TreeItem<CustomItem> treeItem, String newName, String oldName) throws IDEException {
        treeItem.getValue().getTextArea().setText(treeItem.getValue().getTextArea().getText()
                .replaceAll(oldName, newName));

        FileUtils.updateFile(treeItem.getValue().getTextArea().getText(), treeItem.getValue().getPath());
    }

    /**
     * Recursive method, which changes the content of files withing packages and of the files within packages, within packages and so on...
     *
     * @param list    children of treeItem (package)
     * @param newName new Name of parent-treeItem
     * @param oldName old Name of parent-treeItem
     */
    private static void changeClassContentRec(final ObservableList<TreeItem<CustomItem>> list, final String newName, final String oldName) {

        if (list.isEmpty())
            return;

        list.forEach(t -> {
            t.getValue().setPath(t.getValue().getPath().replaceAll(oldName, newName));
            if (!t.getValue().getClassType().equals(ClassType.PACKAGE)) {
                try {
                    changeClassContent(t, newName, oldName);
                } catch (IDEException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                changeClassContentRec(t.getChildren(), newName, oldName);
        });
    }

}
