package app.frontend;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/**
 * Gets displayed if the user has to be alerted in some way
 *
 */
public class AlertBox {

    public static void display(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }


}