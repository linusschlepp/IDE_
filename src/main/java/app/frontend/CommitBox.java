package app.frontend;

import app.utils.GitUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;


public class CommitBox {


    public static void display(String path) {


        Stage window = new Stage();
        window.setTitle("Enter commit message");
        GridPane grdPane = new GridPane();
        grdPane.setPadding(new Insets(8, 8, 8, 8));
        Label label = new Label("Enter commit message");
        TextField textField = new TextField("Enter commit message");
        textField.setPrefWidth(300);
        textField.setMaxWidth(300);
        Button button = new Button("Ok");
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(textField, 0, 2);
        GridPane.setConstraints(button, 0, 5);
        grdPane.getChildren().addAll(label, textField, button);
        window.setScene(new Scene(grdPane, 300, 300));
        window.show();

        button.setOnAction( e-> {
            try {
                GitUtils.gitCommit(textField.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            window.close();

        });

    }


}
