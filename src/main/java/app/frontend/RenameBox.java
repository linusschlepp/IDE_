package app.frontend;

import app.backend.ClassType;
import app.backend.CustomItem;
import app.utils.FileUtils;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.*;


public class RenameBox {

    public static void display(TreeItem<CustomItem> treeItem) {


        Stage window = new Stage();
        window.setTitle("Rename " + treeItem.getValue().getLabelText());
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label("Enter new name for: " + treeItem.getValue().getLabelText());
        TextField textField = new TextField("Enter new name of " + treeItem.getValue().getClassType());
        textField.setPrefWidth(300);
        textField.setMaxWidth(300);
        Button button = new Button("Ok");
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(textField, 0, 2);
        GridPane.setConstraints(button, 0, 5);
        grdPane.getChildren().addAll(label, textField, button);
        window.setScene(new Scene(grdPane, 300, 300));
        window.show();

        //button initiates renaming process
        button.setOnAction(e -> {

            try {
                // if the required TreeItem is of classType package
                if (treeItem.getValue().getClassType().equals(ClassType.PACKAGE)) {

                    String oldName = treeItem.getValue().getLabelText();
                    Path source = Paths.get(treeItem.getValue().getPath());

                    treeItem.getValue().setBoxText(textField.getText());
                    GridPaneNIO.packageNameHashMap.put(textField.getText(), GridPaneNIO.packageNameHashMap.remove(oldName));
                    Files.move(source, source.resolveSibling(textField.getText()));

                    // changes the name of the parent-package
                    treeItem.getValue().setPath(treeItem.getValue().getPath().replaceAll(oldName, textField.getText()));
                    //takes a recursive-approach changes the content of the children of the package and the children of the packages within in the packages
                    changeClassContentRec(treeItem.getChildren(), textField.getText(), oldName);
                }
                //if the required TreeItem is of classType enum, interface or class
                else {
                    String oldName = treeItem.getValue().getLabelText();
                    Path source = Paths.get(treeItem.getValue().getPath().contains(".java") ? treeItem.getValue().getPath() : treeItem.getValue().getPath() + ".java");
                    //path of treeItem is getting changed to new name
                    treeItem.getValue().setPath(treeItem.getValue().getPath()
                            .replaceAll(oldName, textField.getText()));
                    treeItem.getValue().setBoxText(textField.getText());
                    Files.move(source, source.resolveSibling(textField.getText() + ".java"));
                    changeClassContent(treeItem, textField.getText(), oldName);
                }
            } catch (IOException ex) {
                if (ex instanceof FileAlreadyExistsException)
                    AlertBoxName.display(textField.getText());
                else
                    ex.printStackTrace();
            } finally {
                window.close();
            }
        });
    }

    /**
     * Changes classContent of files corresponding to renaming processes
     *
     * @param treeItem treeItem, of which the content is getting changed
     * @param newName  new name of the file
     * @param oldName  name which is getting replaced
     */
    private static void changeClassContent(TreeItem<CustomItem> treeItem, String newName, String oldName) {
        treeItem.getValue().getTextArea().setText(treeItem.getValue().getTextArea().getText()
                .replaceAll(oldName, newName));

        FileUtils.updateFile(treeItem.getValue().getTextArea().getText(), treeItem.getValue().getPath());
    }

    /**
     * Recursive method, which changes the content of files withing packages and of the files within packages within packages and so on...
     *
     * @param list    children of treeItem (package)
     * @param newName new Name of parent-treeItem
     * @param oldName old Name of parent-treeItem
     */
    private static void changeClassContentRec(ObservableList<TreeItem<CustomItem>> list, String newName, String oldName) {

        if (list.isEmpty())
            return;

        list.forEach(t -> {
            t.getValue().setPath(t.getValue().getPath().replaceAll(oldName, newName));
            if (!t.getValue().getClassType().equals(ClassType.PACKAGE))
                changeClassContent(t, newName, oldName);
            else
                changeClassContentRec(t.getChildren(), newName, oldName);
        });





    }

}
