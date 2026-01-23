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
package io.inugami.framework.commons.files;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;

/**
 * @since 2026-01-23
 */
@UtilityClass
public class UnzipUtils {
    private static final Unzip UNZIP = new Unzip();

    public static void unzipLogLess(final File zipFile, final File destination) throws IOException {
        UNZIP.unzipLogLess(zipFile, destination);
    }

    public static  void unzip(final File zipFile, final File destination) throws IOException {
        UNZIP.unzip(zipFile, destination);
    }
}
