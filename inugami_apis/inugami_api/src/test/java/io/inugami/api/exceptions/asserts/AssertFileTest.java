package io.inugami.api.exceptions.asserts;

import io.inugami.interfaces.exceptions.DefaultErrorCode;
import io.inugami.interfaces.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static io.inugami.api.exceptions.Asserts.*;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssertFileTest {
    private static final String    FOLDER      = "/some/path";
    private static final String    ERR_MESSAGE = "some error";
    private static final ErrorCode ERROR_CODE  = DefaultErrorCode.buildUndefineError();


    @Mock
    private File file;

    // =========================================================================
    // FILE
    // =========================================================================
    @Test
    void assertFileExists_nominal() {
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        assertFileExists(file);
        assertFileExists(ERR_MESSAGE, file);
        assertFileExists(ERROR_CODE, file);
    }

    @Test
    void assertFileExists_withError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(false);
        assertThrows("file /some/path doesn't exists", () -> assertFileExists(file));
        assertThrows(ERR_MESSAGE, () -> assertFileExists(ERR_MESSAGE, file));
        assertThrows(ERROR_CODE, () -> assertFileExists(ERROR_CODE, file));
    }

    @Test
    void assertFileExists_withNotFileError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(false);

        assertThrows("file /some/path is a folder", () -> assertFileExists(file));
        assertThrows(ERR_MESSAGE, () -> assertFileExists(ERR_MESSAGE, file));
        assertThrows(ERROR_CODE, () -> assertFileExists(ERROR_CODE, file));
    }

    @Test
    void assertFileReadable_nominal() {
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        assertFileReadable(file);
        assertFileReadable(ERR_MESSAGE, file);
        assertFileReadable(ERROR_CODE, file);
    }


    @Test
    void assertFileReadable_withError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(false);

        assertThrows("file /some/path can't be read", () -> assertFileReadable(file));
        assertThrows(ERR_MESSAGE, () -> assertFileReadable(ERR_MESSAGE, file));
        assertThrows(ERROR_CODE, () -> assertFileReadable(ERROR_CODE, file));
    }


    @Test
    void assertFileWrite_nominal() {
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canWrite()).thenReturn(true);

        assertFileWrite(file);
        assertFileWrite(ERR_MESSAGE, file);
        assertFileWrite(ERROR_CODE, file);
    }

    @Test
    void assertFileWrite_withError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canWrite()).thenReturn(false);

        assertThrows("file /some/path can't be write", () -> assertFileWrite(file));
        assertThrows(ERR_MESSAGE, () -> assertFileWrite(ERR_MESSAGE, file));
        assertThrows(ERROR_CODE, () -> assertFileWrite(ERROR_CODE, file));
    }

    @Test
    void assertFileExecutable_nominal() {
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canExecute()).thenReturn(true);

        assertFileExecutable(file);
        assertFileExecutable(ERR_MESSAGE, file);
        assertFileExecutable(ERROR_CODE, file);
    }

    @Test
    void assertFileExecutable_withError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canExecute()).thenReturn(false);

        assertThrows("file /some/path can't be execute", () -> assertFileExecutable(file));
        assertThrows(ERR_MESSAGE, () -> assertFileExecutable(ERR_MESSAGE, file));
        assertThrows(ERROR_CODE, () -> assertFileExecutable(ERROR_CODE, file));
    }


    // =========================================================================
    // FOLDER
    // =========================================================================
    @Test
    void assertFolderExists_nominal() {
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(true);

        assertFolderExists(file);
        assertFolderExists(ERR_MESSAGE, file);
        assertFolderExists(ERROR_CODE, file);
    }

    @Test
    void assertFolderExists_withError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(false);

        assertThrows("folder /some/path doesn't exists", () -> assertFolderExists(file));
        assertThrows(ERR_MESSAGE, () -> assertFolderExists(ERR_MESSAGE, file));
        assertThrows(ERROR_CODE, () -> assertFolderExists(ERROR_CODE, file));
    }

    @Test
    void assertFolderExists_withoutFolderError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);

        assertThrows("folder /some/path is a file", () -> assertFolderExists(file));
        assertThrows(ERR_MESSAGE, () -> assertFolderExists(ERR_MESSAGE, file));
        assertThrows(ERROR_CODE, () -> assertFolderExists(ERROR_CODE, file));
    }
}