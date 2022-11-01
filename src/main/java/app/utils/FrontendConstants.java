package app.utils;

import app.backend.CustomItem;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Constants, containing important objects for building the frontend
 */
public final class FrontendConstants {


    private FrontendConstants() {
        // Private-constructor as this is Constants-class
    }

    public static Stage primaryStage;

    public static Label label;

    public static TreeItem<CustomItem> retTreeItem;

    public static final StringBuilder sb = new StringBuilder();

    public static TextFlow textFlow = new TextFlow();

    public static String path = Constants.EMPTY_STRING;

    public static HashMap<String, TreeItem<CustomItem>> packageNameHashMap = new HashMap<>();

    public static HashMap<TextArea, String> textAreaStringHashMap = new HashMap<>();

    public static TreeView<CustomItem> treeView = new TreeView<>();

    public static Menu menuClose = new Menu();

    public static MenuItem menuItemClose1 = new MenuItem("Close Program");

    public static Menu menuExecute = new Menu();

    public static MenuItem menuItemExec1 = new MenuItem("Execute Program");

    public static Menu menuAdd = new Menu();

    public static MenuItem menuItemAddClass = new MenuItem("Add Class");

    public static MenuItem menuItemAddInterface = new MenuItem("Add Interface");

    public static MenuItem menuItemAddEnum = new MenuItem("Add Enum");

    public static MenuItem menuItemAddPackage = new MenuItem("Add Package");

    public static MenuItem menuItemAddProject = new MenuItem("New Project");

    public static MenuItem menuItemSelectProject = new MenuItem("Select Project");

    public static MenuItem menuItemDelete = new MenuItem("Delete");

    public static MenuItem menuItemRename = new MenuItem("Rename");

    public static MenuItem menutItemAddGit = new MenuItem("Add");

    public static Menu menuGit = new Menu();

    public static MenuItem menuItemInit = new MenuItem("Create Repository");

    public static MenuItem menuItemCommit = new Menu("Create Commit");

    public static GridPane gridPane = new GridPane();

    public static TreeItem<CustomItem> TreeItemProject;

    public static String fileName = Constants.EMPTY_STRING;

    public static TextArea textArea = new TextArea();

    public static List<File> listFiles = new ArrayList<>();

    public static ContextMenu contextMenuPackages;

    public static ContextMenu contextMenuClasses;



}
