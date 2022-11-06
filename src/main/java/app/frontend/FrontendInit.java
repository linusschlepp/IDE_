package app.frontend;


import app.backend.ClassType;
import app.backend.CustomItem;
import app.exceptions.IDEException;
import app.utils.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FrontendInit {

    private static final Logger LOG = LoggerFactory.getLogger(FrontendInit.class);


    /**
     * Constructor of class: GridPaneNIO
     * Calls method recreateProject
     *
     * @param primaryStage mainStage, which is getting passed by the main class
     */
    public FrontendInit(final Stage primaryStage) throws IDEException {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        FrontendConstants.primaryStage = primaryStage;
        // Icon is added to primaryStage
        FrontendConstants.primaryStage.getIcons().add(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("AppIcon.png"))));
        FrontendConstants.primaryStage.setX(bounds.getMinX());
        FrontendConstants.primaryStage.setX(bounds.getMinY());
        FrontendConstants.primaryStage.setWidth(bounds.getWidth());
        FrontendConstants.primaryStage.setHeight(bounds.getHeight());
        CommandUtils.recreateProject();
    }

    /**
     * Creates the Stage as well as the layout
     */
    public void init() {

        LOG.info("Starting to build stage...");
        FrontendConstants.menuExecute.getItems().add(FrontendConstants.menuItemExec1);
        FrontendConstants.menuAdd.getItems().addAll(FrontendConstants.menuItemAddClass, FrontendConstants.menuItemAddInterface, FrontendConstants.menuItemAddEnum, FrontendConstants.menuItemAddPackage,
                FrontendConstants.menuItemAddProject, FrontendConstants.menuItemSelectProject);
        FrontendConstants.menuClose.getItems().add(FrontendConstants.menuItemClose1);
        FrontendConstants.menuGit.getItems().addAll(FrontendConstants.menuItemInit, FrontendConstants.menuItemCommit);

        FrontendConstants.primaryStage.setTitle(Constants.IDE_NAME);

        ImageView viewMenu = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/greenPlay.png"))));
        FrontendConstants.menuExecute.setGraphic(viewMenu);
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);

        ImageView viewMenuItem = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/greenPlay.png"))));
        FrontendConstants.menuItemExec1.setGraphic(viewMenuItem);
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menuItemExec1.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        FrontendConstants.menuItemExec1.setOnAction(e -> {
            try {
                CommandUtils.execute();
            } catch (IDEException ex) {
                throw new RuntimeException(ex);
            }
        });

        viewMenu = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/terminateIcon.png"))));
        FrontendConstants.menuClose.setGraphic(viewMenu);
        FrontendConstants.menuItemClose1.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
        FrontendConstants.menuItemClose1.setOnAction(e -> FrontendConstants.primaryStage.close());
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);

        viewMenuItem = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/renameIcon.png"))));
        FrontendConstants.menuItemRename.setGraphic(viewMenuItem);
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menuItemRename.setOnAction(e -> RenameBox.display(CommandUtils.getRetTreeItem()));


        viewMenu = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/terminateIcon.png"))));
        FrontendConstants.menuItemDelete.setGraphic(viewMenu);
        FrontendConstants.menuItemDelete.setOnAction(e -> DeleteBox.display(CommandUtils.getRetTreeItem()));
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);


        viewMenu = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/plusIcon.png"))));
        FrontendConstants.menuAdd.setGraphic(viewMenu);
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);

        viewMenuItem = ClassType.CLASS.getImage();
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menuItemAddClass.setAccelerator(KeyCombination.keyCombination("Ctrl+K"));
        FrontendConstants.menuItemAddClass.setOnAction(e -> ClassBox.display(ClassType.CLASS, false));
        FrontendConstants.menuItemAddClass.setGraphic(viewMenuItem);

        viewMenuItem = ClassType.INTERFACE.getImage();
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menuItemAddInterface.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
        FrontendConstants.menuItemAddInterface.setGraphic(viewMenuItem);
        FrontendConstants.menuItemAddInterface.setOnAction(e -> ClassBox.display(ClassType.INTERFACE, false));


        FrontendConstants.label = new Label();
        FrontendConstants.contextMenuPackages = new ContextMenu();
        FrontendConstants.contextMenuClasses = new ContextMenu();

        viewMenuItem = ClassType.ENUM.getImage();
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menuItemAddEnum.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        FrontendConstants.menuItemAddEnum.setGraphic(viewMenuItem);
        FrontendConstants.menuItemAddEnum.setOnAction(e -> ClassBox.display(ClassType.ENUM, false));

        viewMenuItem = ClassType.PACKAGE.getImage();
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menuItemAddPackage.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        FrontendConstants.menuItemAddPackage.setGraphic(viewMenuItem);
        FrontendConstants.menuItemAddPackage.setOnAction(e -> ClassBox.display(ClassType.PACKAGE, true));


        viewMenuItem = ClassType.PROJECT.getImage();
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menuItemAddProject.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        FrontendConstants.menuItemAddProject.setGraphic(viewMenuItem);

        FrontendConstants.menuItemAddProject.setOnAction(e -> {
            try {
                CommandUtils.addProject();
            } catch (IOException | IDEException ex) {
                try {
                    new IDEException("Project could not be added").throwWithLogging(LOG);
                } catch (IDEException exc) {
                    throw new RuntimeException(exc);
                }
            }
        });


        viewMenu = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/git.png"))));
        viewMenu.setFitHeight(20);
        viewMenu.setPreserveRatio(true);
        FrontendConstants.menuGit.setGraphic(viewMenu);

        viewMenuItem = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/commit.png"))));
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menuItemCommit.setGraphic(viewMenuItem);
        FrontendConstants.menuItemCommit.setOnAction(e -> CommitBox.display());

        // TODO: Find out, why this image is not added!
        viewMenuItem = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/plusIcon.png"))));
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menutItemAddGit.setGraphic(viewMenuItem);
        FrontendConstants.menutItemAddGit.setOnAction(e -> {
            try {
                GitUtils.gitAdd(CommandUtils.getRetTreeItem());
            } catch (IOException ex) {
                try {
                    new IDEException("File: [{}] could not be added to Git",
                            CommandUtils.getRetTreeItem().getValue().getBoxText().getText()).throwWithLogging(LOG);
                } catch (IDEException exc) {
                    throw new RuntimeException(exc);
                }
            }
        });


        viewMenuItem = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/init.png"))));
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menuItemInit.setGraphic(viewMenuItem);
        FrontendConstants.menuItemInit.setOnAction(e -> {
            try {
                GitUtils.gitInit(FrontendConstants.path);
            } catch (IOException ex) {
                try {
                    new IDEException("Init for path: [{}] was unsuccessful", FrontendConstants.path).throwWithLogging(LOG);
                } catch (IDEException exc) {
                    throw new RuntimeException(exc);
                }
            }
        });

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(FrontendConstants.menuAdd, FrontendConstants.menuExecute, FrontendConstants.menuGit, FrontendConstants.menuClose);
        FrontendConstants.gridPane.add(menuBar, 0, 0);
        GridPane.setColumnSpan(menuBar, 2);
        FrontendConstants.gridPane.add(FrontendConstants.textFlow, 0, 1);
        GridPane.setColumnSpan(FrontendConstants.textFlow, 2);
        FrontendConstants.gridPane.add(FrontendConstants.treeView, 0, 2);
        FrontendConstants.gridPane.add(FrontendConstants.label, 1, 0);
        GridPane.setRowSpan(FrontendConstants.label, 1);
        FrontendConstants.gridPane.add(FrontendConstants.textArea, 1, 2);
        Scene scene = new Scene(FrontendConstants.gridPane, 800, 800);


        viewMenuItem = new ImageView(new Image(Objects.requireNonNull(FrontendInit.class.getClassLoader().getResourceAsStream("images/selectProjectIcon.png"))));
        viewMenuItem.setFitHeight(20);
        viewMenuItem.setPreserveRatio(true);
        FrontendConstants.menuItemSelectProject.setGraphic(viewMenuItem);
        FrontendConstants.menuItemSelectProject.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        FrontendConstants.menuItemSelectProject.setOnAction(e -> {
            try {
                CommandUtils.selectProject();
            } catch (IDEException ex) {
                throw new RuntimeException(ex);
            }
        });


        //items get added to contextMenuPackages
        AtomicReference<TreeItem<CustomItem>> tempTreeItem = new AtomicReference<>();
        FrontendConstants.treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //to avoid the addition of too many menuItems within the contextMenu
            if (FrontendConstants.contextMenuPackages.getItems().isEmpty() && FrontendConstants.contextMenuClasses.getItems().isEmpty()) {
                //menuItems get added to contextMenuPackages
                FrontendConstants.contextMenuPackages.getItems().addAll(CopyUtils.copyMenuItem(FrontendConstants.menuItemAddClass), CopyUtils.copyMenuItem(FrontendConstants.menuItemAddInterface),
                        CopyUtils.copyMenuItem(FrontendConstants.menuItemAddEnum), CopyUtils.copyMenuItem(FrontendConstants.menuItemAddPackage), CopyUtils.copyMenuItem(FrontendConstants.menuItemRename),
                        CopyUtils.copyMenuItem(FrontendConstants.menuItemDelete), CopyUtils.copyMenuItem(FrontendConstants.menutItemAddGit));
                //menuItems get added to contextMenuClasses
                FrontendConstants.contextMenuClasses.getItems().addAll(CopyUtils.copyMenuItem(FrontendConstants.menuItemRename), CopyUtils.copyMenuItem(FrontendConstants.menuItemDelete),
                        CopyUtils.copyMenuItem(FrontendConstants.menutItemAddGit));
            }
            if (newValue != null) {
                tempTreeItem.set(newValue);
                if (newValue.getValue().getTextArea() != null)
                    FrontendConstants.textArea.setText(newValue.getValue().getTextArea().getText());
            }
            //right-click on TreeItem
            tempTreeItem.get().getValue().getBoxText().setOnContextMenuRequested(e -> {
                //if TreeItem corresponds to project or package

                CommandUtils.setRetTreeItem(tempTreeItem.get());
                if (tempTreeItem.get().getValue().getClassType().equals(ClassType.PACKAGE) ||
                        tempTreeItem.get().getValue().getClassType().equals(ClassType.PROJECT)) {
                    FrontendConstants.contextMenuPackages.show(tempTreeItem.get().getValue(), e.getScreenX(), e.getScreenY());
                    if (tempTreeItem.get().getValue().getClassType().equals(ClassType.PACKAGE))
                        ClassBox.defaultValue = tempTreeItem.get().getValue().getLabelText();
                }
                // if TreeItem corresponds to class
                else
                    FrontendConstants.contextMenuClasses.show(tempTreeItem.get().getValue(), e.getScreenX(), e.getScreenY());

            });
        });


        /*
        everytime the text of textArea of a class is getting changed,
         the content in the file is getting updated as well
         */
        FrontendConstants.textArea.textProperty().addListener((observableValue, s, t1) -> {
            tempTreeItem.get().getValue().setText(t1);
            try {
                FileUtils.updateFile(tempTreeItem.get().getValue().getTextArea().getText(), tempTreeItem.get().getValue().getPath());
            } catch (IDEException e) {
                throw new RuntimeException(e);
            }
            CommandUtils.setImageLabel(tempTreeItem.get());
            FrontendConstants.label.setText(tempTreeItem.get().getValue().getLabelText());
        });

        scene.getStylesheets().add(Constants.ROOT_STYLE_SHEET);

        FrontendConstants.primaryStage.setScene(scene);
        FrontendConstants.primaryStage.show();
        LOG.info("Successfully built stage");
    }
}

