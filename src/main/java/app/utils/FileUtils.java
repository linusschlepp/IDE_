package app.utils;

import app.backend.ClassType;
import app.frontend.GridPaneNIO;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Collectors;

import static app.backend.ClassType.*;
import static app.backend.ClassType.CLASS;

public class FileUtils {


    /**
     * Creates files in the requested locations
     *
     * @param classContent content of the class/ file
     * @param className    name of the class/ file
     */
    public static void createFile(String path, String classContent, String className) {

        try {
            if (!Files.exists(Paths.get(path + "\\" + className + ".java")))
                Files.createFile(Paths.get(path + "\\" + className + ".java"));
            Files.writeString(Paths.get(path + "\\" + className + ".java"), classContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Output-folder is getting created, files are getting stored in it
     *
     * @throws IOException new files are getting instantiated
     */
    public static void generateOutputFolder(String path) throws IOException {

        //Output-Folder gets deleted before every execution of the program
        if (Files.exists(Paths.get(path + "\\" + "output")))
            Files.walk(Paths.get(path + "\\" + "output")).sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

        try {
            //if the output-folder doesn't exist yet, it is getting created
            Files.createDirectory(Paths.get(path + "\\" + "output"));
        }catch(FileAlreadyExistsException ignored){

        }


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
     * Returns the content of a file as a String variable
     *
     * @param file the file, which content is needed
     * @return content of file-parameter as String
     */
    public static String getClassContent(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            Files.lines(Paths.get(String.valueOf(file))).forEach(s -> sb.append(s).append("\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * When the text in the textArea of each class changes, the content of the file is getting changed as well
     *
     * @param fileContent content of the file/ class
     * @param pathOfFile  the location of the file
     */
    public static void updateFile(String fileContent, String pathOfFile) {


        Path newPath;
        newPath = pathOfFile.contains(".java") ? Paths.get(pathOfFile) : Paths.get(pathOfFile + ".java");
        try {
            Files.writeString(newPath, fileContent);
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
    public static void createFile(String path, String classContent, String packageName, String className, boolean isPackage) {

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
     * Determines the class-type by checking the content of the files for the keywords: enum, interface and class
     *
     * @param entry file, which is getting checked
     * @return the corresponding classType
     */
    public static ClassType checkForClassType(File entry) {

        try {
            String fileContent = Files.lines(Paths.get(entry.getPath())).collect(Collectors.toList()).toString();

            if (fileContent.contains("class"))
                return CLASS;
            if (fileContent.contains("enum"))
                return ENUM;
            if (fileContent.contains("interface"))
                return INTERFACE;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return CLASS;


    }


    /**
     * Returns the path, where the IDE is stored
     *
     * @return relativePath of the IDE
     */
    public static String getRelativePath() {

        try {
            return new File(GridPaneNIO.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return "";
    }


}
