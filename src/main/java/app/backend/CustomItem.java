package app.backend;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Represents the layout of the classes and packages within the TreeView
 *
 */
public class CustomItem extends GridPane {

    private final Label boxText;
    private TextArea textArea;
    private final ImageView imageView;
    private String path;
    private final ClassType classType;

    private final boolean isPackage;


    /**
     * 1st Constructor
     *
     * @param imageView corresponding image of class, package, enum or interface
     * @param boxText name of class/ package
     */
    public CustomItem(ImageView imageView, Label boxText, ClassType classType, String path, boolean isPackage) {
        this.imageView = imageView;
        this.boxText = boxText;
        this.classType = classType;
        this.path = path;
        this.isPackage = isPackage;


        imageView.setFitHeight(15);
        imageView.setPreserveRatio(true);
        this.getChildren().addAll(boxText, imageView);
        setConstraints(boxText, 1, 0);
        setConstraints(imageView, 0, 0);

    }

    /**
     * 2nd Constructor
     *
     * @param imageView corresponding image of class, package, enum or interface
     * @param boxText name of class/ package
     * @param textArea textArea corresponding to interface, class or enum
     * @param path path of the instances within the filesystem
     */
    public CustomItem(ImageView imageView, Label boxText, TextArea textArea, String path, ClassType classType, boolean isPackage){
        this.imageView = imageView;
        this.boxText = boxText;
        this.textArea = textArea;
        this.path = path;
        this.classType = classType;
        this.isPackage = isPackage;

        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        this.getChildren().addAll(imageView, boxText);

        setConstraints(imageView,0, 0, 1, 1);
        setConstraints(boxText, 1, 0, 1, 1);


    }

    /**
     * Returns name of CustomItem
     *
     * @return Name of CustomItem
     */
    public String getName() {
        return boxText.getText();
    }

    /**
     * Returns the individual name, storing the name of the CustomItem
     *
     * @return Label of the CustomItem, storing the name
     */
    public Label getLabel() {
        return boxText;
    }

    /**
     * Updates the name of CustomItem in form of a Label
     *
     * @param text New name of CustomItem
     */
    public void setBoxText(String text){
        this.boxText.setText(text);
    }


    /**
     * Returns the content of the customItem in form of a TextArea
     *
     * @return TextArea of this CustomItem
     */
    public TextArea getTextArea(){
        return textArea;
    }

    /**
     * Returns the classType of this customItem
     *
     * @return ClassType of this CustomItem
     */
    public ClassType getClassType() {
        return classType;
    }

    /**
     * Sets new context of customItem
     *
      * @param text New content of CustomItem
     */
    public void setText(String text){
        textArea.setText(text);
    }

    /**
     * Returns path of CustomItem
     *
     * @return Path of custom item
     */
    public String getPath(){
        return path;
    }

    /**
     * Updates path of the CustomItem within file-structure
     *
     * @param path New paht of this CustomItem
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Returns if CustomItem is package or not
     *
     * @return True if customItem is a package, false if not
     */
    public boolean isPackage() {
        return isPackage;
    }
}