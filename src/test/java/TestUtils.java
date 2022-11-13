import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Utils-class, containing helper-methods for testing
 */
public final class TestUtils {


    private TestUtils() {
        // Private-constructor as this is utils-class
    }

    /**
     * Checks if file corresponding to given path exists
     *
     * @param filePath Path of given file
     * @return true if file exists, false if not
     */
    public static boolean checkIfFileExists(final String filePath) {

        return new File(filePath).exists();
    }

    /**
     * Crates temporary directory for given path
     *
     * @param filePath Path, where directory is created
     * @throws IOException If error occurs
     */
    public static void createTempDirectory(final String filePath) throws IOException {

        Files.createDirectory(Paths.get(filePath));
    }


    /**
     * Deletes directory for given path
     *
     * @param dirPath Path, of given directory
     * @throws IOException If error occurs
     */
    public static void deleteTempDirectory(final String dirPath) throws IOException {

        try (Stream<Path> walk = Files.walk(Paths.get(dirPath))) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }


    /**
     * Reads content within a file
     *
     * @param filePath Path of file
     * @return Content of given file path
     * @throws IOException If error occurs
     */
    public static String readTempFile(final String filePath) throws IOException {

        return Files.readString(Paths.get(filePath));
    }

    /**
     * Checks if given path is a directory
     *
     * @param dirPath Path, for corresponding directory
     * @return True, if path is a directory, false if not
     */
    public static boolean isDir(final String dirPath) {

        return Files.isDirectory(Paths.get(dirPath));
    }


}
