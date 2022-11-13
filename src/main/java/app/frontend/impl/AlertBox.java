package app.frontend.impl;

import app.frontend.api.IAlertBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/**
 * Gets displayed if the user has to be alerted in some way
 */
public class AlertBox implements IAlertBox {

    private final Alert.AlertType alertType;

    private final String errorMessage;

    public AlertBox(final Alert.AlertType alertType, final String errorMessage) {
        this.alertType = alertType;
        this.errorMessage = errorMessage;

    }

    @Override
    public void display(){
        Alert alert = new Alert(alertType, errorMessage, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
}