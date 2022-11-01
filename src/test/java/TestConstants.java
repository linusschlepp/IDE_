import app.utils.Constants;

/**
 * Constants-class, used for testing-purposes
 */
public final class TestConstants {

    public static final String CLASS_NAME = "TestClass";

    public static final String DIR_TEST_NAME = "test_dir";

    public static final String DIR_NAME = "test_1";

    public static final String RELATIVE_PATH_DIR = "src" + Constants.FILE_SEPARATOR + "test" + Constants.FILE_SEPARATOR + DIR_NAME;

    public static final String FILE_PATH = "src" + Constants.FILE_SEPARATOR + "test" + Constants.FILE_SEPARATOR + DIR_NAME + Constants.FILE_SEPARATOR + CLASS_NAME + Constants.JAVA_FILE_EXTENSION;

    public static final String FILE_CONTENT = "test_content\n\ntest_content";

    public static final String ENUM_CLASS = "public enum TestEnum {\n\n}";


}
