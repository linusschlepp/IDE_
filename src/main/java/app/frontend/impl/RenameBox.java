package app.frontend.impl;

import app.backend.ClassType;
import app.backend.CustomItem;
import app.exceptions.IDEException;
import app.frontend.api.IBox;
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

/**
 * Default-implementation of {@link IBox}. Gets displayed, if the user wants to rename a
 * file/ dir.
 */
public class RenameBox implements IBox {


    private final Logger LOG = LoggerFactory.getLogger(RenameBox.class);




    private final ClassType classType;

    private final String boxText;

    private final String path;

    private final TreeItem<CustomItem> thisTreeItem;

    private String newName;

    public RenameBox(final TreeItem<CustomItem> treeItem) {
        this.classType = treeItem.getValue().getClassType();
        this.boxText = treeItem.getValue().getName();
        this.path = treeItem.getValue().getPath();
        this.thisTreeItem = treeItem;
    }


    public void display() {


        Stage window = new Stage();
        window.setTitle("Rename " + this.boxText);
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label("Enter new name for: " + this.boxText);
        TextField textField = new TextField("Enter new name of " + this.classType);
        textField.setPrefWidth(300);
        textField.setMaxWidth(300);
        Button button = new Button(Constants.OK);
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(textField, 0, 2);
        GridPane.setConstraints(button, 0, 5);
        grdPane.getChildren().addAll(label, textField, button);
        window.setScene(new Scene(grdPane, 300, 300));
        window.show();
        newName = textField.getText();

        //button initiates renaming process
        button.setOnAction(e -> {
            try {
                buttonAction();
            } catch (IDEException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        });

        window.close();

    }

    public void buttonAction() throws IDEException {
        try {
            String oldName = this.boxText;
            // if the required TreeItem is of classType package
            if (classType.equals(ClassType.PACKAGE)) {


                final Path source = Paths.get(this.path);

                thisTreeItem.getValue().setBoxText(newName);
                FrontendConstants.packageNameHashMap.put(this.newName, FrontendConstants.packageNameHashMap.remove(oldName));
                Files.move(source, source.resolveSibling(this.newName));

                // changes the name of the parent-package
                thisTreeItem.getValue().setPath(path.replaceAll(oldName, this.newName));
                //takes a recursive-approach changes the content of the children of the package and the children of the packages within in the packages
                changeClassContentRec(thisTreeItem.getChildren(), this.newName, oldName);
            }
            //if the required TreeItem is of classType enum, interface or class
            else {
                final Path source = Paths.get(path.contains(Constants.JAVA_FILE_EXTENSION) ? this.path : this.path + Constants.JAVA_FILE_EXTENSION);
                //path of treeItem is getting changed to new name
                thisTreeItem.getValue().setPath(this.path
                        .replaceAll(oldName, this.newName));
                thisTreeItem.getValue().setBoxText(this.newName);
                Files.move(source, source.resolveSibling(this.newName + Constants.JAVA_FILE_EXTENSION));
                changeClassContent(this.thisTreeItem, this.newName, oldName);
            }
        } catch (final IOException ex) {
            if (ex instanceof FileAlreadyExistsException) {
                new AlertBox(Alert.AlertType.WARNING, String.format("A file with the name: %s already exists, please choose a different name",
                        this.newName)).display();
            } else
                new IDEException("[{}], [{}] could not be renamed", classType,
                        boxText).throwWithLogging(LOG);
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
                } catch (final IDEException e) {
                    throw new RuntimeException(e);
                }
            } else
                changeClassContentRec(t.getChildren(), newName, oldName);
        });
    }

}
