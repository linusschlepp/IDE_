package app.utils;

import app.backend.CustomItem;
import app.frontend.AlertBox;
import app.frontend.GridPaneNIO;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GitUtils {

    private static final ProcessBuilder processBuilder = new ProcessBuilder().directory(new File(GridPaneNIO.path));


    /**
     * Initializes git repo in corresponding path
     *
     * @param path Path in which the git repo is initialized
     * @throws IOException if error occurs
     */
    public static void gitInit(String path) throws IOException {

        if (!checkIfInit(path)) {

            String[] command = new String[]{"cmd.exe", "/C", "start", "git", "init"};
            executeGitCommand(command);

            AlertBox.display(Alert.AlertType.INFORMATION, "Git-Repository has been successfully initialized");
        } else
            AlertBox.display(Alert.AlertType.WARNING, "Git-Repository has already been initialized");

    }

    /**
     * Checks if git repository has already been initialized
     *
     * @param path path in which we check
     * @return true if git-repo exists, false if not
     */
    public static boolean checkIfInit(String path) {
        return Files.exists(Paths.get(path + "\\" + ".git"));
    }

    /**
     * Creates commit within repo
     *
     * @param message Commit-message
     * @throws IOException if error occurs
     */
    public static void gitCommit(String message) throws IOException {

        if (checkIfInit(GridPaneNIO.path)) {

            String[] command = new String[]{"cmd.exe", "/C", "start", "cmd.exe", "git", "commit", "-m", "\""+message+
                    "\""};
            executeGitCommand(command);

        } else
            AlertBox.display(Alert.AlertType.WARNING, "A Git-Repository has to be initialized first");

        gitLog(GridPaneNIO.path);
    }

    /**
     * Prints out the git log
     *
     * @param path path in which the git-repo is located
     * @throws IOException if error occurs
     */
    public static void gitLog(String path) throws IOException {
        // ToDo: Implement method returning bufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(new ProcessBuilder().directory(new File(path))
                .command("cmd.exe", "/C", "start", "git", "log")
                .start().getInputStream()));

        br.lines().forEach(System.out::println);

    }

    /**
     * Adds file or package to git staging area
     *
     * @param treeItem item, which is added to git
     * @throws IOException if error occurs
     */
    public static void gitAdd(TreeItem<CustomItem> treeItem) throws IOException {

        if (checkIfInit(GridPaneNIO.path)) {
            String relativePathToAdd = treeItem.getValue().getPath().replace(GridPaneNIO.path+"\\",  "");
            new ProcessBuilder().directory(new File(GridPaneNIO.path))
                    .command("cmd.exe", "/C", "start", "git", "add", relativePathToAdd
                            ).start();
        } else
            AlertBox.display(Alert.AlertType.WARNING, "A Git-Repository has to be initialized first");
    }


    /**
     * Executes given git-command
     *
     * @param command Command, which is executed
     * @throws IOException if error occurs
     */
    private static void executeGitCommand(String... command) throws IOException {
        processBuilder.command(command).start();
    }


}


