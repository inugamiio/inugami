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
package io.inugami.logs.obfuscator.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UnitTestHelper {

    // =========================================================================
    // TEXT
    // =========================================================================
    public static void assertTextIntegration(final String value, final String path, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertTextIntegration(value, path, lineMatchers);
    }

    public static void assertTextIntegration(final Object value, final String path, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertTextIntegration(value, path, lineMatchers);
    }

    public static void assertTextRelative(final String value, final String path, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertTextRelative(value, path, lineMatchers);
    }

    public static void assertTextRelative(final Object value, final String path, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertTextRelative(value, path, lineMatchers);
    }

    public static void assertText(final Object value, final String jsonRef, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertText(value, jsonRef, lineMatchers);
    }

    public static void assertText(final String value, final String jsonRef, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertText(value, jsonRef, lineMatchers);
    }

    public static LineMatcher[] buildSkipLines(final int... skipLines) {
        return UnitTestHelperText.buildSkipLines(skipLines);
    }

}
