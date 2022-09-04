package app.utils;

import app.backend.ClassType;
import app.frontend.GridPaneNIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Collectors;

import static app.backend.ClassType.*;
import static app.backend.ClassType.CLASS;

public class FileUtils {


    private static final Logger LOG  = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Creates files in the requested locations
     *
     * @param classContent content of the class/ file
     * @param className    name of the class/ file
     */
    public static void createFile(String path, String classContent, String className) {

        try {
            if (!Files.exists(Paths.get(path + Constants.FILE_SEPARATOR + className + Constants.JAVA_FILE_EXTENSION)))
                Files.createFile(Paths.get(path + Constants.FILE_SEPARATOR + className + Constants.JAVA_FILE_EXTENSION));
            Files.writeString(Paths.get(path + Constants.FILE_SEPARATOR + className + Constants.JAVA_FILE_EXTENSION), classContent);
        } catch (IOException e) {
            LOG.error("File: [{}] in [{}] could not be created", className, path);
        }
    }

    /**
     * Output-folder is getting created, files are getting stored in it
     *
     * @throws IOException new files are getting instantiated
     */
    public static void generateOutputFolder(String path) throws IOException {

        //Output-Folder gets deleted before every execution of the program
        if (Files.exists(Paths.get(path + Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR)))
            Files.walk(Paths.get(path + Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR)).sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

        try {
            //if the output-folder doesn't exist yet, it is getting created
            Files.createDirectory(Paths.get(path + Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR));
        }catch(FileAlreadyExistsException e){
            LOG.error("File in path: [{}] already exists and could therefore not be created", path);
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
                        if (!source.getFileName().toString().equals(Constants.OUTPUT_DIR))
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        LOG.error("Directory: [{}] could not be copied into: [{}]", sourceDirectoryLocation, destinationDirectoryLocation);
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
            Files.lines(Paths.get(String.valueOf(file))).forEach(s -> sb.append(s).append(Constants.NEW_LINE));
        } catch (Exception e) {
            LOG.error("File: [{}] could not be read", file.getPath());
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
        newPath = pathOfFile.contains(Constants.JAVA_FILE_EXTENSION) ? Paths.get(pathOfFile) : 
                Paths.get(pathOfFile + Constants.JAVA_FILE_EXTENSION);
        try {
            Files.writeString(newPath, fileContent);
        } catch (Exception ex) {
            LOG.error("File in path: [{}] could not be updated",pathOfFile);
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
                Path newFile = Files.createFile(Paths.get(path + packageName + className + Constants.JAVA_FILE_EXTENSION));
                Files.writeString(newFile, classContent);
            } else {
                if (!Files.exists(Paths.get(path + packageName + className)))
                    Files.createDirectory(Paths.get(path + packageName + className));
            }
        } catch (IOException e) {
            LOG.error("File: [{}] in directory: [{}] could not be created", className, packageName);
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

            if (fileContent.contains(Constants.CLASS_STRING))
                return CLASS;
            if (fileContent.contains(Constants.ENUM_STRING))
                return ENUM;
            if (fileContent.contains(Constants.INTERFACE_STRING))
                return INTERFACE;

        } catch (IOException e) {
           LOG.error("ClassType of [{}] could not be determined", entry.getPath());
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
            LOG.error("String could not be passed into URI");
        }

        return Constants.EMPTY_STRING;
    }


}
