package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.MessagesFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertFiles {
    private static final String FILE_PATH_EMPTY   = "file {0} mustn't be empty";
    private static final String FILE_IS_REQUIRED  = "the file is required";
    private static final String FILE_NOT_EXISTS   = "the file {0} doesn't exists";
    private static final String FILE_NOT_READABLE = "the file {0} isn't readable";
    private static final String FILE_NOT_WRITABLE = "file {0} can't be write";
    private static final String FILE_IS_FOLDER    = "the file is a folder : {0}";
    private static final String FILE_IS_FILE      = "The file isn't a directory {0}";


    //  =======================================================================
    // ASSERT EXISTS
    //  =======================================================================
    public static File assertExists(final String path) {
        Asserts.assertNotEmpty(MessagesFormatter.format(FILE_PATH_EMPTY, path), path);
        File result = new File(path);
        assertExists(result);
        return result;
    }

    public static void assertExists(final File file) {
        Asserts.assertNotNull(FILE_IS_REQUIRED, file);
        Asserts.assertTrue(MessagesFormatter.format(FILE_NOT_EXISTS, file), file.exists());
    }

    public static File assertExists(final ErrorCode errorCode, final String path) {
        final ErrorCode currentErrorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
        Asserts.assertNotEmpty(currentErrorCode.addDetail(FILE_PATH_EMPTY, path), path);
        File result = new File(path);
        assertExists(currentErrorCode, result);
        return result;
    }

    public static void assertExists(final ErrorCode errorCode, final File file) {
        final ErrorCode currentErrorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
        Asserts.assertNotNull(currentErrorCode.addDetail(FILE_IS_REQUIRED), file);
        Asserts.assertTrue(currentErrorCode.addDetail(FILE_NOT_EXISTS, file), file.exists());
    }


    //  =======================================================================
    // ASSERT FILE / DIRECTORY
    //  =======================================================================
    public static File assertFile(final String path) {
        Asserts.assertNotEmpty(MessagesFormatter.format(FILE_PATH_EMPTY, path), path);
        File result = new File(path);
        assertFile(result);
        return result;
    }

    public static void assertFile(final File file) {
        Asserts.assertNotNull(FILE_IS_REQUIRED, file);
        Asserts.assertTrue(MessagesFormatter.format(FILE_IS_FOLDER, file), file.isFile());
    }

    //
    public static File assertFile(final ErrorCode errorCode, final String path) {
        final ErrorCode currentErrorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
        Asserts.assertNotEmpty(currentErrorCode.addDetail(FILE_PATH_EMPTY, path), path);
        File result = new File(path);
        assertFile(currentErrorCode, result);
        return result;
    }

    public static void assertFile(final ErrorCode errorCode, final File file) {
        final ErrorCode currentErrorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
        Asserts.assertNotNull(currentErrorCode.addDetail(FILE_IS_REQUIRED), file);
        Asserts.assertTrue(currentErrorCode.addDetail(FILE_IS_FOLDER, file), file.isFile());
    }


    public static File assertDirectory(final String path) {
        Asserts.assertNotEmpty(MessagesFormatter.format(FILE_PATH_EMPTY, path), path);
        File result = new File(path);
        assertDirectory(result);
        return result;
    }

    public static void assertDirectory(final File file) {
        Asserts.assertNotNull(FILE_IS_REQUIRED, file);
        Asserts.assertTrue(MessagesFormatter.format(FILE_IS_FILE, file), file.isDirectory());
    }

    public static File assertDirectory(final ErrorCode errorCode, final String path) {
        final ErrorCode currentErrorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
        Asserts.assertNotEmpty(currentErrorCode.addDetail(FILE_PATH_EMPTY, path), path);
        File result = new File(path);
        assertDirectory(currentErrorCode, result);
        return result;
    }


    public static void assertDirectory(final ErrorCode errorCode, final File file) {
        final ErrorCode currentErrorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
        Asserts.assertNotNull(currentErrorCode.addDetail(FILE_IS_REQUIRED), file);
        Asserts.assertTrue(currentErrorCode.addDetail(FILE_IS_FILE, file), file.isDirectory());
    }


    //  =======================================================================
    // ASSERT FILE READABLE
    //  =======================================================================
    public static File assertFileReadable(final String path) {
        Asserts.assertNotEmpty(MessagesFormatter.format(FILE_PATH_EMPTY, path), path);
        File result = new File(path);
        assertFileReadable(result);
        return result;
    }

    public static void assertFileReadable(final File file) {
        Asserts.assertNotNull(FILE_IS_REQUIRED, file);
        Asserts.assertTrue(MessagesFormatter.format(FILE_NOT_EXISTS, file), file.exists());
        Asserts.assertTrue(MessagesFormatter.format(FILE_NOT_READABLE, file), file.canRead());
    }

    public static File assertFileReadable(final ErrorCode errorCode, final String path) {
        final ErrorCode currentErrorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
        Asserts.assertNotEmpty(currentErrorCode.addDetail(FILE_PATH_EMPTY, path), path);
        File result = new File(path);
        assertFileReadable(currentErrorCode, result);
        return result;
    }

    public static void assertFileReadable(final ErrorCode errorCode, final File file) {
        final ErrorCode currentErrorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
        Asserts.assertNotNull(currentErrorCode.addDetail(FILE_IS_REQUIRED), file);
        Asserts.assertTrue(currentErrorCode.addDetail(FILE_NOT_EXISTS, file), file.exists());
        Asserts.assertTrue(currentErrorCode.addDetail(FILE_NOT_READABLE, file), file.canRead());
    }


    //  =======================================================================
    // ASSERT FILE WRITABLE
    //  =======================================================================
    public static File assertFileWritable(final String path) {
        Asserts.assertNotEmpty(MessagesFormatter.format(FILE_PATH_EMPTY, path), path);
        File result = new File(path);
        assertFileWritable(result);
        return result;
    }

    public static void assertFileWritable(final File file) {
        Asserts.assertNotNull(FILE_IS_REQUIRED, file);
        Asserts.assertTrue(MessagesFormatter.format(FILE_NOT_EXISTS, file), file.exists());
        Asserts.assertTrue(MessagesFormatter.format(FILE_NOT_WRITABLE, file), file.canWrite());
    }

    public static File assertFileWritable(final ErrorCode errorCode, final String path) {
        final ErrorCode currentErrorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
        Asserts.assertNotEmpty(currentErrorCode.addDetail(FILE_PATH_EMPTY, path), path);
        File result = new File(path);
        assertFileWritable(currentErrorCode, result);
        return result;
    }

    public static void assertFileWritable(final ErrorCode errorCode, final File file) {
        final ErrorCode currentErrorCode = errorCode == null ? DefaultErrorCode.buildUndefineError() : errorCode;
        Asserts.assertNotNull(currentErrorCode.addDetail(FILE_IS_REQUIRED), file);
        Asserts.assertTrue(currentErrorCode.addDetail(FILE_NOT_EXISTS, file), file.exists());
        Asserts.assertTrue(currentErrorCode.addDetail(FILE_NOT_WRITABLE, file), file.canWrite());
    }
}
