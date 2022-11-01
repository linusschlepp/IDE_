
import app.backend.ClassType;
import app.utils.Constants;
import app.utils.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class FileTest {


    @BeforeEach
    void prepare() throws IOException {
        // Creates temporary before testing
        TestUtils.createTempDirectory(TestConstants.RELATIVE_PATH_DIR);
    }


    @AfterEach
    void end() throws IOException {
        // Delete temporary directory after testing
        TestUtils.deleteTempDirectory(TestConstants.RELATIVE_PATH_DIR);
    }

    /**
     * Tests the behaviour of {@link FileUtils#createFile(String, String, String)}
     *
     * @throws IOException When error occurs
     */
    @Test
    void createFileTest() throws IOException {
        // Create temporary directory
        FileUtils.createFile(TestConstants.RELATIVE_PATH_DIR, TestConstants.FILE_CONTENT, TestConstants.CLASS_NAME);
        Assertions.assertTrue(TestUtils.checkIfFileExists(TestConstants.FILE_PATH));
        Assertions.assertEquals(TestConstants.FILE_CONTENT, TestUtils.readTempFile(TestConstants.FILE_PATH));
    }


    /**
     * Tests the behaviour of {@link FileUtils#getClassContent(String)}
     *
     */
    @Test
    void getClassContentTest() {
        FileUtils.createFile(TestConstants.RELATIVE_PATH_DIR, TestConstants.FILE_CONTENT, TestConstants.CLASS_NAME);
        Assertions.assertEquals(TestConstants.FILE_CONTENT, FileUtils.getClassContent(TestConstants.FILE_PATH));
    }

    /**
     * Tests the behaviour of {@link FileUtils#getClassType(File)}
     *
     */
    @Test
    void getClassTypeTest() {
        FileUtils.createFile(TestConstants.RELATIVE_PATH_DIR, TestConstants.ENUM_CLASS, TestConstants.CLASS_NAME);
        Assertions.assertEquals(ClassType.ENUM, FileUtils.getClassType(new File(TestConstants.FILE_PATH)));
    }

    /**
     * Tests the default-behaviour of {@link FileUtils#getClassType(File)}
     *
     */
    @Test
    void getClassTypeTestDefault() {
        FileUtils.createFile(TestConstants.RELATIVE_PATH_DIR, TestConstants.ENUM_CLASS, TestConstants.CLASS_NAME);
        // Default response should be class
        Assertions.assertEquals(ClassType.CLASS, FileUtils.getClassType(new File(TestConstants.FILE_PATH)));
    }

    /**
     * Tests the behaviour of {@link FileUtils#updateFile(String, String)}
     *
     * @throws IOException When error occurs
     */
    @Test
    void updateFileTest() throws IOException {
        FileUtils.createFile(TestConstants.RELATIVE_PATH_DIR, TestConstants.FILE_CONTENT, TestConstants.CLASS_NAME);
        final String classContentBefore = TestUtils.readTempFile(TestConstants.FILE_PATH);
        // Update file
        FileUtils.updateFile(TestConstants.FILE_CONTENT+"test", TestConstants.FILE_PATH);
        // File content should differ, due to the update of the file
        Assertions.assertNotEquals(classContentBefore, TestUtils.readTempFile(TestConstants.FILE_PATH));

    }

    /**
     * Tests the behaviour of {@link FileUtils#createFile(String, String, String, String, boolean)}
     *
     */
    @Test
    void createPackageTest() {
        FileUtils.createFile(TestConstants.RELATIVE_PATH_DIR+Constants.FILE_SEPARATOR, Constants.EMPTY_STRING, "", TestConstants.DIR_TEST_NAME ,true);
        final String tempDirPath = TestConstants.RELATIVE_PATH_DIR+Constants.FILE_SEPARATOR + TestConstants.DIR_TEST_NAME;
        // Created file should be a directory
        Assertions.assertTrue(TestUtils.isDir(tempDirPath));
        // Created file should exist
        Assertions.assertTrue(TestUtils.checkIfFileExists(tempDirPath));

    }



}
