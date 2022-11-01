package app.frontend;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/**
 * Gets displayed if the user has to be alerted in some way
 */
public class AlertBox {

    private AlertBox() {
        // Private-Constructor to hide implicit, default constructor
    }

    public static void display(final Alert.AlertType alertType, final String message){
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
}