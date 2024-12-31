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
package io.inugami.commons.test;

import io.inugami.commons.test.api.LineMatcher;
import io.inugami.framework.api.tools.ReflectionUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@UtilityClass
public class UnitTestHelperEnum {

    static void assertEnum(final Class<? extends Enum<?>> enumClass,
                           final String reference,
                           final LineMatcher... matchers) {
        final Map<String, Map<String, Object>> data = ReflectionUtils.convertEnumToMap(enumClass);
        UnitTestHelperText.assertText(data, reference, matchers);
    }

    static void assertEnumRelative(final Class<? extends Enum<?>> enumClass,
                                   final String path,
                                   final LineMatcher... matchers) {
        final Map<String, Map<String, Object>> data = ReflectionUtils.convertEnumToMap(enumClass);
        UnitTestHelperText.assertTextRelative(data, path, matchers);
    }


}

