package app.utils;

import app.backend.ClassType;
import app.frontend.FrontendInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

import static app.backend.ClassType.*;
import static app.backend.ClassType.CLASS;

public final class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
        // private constructor as this is an utils-class
    }

    /**
     * Creates files in the requested locations
     *
     * @param classContent Content of the class/ file
     * @param className    Name of the class/ file
     */
    public static void createFile(final String path, final String classContent, final String className) {

        try {
            final Path concatenatedPath = Paths.get(path + Constants.FILE_SEPARATOR + className + Constants.JAVA_FILE_EXTENSION);
            if (!Files.exists(concatenatedPath))
                Files.createFile(concatenatedPath);
            Files.writeString(concatenatedPath, classContent);
            LOG.info("Created file: {} in {}", className, path);
        } catch (final IOException e) {
            LOG.error("File: [{}] in [{}] could not be created", className,
                    path);
        }
    }

    /**
     * Output-folder is getting created, files are getting stored in it
     *
     * @throws IOException New files are getting instantiated
     */
    public static void generateOutputFolder(final String path) throws IOException {


        final Path concatenatedPath = Paths.get(path + Constants.FILE_SEPARATOR + Constants.OUTPUT_DIR);
        //Output-Folder gets cleared before every execution of the program
        if (Files.exists(concatenatedPath))
            try (Stream<Path> walk = Files.walk(concatenatedPath)) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }

        try {
            //if the output-folder doesn't exist yet, it is getting created
            Files.createDirectory(concatenatedPath);
        } catch (FileAlreadyExistsException e) {
            LOG.error("File: [{}] already exists and could therefore not be created", path);
        }
    }

    /**
     * Copies directories the contents of the source-directory into the directory
     *
     * @param sourceDirectoryLocation      Path of the individual project
     * @param destinationDirectoryLocation Path of the output folder
     * @throws IOException NIO-segments are getting used
     */
    public static void copyDirectory(final String sourceDirectoryLocation, final String destinationDirectoryLocation) throws IOException {

        try (Stream<Path> walk = Files.walk(Paths.get(sourceDirectoryLocation))) {
            walk.forEach(source -> {
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

    }


    /**
     * Returns the content of a file as a String variable
     *
     * @param filePath Path of the file, which content is needed
     * @return Content of given file
     */
    public static String getClassContent(final String filePath) {

        try {
            return Files.readString(Paths.get(filePath));
        } catch (final Exception e) {
            LOG.error("File: [{}] could not be read", filePath);
        }
        return "";
    }


    /**
     * When the text in the textArea of each class changes, the content of the file is getting changed as well
     *
     * @param fileContent content of the file/ class
     * @param filePath  the location of the file
     */
    public static void updateFile(final String fileContent, final String filePath) {
        Path newPath;
        newPath = filePath.contains(Constants.JAVA_FILE_EXTENSION) ? Paths.get(filePath) :
                Paths.get(filePath + Constants.JAVA_FILE_EXTENSION);
        try {
            Files.writeString(newPath, fileContent);
        } catch (Exception ex) {
            LOG.error("File: [{}] could not be updated", filePath);
        }

    }


    /**
     * Creates Files within packages  in the fileSystem
     *
     * @param classContent Content of the class/ file
     * @param packageName  Name of the package/ directory
     * @param className    Name of the file
     * @param isPackage    Checks if the file is a package and therefore a directory has to be created
     */
    public static void createFile(final String path, final String classContent, final String packageName, final String className, final boolean isPackage) {

        try {
            if (!isPackage) {
                Path newFile = Files.createFile(Paths.get(path + packageName + className + Constants.JAVA_FILE_EXTENSION));
                Files.writeString(newFile, classContent);
            } else {
                if (!Files.exists(Paths.get(path + packageName + className)))
                    Files.createDirectory(Paths.get(path + packageName + className));
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("File: [{}] in directory: [{}] could not be created", className, packageName);
        }
    }

    /**
     * Determines the class-type by checking the content of the files for the keywords: enum, interface and class
     *
     * @param entry File, which is getting checked
     * @return The corresponding classType
     */
    public static ClassType getClassType(final File entry) {

        try {
            try (Stream<String> lines = Files.lines(Paths.get(entry.getPath()))) {

                String fileContent = lines.toList().toString();

                if (fileContent.contains(Constants.CLASS_STRING))
                    return CLASS;
                if (fileContent.contains(Constants.ENUM_STRING))
                    return ENUM;
                if (fileContent.contains(Constants.INTERFACE_STRING))
                    return INTERFACE;
            }

            } catch (IOException e) {
                LOG.error("ClassType of file: [{}] could not be determined", entry.getPath());
            }
            // default return value
            return CLASS;

    }


    /**
     * Returns the path, where the IDE is stored
     *
     * @return RelativePath of the IDE
     */
    public static String getRelativePath() {

        try {
            return new File(FrontendInit.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile().getPath();
        } catch (URISyntaxException e) {
            LOG.error("String could not be passed into URI");
        }

        return Constants.EMPTY_STRING;
    }


}
