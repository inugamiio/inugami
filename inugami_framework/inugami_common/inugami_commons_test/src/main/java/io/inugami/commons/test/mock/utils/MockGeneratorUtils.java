/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.commons.test.mock.utils;

import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.tools.StringTools;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static io.inugami.commons.test.mock.MockGeneratorError.REST_CLIENT_CLASS_REQUIRED;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotNull;

@UtilityClass
public class MockGeneratorUtils {
    public static final  String  EMPTY     = "";
    public static final  String  DOT       = "[.]";
    private static final Pattern DOT_REGEX = Pattern.compile(DOT);
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================

    // =================================================================================================================
    // PACKAGE NAME
    // =================================================================================================================
    public static String resolvePackageName(final @NonNull Class<?> objectClass) {
        Objects.requireNonNull(objectClass);
        final List<String> packagePaths = new ArrayList<>();
        final String[]     parts        = objectClass.getName().split(DOT);
        for (String part : parts) {
            if (StringTools.fistCharUpperCase(part)) {
                break;
            }
            packagePaths.add(part);
        }
        return String.join(".", packagePaths);
    }


    // =================================================================================================================
    // BUILD FILES
    // =================================================================================================================
    public static @NonNull String buildFileName(@Nullable final ErrorCode errorCode) {
        String fileName = "context.json";
        if (errorCode != null) {
            fileName = "error-" + errorCode.getErrorCode() + ".json";
        }
        return fileName;
    }

    public static @Nullable File buildMockFilePath(@NonNull final File mockFolder,
                                                   @Nullable final String folder,
                                                   @NonNull final String fileName) {
        final StringBuilder fullPath          = new StringBuilder();
        final var           currentMockFolder = buildMockFileFolder(mockFolder, folder == null ? "" : folder);
        Objects.requireNonNull(currentMockFolder);
        fullPath.append(currentMockFolder.getAbsoluteFile());
        fullPath.append(File.separator);
        fullPath.append(fileName);

        return MockGeneratorUtils.canonicalFile(new File(fullPath.toString()));
    }

    public static @Nullable File buildMockFileFolder(@NonNull final File mockFolder, @Nullable final String folder) {
        final StringBuilder fullPath = new StringBuilder();
        fullPath.append(mockFolder.getPath());
        fullPath.append(File.separator)
                .append("src")
                .append(File.separator)
                .append("test")
                .append(File.separator)
                .append("resources");

        if (folder != null) {
            if (!folder.startsWith(File.separator)) {
                fullPath.append(File.separator);
            }
            if (folder.endsWith(File.separator)) {
                fullPath.append(folder.substring(0, folder.length() - 1));
            } else {
                fullPath.append(folder);
            }

        }

        return MockGeneratorUtils.canonicalFile(new File(fullPath.toString()));
    }


    public static @Nullable File getFolder(@Nullable final String path) {
        final File folder = Optional.ofNullable(path).map(File::new).orElse(null);

        File absoluteFile = canonicalFile(folder);
        if (absoluteFile == null) {
            return null;
        }

        if (!absoluteFile.exists()) {
            absoluteFile.mkdir();
        }
        return absoluteFile;
    }

    public static File canonicalFile(final File file) {
        try {
            return file == null ? null : file.getCanonicalFile().getAbsoluteFile();
        } catch (IOException e) {
            return null;
        }
    }

    public static String resolvePackagePath(@NonNull final Class<?> objClass) {
        assertNotNull(REST_CLIENT_CLASS_REQUIRED, objClass);
        String fullName    = objClass.getName();
        String packageName = fullName.replace(objClass.getSimpleName(), EMPTY);
        return StringTools.replaceAll(DOT_REGEX, packageName, File.separator);
    }

    public static @Nullable String resolveLastParentFolder(@NonNull final File file) {
        final String[] parts = file.getAbsolutePath().split(File.separator);
        if (parts.length <= 1) {
            return null;
        }
        return parts[parts.length - 2];
    }

    public void createFolderIfNotExists(@NonNull final File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
