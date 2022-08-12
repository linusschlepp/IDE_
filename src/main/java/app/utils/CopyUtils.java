package app.utils;

import app.frontend.GridPaneNIO;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

import static app.backend.ClassType.*;
import static app.backend.ClassType.PACKAGE;

public class CopyUtils {

    /**
     * Helper-method, which copies MenuItems. This enables the addition of one MenuItem to multiple ContextMenus
     *
     * @param menuItem menuItem, which is getting copied
     * @return copy of menuItem
     */
    public static MenuItem copyMenuItem(MenuItem menuItem) {

        MenuItem menuItemCopy = new MenuItem();
        menuItemCopy.setText(menuItem.getText());
        menuItemCopy.setGraphic(copyImageMenuItem(menuItemCopy));
        menuItemCopy.setOnAction(menuItem.getOnAction());

        return menuItemCopy;
    }



    /**
     * Helper-method, which returns the Image of the corresponding MenuItem
     *
     * @param menuItem the menuItem of which the image is required
     * @return a copy of the ImageView from menuItem
     */
    public static ImageView copyImageMenuItem(MenuItem menuItem) {

        ImageView imageView = new ImageView();
        switch (menuItem.getText()) {
            case "Delete" -> imageView = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/terminateIcon.png"))));
            case "Add Interface" -> imageView = INTERFACE.getImage();
            case "Add Class" -> imageView = CLASS.getImage();
            case "Add Enum" -> imageView = ENUM.getImage();
            case "Add Package" -> imageView = PACKAGE.getImage();
            case "Rename" -> imageView = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/renameIcon.png"))));

        }
        imageView.setFitHeight(17);
        imageView.setPreserveRatio(true);

        return imageView;

    }


}
