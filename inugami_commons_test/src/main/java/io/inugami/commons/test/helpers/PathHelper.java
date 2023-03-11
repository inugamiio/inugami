package io.inugami.commons.test.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PathHelper {

    public static File buildTestFilePath(final String... filePathParts) {
        final List<String> parts = new ArrayList<>(Arrays.asList("src", "test", "resources"));
        Arrays.asList(filePathParts)
              .forEach(parts::add);
        return buildPath(parts.toArray(new String[]{}));
    }

    public static File buildPath(final String... parts) {
        final File basePath = new File(".");

        final String[] allPathParts = new String[parts.length + 1];
        allPathParts[0] = basePath.getAbsolutePath();
        System.arraycopy(parts, 0, allPathParts, 1, parts.length);

        return new File(String.join(File.separator, allPathParts));
    }


    public static File buildRefJsonPath(final File jsonLoaderFile) {
        final String fullPath    = jsonLoaderFile.getAbsolutePath();
        final String usecasePath = fullPath.substring(0, fullPath.length() - ".load.json".length()) + ".json";
        return new File(usecasePath);
    }
}
