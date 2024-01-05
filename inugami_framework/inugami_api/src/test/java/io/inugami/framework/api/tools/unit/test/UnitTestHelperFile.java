package io.inugami.framework.api.tools.unit.test;

import io.inugami.framework.interfaces.exceptions.Asserts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperFile {
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    // =========================================================================
    // BUILD PATH
    // =========================================================================
    static String readFileIntegration(final String relativePath) {
        if (relativePath == null) {
            throw new RuntimeException("can't read file from null relative path!");
        }

        File path = null;
        try {
            path = UnitTestHelperPath.buildIntegrationTestFilePath(relativePath.split("/")).getCanonicalFile();
        } catch (final IOException e) {
            UnitTestHelperException.throwException(e);
        }
        Asserts.assertFileReadable(path);
        return readFile(path);
    }

    static String readFileRelative(final String relativePath) {
        if (relativePath == null) {
            throw new RuntimeException("can't read file from null relative path!");
        }

        File path = null;
        try {
            path = UnitTestHelperPath.buildTestFilePath(relativePath.split("/")).getCanonicalFile();
        } catch (final IOException e) {
            UnitTestHelperException.throwException(e);
        }
        Asserts.assertFileReadable(path);
        return readFile(path);
    }

    static String readFile(final String file) {
        if (file == null) {
            return null;
        }
        return readFile(new File(file));
    }

    static String readFile(final File file) {
        String result = null;
        if (file == null) {
            return null;
        }
        try {
            final String realPath = file.getCanonicalFile().getAbsolutePath();
            result = CACHE.get(realPath);
            if (result == null) {
                result = FileUtils.readFileToString(file, UnitTestHelperCommon.UTF_8);
                CACHE.put(realPath, result);
            }
        } catch (final IOException e) {
            UnitTestHelperException.throwException(e);
        }
        return UnitTestHelperCommon.cleanWindowsChar(result);
    }


    static void cacheClean() {
        CACHE.clear();
    }
}
