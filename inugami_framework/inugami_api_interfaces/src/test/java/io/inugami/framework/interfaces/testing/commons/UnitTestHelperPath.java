package io.inugami.framework.interfaces.testing.commons;

import io.inugami.framework.interfaces.exceptions.UncheckedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperPath {


    // =========================================================================
    // BUILD PATH
    // =========================================================================
    static File buildTestFilePath(final String... filePathParts) {
        final List<String> parts = new ArrayList<>(Arrays.asList("src", "test", "resources"));
        Arrays.asList(filePathParts)
              .forEach(parts::add);
        return buildPath(parts.toArray(new String[]{}));
    }

    static File buildIntegrationTestFilePath(final String... filePathParts) {
        final List<String> parts = new ArrayList<>(Arrays.asList("src", "test_it", "resources"));
        Arrays.asList(filePathParts)
              .forEach(parts::add);
        return buildPath(parts.toArray(new String[]{}));
    }

    static File buildPath(final String... parts) {
        if (parts == null) {
            return null;
        }

        final File basePath = new File(".");

        final String[] allPathParts = new String[parts.length + 1];
        allPathParts[0] = basePath.getAbsolutePath();
        System.arraycopy(parts, 0, allPathParts, 1, parts.length);

        return new File(String.join(File.separator, allPathParts));
    }

    static File resolveFullPath(final File file) {
        try {
            return file == null ? null : file.getCanonicalFile();
        } catch (final IOException e) {
            throw new UncheckedException(e.getMessage(), e);
        }
    }
}
