package app.utils;

import app.frontend.AlertBox;
import app.frontend.CommitBox;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GitUtils {

    /**
     * Initializes git repo in corresponding path
     *
     * @param path Path in which the git repo is initialized
     * @throws IOException if error occurs
     */
    public static void gitInit(String path) throws IOException {

        if (!checkIfInit(path)) {
            new ProcessBuilder().directory(new File(path))
                    .command("cmd.exe", "/C", "start", "git", "init").start();

            AlertBox.display(Alert.AlertType.INFORMATION, "Git-Repository has been successfully initialized");
        } else
            AlertBox.display(Alert.AlertType.WARNING ,"Git-Repository has already been initialized");

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
     * @param path path, where git-repository is located
     * @param message Commit-message
     * @throws IOException if error occurs
     */
    public static void gitCommit(String path, String message) throws IOException {

        if (checkIfInit(path)) {

            //TODO: Remove cmd.exe /K cmd has to disappear after commit has been made
            new ProcessBuilder().directory(new File(path))
                    .command("cmd.exe", "/C", "start", "cmd.exe", "/K", "git", "commit", "-m", "\"", message,
                            "\"").start();

        } else
            AlertBox.display(Alert.AlertType.WARNING, "A Git-Repository has to be initialized first");

        gitLog(path);
    }

    /**
     * Prints out the git log
     *
     * @param path path in which the git-repo is located
     * @throws IOException
     */
    public static void gitLog(String path) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new ProcessBuilder().directory(new File(path))
                .command("cmd.exe", "/C", "start", "git", "log")
                .start().getInputStream()));

        br.lines().forEach(System.out::println);

    }

}
