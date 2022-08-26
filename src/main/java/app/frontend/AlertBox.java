package app.frontend;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/**
 * Gets displayed if the user has to be alerted in some way
 *
 */
public class AlertBox {

    public static void display(Alert.AlertType alertType, String message){
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
}