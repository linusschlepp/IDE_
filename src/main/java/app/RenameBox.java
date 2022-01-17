package app;

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


//TODO: Check renaming for files within multiple packages

/**
 * This dialog-box gets shown if files/ directories have to be renamed
 */
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

            // if the required TreeItem is of classType package
            if (treeItem.getValue().getClassType().equals(ClassType.PACKAGE)) {

                String oldName = treeItem.getValue().getLabelText();

                Path source = Paths.get(treeItem.getValue().getPath());
                try {
                    treeItem.getValue().setBoxText(textField.getText());
                    Files.move(source, source.resolveSibling(textField.getText()));
                    //TODO: the classcontent of files within packages within packages doesn't get changed this still requires fixing
                    treeItem.getChildren().forEach(t -> {
                        //Path of files within renamend packages get changed to fit new path of renamed package
                        t.getValue().setPath(t.getValue().getPath().replaceAll(oldName, textField.getText()));
                        //the class content of the individual files gets changed as well to fit
                        if (!(t.getValue().getClassType().equals(ClassType.PACKAGE)))
                            changeClassContent(t, textField.getText(), oldName);
                        System.out.println(t.getValue().getPath());
                    });
                } catch (IOException ex) {
                    if (ex instanceof FileAlreadyExistsException)
                        AlertBoxName.display(textField.getText());
                }
            }
            //if the required TreeItem is of classType enum, interface or class
            else {

                String oldName = treeItem.getValue().getLabelText();
                Path source = Paths.get(treeItem.getValue().getPath().contains(".java") ? treeItem.getValue().getPath() : treeItem.getValue().getPath() + ".java");
                //path of treeItem is getting changed to new name
                treeItem.getValue().setPath(treeItem.getValue().getPath()
                        .replaceAll(oldName, textField.getText()));

                try {

                    System.out.println(source);
                    System.out.println(source.resolveSibling(textField.getText()));
                    treeItem.getValue().setBoxText(textField.getText());
                    Files.move(source, source.resolveSibling(textField.getText() + ".java"));
                    changeClassContent(treeItem, textField.getText(), oldName);

                } catch (IOException ex) {
                    if (ex instanceof FileAlreadyExistsException)
                        AlertBoxName.display(textField.getText());
                }
            }

            window.close();

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
        System.out.println(oldName);
        System.out.println(newName);

        System.out.println(treeItem.getValue().getTextArea().getText());
        treeItem.getValue().getTextArea().setText(treeItem.getValue().getTextArea().getText()
                .replaceAll(oldName, newName));
        System.out.println(treeItem.getValue().getTextArea().getText());

        GridPaneNIO.updateFile(treeItem.getValue().getTextArea().getText(), treeItem.getValue().getPath());


    }

}
