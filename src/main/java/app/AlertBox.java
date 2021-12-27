package app;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/**
 * Gets displayed if the project-path is empty
 *
 */
public class AlertBox {

    public static void display(){
        Alert alert = new Alert(Alert.AlertType.WARNING, "Create project first", ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }


}