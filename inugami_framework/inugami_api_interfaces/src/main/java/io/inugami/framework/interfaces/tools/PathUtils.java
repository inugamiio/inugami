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
package io.inugami.framework.interfaces.tools;

import lombok.experimental.UtilityClass;

@SuppressWarnings({"java:S5361"})
@UtilityClass
public class PathUtils {

    public static final String FILE_URL       = "file:";
    public static final int    FILE_URL_SIZE  = FILE_URL.length();
    public static final String PATH_SEPARATOR = "/";

    public static String toUnixPath(final String file) {
        if (file == null) {
            return null;
        }

        String result = file.replace('\\', '/');
        if (result.startsWith(FILE_URL)) {
            result = result.substring(FILE_URL_SIZE);
        }

        if (result.contains(":/")) {
            result = result.replaceAll(":/", PATH_SEPARATOR);
        }
        if (!result.startsWith(PATH_SEPARATOR)) {
            result = PATH_SEPARATOR + result;
        }
        return result;


    }
}
