package app.frontend;


import app.backend.ClassType;
import app.backend.CustomItem;
import app.utils.Constants;
import app.utils.CopyUtils;
import app.utils.FileUtils;
import app.utils.GitUtils;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;

import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class GridPaneNIO {


    private static final Logger LOG = LoggerFactory.getLogger(GridPaneNIO.class);
    static Stage primaryStage;
    static Label label;
    TreeItem<CustomItem> retTreeItem;
    static StringBuilder sb = new StringBuilder();
    static TextFlow textFlow = new TextFlow();
    public static String path = Constants.EMPTY_STRING;
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
    MenuItem menutItemAddGit = new MenuItem("Add");
    Menu menuGit = new Menu();
    MenuItem menuItemInit = new MenuItem("Create Repository");
    MenuItem menuItemCommit = new Menu("Create Commit");
    static GridPane gridPane = new GridPane();
    static TreeItem<CustomItem> TreeItemProject;
    static String fileName = Constants.EMPTY_STRING;
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
    public GridPaneNIO(Stage primaryStage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        GridPaneNIO.primaryStage = primaryStage;
        // Icon is added to primaryStage
        GridPaneNIO.primaryStage.getIcons().add(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("AppIcon.png"))));
        GridPaneNIO.primaryStage.setX(bounds.getMinX());
        GridPaneNIO.primaryStage.setX(bounds.getMinY());
        GridPaneNIO.primaryStage.setWidth(bounds.getWidth());
        GridPaneNIO.primaryStage.setHeight(bounds.getHeight());
        recreateProject();
    }

    /**
     * Creates the Stage as well as the layout
     */
    public void init() {

        LOG.info("Starting to build stage...");
        menuExecute.getItems().add(menuItemExec1);
        menuAdd.getItems().addAll(menuItemAddClass, menuItemAddInterface, menuItemAddEnum, menuItemAddPackage, menuItemAddProject, menuItemSelectProject);
        menuClose.getItems().add(menuItemClose1);
        menuGit.getItems().addAll(menuItemInit, menuItemCommit);

        primaryStage.setTitle(Constants.IDE_NAME);

        ImageView viewMenu = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/greenPlay.png"))));
        menuExecute.setGraphic(viewMenu);
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);

        ImageView viewMenuItem = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/greenPlay.png"))));
        menuItemExec1.setGraphic(viewMenuItem);
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menuItemExec1.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        menuItemExec1.setOnAction(e -> execute());

        viewMenu = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/terminateIcon.png"))));
        menuClose.setGraphic(viewMenu);
        menuItemClose1.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
        menuItemClose1.setOnAction(e -> primaryStage.close());
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);

        viewMenuItem = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/renameIcon.png"))));
        menuItemRename.setGraphic(viewMenuItem);
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menuItemRename.setOnAction(e -> RenameBox.display(getRetTreeItem()));


        viewMenu = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/terminateIcon.png"))));
        menuItemDelete.setGraphic(viewMenu);
        menuItemDelete.setOnAction(e -> DeleteBox.display(getRetTreeItem()));
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);


        viewMenu = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/plusIcon.png"))));
        menuAdd.setGraphic(viewMenu);
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);

        viewMenuItem = ClassType.CLASS.getImage();
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menuItemAddClass.setAccelerator(KeyCombination.keyCombination("Ctrl+K"));
        menuItemAddClass.setOnAction(e -> ClassBox.display(ClassType.CLASS, false));
        menuItemAddClass.setGraphic(viewMenuItem);

        viewMenuItem = ClassType.INTERFACE.getImage();
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menuItemAddInterface.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
        menuItemAddInterface.setGraphic(viewMenuItem);
        menuItemAddInterface.setOnAction(e -> ClassBox.display(ClassType.INTERFACE, false));


        label = new Label();
        contextMenuPackages = new ContextMenu();
        contextMenuClasses = new ContextMenu();

        viewMenuItem = ClassType.ENUM.getImage();
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menuItemAddEnum.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        menuItemAddEnum.setGraphic(viewMenuItem);
        menuItemAddEnum.setOnAction(e -> ClassBox.display(ClassType.ENUM, false));

        viewMenuItem = ClassType.PACKAGE.getImage();
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menuItemAddPackage.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        menuItemAddPackage.setGraphic(viewMenuItem);
        menuItemAddPackage.setOnAction(e -> ClassBox.display(ClassType.PACKAGE, true));


        viewMenuItem = ClassType.PROJECT.getImage();
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menuItemAddProject.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        menuItemAddProject.setGraphic(viewMenuItem);

        menuItemAddProject.setOnAction(e -> {
            try {
                addProject();
            } catch (IOException ex) {
                LOG.error("Project could not be added");
            }
        });


        viewMenu = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/git.png"))));
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);
        menuGit.setGraphic(viewMenu);

        viewMenuItem = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/commit.png"))));
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menuItemCommit.setGraphic(viewMenuItem);
        menuItemCommit.setOnAction(e -> CommitBox.display());

        // TODO: Find out, why this image is not added!
        viewMenuItem = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/plusIcon.png"))));
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menutItemAddGit.setGraphic(viewMenuItem);
        menutItemAddGit.setOnAction(e -> {
            try {
                GitUtils.gitAdd(getRetTreeItem());
            } catch (IOException ex) {
                LOG.error("File: [{}] could not be added to Git",
                        getRetTreeItem().getValue().getBoxText().getText());
            }
        });


        viewMenuItem = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/init.png"))));
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menuItemInit.setGraphic(viewMenuItem);
        menuItemInit.setOnAction(e -> {
            try {
                GitUtils.gitInit(path);
            } catch (IOException ex) {
                LOG.error("Init for path: [{}] was unsuccessful", path);
            }
        });

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuAdd, menuExecute, menuGit, menuClose);
        gridPane.add(menuBar, 0, 0);
        GridPane.setColumnSpan(menuBar, 2);
        gridPane.add(textFlow, 0, 1);
        GridPane.setColumnSpan(textFlow, 2);
        gridPane.add(treeView, 0, 2);
        gridPane.add(label, 1, 0);
        GridPane.setRowSpan(label, 1);
        gridPane.add(textArea, 1, 2);
        Scene scene = new Scene(gridPane, 800, 800);


        viewMenuItem = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/selectProjectIcon.png"))));
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        menuItemSelectProject.setGraphic(viewMenuItem);
        menuItemSelectProject.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        menuItemSelectProject.setOnAction(e -> {
            try {
                selectProject();
            } catch (FileNotFoundException ex) {
                LOG.error("Project could not be selected");
            }
        });



        //items get added to contextMenuPackages
        AtomicReference<TreeItem<CustomItem>> tempTreeItem = new AtomicReference<>();
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //to avoid the addition of too many menuItems within the contextMenu
            if (contextMenuPackages.getItems().isEmpty() && contextMenuClasses.getItems().isEmpty()) {
                //menuItems get added to contextMenuPackages
                contextMenuPackages.getItems().addAll(CopyUtils.copyMenuItem(menuItemAddClass), CopyUtils.copyMenuItem(menuItemAddInterface),
                        CopyUtils.copyMenuItem(menuItemAddEnum), CopyUtils.copyMenuItem(menuItemAddPackage), CopyUtils.copyMenuItem(menuItemRename), CopyUtils.copyMenuItem(menuItemDelete), CopyUtils.copyMenuItem(menutItemAddGit));
                //menuItems get added to contextMenuClasses
                contextMenuClasses.getItems().addAll(CopyUtils.copyMenuItem(menuItemRename), CopyUtils.copyMenuItem(menuItemDelete),
                        CopyUtils.copyMenuItem(menutItemAddGit));
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
                if (tempTreeItem.get().getValue().getClassType().equals(ClassType.PACKAGE) ||
                        tempTreeItem.get().getValue().getClassType().equals(ClassType.PROJECT)) {
                    contextMenuPackages.show(tempTreeItem.get().getValue(), e.getScreenX(), e.getScreenY());
                    if (tempTreeItem.get().getValue().getClassType().equals(ClassType.PACKAGE))
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
            FileUtils.updateFile(tempTreeItem.get().getValue().getTextArea().getText(), tempTreeItem.get().getValue().getPath());
            setImageLabel(tempTreeItem.get());
            label.setText(tempTreeItem.get().getValue().getLabelText());
        });

        scene.getStylesheets().add(Constants.ROOT_STYLE_SHEET);

        primaryStage.setScene(scene);
        primaryStage.show();
        LOG.info("Successfully built stage");
    }

    /**
     * Helper-method which copies ImageViews for the label
     *
     * @param treeItem treeItem, which image is getting copied
     */
    private static void setImageLabel(TreeItem<CustomItem> treeItem) {

        ImageView imageView;

        if (treeItem.getValue().getClassType().equals(ClassType.PACKAGE))
            imageView = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/packageIcon.png"))));
        else if (treeItem.getValue().getClassType().equals(ClassType.CLASS))
            imageView = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/classIcon.png"))));
        else if (treeItem.getValue().getClassType().equals(ClassType.INTERFACE))
            imageView = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/interfaceIcon.png"))));
        else
            imageView = new ImageView(new Image(Objects.requireNonNull(GridPaneNIO.class.getClassLoader().getResourceAsStream("images/enumIcon.png"))));
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        label.setGraphic(imageView);


    }

    /**
     * Opens File-Explorer and selects location of the project
     *
     * @throws FileNotFoundException NIO-codesegments are getting used
     */
    private static void selectProject() throws FileNotFoundException {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        try {
            textAreaStringHashMap = new HashMap<>();
            packageNameHashMap = new HashMap<>();
            listFiles.clear();
            File tempFile = directoryChooser.showDialog(primaryStage);
            path = tempFile != null ? tempFile.getPath() : path;
            fileName = tempFile != null ? tempFile.getName() : fileName;

            Files.writeString((Paths.get(FileUtils.getRelativePath() + Constants.FILE_SEPARATOR +
                    Constants.PROJECT_FILES + Constants.FILE_SEPARATOR + Constants.CURRENT_PROJECT)), path);


            //textArea gets resetted after selection
            textArea.setText(Constants.EMPTY_STRING);
        } catch (Exception e) {
            LOG.error("Project could not be selected");
            return;
        }


        Text projectText = new Text(Constants.HYPHEN + Constants.SPACE_STRING + fileName + Constants.SPACE_STRING);
        projectText.setFont(Font.font(Constants.CURRENT_FONT, FontWeight.BOLD, FontPosture.REGULAR, 15));
        projectText.setFill(Color.BLACK);
        Text pathText = new Text(path);
        pathText.setFill(Color.GRAY);
        pathText.setFont(Font.font(Constants.CURRENT_FONT, FontPosture.REGULAR, 15));


        textFlow.getChildren().addAll(projectText, pathText);
        gridPane.getChildren().add(textFlow);
        TreeItemProject = new TreeItem<>(new CustomItem(ClassType.PROJECT.getImage(), new Label(fileName), ClassType.PROJECT, path));
        treeView.setRoot(TreeItemProject);
        recreateRecProject(new File(path));

    }


    /**
     * Gets called if a class is only added to the TreeView and not in the fileSystem
     *
     * @param file      file which is getting added
     * @param classKind kind of the class enum, interface etc.
     */
    private static void addClass(File file, ClassType classKind) {
        TextArea tArea = new TextArea(FileUtils.getClassContent(file));
        TreeItem<CustomItem> treeItem = new TreeItem<>(new CustomItem(classKind.getImage(), new Label(file.getName().replaceAll(Constants.JAVA_FILE_EXTENSION, Constants.EMPTY_STRING)),
                tArea, file.getPath(), classKind));
        TreeItemProject.getChildren().add(treeItem);
        textAreaStringHashMap.put(tArea, file.getName().replaceAll(Constants.JAVA_FILE_EXTENSION, Constants.EMPTY_STRING));
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
    public static void addToPackage(String packageName, String filePath, String className, ClassType
            classKind, File file) {

        TreeItem<CustomItem> treeItem;
        if (classKind.equals(ClassType.PACKAGE)) {
            treeItem = new TreeItem<>(new CustomItem(classKind.getImage(), new Label(className), ClassType.PACKAGE, file.getPath()));
            packageNameHashMap.put(className, treeItem);
            packageNameHashMap.get(packageName).getChildren().add(treeItem);
        } else {
            TextArea tArea = new TextArea(FileUtils.getClassContent(file));
            
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

        TreeItem<CustomItem> treeItem = new TreeItem<>(new CustomItem(ClassType.PACKAGE.getImage(), new Label(packageName),
                ClassType.PACKAGE, file.getPath()));
        packageNameHashMap.put(packageName, treeItem);
        TreeItemProject.getChildren().add(treeItem);

    }

    /**
     * Recreates last used project
     */
    private static void recreateProject() {
        //if the project exists it gets added and recreated
        LOG.info("Starting to recreate project...");
        if (getProjectPath() != null) {
            addProject(getProjectPath());
            recreateRecProject(getProjectPath());
        }

        LOG.info("Successfully recreated project");

    }

    /**
     * Recreates the Project-structure in the TreeView by the given link
     *
     * @param file represents the project structure or is the project
     */
    private static void recreateRecProject(File file) {

        if (file.isDirectory()) {
            if (!file.getName().equals(Constants.OUTPUT_DIR) && !file.getName().equals(Constants.GIT_DIR)) {
                File[] entries = file.listFiles();
                if (entries != null) {
                    for (File entry : entries) {
                        if (file.getPath().equals(path)) {
                            if (entry.isDirectory() && !entry.getName().equals(Constants.OUTPUT_DIR) && !entry.getName().equals(Constants.GIT_DIR))
                                addPackage1(entry.getName(), entry);
                            else if (entry.isFile() && !entry.getName().equals(Constants.OUTPUT_DIR) && !entry.getName().equals(Constants.GIT_DIR))
                                addClass(entry, FileUtils.checkForClassType(entry));
                        } else {
                            if (entry.isDirectory() && !entry.getName().equals(Constants.OUTPUT_DIR) && !entry.getName().equals(Constants.GIT_DIR))
                                addToPackage(new File(entry.getParent()).getName(),
                                        entry.getPath(), entry.getName(), ClassType.PACKAGE, entry);
                            else
                                addToPackage(new File(entry.getParent()).getName(), entry.getPath(),
                                        entry.getName().replaceAll(Constants.JAVA_FILE_EXTENSION, Constants.EMPTY_STRING), 
                                        FileUtils.checkForClassType(entry), entry);
                        }
                        if (entry.isDirectory())
                            recreateRecProject(entry);
                    }
                }
            }
        } else {
            if (file.getPath().equals(path)) {
                addClass(file, FileUtils.checkForClassType(file));
            } else {
                addToPackage(new File(file.getParent()).getName(), file.getPath(),
                        file.getName().replaceAll(Constants.JAVA_FILE_EXTENSION, Constants.EMPTY_STRING), FileUtils.checkForClassType(file), file);
            }

        }
    }
    
    /**
     * Writes path of file in currentProject
     *
     * @return File, which corresponds to the first line of the file currentProject
     */
    private static File getProjectPath() {

        try {
            // if the file does not exist yet, it gets created
            if (!Files.exists(Paths.get(FileUtils.getRelativePath() + Constants.FILE_SEPARATOR +
                    Constants.PROJECT_FILES + Constants.FILE_SEPARATOR + Constants.CURRENT_PROJECT))) {
                Files.createDirectory(Paths.get(FileUtils.getRelativePath() + Constants.FILE_SEPARATOR +
                        Constants.PROJECT_FILES));
                Files.createFile(Paths.get(FileUtils.getRelativePath() + Constants.FILE_SEPARATOR +
                        Constants.PROJECT_FILES + Constants.FILE_SEPARATOR + Constants.CURRENT_PROJECT));
            }

            try(BufferedReader br = new BufferedReader(new FileReader(FileUtils.getRelativePath() + Constants.FILE_SEPARATOR +
                    Constants.PROJECT_FILES + Constants.FILE_SEPARATOR + Constants.CURRENT_PROJECT))) {

                return new File(br.readLine());
            }
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                textFlow = new TextFlow();
                Text projectText = new Text(Constants.SELECT_FILE);
                projectText.setFont(Font.font(Constants.CURRENT_FONT, FontWeight.BOLD, FontPosture.REGULAR, 15));
                projectText.setFill(Color.BLACK);
                Text pathText = new Text(path);
                pathText.setFill(Color.GRAY);
                pathText.setFont(Font.font(Constants.CURRENT_FONT, FontPosture.REGULAR, 15));

                textFlow.getChildren().addAll(projectText, pathText);
            } else
               LOG.error("Path could not be written in currentProject");
        }
        return null;
    }


    /**
     * Creates/ overwrites the currentProject file
     */
    private static void createProjectFile() {

        try {
            Files.writeString(Paths.get(FileUtils.getRelativePath() + Constants.FILE_SEPARATOR +
                    Constants.PROJECT_FILES + Constants.FILE_SEPARATOR + Constants.CURRENT_PROJECT), path);
            // Clears text area after project file has been created
            textArea.setText(Constants.EMPTY_STRING);
        } catch (Exception ex) {
            LOG.error("Project file could not be created for path: [{}]", path);
        }
    }


    /**
     * Opens fileDialog window and saves in the project in the requested location
     *
     * @throws IOException NIO code-segments are getting used
     */
    private static void addProject() throws IOException {


        LOG.info("Open FileDialog and adding project");

        textAreaStringHashMap = new HashMap<>();
        packageNameHashMap = new HashMap<>();
        listFiles.clear();

        FileDialog fd = new FileDialog(new Frame(), Constants.SELECT_LOCATION, FileDialog.SAVE);
        fd.setDirectory(Constants.C_ROOT);
        fd.setVisible(true);
        fileName = fd.getFile() != null ? fd.getFile() : fileName;
        path = fd.getFile() != null && fd.getDirectory() != null ? fd.getDirectory() + fd.getFile() : path;
        if (!Files.exists(Paths.get(path)))
            Files.createDirectory(Paths.get(path));
        createProjectFile();
        textFlow = new TextFlow();
        Text projectText = new Text(Constants.HYPHEN + Constants.SPACE_STRING + fileName + Constants.SPACE_STRING);
        projectText.setFont(Font.font(Constants.CURRENT_FONT, FontWeight.BOLD, FontPosture.REGULAR, 15));
        projectText.setFill(Color.BLACK);
        Text pathText = new Text(path);
        pathText.setFill(Color.GRAY);
        pathText.setFont(Font.font(Constants.CURRENT_FONT, FontPosture.REGULAR, 15));

        textFlow.getChildren().addAll(projectText, pathText);
        TreeItemProject = new TreeItem<>(new CustomItem(ClassType.PROJECT.getImage(), new Label(fileName),
                ClassType.PROJECT, path));
        treeView.setRoot(TreeItemProject);

        LOG.info("Successfully added project");
    }

    /**
     * It just adds the project to the TreeView and creates the whole layout
     *
     * @param currentPath path, of the project, which is getting recreated in the TreeView
     */
    private static void addProject(File currentPath) {

        LOG.info("Adding project to stage...");
        path = currentPath.getPath();
        fileName = currentPath.getName();
        textFlow = new TextFlow();
        Text projectText = new Text(Constants.HYPHEN + Constants.SPACE_STRING + currentPath.getName() + Constants.SPACE_STRING);
        projectText.setFont(Font.font(Constants.CURRENT_FONT, FontWeight.BOLD, FontPosture.REGULAR, 15));
        projectText.setFill(Color.BLACK);
        Text pathText = new Text(path);
        pathText.setFill(Color.GRAY);
        pathText.setFont(Font.font(Constants.CURRENT_FONT, FontPosture.REGULAR, 15));


        textFlow.getChildren().addAll(projectText, pathText);
        TreeItemProject = new TreeItem<>(new CustomItem(ClassType.PROJECT.getImage(), new Label(currentPath.getName()),
                ClassType.PROJECT, path));
        treeView.setRoot(TreeItemProject);

        LOG.info("Successfully added project to stage");
    }

    /**
     * Checks class content for Main-class header. This method basically determines the main class
     *
     * @param content Content of class/ file
     * @return True if file is main, False if it isn't
     */
    private boolean isMain(String content) {

        return content.contains(Constants.PSVM);
    }

    /**
     * New class/ file gets created in the fileSystem as well as in TreeView
     *
     * @param className name of the class/ file
     * @param classKind kind of the class e.g. enum, interface  etc.
     */
    public static void addClass(String className, ClassType classKind) {

        LOG.info("Adding class: [{}]", className);

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
        TreeItem<CustomItem> treeItem = new TreeItem<>(new CustomItem(classKind.getImage(),
                new Label(className), tArea, path + Constants.FILE_SEPARATOR + className + Constants.JAVA_FILE_EXTENSION, classKind));

        TreeItemProject.getChildren().add(treeItem);
        textAreaStringHashMap.put(tArea, className);
        FileUtils.createFile(path, tArea.getText(), className);

        LOG.info("Successfully added class: [{}]", className);
    }

    /**
     * New package/ directory is getting created in the fileSystem and TreeView
     *
     * @param packageName name of the package
     * @throws IOException due to the creation of a directory
     */
    public static void addPackage(String packageName, File file) throws IOException {

        LOG.info("Adding package: [{}]", packageName);

        TreeItem<CustomItem> treeItem = new TreeItem<>(new CustomItem(ClassType.PACKAGE.getImage(),
                new Label(packageName), ClassType.PACKAGE, file.getPath()));
        packageNameHashMap.put(packageName, treeItem);
        if (!Files.exists(Paths.get(path + Constants.FILE_SEPARATOR + packageName)))
            Files.createDirectory(Paths.get(path + Constants.FILE_SEPARATOR + packageName));
        TreeItemProject.getChildren().add(treeItem);

        LOG.info("Successfully added package: [{}]", packageName);

    }


    /**
     * Classes/ files are getting added to the directories in the fileSystem
     *
     * @param packageName name of the individual-package
     * @param className   name of the class/ file, which is getting stored in the package
     * @param classKind   kind of the class enum, interface etc.
     * @throws FileNotFoundException gets thrown because createFile-method is getting called
     */
    public static void addToPackage(String packageName, String className, ClassType classKind, File file) throws
            FileNotFoundException {

        LOG.info("Adding class: [{}] to package: [{}]", className, packageName);

        TreeItem<CustomItem> treeItem;

        if (classKind.equals(ClassType.PACKAGE)) {
            treeItem = new TreeItem<>(new CustomItem(classKind.getImage(), new Label(className), ClassType.PACKAGE, file.getPath()));
            packageNameHashMap.put(className, treeItem);
            packageNameHashMap.get(packageName).getChildren().add(treeItem);


            FileUtils.createFile(path, Constants.EMPTY_STRING, getCorrectPath(treeItem), className, true);
        } else {
            TextArea tArea = generateTextAreaContent(packageName, className, classKind);
            treeItem = new TreeItem<>(new CustomItem(classKind.getImage(), new Label(className),
                    tArea, path + Constants.FILE_SEPARATOR + packageName + 
                    Constants.FILE_SEPARATOR + className + Constants.JAVA_FILE_EXTENSION, classKind));
            textAreaStringHashMap.put(tArea, className);
            packageNameHashMap.get(packageName).getChildren().add(treeItem);
            treeItem.getValue().setPath(path + getCorrectPath(treeItem) + className);
            FileUtils.createFile(path, tArea.getText(), getCorrectPath(treeItem), className, false);

        }

        LOG.info("Successfully added class: [{}] to package: [{}]", className, packageName);
    }

    /**
     * Adds the class headers to the individual TextAreas e.g. package1.package2 by analyzing the file-structures of the files;
     *
     * @param packageName name of the packages which are getting added
     * @param className   name of the class/enum or interface
     * @param classKind   classKind e.g. enum
     * @return instance of TextArea with corresponding content
     */
    private static TextArea generateTextAreaContent(String packageName, String className, ClassType classKind) {

        LOG.info("Creating file-content of: [{}]", className);

        TreeItem<CustomItem> dummyItem = new TreeItem<>();
        packageNameHashMap.get(packageName).getChildren().add(dummyItem);
        getCorrectPath(dummyItem);
        packageNameHashMap.get(packageName).getChildren().remove(dummyItem);

        LOG.info("Successfully created file-content of: [{}]", className);

        return new TextArea(getClassContent(className, classKind.getClassType()));
    }

    /**
     * Gets right path of packages
     */
    public static String getCorrectPath(TreeItem<CustomItem> treeItem) {

        LOG.info("Creating correct path...");
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
            LOG.error("Path could not be re/created");
        }

        sb.append(Constants.PACKAGE_STRING +Constants.SPACE_STRING);

        stringBuilder.append(Constants.FILE_SEPARATOR);
        for (int i = stringList.size() - 1; i >= 0; i--) {
            stringBuilder.append(stringList.get(i)).append(Constants.FILE_SEPARATOR);
            if (!stringList.get(i).equals(fileName))
                sb.append(stringList.get(i));
            if (i > 0)
                sb.append(Constants.DOT);
        }


        sb.append(Constants.SEMI_COLON).append(Constants.DOUBLE_NEW_LINE);

        LOG.info("Successfully created path");


        return stringBuilder.toString().contains(fileName) ? stringBuilder.toString().replaceAll(fileName, Constants.EMPTY_STRING) :
                stringBuilder.toString();
    }


    /**
     * Creates the contents of the classes right after their creation
     *
     * @param classContent creates standard class content of each class
     * @param className    name of the class/ file
     * @return standard content of each class
     */
    private static String getClassContent(String classContent, String className) {

        sb.append(Constants.PUBLIC+Constants.SPACE_STRING).append(className).append(Constants.SPACE_STRING).append(classContent).append(Constants.CURLY_BRACKETS_OPEN)
                .append(Constants.DOUBLE_NEW_LINE);
        //if it's the main-class, the main-method-head is getting added
        if (ClassBox.isSelected)
            sb.append(Constants.PSVM_INPUT).append(Constants.DOUBLE_NEW_LINE).append(Constants.CURLY_BRACKETS_CLOSE).append(Constants.DOUBLE_NEW_LINE);
        sb.append(Constants.CURLY_BRACKETS_CLOSE);
        String retString = sb.toString();
        sb.setLength(0);


        return retString;
    }

    /**
     * Opens cmd, compiles each file and runs them
     */
    public void execute() {

        LOG.info("Trying to execute code...");

        listFiles = new ArrayList<>();
        try {
            findFilesRec(new File(path));
            FileUtils.generateOutputFolder(path);
            FileUtils.copyDirectory(path, path + Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR);
            findPairs();
            AtomicReference<String> nameMain = new AtomicReference<>(Constants.EMPTY_STRING);
            AtomicReference<String> pathMain = new AtomicReference<>(Constants.EMPTY_STRING);
            //nameMain is getting initialized
            textAreaStringHashMap.forEach((k, v) -> {
                if (isMain(k.getText().replaceAll(Constants.SPACE_STRING, Constants.EMPTY_STRING)))
                    nameMain.set(textAreaStringHashMap.get(k));
            });
            nameMain.set(nameMain + Constants.JAVA_FILE_EXTENSION);
            listFiles.forEach(f -> {
                if (nameMain.get().equals(f.getName())) {
                    pathMain.set(f.getPath());
                }
            });
            //cmd is getting called, java files are compiled and executed
            String relativePathMain = pathMain.get().replace(path + Constants.FILE_SEPARATOR, Constants.EMPTY_STRING);

//            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd " + path +
//                    Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR + "&&" + "javac -cp " + path + Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR +
//                    Constants.SPACE_STRING + path + Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR + Constants.FILE_SEPARATOR + relativePathMain +
//                    "&&" + "java " + relativePathMain + "\Constants.EMPTY_STRING);

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(path + Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR));
            processBuilder.command(Constants.CMD, Constants.C, Constants.START, Constants.JAVA_COMPILE, Constants.CP, 
                    path + Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR, path + 
                            Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR + Constants.FILE_SEPARATOR + relativePathMain).start();
            TimeUnit.MILLISECONDS.sleep(1000);
            processBuilder.command(Constants.CMD, Constants.C, Constants.START, Constants.CMD, Constants.K, Constants.JAVA, relativePathMain + "\"").start();

        } catch (Exception ex) {
            LOG.error("A problem while executing the code occurred");
        }

        LOG.info("Successfully executed code");
    }

    /**
     * All files of dir Directory are getting stored in fileList
     *
     * @param dir directory, where the project is located
     */
    static void findFilesRec(File dir) {
        if (dir.isDirectory()) {
            if (!dir.getName().equals(Constants.OUTPUT_DIR) && !dir.getName().equals(Constants.GIT_DIR)) {
                File[] entries = dir.listFiles();
                if (entries != null) {
                    for (File entry : entries) {
                        if (entry.isFile())
                            listFiles.add(entry);
                        if (entry.isDirectory())
                            findFilesRec(entry);
                    }
                }
            }
        } else
            listFiles.add(dir);
    }


    /**
     * Pairs of .java and .class files are getting found and created
     */
    private static void findPairs() {

        LOG.info(String.format("Creating pairs for %s and %s-files...", Constants.CLASS_FILE_EXTENSION, Constants.JAVA_FILE_EXTENSION));

        File[] dir = new File(path + Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR).listFiles();
        int counter = 0;

        if (dir == null)
            return;

        for (File f : listFiles) {
            String tempPath;
            Path tempFile;

            for (File f1 : dir) {
                if (f1.isDirectory() || !f1.getName().contains(Constants.CLASS_FILE_EXTENSION))
                    continue;
                if (f.getName().replaceAll(Constants.JAVA_FILE_EXTENSION, Constants.EMPTY_STRING).
                        equals(f1.getName().replaceAll(Constants.CLASS_FILE_EXTENSION, Constants.EMPTY_STRING))) {
                    tempPath = listFiles.get(counter).getPath().replace(path, path + Constants.FILE_SEPARATOR +
                            Constants.OUTPUT_DIR);
                    tempFile = Paths.get(f1.getPath());
                    try {
                        Files.copy(tempFile,
                                Paths.get(tempPath.replaceAll(Constants.JAVA_FILE_EXTENSION, Constants.CLASS_FILE_EXTENSION)),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        LOG.error("A problem while finding pairs occurred");
                    }
                    break;
                }
            }
            counter++;
        }

        LOG.info(String.format("Successfully created pairs for %s and %s-files...", Constants.CLASS_FILE_EXTENSION, Constants.JAVA_FILE_EXTENSION));

    }

    //needed for renaming and deleting purposes, treeItem which is getting changed
    public TreeItem<CustomItem> getRetTreeItem() {
        return retTreeItem;
    }

    public void setRetTreeItem(TreeItem<CustomItem> retTreeItem) {
        this.retTreeItem = retTreeItem;
    }


    public HashMap<String, TreeItem<CustomItem>> getPackageNameHashMap() {

        return packageNameHashMap;
    }

    public HashMap<TextArea, String> getTextAreaStringHashMap() {

        return textAreaStringHashMap;
    }


}