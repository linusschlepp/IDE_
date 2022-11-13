package app.frontend.impl;
import app.backend.ClassType;
import app.backend.CustomItem;
import app.exceptions.IDEException;
import app.frontend.api.IBox;
import app.utils.CommandUtils;
import app.utils.Constants;
import app.utils.FrontendConstants;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


import static app.backend.ClassType.*;


/**
 * Default-implementation of {@link IBox}. Gets displayed, if new classes/ packages are added to the TreeView
 * Enables user to select name location of the file (package-hierarchy)
 */
public class ClassBox implements IBox {

    public static String defaultValue;
    public static boolean isSelected = false;
    private ComboBox<String> comboBox = new ComboBox<>();
    public Logger LOG = LoggerFactory.getLogger(ClassBox.class);

    private boolean isPackage;

    private ClassType classType;

    private String className;


    public ClassBox(final TreeItem<CustomItem> treeItem) {
        this.classType = treeItem.getValue().getClassType();
        this.isPackage = treeItem.getValue().isPackage();
    }

    //TODO: Pass variables from above in constructor and don't make them static


    @Override
    public void display() {

        if (!FrontendConstants.path.isEmpty()) {

            CheckBox checkBox = new CheckBox(Constants.IS_MAIN);
            GridPane grdPane = new GridPane();
            grdPane.setPadding(new Insets(8, 8, 8, 8));
            grdPane.setVgap(8);
            grdPane.setHgap(10);
            Stage window = new Stage();
            window.setTitle(Constants.ADD + Constants.SPACE_STRING + this.classType);
            Label label = new Label(Constants.ENTER_NAME + Constants.SPACE_STRING + this.classType);
            TextField textField = new TextField();
            textField.setPrefWidth(150);
            textField.setMaxWidth(150);
            Button button = new Button(Constants.OK);
            GridPane.setConstraints(label, 0, 0);
            GridPane.setConstraints(textField, 0, 1);
            if (this.classType.equals(CLASS)) {
                GridPane.setConstraints(checkBox, 0, 2);
                grdPane.getChildren().add(checkBox);
            }
            //if no packages exist, the dropdown will not be displayed
            if (FrontendConstants.packageNameHashMap.isEmpty()) {
                GridPane.setConstraints(button, 0, 3);
                grdPane.getChildren().addAll(label, textField, button);
                //if packages exist, the dropdown will be filled with the contents of the packageNameHashMap
            } else {
                //comboBox gets initialized everytime when ClassBox is called
                this.comboBox = new ComboBox<>();
                this.comboBox.setValue(defaultValue);
                for (String s : FrontendConstants.packageNameHashMap.keySet())
                    this.comboBox.getItems().add(s);
                Label labelPackage = new Label(Constants.ADD+Constants.SPACE_STRING+classType+
                        Constants.SPACE_STRING+Constants.SPECIFIC_PACKAGE);
                GridPane.setConstraints(labelPackage, 0, 3);
                GridPane.setConstraints(comboBox, 0, 4);
                GridPane.setConstraints(button, 0, 5);
                grdPane.getChildren().addAll(label, textField, labelPackage, comboBox, button);
            }
            this.comboBox.getItems().add(Constants.EMPTY_STRING);
            window.setScene(new Scene(grdPane, 200, 200));
            window.show();
            //checks if the class is Main
            isSelected = checkBox.isSelected();
            className = textField.getText();
            button.setOnAction(e -> {
                try {
                    buttonAction();
                } catch (IDEException ex) {
                    throw new RuntimeException(ex);
                }

            });
            window.close();

        } else {
            //if the path is empty the AlertBox will be displayed
            new AlertBox(Alert.AlertType.WARNING, Constants.CREATE_PROJECT).display();
        }
    }


    /**
     * Helper-method which returns TreeItem, according to its corresponding name
     *
     * @param packageName name of the TreeItem/ package, which has been chosen in the ComboBox above
     * @return corresponding TreeItem to the given name
     */
    private static TreeItem<CustomItem> getRequiredTreeItem(String packageName) {

        for(String s : FrontendConstants.packageNameHashMap.keySet()){
            if (s.equals(packageName)) {
                return FrontendConstants.packageNameHashMap.get(s);
            }
        }

        return null;
    }


    @Override
    public void buttonAction() throws IDEException {

        String selectedValue = this.comboBox.getValue();
        //Exception needs to be caught, because addToPackage, addPackage and addClass are throwing IOExceptions
        try {
            if (selectedValue != null && !Objects.requireNonNull(selectedValue).isEmpty()) {
                CommandUtils.addToPackage(selectedValue, this.className, this.classType,
                        new File(FrontendConstants.path + CommandUtils.getCorrectPath(Objects
                                .requireNonNull(getRequiredTreeItem(selectedValue))) + Constants.FILE_SEPARATOR + selectedValue));
            } else if (!this.isPackage) {
                //if isPackage is false, we just add a Class to the filesystem as well as to the TreeView
                CommandUtils.addClass(this.className, this.classType);
            } else {
                // if isPackage is true, we add a Package to the filesystem as well as to the TreeView
                CommandUtils.addPackage(this.className, new File(FrontendConstants.path +
                        Constants.FILE_SEPARATOR + this.className));
            }
        } catch (final IOException | IDEException ex) {
            new IDEException("[{}] [{}] could not be added", this.classType,
                    selectedValue).throwWithLogging(LOG);
        }

        defaultValue = Constants.EMPTY_STRING;

    }
}
