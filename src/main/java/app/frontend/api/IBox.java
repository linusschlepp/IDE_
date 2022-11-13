package app.frontend.api;

import app.backend.CustomItem;
import app.exceptions.IDEException;
import javafx.scene.control.TreeItem;

public interface IBox {


    public void display();

    void buttonAction() throws IDEException;



}
