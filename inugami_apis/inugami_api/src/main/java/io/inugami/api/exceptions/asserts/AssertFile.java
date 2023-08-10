package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.MessagesFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertFile {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

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
    public static void assertFileExists(final File path) {
        AssertNull.assertNotNull(() -> MessagesFormatter.format(FILE_NOT_EXISTS, path), path);
        if (!path.exists()) {
            AssertCommons.throwException(MessagesFormatter.format(FILE_NOT_EXISTS, path));
        }

        if (!path.isFile()) {
            AssertCommons.throwException(MessagesFormatter.format(FILE_NOT_FILE, path));
        }
    }

    public static void assertFileExists(final String message, final File path) {
        AssertNull.assertNotNull(message, path);
        if (!path.exists()) {
            AssertCommons.throwException(message);
        }

        if (!path.isFile()) {
            AssertCommons.throwException(message);
        }
    }

    public static void assertFileExists(final ErrorCode errorCode, final File path) {
        AssertNull.assertNotNull(errorCode, path);
        if (!path.exists()) {
            AssertCommons.throwException(errorCode);
        }

        if (!path.isFile()) {
            AssertCommons.throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    public static void assertFileReadable(final File path) {
        assertFileExists(path);
        if (!path.canRead()) {
            AssertCommons.throwException(MessagesFormatter.format(FILE_NOT_READABLE, path));
        }
    }

    public static void assertFileReadable(final String message, final File path) {
        assertFileExists(message, path);
        if (!path.canRead()) {
            AssertCommons.throwException(message);
        }
    }

    public static void assertFileReadable(final ErrorCode errorCode, final File path) {
        assertFileExists(errorCode, path);
        if (!path.canRead()) {
            AssertCommons.throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    public static void assertFileWrite(final File path) {
        assertFileExists(path);
        if (!path.canWrite()) {
            AssertCommons.throwException(MessagesFormatter.format(FILE_NOT_WRITE, path));
        }
    }

    public static void assertFileWrite(final String message, final File path) {
        assertFileExists(message, path);
        if (!path.canWrite()) {
            AssertCommons.throwException(message);
        }
    }

    public static void assertFileWrite(final ErrorCode errorCode, final File path) {
        assertFileExists(errorCode, path);
        if (!path.canWrite()) {
            AssertCommons.throwException(errorCode);
        }
    }

    // -------------------------------------------------------------------------
    public static void assertFileExecutable(final File path) {
        assertFileExists(path);
        if (!path.canExecute()) {
            AssertCommons.throwException(MessagesFormatter.format(FILE_NOT_EXECUTABLE, path));
        }
    }

    public static void assertFileExecutable(final String message, final File path) {
        assertFileExists(message, path);
        if (!path.canExecute()) {
            AssertCommons.throwException(message);
        }
    }

    public static void assertFileExecutable(final ErrorCode errorCode, final File path) {
        assertFileExists(errorCode, path);
        if (!path.canExecute()) {
            AssertCommons.throwException(errorCode);
        }
    }

    // =========================================================================
    // FOLDER
    // =========================================================================
    public static void assertFolderExists(final File path) {
        AssertNull.assertNotNull(() -> MessagesFormatter.format(FOLDER_NOT_EXISTS, path), path);
        if (!path.exists()) {
            AssertCommons.throwException(MessagesFormatter.format(FOLDER_NOT_EXISTS, path));
        }

        if (!path.isDirectory()) {
            AssertCommons.throwException(MessagesFormatter.format(FOLDER_NOT_IS_FILE, path));
        }
    }

    public static void assertFolderExists(final String message, final File path) {
        AssertNull.assertNotNull(message, path);
        if (!path.exists()) {
            AssertCommons.throwException(message);
        }

        if (!path.isDirectory()) {
            AssertCommons.throwException(message);
        }
    }

    public static void assertFolderExists(final ErrorCode errorCode, final File path) {
        AssertNull.assertNotNull(errorCode, path);
        if (!path.exists()) {
            AssertCommons.throwException(errorCode);
        }

        if (!path.isDirectory()) {
            AssertCommons.throwException(errorCode);
        }
    }

}
