package app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        button.setOnAction(e -> {

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
                    GridPaneNIO.findFilesRec(new File(GridPaneNIO.path));
                    GridPaneNIO.listFiles.forEach(f -> {
                        System.out.println(f.getName().replaceAll(".java", ""));
                        if (f.getName().replaceAll(".java", "").equals(className)) {
                            Path source = Paths.get(f.getPath());
                            try {
                                Files.move(source, source.resolveSibling(textField.getText()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }

            window.close();

    });
    }

}
