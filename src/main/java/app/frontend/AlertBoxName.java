package app.frontend;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

public class AlertBoxName {

    public static void display(String className) {
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "A file with the name: " + className + " already exists, please choose a different name", ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }


}
