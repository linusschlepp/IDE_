package app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This dialog-box gets shown if files/ directories have to be renamed
 *
 */
public class RenameBox {

    public static void display(String className, ClassType classType) {


        Stage window = new Stage();
        window.setTitle("Rename " + className);
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label("Enter new name for: " + className);
        TextField textField = new TextField("Enter new name of " + classType.getClassType());
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
            if(classType.equals(ClassType.PACKAGE)) {
                //Searches in the HashMap and changes value (TreeItem) Label to the new input of the user
                GridPaneNIO.packageNameHashMap.forEach((k, v) -> {
                    if (k.equals(className)) {
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
            //if object to rename is class/ enum or interface
            else{
                //TODO: This does not work, maybe implement HashTree in GridPaneNIO which contains all single files and then implement same logic as for directories
                GridPaneNIO.TreeItemProject.getChildren().forEach(l ->{
                            System.out.println(l.getValue().getLabelText());
                   if( l.getValue().getLabelText().equals(className)){
                       Path source = Paths.get(l.getValue().getPath());
                            try {
                                Files.move(source, source.resolveSibling(textField.getText()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                   }

                        );
                }

            window.close();

    });
    }
}
