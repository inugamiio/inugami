package io.inugami.framework.interfaces.testing.exceptions.asserts;


import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

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
        Asserts.assertFileExists(file);
        Asserts.assertFileExists(ERR_MESSAGE, file);
        Asserts.assertFileExists(ERROR_CODE, file);
    }

    @Test
    void assertFileExists_withError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(false);
        UnitTestHelper.assertThrows("file /some/path doesn't exists", () -> Asserts.assertFileExists(file));
        UnitTestHelper.assertThrows(ERR_MESSAGE, () -> Asserts.assertFileExists(ERR_MESSAGE, file));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertFileExists(ERROR_CODE, file));
    }

    @Test
    void assertFileExists_withNotFileError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(false);

        UnitTestHelper.assertThrows("file /some/path is a folder", () -> Asserts.assertFileExists(file));
        UnitTestHelper.assertThrows(ERR_MESSAGE, () -> Asserts.assertFileExists(ERR_MESSAGE, file));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertFileExists(ERROR_CODE, file));
    }

    @Test
    void assertFileReadable_nominal() {
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        Asserts.assertFileReadable(file);
        Asserts.assertFileReadable(ERR_MESSAGE, file);
        Asserts.assertFileReadable(ERROR_CODE, file);
    }


    @Test
    void assertFileReadable_withError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(false);

        UnitTestHelper.assertThrows("file /some/path can't be read", () -> Asserts.assertFileReadable(file));
        UnitTestHelper.assertThrows(ERR_MESSAGE, () -> Asserts.assertFileReadable(ERR_MESSAGE, file));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertFileReadable(ERROR_CODE, file));
    }


    @Test
    void assertFileWrite_nominal() {
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canWrite()).thenReturn(true);

        Asserts.assertFileWrite(file);
        Asserts.assertFileWrite(ERR_MESSAGE, file);
        Asserts.assertFileWrite(ERROR_CODE, file);
    }

    @Test
    void assertFileWrite_withError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canWrite()).thenReturn(false);

        UnitTestHelper.assertThrows("file /some/path can't be write", () -> Asserts.assertFileWrite(file));
        UnitTestHelper.assertThrows(ERR_MESSAGE, () -> Asserts.assertFileWrite(ERR_MESSAGE, file));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertFileWrite(ERROR_CODE, file));
    }

    @Test
    void assertFileExecutable_nominal() {
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canExecute()).thenReturn(true);

        Asserts.assertFileExecutable(file);
        Asserts.assertFileExecutable(ERR_MESSAGE, file);
        Asserts.assertFileExecutable(ERROR_CODE, file);
    }

    @Test
    void assertFileExecutable_withError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canExecute()).thenReturn(false);

        UnitTestHelper.assertThrows("file /some/path can't be execute", () -> Asserts.assertFileExecutable(file));
        UnitTestHelper.assertThrows(ERR_MESSAGE, () -> Asserts.assertFileExecutable(ERR_MESSAGE, file));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertFileExecutable(ERROR_CODE, file));
    }


    // =========================================================================
    // FOLDER
    // =========================================================================
    @Test
    void assertFolderExists_nominal() {
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(true);

        Asserts.assertFolderExists(file);
        Asserts.assertFolderExists(ERR_MESSAGE, file);
        Asserts.assertFolderExists(ERROR_CODE, file);
    }

    @Test
    void assertFolderExists_withError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(false);

        UnitTestHelper.assertThrows("folder /some/path doesn't exists", () -> Asserts.assertFolderExists(file));
        UnitTestHelper.assertThrows(ERR_MESSAGE, () -> Asserts.assertFolderExists(ERR_MESSAGE, file));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertFolderExists(ERROR_CODE, file));
    }

    @Test
    void assertFolderExists_withoutFolderError() {
        when(file.toString()).thenReturn(FOLDER);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);

        UnitTestHelper.assertThrows("folder /some/path is a file", () -> Asserts.assertFolderExists(file));
        UnitTestHelper.assertThrows(ERR_MESSAGE, () -> Asserts.assertFolderExists(ERR_MESSAGE, file));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertFolderExists(ERROR_CODE, file));
    }
}