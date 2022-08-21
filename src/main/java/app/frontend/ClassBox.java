package app.frontend;
import app.backend.ClassType;
import app.backend.CustomItem;
import app.utils.Constants;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


import static app.backend.ClassType.*;


/**
 * Gets displayed, if new classes/ packages are added to the TreeView
 * Enables user to select name location of the file (package-hierarchy)
 */
public class ClassBox {

    static String defaultValue;
    static boolean isSelected = false;
    static ComboBox<String> comboBox = new ComboBox<>();

    public static void display(ClassType classKind, boolean isPackage) {


        if (!GridPaneNIO.path.isEmpty()) {

            CheckBox checkBox = new CheckBox(Constants.IS_MAIN);

            GridPane grdPane = new GridPane();
            grdPane.setPadding(new Insets(8, 8, 8, 8));
            grdPane.setVgap(8);
            grdPane.setHgap(10);
            Stage window = new Stage();
            window.setTitle(Constants.ADD + Constants.SPACE_STRING + classKind.getClassType());
            Label label = new Label(Constants.ENTER_NAME +Constants.SPACE_STRING + classKind.getClassType());
            TextField textField = new TextField();
            textField.setPrefWidth(150);
            textField.setMaxWidth(150);
            Button button = new Button(Constants.OK);
            GridPane.setConstraints(label, 0, 0);
            GridPane.setConstraints(textField, 0, 1);
            if (classKind.equals(CLASS)) {
                GridPane.setConstraints(checkBox, 0, 2);
                grdPane.getChildren().add(checkBox);
            }
            //if no packages exist, the dropdown will not be displayed
            if (GridPaneNIO.packageNameHashMap.isEmpty()) {
                GridPane.setConstraints(button, 0, 3);
                grdPane.getChildren().addAll(label, textField, button);
                //if packages exist, the dropdown will be filled with the contents of the packageNameHashMap
            } else {
                //comboBox gets initialized everytime when ClassBox is called
                comboBox = new ComboBox<>();
                comboBox.setValue(defaultValue);
                for (String s : GridPaneNIO.packageNameHashMap.keySet())
                    comboBox.getItems().add(s);
                Label labelPackage = new Label(Constants.ADD+Constants.SPACE_STRING+classKind.getClassType()+
                        Constants.SPACE_STRING+Constants.SPECIFIC_PACKAGE);
                GridPane.setConstraints(labelPackage, 0, 3);
                GridPane.setConstraints(comboBox, 0, 4);
                GridPane.setConstraints(button, 0, 5);
                grdPane.getChildren().addAll(label, textField, labelPackage, comboBox, button);
            }
            comboBox.getItems().add(Constants.EMPTY_STRING);
            window.setScene(new Scene(grdPane, 200, 200));
            window.show();

            button.setOnAction(e -> {
                //checks if the class is Main
                isSelected = checkBox.isSelected();
                String selectedValue = comboBox.getValue();
                //Exception needs to be caught, because addToPackage, addPackage and addClass are throwing IOExceptions
                try {
                    if (selectedValue != null && !Objects.requireNonNull(selectedValue).isEmpty())

                        GridPaneNIO.addToPackage(selectedValue, textField.getText(), classKind,
                                new File(GridPaneNIO.path + GridPaneNIO.getCorrectPath(Objects
                                        .requireNonNull(getRequiredTreeItem(selectedValue)))+ Constants.FILE_SEPARATOR+selectedValue));
                    else if (!isPackage)
                        //if isPackage is false, we just add a Class to the filesystem as well as to the TreeView
                        GridPaneNIO.addClass(textField.getText(), classKind);
                    else
                        // if is Package is true, we add a Package to the filesystem as well as to the TreeView
                        GridPaneNIO.addPackage(textField.getText(), new File(GridPaneNIO.path +
                                Constants.FILE_SEPARATOR + textField.getText()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                window.close();
                defaultValue = Constants.EMPTY_STRING;
            });
            //if the path is empty the AlertBox will be displayed
        } else
            AlertBox.display(Alert.AlertType.WARNING, Constants.CREATE_PROJECT);
    }


    /**
     * Helper-method which returns TreeItem, according to its corresponding name
     *
     * @param packageName name of the TreeItem/ package, which has been chosen in the ComboBox above
     * @return corresponding TreeItem to the given name
     */
    private static TreeItem<CustomItem> getRequiredTreeItem(String packageName) {

        for(String s : GridPaneNIO.packageNameHashMap.keySet()){
            if (s.equals(packageName))
                return GridPaneNIO.packageNameHashMap.get(s);
        }


        return null;

    }
}
