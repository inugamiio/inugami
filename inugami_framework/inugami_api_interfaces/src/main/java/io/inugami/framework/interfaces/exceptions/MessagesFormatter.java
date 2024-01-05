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
package io.inugami.framework.interfaces.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.regex.Pattern;

/**
 * MessagesFormatter
 *
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */

@SuppressWarnings({"java:S2479", "java:S5361", "java:S6397", "java:S1124"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessagesFormatter {

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private final static Pattern REGEX = Pattern.compile("[{][^}]+[}]");
    private final static String  NBSP  = " ";


    // =========================================================================
    // METHODS
    // =========================================================================
    public static String format(final String format, final Object... values) {
        String result = null;
        if (format != null) {
            if ((values == null) || (values.length == 0)) {
                result = format;
            } else if (REGEX.matcher(format).find()) {
                final MessageFormat formater = new MessageFormat(format.replaceAll("'", "''"));
                result = formater.format(convertToStringArray(values));
            } else {
                result = format;
            }
        }

        return result == null ? null : result.replaceAll(NBSP, " ");
    }

    private static String[] convertToStringArray(final Object[] values) {
        final String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = String.valueOf(values[i]);
        }
        return result;
    }
}
