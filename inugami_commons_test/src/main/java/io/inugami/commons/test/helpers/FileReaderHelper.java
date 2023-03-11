package io.inugami.commons.test.helpers;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.MessagesFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileReaderHelper {



    public static String loadRelativeFile(final String relativePath) {
        return loadRelativeFile(relativePath, CommonsHelper.UTF_8);
    }

    public static String loadRelativeFile(final String relativePath, Charset charset) {
        if (relativePath == null) {
            throw new RuntimeException("can't read file from null relative path!");
        }

        final File path = PathHelper.buildTestFilePath(relativePath.split("/"));
        return path.exists() ? readFile(path, charset) : null;
    }

    public static String readFile(final File file) {
        return readFile(file, CommonsHelper.UTF_8);
    }

    public static String readFile(final File file, Charset charset) {
        String result = null;
        try {
            result = FileUtils.readFileToString(file, CommonsHelper.UTF_8);
        } catch (final IOException e) {
            CommonsHelper.throwException(e);
        }
        return result;
    }


}
