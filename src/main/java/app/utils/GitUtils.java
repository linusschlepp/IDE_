package app.utils;

import app.backend.CustomItem;
import app.frontend.AlertBox;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class GitUtils {

    private static final Logger LOG = LoggerFactory.getLogger(GitUtils.class);
    private static final ProcessBuilder processBuilder = new ProcessBuilder().directory(new File(FrontendConstants.path));

    private GitUtils() {
        //private Constructor as this is an Utils-class
    }

    /**
     * Initializes git repo in corresponding path
     *
     * @param path Path in which the git repo is initialized
     * @throws IOException if error occurs
     */
    public static void gitInit(final String path) throws IOException {

        if (!checkIfInit(path)) {

            String[] command = new String[]{Constants.CMD, Constants.C, Constants.START, Constants.GIT, Constants.GIT_INIT};
            executeGitCommand(command);

            AlertBox.display(Alert.AlertType.INFORMATION, Constants.INIT_GIT_REPO_SUCCESS);
        } else
            AlertBox.display(Alert.AlertType.WARNING, Constants.INIT_GIT_REPO_ALREADY);

    }

    /**
     * Checks if git repository has already been initialized
     *
     * @param path path in which we check
     * @return true if git-repo exists, false if not
     */
    public static boolean checkIfInit(final String path) {
        return Files.exists(Paths.get(path + Constants.FILE_SEPARATOR + Constants.GIT_DIR));
    }

    /**
     * Creates commit within repo
     *
     * @param message Commit-message
     * @throws IOException if error occurs
     */
    public static void gitCommit(final String message) throws IOException {

        if (checkIfInit(FrontendConstants.path)) {

            String[] command = new String[]{Constants.CMD, Constants.C, Constants.START, Constants.CMD,
                    Constants.GIT, Constants.COMMIT, Constants.COMMIT_MESSAGE, "\""+message+
                    "\""};
            executeGitCommand(command);

        } else
            AlertBox.display(Alert.AlertType.WARNING, Constants.INIT_GIT_REPO_FIRST);

        gitLog(FrontendConstants.path);
    }

    /**
     * Prints out the git log
     *
     * @param path path in which the git-repo is located
     * @throws IOException if error occurs
     */
    public static void gitLog(final String path) throws IOException {
        // ToDo: Implement method returning bufferedreader
        BufferedReader br = new BufferedReader(new InputStreamReader(new ProcessBuilder().directory(new File(path))
                .command(Constants.CMD, Constants.C, Constants.START, Constants.GIT, Constants.LOG)
                .start().getInputStream()));

    }

    /**
     * Adds file or package to git staging area
     *
     * @param treeItem item, which is added to git
     * @throws IOException if error occurs
     */
    public static void gitAdd(final TreeItem<CustomItem> treeItem) throws IOException {

        if (checkIfInit(FrontendConstants.path)) {
            String relativePathToAdd = treeItem.getValue().getPath().replace(FrontendConstants.path+Constants.FILE_SEPARATOR,
                    Constants.EMPTY_STRING);

            String[] command = {Constants.CMD, Constants.C, Constants.START, Constants.GIT, Constants.GIT_ADD, relativePathToAdd};

            executeGitCommand(command);
        } else
            AlertBox.display(Alert.AlertType.WARNING, Constants.INIT_GIT_REPO_FIRST);
    }


    /**
     * Executes given git-command
     *
     * @param command Command, which is executed
     * @throws IOException if error occurs
     */
    private static void executeGitCommand(final String... command) throws IOException {
        processBuilder.command(command).start();

    }




}


