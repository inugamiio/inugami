package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.MessagesFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;

import static io.inugami.api.exceptions.Asserts.assertNotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertFile {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final AssertFile INSTANCE = new AssertFile();

    private static final String FILE_NOT_EXISTS     = "file {0} doesn't exists";
    private static final String FILE_NOT_FILE       = "file {0} is a folder";
    private static final String FILE_NOT_READABLE   = "file {0} can't be read";
    private static final String FILE_NOT_WRITE      = "file {0} can't be write";
    private static final String FILE_NOT_EXECUTABLE = "file {0} can't be execute";

    private static final String FOLDER_NOT_EXISTS  = "folder {0} doesn't exists";
    private static final String FOLDER_NOT_IS_FILE = "folder {0} is a file";

    // =========================================================================
    // FILE
    // =========================================================================
    public void assertFileExists(final File path) {
        assertNotNull(() -> MessagesFormatter.format(FILE_NOT_EXISTS, path), path);
        if (!path.exists()) {
            AssertCommons.INSTANCE.throwException(MessagesFormatter.format(FILE_NOT_EXISTS, path));
        }

        if (!path.isFile()) {
            AssertCommons.INSTANCE.throwException(MessagesFormatter.format(FILE_NOT_FILE, path));
        }
    }

    public void assertFileExists(final String message, final File path) {
        assertNotNull(message, path);
        if (!path.exists()) {
            AssertCommons.INSTANCE.throwException(message);
        }

        if (!path.isFile()) {
            AssertCommons.INSTANCE.throwException(message);
        }
    }

    public void assertFileExists(final ErrorCode errorCode, final File path) {
        assertNotNull(errorCode, path);
        if (!path.exists()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }

        if (!path.isFile()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    public void assertFileReadable(final File path) {
        assertFileExists(path);
        if (!path.canRead()) {
            AssertCommons.INSTANCE.throwException(MessagesFormatter.format(FILE_NOT_READABLE, path));
        }
    }

    public void assertFileReadable(final String message, final File path) {
        assertFileExists(message, path);
        if (!path.canRead()) {
            AssertCommons.INSTANCE.throwException(message);
        }
    }

    public void assertFileReadable(final ErrorCode errorCode, final File path) {
        assertFileExists(errorCode, path);
        if (!path.canRead()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    public void assertFileWrite(final File path) {
        assertFileExists(path);
        if (!path.canWrite()) {
            AssertCommons.INSTANCE.throwException(MessagesFormatter.format(FILE_NOT_WRITE, path));
        }
    }

    public void assertFileWrite(final String message, final File path) {
        assertFileExists(message, path);
        if (!path.canWrite()) {
            AssertCommons.INSTANCE.throwException(message);
        }
    }

    public void assertFileWrite(final ErrorCode errorCode, final File path) {
        assertFileExists(errorCode, path);
        if (!path.canWrite()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    public void assertFileExecutable(final File path) {
        assertFileExists(path);
        if (!path.canExecute()) {
            AssertCommons.INSTANCE.throwException(MessagesFormatter.format(FILE_NOT_EXECUTABLE, path));
        }
    }

    public void assertFileExecutable(final String message, final File path) {
        assertFileExists(message, path);
        if (!path.canExecute()) {
            AssertCommons.INSTANCE.throwException(message);
        }
    }

    public void assertFileExecutable(final ErrorCode errorCode, final File path) {
        assertFileExists(errorCode, path);
        if (!path.canExecute()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

    // =========================================================================
    // FOLDER
    // =========================================================================
    public void assertFolderExists(final File path) {
        assertNotNull(() -> MessagesFormatter.format(FOLDER_NOT_EXISTS, path), path);
        if (!path.exists()) {
            AssertCommons.INSTANCE.throwException(MessagesFormatter.format(FOLDER_NOT_EXISTS, path));
        }

        if (!path.isDirectory()) {
            AssertCommons.INSTANCE.throwException(MessagesFormatter.format(FOLDER_NOT_IS_FILE, path));
        }
    }

    public void assertFolderExists(final String message, final File path) {
        assertNotNull(message, path);
        if (!path.exists()) {
            AssertCommons.INSTANCE.throwException(message);
        }

        if (!path.isDirectory()) {
            AssertCommons.INSTANCE.throwException(message);
        }
    }

    public void assertFolderExists(final ErrorCode errorCode, final File path) {
        assertNotNull(errorCode, path);
        if (!path.exists()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }

        if (!path.isDirectory()) {
            AssertCommons.INSTANCE.throwException(errorCode);
        }
    }

}
