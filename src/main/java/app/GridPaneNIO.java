package app;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static app.ClassType.*;

//TODO: Main objectives: prepare program for .jar

public class GridPaneNIO {

    static Stage primaryStage;

    static Label label;
    TreeItem<CustomItem> retTreeItem;
    static StringBuilder sb = new StringBuilder();
    static TextFlow textFlow = new TextFlow();
    static String path = "";
    static HashMap<String, TreeItem<CustomItem>> packageNameHashMap = new HashMap<>();
    static HashMap<TextArea, String> textAreaStringHashMap = new HashMap<>();
    static TreeView<CustomItem> treeView = new TreeView<>();
    Menu menuClose = new Menu();
    MenuItem menuItemClose1 = new MenuItem("Close Program");
    Menu menuExecute = new Menu();
    MenuItem menuItemExec1 = new MenuItem("Execute Program");
    Menu menuAdd = new Menu();
    MenuItem menuItemAddClass = new MenuItem("Add Class");
    MenuItem menuItemAddInterface = new MenuItem("Add Interface");
    MenuItem menuItemAddEnum = new MenuItem("Add Enum");
    MenuItem menuItemAddPackage = new MenuItem("Add Package");
    MenuItem menuItemAddProject = new MenuItem("New Project");
    MenuItem menuItemSelectProject = new MenuItem("Select Project");
    MenuItem menuItemDelete = new MenuItem("Delete");
    MenuItem menuItemRename = new MenuItem("Rename");
    static GridPane gridPane = new GridPane();
    static TreeItem<CustomItem> TreeItemProject;
    static String fileName = "";
    static TextArea textArea = new TextArea();
    static List<File> listFiles = new ArrayList<>();
    ContextMenu contextMenuPackages;
    ContextMenu contextMenuClasses;

    /**
     * Constructor of class: GridPaneNIO
     * Calls method recreateProject
     *
     * @param primaryStage mainStage, which is getting passed by the main class
     */
    GridPaneNIO(Stage primaryStage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        this.primaryStage = primaryStage;
        this.primaryStage.setX(bounds.getMinX());
        this.primaryStage.setX(bounds.getMinY());
        this.primaryStage.setWidth(bounds.getWidth());
        this.primaryStage.setHeight(bounds.getHeight());
        recreateProject();
    }

    /**
     * Creates the Stage as well as the layout
     *
     * @throws FileNotFoundException because new files are initialized
     */
    public void start() throws FileNotFoundException {


        menuExecute.getItems().add(menuItemExec1);
        menuAdd.getItems().addAll(menuItemAddClass, menuItemAddInterface, menuItemAddEnum, menuItemAddPackage, menuItemAddProject, menuItemSelectProject);
        menuClose.getItems().add(menuItemClose1);

        primaryStage.setTitle("Linus IDE");


        ImageView viewMenu = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/greenplay.png")));
        menuExecute.setGraphic(viewMenu);
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);
        menuItemExec1.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        menuItemExec1.setOnAction(e -> execute());

        viewMenu = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/terminate.png")));
        menuClose.setGraphic(viewMenu);
        menuItemClose1.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
        menuItemClose1.setOnAction(e -> primaryStage.close());
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);
        ImageView viewMenuItem = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/renameicon.png")));
        menuItemRename.setGraphic(viewMenuItem);
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);

        menuItemRename.setOnAction(e -> RenameBox.display(getRetTreeItem()));


        viewMenu = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/terminate.png")));
        menuItemDelete.setGraphic(viewMenu);
        menuItemDelete.setOnAction(e -> DeleteBox.display(getRetTreeItem()));
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);
        viewMenuItem = CLASS.getImage();
        viewMenu = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/plus.png")));
        viewMenuItem.setFitHeight(30);
        viewMenuItem.setPreserveRatio(true);
        menuAdd.setGraphic(viewMenu);
        menuItemAddClass.setAccelerator(KeyCombination.keyCombination("Ctrl+K"));
        menuItemAddClass.setOnAction(e -> ClassBox.display(CLASS, false));
        menuItemAddClass.setGraphic(viewMenuItem);
        viewMenuItem = INTERFACE.getImage();
        viewMenuItem.setFitHeight(30);
        viewMenuItem.setPreserveRatio(true);
        menuItemAddInterface.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
        menuItemAddInterface.setGraphic(viewMenuItem);
        menuItemAddInterface.setOnAction(e -> ClassBox.display(INTERFACE, false));
        viewMenuItem = ENUM.getImage();
        viewMenuItem.setFitHeight(30);
        viewMenuItem.setPreserveRatio(true);
        contextMenuPackages = new ContextMenu();
        contextMenuClasses = new ContextMenu();
        menuItemAddEnum.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        menuItemAddEnum.setGraphic(viewMenuItem);
        menuItemAddEnum.setOnAction(e -> ClassBox.display(ENUM, false));
        viewMenuItem = PACKAGE.getImage();
        viewMenuItem.setFitHeight(17);
        viewMenuItem.setPreserveRatio(true);
        menuItemAddPackage.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        menuItemAddPackage.setGraphic(viewMenuItem);
        menuItemAddPackage.setOnAction(e -> ClassBox.display(PACKAGE, true));
        viewMenu.setFitHeight(20);
        label = new Label();
        viewMenu.setPreserveRatio(true);
        menuItemAddProject.setOnAction(e -> {
            try {
                addProject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        viewMenuItem = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/projectpicture.png")));
        viewMenuItem.setFitHeight(17);
        viewMenuItem.setPreserveRatio(true);
        menuItemAddProject.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        menuItemAddProject.setGraphic(viewMenuItem);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuAdd, menuExecute, menuClose);
        gridPane.add(menuBar, 0, 0);
        GridPane.setColumnSpan(menuBar, 2);
        gridPane.add(textFlow, 0, 1);
        GridPane.setColumnSpan(textFlow, 2);
        gridPane.add(treeView, 0, 2);
        gridPane.add(label, 1, 0);
        GridPane.setRowSpan(label, 1);
        gridPane.add(textArea, 1, 2);
        Scene scene = new Scene(gridPane, 800, 800);
        menuItemSelectProject.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        menuItemSelectProject.setOnAction(e -> {
            try {
                selectProject();
            } catch (FileNotFoundException ignored) {

            }
        });
        viewMenuItem = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/selectprojecticon.png")));
        viewMenuItem.setFitHeight(17);
        viewMenuItem.setPreserveRatio(true);
        menuItemSelectProject.setGraphic(viewMenuItem);
        //items get added to contextMenuPackages

        AtomicReference<TreeItem<CustomItem>> tempTreeItem = new AtomicReference<>();
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //to avoid the addition of too many menuItems within the contextMenu
            if (contextMenuPackages.getItems().size() == 0 && contextMenuClasses.getItems().size() == 0) {
                //menuItems get added to contextMenuPackages
                contextMenuPackages.getItems().addAll(copyMenuItem(menuItemAddClass), copyMenuItem(menuItemAddInterface), copyMenuItem(menuItemAddEnum),
                        copyMenuItem(menuItemAddPackage), copyMenuItem(menuItemRename), copyMenuItem(menuItemDelete));
                //menuItems get added to contextMenuClasses
                contextMenuClasses.getItems().addAll(copyMenuItem(menuItemRename), copyMenuItem(menuItemDelete));
            }
            if (newValue != null) {
                tempTreeItem.set(newValue);
                if (newValue.getValue().getTextArea() != null)
                    textArea.setText(newValue.getValue().getTextArea().getText());
            }
            //right-click on TreeItem
            tempTreeItem.get().getValue().getBoxText().setOnContextMenuRequested(e -> {
                //if TreeItem corresponds to project or package

                setRetTreeItem(tempTreeItem.get());
                if (tempTreeItem.get().getValue().getClassType().equals(PACKAGE) ||
                        tempTreeItem.get().getValue().getClassType().equals(PROJECT)) {
                    contextMenuPackages.show(tempTreeItem.get().getValue(), e.getScreenX(), e.getScreenY());
                    if (tempTreeItem.get().getValue().getClassType().equals(PACKAGE))
                        ClassBox.defaultValue = tempTreeItem.get().getValue().getLabelText();
                }
                // if TreeItem corresponds to class
                else
                    contextMenuClasses.show(tempTreeItem.get().getValue(), e.getScreenX(), e.getScreenY());

            });
        });


        /*
        everytime the text of textArea of a class is getting changed,
         the content in the file is getting updated as well
         */
        textArea.textProperty().addListener((observableValue, s, t1) -> {
            tempTreeItem.get().getValue().setText(t1);
            updateFile(tempTreeItem.get().getValue().getTextArea().getText(), tempTreeItem.get().getValue().getPath());
            setImageLabel(tempTreeItem.get());
            label.setText(tempTreeItem.get().getValue().getLabelText());
        });

        scene.getStylesheets().add("styles/style.css");

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Helper-method which copies ImageViews for the label
     *
     * @param treeItem represents treeItem, which image is getting copied
     */
    private static void setImageLabel(TreeItem<CustomItem> treeItem) {

        ImageView imageView;
        try {

            if (treeItem.getValue().getClassType().equals(PACKAGE))
                imageView = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/packagepicture.png")));
            else if (treeItem.getValue().getClassType().equals(CLASS))
                imageView = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/classpicture.png")));
            else if (treeItem.getValue().getClassType().equals(INTERFACE))
                imageView = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/interfacepicture.png")));
            else
                imageView = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/enumpicture.png")));
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            label.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * Helper-method, which copies MenuItems. This enables the addition of one MenuItem to multiple ContextMenus
     *
     * @param menuItem menuItem, which is getting copied
     * @return copy of menuItem
     */
    private static MenuItem copyMenuItem(MenuItem menuItem) {

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
    private static ImageView copyImageMenuItem(MenuItem menuItem) {

        ImageView imageView = new ImageView();
        try {
            switch (menuItem.getText()) {
                case "Delete" -> imageView = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/terminate.png")));
                case "Add Interface" -> imageView = INTERFACE.getImage();
                case "Add Class" -> imageView = CLASS.getImage();
                case "Add Enum" -> imageView = ENUM.getImage();
                case "Add Package" -> imageView = PACKAGE.getImage();
                case "Rename" -> imageView = new ImageView(new Image(new FileInputStream(getRelativePath() + File.separator + "pictures/renameicon.png")));

            }
            imageView.setFitHeight(17);
            imageView.setPreserveRatio(true);

            return imageView;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new ImageView();
    }

    /**
     * Returns the path, where the IDE is stored
     *
     * @return relativePath of the IDE
     */
    public static String getRelativePath() {

        //  return new File("").getAbsolutePath() + "//" + "src" + "//" + "main" + "//" + "java";
//TODO: Issue with relativePath needs to be resolved for creating .jar file
        return "C:\\Intellij-Projekte\\IDE_\\src\\main\\java";
    }

    /**
     * When the text in the textArea of each class changes, the content of the file is getting changed as well
     *
     * @param fileContent content of the file/ class
     * @param pathOfFile  the location of the file
     */
    static void updateFile(String fileContent, String pathOfFile) {


        Path newPath;
        newPath = pathOfFile.contains(".java") ? Paths.get(pathOfFile) : Paths.get(pathOfFile + ".java");
        try {
            Files.writeString(newPath, fileContent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Opens File-Explorer and selects location of the project
     *
     * @throws FileNotFoundException NIO-codesegments are getting used
     * @throws NullPointerException  when no project path is getting chosen
     */
    private static void selectProject() throws FileNotFoundException {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        try {
            textAreaStringHashMap = new HashMap<>();
            packageNameHashMap = new HashMap<>();
            listFiles.clear();
            path = directoryChooser.showDialog(primaryStage) != null ? directoryChooser.showDialog(primaryStage).getPath() : path;
            fileName = directoryChooser.showDialog(primaryStage) != null ? directoryChooser.showDialog(primaryStage).getName() : fileName;

            Files.writeString((Paths.get(getRelativePath() + File.separator +
                    "projectfiles" + File.separator + "currentProject")), path);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        textFlow = new TextFlow();
        Text projectText = new Text("- " + fileName + " ");
        projectText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        projectText.setFill(Color.BLACK);
        Text pathText = new Text(path);
        pathText.setFill(Color.GRAY);
        pathText.setFont(Font.font("verdana", FontPosture.REGULAR, 15));

        textFlow.getChildren().addAll(projectText, pathText);
        TreeItemProject = new TreeItem<>(new CustomItem(PROJECT.getImage(), new Label(fileName), PROJECT, path));
        treeView.setRoot(TreeItemProject);
        recreateRecProject(new File(path));

    }

    /**
     * Creates files in the requested locations
     *
     * @param classContent content of the class/ file
     * @param className    name of the class/ file
     */
    private static void createFile(String classContent, String className) {
        //TODO: three times File.seperator?, whats about this?
        try {
            if (!Files.exists(Paths.get(path + File.separator + File.separator + File.separator + className + ".java")))
                Files.createFile(Paths.get(path + File.separator + File.separator + File.separator + className + ".java"));
            Files.writeString(Paths.get(path + File.separator + File.separator + File.separator + className + ".java"), classContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets called if a class is only added to the TreeView and not in the fileSystem
     *
     * @param file      file which is getting added
     * @param classKind kind of the class enum, interface etc.
     */
    private static void addClass(File file, ClassType classKind) {
        TextArea tArea = new TextArea(getClassContent(file));
        TreeItem<CustomItem> treeItem = new TreeItem<>(new CustomItem(classKind.getImage(), new Label(file.getName().replaceAll(".java", "")),
                tArea, file.getPath(), classKind));
        TreeItemProject.getChildren().add(treeItem);
        textAreaStringHashMap.put(tArea, file.getName().replaceAll(".java", ""));
    }

    /**
     * Returns the content of a file as a String variable
     *
     * @param file the file, which content is needed
     * @return content of file-parameter as String
     */
    private static String getClassContent(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            Files.lines(Paths.get(String.valueOf(file))).forEach(s -> sb.append(s).append("\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Gets called if a class only needs to be added in a package in the TreeView and not in the fileSystem
     *
     * @param packageName name of the package, in which the class is getting stored
     * @param filePath    path of the individual file
     * @param className   name of the class
     * @param classKind   kind of the class enum, interface etc.
     * @param file        individual file
     */
    public static void addToPackage(String packageName, String filePath, String className, ClassType classKind, File file) {

        TreeItem<CustomItem> treeItem;
        if (classKind.equals(PACKAGE)) {
            treeItem = new TreeItem<>(new CustomItem(classKind.getImage(), new Label(className), PACKAGE, file.getPath()));
            packageNameHashMap.put(className, treeItem);
            packageNameHashMap.get(packageName).getChildren().add(treeItem);
        } else {
            TextArea tArea = new TextArea(getClassContent(file));


            treeItem = new TreeItem<>(new CustomItem(classKind.getImage(), new Label(className),
                    tArea, filePath, classKind));

            textAreaStringHashMap.put(tArea, className);
            packageNameHashMap.get(packageName).getChildren().add(treeItem);
        }

    }

    /**
     * Adds package, but only to the TreeView and not in the file system
     *
     * @param packageName name of the package
     */
    public static void addPackage1(String packageName, File file) {

        TreeItem<CustomItem> treeItem = new TreeItem<>(new CustomItem(PACKAGE.getImage(), new Label(packageName), PACKAGE, file.getPath()));
        packageNameHashMap.put(packageName, treeItem);
        TreeItemProject.getChildren().add(treeItem);

    }

    /**
     * Recreates last used project
     */
    private static void recreateProject() {
        //if the project exists it gets added and recreated
        if (getProjectPath() != null) {
            addProject(getProjectPath());
            recreateRecProject(getProjectPath());
        }

    }

    /**
     * Recreates the Project-structure in the TreeView by the given link
     *
     * @param file represents the project structure or is the project
     */
    private static void recreateRecProject(File file) {

        if (file.isDirectory()) {
            if (!file.getName().equals("output")) {
                File[] entries = file.listFiles();
                if (entries != null) {
                    for (File entry : entries) {
                        if (file.getPath().equals(path)) {
                            if (entry.isDirectory() && !entry.getName().equals("output"))
                                addPackage1(entry.getName(), entry);
                            else if (entry.isFile() && !entry.getName().equals("output"))
                                addClass(entry, checkForClassType(entry));
                        } else {
                            if (entry.isDirectory() && !entry.getName().equals("output"))
                                addToPackage(new File(entry.getParent()).getName(),
                                        entry.getPath(), entry.getName(), PACKAGE, entry);
                            else
                                addToPackage(new File(entry.getParent()).getName(), entry.getPath(),
                                        entry.getName().replaceAll(".java", ""), checkForClassType(entry), entry);
                        }

                        if (entry.isDirectory())
                            recreateRecProject(entry);
                    }
                }
            }
        } else {
            if (file.getPath().equals(path)) {
                addClass(file, checkForClassType(file));
            } else {
                addToPackage(new File(file.getParent()).getName(), file.getPath(),
                        file.getName().replaceAll(".java", ""), checkForClassType(file), file);
            }

        }
    }

    /**
     * Determines the class-type by checking the content of the files for the keywords: enum, interface and class
     *
     * @param entry file, which is getting checked
     * @return the corresponding classType
     */
    private static ClassType checkForClassType(File entry) {

        try {
            String s = Files.lines(Paths.get(entry.getPath())).collect(Collectors.toList()).toString();

            if (s.contains("class"))
                return CLASS;
            if (s.contains("enum"))
                return ENUM;
            if (s.contains("interface"))
                return INTERFACE;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return CLASS;


    }

    /**
     * Writes path of file in currentProject
     *
     * @return File, which corresponds to the first line of the file currentProject
     */
    private static File getProjectPath() {

        try {
            BufferedReader br = new BufferedReader(new FileReader(getRelativePath() + File.separator +
                    "projectfiles" + File.separator + "currentProject"));
            return new File(br.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates/ overwrites the currentProject file
     */
    private static void createProjectFile() {

        try {
            Files.writeString(Paths.get(getRelativePath() + File.separator +
                    "projectfiles" + File.separator + "currentProject"), path);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates Files within packages  in the fileSystem
     *
     * @param classContent content of the class/ file
     * @param packageName  name of the package/ directory
     * @param className    name of the file
     * @param isPackage    checks if file is a package and if a directory has to be created
     */
    private static void createFile(String classContent, String packageName, String className, boolean isPackage) {

        try {
            if (!isPackage) {
                Path newFile = Files.createFile(Paths.get(path + packageName + className + ".java"));
                Files.writeString(newFile, classContent);
            } else {
                if (!Files.exists(Paths.get(path + packageName + className)))
                    Files.createDirectory(Paths.get(path + packageName + className));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens fileDialog window and saves in the project in the requested location
     *
     * @throws IOException NIO code-segments are getting used
     */
    private static void addProject() throws IOException {


        textAreaStringHashMap = new HashMap<>();
        packageNameHashMap = new HashMap<>();
        listFiles.clear();

        FileDialog fd = new FileDialog(new Frame(), "Select the location for your project", FileDialog.SAVE);
        fd.setDirectory("C:\\");
        fd.setVisible(true);
        fileName = fd.getFile() != null ? fd.getFile() : fileName;
        path = fd.getFile() != null && fd.getDirectory() != null ? fd.getDirectory() + fd.getFile() : path;
        if (!Files.exists(Paths.get(path)))
            Files.createDirectory(Paths.get(path));
        createProjectFile();
        Text projectText = new Text("- " + fileName + " ");
        projectText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        projectText.setFill(Color.BLACK);
        Text pathText = new Text(path);
        pathText.setFill(Color.GRAY);
        pathText.setFont(Font.font("verdana", FontPosture.REGULAR, 15));

        textFlow.getChildren().addAll(projectText, pathText);
        TreeItemProject = new TreeItem<>(new CustomItem(PROJECT.getImage(), new Label(fileName), PROJECT, path));
        treeView.setRoot(TreeItemProject);
    }

    /**
     * It just adds the project to the TreeView and creates the whole layout
     *
     * @param currentPath path, of the project, which is getting recreated in the TreeView
     */
    private static void addProject(File currentPath) {
        path = currentPath.getPath();
        fileName = currentPath.getName();
        textFlow = new TextFlow();
        Text projectText = new Text("- " + currentPath.getName() + " ");
        projectText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        projectText.setFill(Color.BLACK);
        Text pathText = new Text(path);
        pathText.setFill(Color.GRAY);
        pathText.setFont(Font.font("verdana", FontPosture.REGULAR, 15));


        textFlow.getChildren().addAll(projectText, pathText);
        TreeItemProject = new TreeItem<>(new CustomItem(PROJECT.getImage(), new Label(currentPath.getName()), PROJECT, path));
        treeView.setRoot(TreeItemProject);

    }

    /**
     * Checks class content for Main-class header. This method basically determines the main class
     *
     * @param content Content of class/ file
     * @return True if file is main, False if it isn't
     */
    private boolean isMain(String content) {

        return content.contains("publicstaticvoidmain(String[]args)");
    }

    /**
     * New class/ file gets created in the fileSystem as well as in TreeView
     *
     * @param className name of the class/ file
     * @param classKind kind of the class e.g. enum, interface  etc.
     */
    public static void addClass(String className, ClassType classKind) {


        TextArea tArea = new TextArea(getClassContent(className, classKind.getClassType()));
//        tArea.textProperty().addListener((ObservableValue<? extends String> o, String oldValue, String newValue) ->
//        {
//
//            if (isValid(newValue)) {
//                tArea.setStyle("-fx-text-inner-color: #BA55D3;");
//            } else {
//                tArea.setStyle(null);
//            }
//        });
        //TreeItem is getting created
        TreeItem<CustomItem> treeItem = new TreeItem<>(new CustomItem(classKind.getImage(), new Label(className), tArea, path + File.separator + className + ".java", classKind));

        TreeItemProject.getChildren().add(treeItem);
        textAreaStringHashMap.put(tArea, className);
        createFile(tArea.getText(), className);
    }

    /**
     * New package/ directory is getting created in the fileSystem and TreeView
     *
     * @param packageName name of the package
     * @throws IOException due to the creation of a directory
     */
    public static void addPackage(String packageName, File file) throws IOException {

        TreeItem<CustomItem> treeItem = new TreeItem<>(new CustomItem(PACKAGE.getImage(), new Label(packageName), PACKAGE, file.getPath()));
        packageNameHashMap.put(packageName, treeItem);
        if (!Files.exists(Paths.get(path + File.separator + packageName)))
            Files.createDirectory(Paths.get(path + File.separator + packageName));
        TreeItemProject.getChildren().add(treeItem);

    }
    //TODO: Check for files within mulitple packages, if file path is correct

    /**
     * Classes/ files are getting added to the directories in the fileSystem
     *
     * @param packageName name of the individual-package
     * @param className   name of the class/ file, which is getting stored in the package
     * @param classKind   kind of the class enum, interface etc.
     * @throws FileNotFoundException gets thrown because createFile-method is getting called
     */
    public static void addToPackage(String packageName, String className, ClassType classKind, File file) throws FileNotFoundException {

        TreeItem<CustomItem> treeItem;

        if (classKind.equals(PACKAGE)) {
            treeItem = new TreeItem<>(new CustomItem(classKind.getImage(), new Label(className), PACKAGE, file.getPath()));
            packageNameHashMap.put(className, treeItem);
            packageNameHashMap.get(packageName).getChildren().add(treeItem);


            createFile("", getCorrectPath(treeItem), className, true);
        } else {
            TextArea tArea = generateTextAreaContent(packageName, className, classKind);
            treeItem = new TreeItem<>(new CustomItem(classKind.getImage(), new Label(className),
                    tArea, path + File.separator + packageName + File.separator + className + ".java", classKind));
            textAreaStringHashMap.put(tArea, className);
            packageNameHashMap.get(packageName).getChildren().add(treeItem);
            treeItem.getValue().setPath(path + getCorrectPath(treeItem) + className);
            createFile(tArea.getText(), getCorrectPath(treeItem), className, false);

        }
    }

    /**
     * Adds the class headers to the individual TextAreas e.g. package1.package2 by analyzing the filestructures of the files;
     *
     * @param packageName name of the packages which are getting added
     * @param className   name of the class/enum or interface
     * @param classKind   classKind e.g. enum
     * @return instance of TextArea with corresponding content
     */
    private static TextArea generateTextAreaContent(String packageName, String className, ClassType classKind) {
        TreeItem<CustomItem> dummyItem = new TreeItem<>();
        packageNameHashMap.get(packageName).getChildren().add(dummyItem);
        getCorrectPath(dummyItem);
        packageNameHashMap.get(packageName).getChildren().remove(dummyItem);

        return new TextArea(getClassContent(className, classKind.getClassType()));
    }

    /**
     * gets right path of packages
     */
    public static String getCorrectPath(TreeItem<CustomItem> treeItem) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> stringList = new ArrayList<>();
        sb.setLength(0);

        try {
            while (treeItem.getParent() != null) {
                if (!(treeItem.getParent().getValue().getBoxText().getText().equals(fileName)))
                    stringList.add(treeItem.getParent().getValue().getBoxText().getText());
                treeItem = treeItem.getParent();
            }
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }

        sb.append("package ");

        stringBuilder.append(File.separator);
        for (int i = stringList.size() - 1; i >= 0; i--) {
            stringBuilder.append(stringList.get(i)).append(File.separator);
            if (!stringList.get(i).equals(fileName))
                sb.append(stringList.get(i));
            if (i > 0)
                sb.append(".");
        }


        sb.append(";").append("\n\n");


        return stringBuilder.toString().contains(fileName) ? stringBuilder.toString().replaceAll(fileName, "") : stringBuilder.toString();
    }


    /**
     * Creates the contents of the classes right after their creation
     *
     * @param classContent creates standard class content of each class
     * @param className    name of the class/ file
     * @return standard content of each class
     */
    private static String getClassContent(String classContent, String className) {

        sb.append("public ").append(className).append(" ").append(classContent).append("{").append("\n\n");
        //if it's the main-class, the main-method-head is getting added
        if (ClassBox.isSelected)
            sb.append("public static void main (String[] args) {").append("\n\n").append("}").append("\n\n");
        sb.append("}");
        String retString = sb.toString();
        sb.setLength(0);


        return retString;
    }

    /**
     * Opens cmd, compiles each file and runs them
     */
    public void execute() {

        listFiles = new ArrayList<>();
        try {
            findFilesRec(new File(path));
            generateOutputFolder();
            copyDirectory(path, path + File.separator + "output");
            findPairs();
            AtomicReference<String> nameMain = new AtomicReference<>("");
            //nameMain is getting initialized
            textAreaStringHashMap.forEach((k, v) -> {
                if (isMain(k.getText().replaceAll(" ", "")))
                    nameMain.set(textAreaStringHashMap.get(k));
            });
            //cmd is getting called
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd " + path +
                    File.separator + "output" + "&&" + "javac *.java && java " + nameMain + ".java \"");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * All files of dir Directory are getting stored in fileList
     *
     * @param dir directory, where the project is located
     */
    static void findFilesRec(File dir) {
        if (dir.isDirectory()) {
            if (!dir.getName().equals("output")) {
                File[] entries = dir.listFiles();
                if (entries != null) {
                    for (File entry : entries) {
                        if (dir.getPath().equals(path)) {
                            if (entry.isFile())
                                listFiles.add(entry);
                        } else {
                            if (entry.isFile())
                                listFiles.add(entry);
                        }
                        if (entry.isDirectory())
                            findFilesRec(entry);
                    }
                }
            }
        } else {
            if (dir.getPath().equals(path)) {
                listFiles.add(dir);
            } else {
                listFiles.add(dir);
            }

        }
    }

    /**
     * Output-folder is getting created, files are getting stored in it
     *
     * @throws IOException new files are getting instantiated
     */
    private void generateOutputFolder() throws IOException {

        StringBuilder sb = new StringBuilder();
        //Output-Folder gets deleted before every execution of the program
        if (Files.exists(Paths.get(path + File.separator + "output")))
            Files.walk(Paths.get(path + File.separator + "output")).sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            //if the output-folder doesn't exist yet, it is getting created
        else
            Files.createDirectory(Paths.get(path + File.separator + "output"));

        //all files of listFiles are stored in the output folder
        listFiles.forEach((f) -> {
            sb.setLength(0);
            try {
                if (!Files.exists(Paths.get(path + File.separator + "output" + File.separator + f.getName())))
                    Files.createFile(Paths.get(path + File.separator + "output" + File.separator + f.getName()));
                Files.lines(Paths.get(String.valueOf(f))).forEach(s -> sb.append(s).append("\n"));
                Files.writeString(Paths.get(path + File.separator + "output" + File.separator + f.getName()), sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Copies directories the contents of the source-directory into the directory
     *
     * @param sourceDirectoryLocation      path of the individual project
     * @param destinationDirectoryLocation path of the output folder
     * @throws IOException NIO-segments are getting used
     */
    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) throws IOException {

        Files.walk(Paths.get(sourceDirectoryLocation))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                            .substring(sourceDirectoryLocation.length()));
                    try {
                        if (!source.getFileName().toString().equals("output"))
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ignored) {

                    }
                });
    }


    /**
     * Pairs of .java and .class files are getting found and created
     */
    private static void findPairs() {

        File[] dir = new File(path + File.separator + "output").listFiles();
        int counter = 0;


        for (File f : listFiles) {
            String tempPath;
            Path tempFile;

            assert dir != null;
            for (File f1 : dir) {
                if (f1.isDirectory() || !f1.getName().contains(".class"))
                    continue;
                if (f.getName().replaceAll(".java", "").
                        equals(f1.getName().replaceAll(".class", ""))) {
                    tempPath = listFiles.get(counter).getPath().replace(path, path + "\\" +
                            "output");
                    tempFile = Paths.get(f1.getPath());
                    try {
                        Files.copy(tempFile,
                                Paths.get(tempPath.replaceAll(".java", ".class")),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            counter++;
        }

    }

    //needed for renaming and deleting purposes, treeItem which is getting changed
    public TreeItem<CustomItem> getRetTreeItem() {
        return retTreeItem;
    }

    public void setRetTreeItem(TreeItem<CustomItem> retTreeItem) {
        this.retTreeItem = retTreeItem;
    }
}