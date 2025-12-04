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
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@UtilityClass
public class MockGeneratorUtils {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================


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
        final StringBuilder fullPath = new StringBuilder();
        fullPath.append(mockFolder.getPath());
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
        fullPath.append(File.separator);
        fullPath.append(fileName);

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
}
