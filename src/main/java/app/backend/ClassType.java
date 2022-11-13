package app.backend;

import app.frontend.impl.FrontendInit;
import app.utils.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

/**
 * Returns the image and name of the individual instances of the TextArea and FileSystem in a type-safe manner
 *
 */
public enum ClassType {


    PACKAGE {
        @Override
        public String getClassType() {
            return Constants.PACKAGE_STRING;
        }
        @Override
        public ImageView getImage() {
            return new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/packageIcon.png"))));
        }
    },
    CLASS{
        @Override
        public String getClassType() {
            return Constants.CLASS_STRING;
        }
        @Override
        public ImageView getImage() {
            return new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/classIcon.png"))));
        }
    },
    ENUM{
        @Override
        public String getClassType() {
            return Constants.ENUM_STRING;
        }

        @Override
        public ImageView getImage() {
            return new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/enumIcon.png"))));
        }
    },
    INTERFACE{
        @Override
        public String getClassType() {
            return Constants.INTERFACE_STRING;
        }

        @Override
        public ImageView getImage() {
            return new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/interfaceIcon.png"))));
        }
    },
    PROJECT{

        @Override
        public String getClassType(){
            return Constants.PROJECT_STRING;
        }

        @Override
        public ImageView getImage(){
            return new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/projectIcon.png"))));
        }
    };
    //returns the type of class (class, enum, project, interface, package)  as a String
    public abstract String getClassType();
    //returns corresponding image for class-type
    public abstract ImageView getImage();
}