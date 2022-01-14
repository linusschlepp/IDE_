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
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
                //Searches in the HashMap and changes value (TreeItem) Label to the new input of the user
                GridPaneNIO.packageNameHashMap.forEach((k, v) -> {
                    if (k.equals(treeItem.getValue().getLabelText())) {
                        GridPaneNIO.packageNameHashMap.get(k).getValue().setBoxText(textField.getText());
                        Path source = Paths.get(GridPaneNIO.packageNameHashMap.get(k).getValue().getPath());
                        try {
                            Files.move(source, source.resolveSibling(textField.getText()));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
            //if the required TreeItem is of classType enum, interface or class
            else {
                //TODO: fixed renaming mechanism for files for now, still need to figure out why files are getting multiplied have not really fixed it yet
                GridPaneNIO.textAreaStringHashMap.forEach((k, v) -> {
                            if (GridPaneNIO.textAreaStringHashMap.get(k).equals(treeItem.getValue().getLabelText())) {
                                String oldName = treeItem.getValue().getLabelText();
                                treeItem.getValue().setBoxText(textField.getText());
                                Path source = Paths.get(treeItem.getValue().getPath());
                                changeClassContent(treeItem, textField.getText(), oldName);
//                                treeItem.getValue().getTextArea().setText(treeItem.getValue().getTextArea().getText()
//                                        .replaceAll(oldName, textField.getText()));

                                try {
                                    Files.move(source, source.resolveSibling(textField.getText() + ".java"));

                                } catch (IOException ex) {
                                    if (!(ex instanceof NoSuchFileException))
                                        ex.printStackTrace();
                                }
                            }
                        }
                );
            }

            window.close();

        });
    }

    private static void changeClassContent(TreeItem<CustomItem> treeItem, String newName, String oldName) {


        treeItem.getValue().getTextArea().setText(treeItem.getValue().getTextArea().getText()
                .replaceAll(oldName, newName));
        GridPaneNIO.updateFile(treeItem.getValue().getTextArea().getText(), treeItem.getValue().getPath());


    }

}
