package app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class DeleteBox {


    public static void display(String className, String filePath, ClassType classType) {

        Stage window = new Stage();
        window.setTitle("Delete " + className);
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label("Are you sure that you want to delete: " + className);
        Button button = new Button("Ok");
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(button, 0, 5);
        grdPane.getChildren().addAll(label, button);
        window.setScene(new Scene(grdPane, 300, 300));
        window.show();

        button.setOnAction(e -> {

            try {
                if(classType.equals(ClassType.PACKAGE)){
                    Files.walk(Paths.get(filePath)).sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                }
                else {
                    if (Files.deleteIfExists(Paths.get(filePath)))
                        Files.delete(Paths.get(filePath));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        window.close();

    }
}
