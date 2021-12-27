package app;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static app.GridPaneNIO.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public enum ClassType {


    PACKAGE {
        @Override
        public String getClassType() {
            return "package";
        }
        @Override
        public ImageView getImage() {
            try {
                return  new ImageView(new Image(new FileInputStream(getRelativePath() +File.separator + "pictures/packagepicture.png")));
            }
            catch (IOException e){
                return null;
            }
        }
    },
    CLASS{
        @Override
        public String getClassType() {
            return "class";
        }
        @Override
        public ImageView getImage() {
            try {
                return  new ImageView(new Image(new FileInputStream(getRelativePath() +  File.separator + "pictures/classpicture.png")));
            }
            catch (IOException e){
                return null;
            }
        }
    },
    ENUM{
        @Override
        public String getClassType() {
            return "enum";
        }

        @Override
        public ImageView getImage() {
            try {
                return  new ImageView(new Image(new FileInputStream(getRelativePath() +  File.separator + "pictures/enumpicture.png")));
            }
            catch (IOException e){
                return null;
            }
        }
    },
    INTERFACE{
        @Override
        public String getClassType() {
            return "interface";
        }

        @Override
        public ImageView getImage() {
            try {
                return  new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator +  "pictures/interfacepicture.png")));
            }
            catch (IOException e){
                return null;
            }
        }
    },
    PROJECT{

        @Override
        public String getClassType(){
            return "project";
        }

        @Override
        public ImageView getImage(){
            try {
                return  new ImageView(new Image(new FileInputStream(getRelativePath() +  File.separator + "pictures/projectpicture.png")));
            }
            catch (IOException e){
                return null;
            }
        }
    };
    //returns the type of class as String
    public abstract String getClassType();
    //returns corresponding image for class-type
    public abstract ImageView getImage();
}