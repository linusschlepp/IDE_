package app.frontend;

import app.backend.ClassType;
import app.backend.CustomItem;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class DeleteBox {


    public static void display(TreeItem<CustomItem> treeItem) {

        Stage window = new Stage();
        window.setTitle("Delete " + treeItem.getValue().getLabelText());
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label("Are you sure that you want to delete: " + treeItem.getValue().getLabelText());
        Button button = new Button("Ok");
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(button, 0, 5);
        grdPane.getChildren().addAll(label, button);
        window.setScene(new Scene(grdPane, 300, 300));
        window.show();

        button.setOnAction(e -> {

            try {
                if (treeItem.getValue().getClassType().equals(ClassType.PACKAGE)) {
                    Files.walk(Paths.get(treeItem.getValue().getPath())).sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                } else {
                    String tempPath = treeItem.getValue().getPath();
                    if (!tempPath.contains(".java"))
                        tempPath += ".java";

                    if (Files.deleteIfExists(Paths.get(tempPath)))
                        Files.delete(Paths.get(tempPath));

                }


            } catch (IOException ex) {
                if (!(ex instanceof NoSuchFileException))
                    ex.printStackTrace();
            } finally {
                treeItem.getParent().getChildren().remove(treeItem);
            }
            window.close();
        });


    }
}
