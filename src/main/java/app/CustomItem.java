package app;
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


    /**
     * 1st Constructor
     *
     * @param imageView corresponding image of class, package, enum or interface
     * @param boxText name of class/ package
     */
    public CustomItem(ImageView imageView, Label boxText) {
        this.imageView = imageView;
        this.boxText = boxText;


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
    public CustomItem(ImageView imageView, Label boxText, TextArea textArea, String path){
        this.imageView = imageView;
        this.boxText = boxText;
        this.textArea = textArea;
        this.path = path;

        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        this.getChildren().addAll(imageView, boxText);

        setConstraints(imageView,0, 0, 1, 1);
        setConstraints(boxText, 1, 0, 1, 1);


    }

    public Label getBoxText() {
        return boxText;
    }


    public TextArea getTextArea(){

        return textArea;
    }




    public String getLabelText(){
        return boxText.getText();
    }


    public ImageView getImage(){
        return imageView;
    }



    public void setText(String text){
        textArea.setText(text);
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}