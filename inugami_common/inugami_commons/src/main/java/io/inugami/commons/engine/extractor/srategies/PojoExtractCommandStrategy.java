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
package io.inugami.commons.engine.extractor.srategies;

import io.inugami.api.loggers.Loggers;
import io.inugami.commons.engine.extractor.ExtractCommand;
import io.inugami.commons.engine.extractor.ExtractCommandStrategy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PojoExtractCommandStrategy
 *
 * @author patrick_guillerm
 * @since 22 mai 2018
 */
@SuppressWarnings({"java:S3011"})
public class PojoExtractCommandStrategy implements ExtractCommandStrategy {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public boolean accept(final Object value, final ExtractCommand cmd) {
        return true;
    }

    @Override
    public Object process(final Object value, final ExtractCommand cmd) {
        Object       result    = null;
        final String fieldName = cmd.getFieldName();

        Field field = null;
        if (fieldName != null) {
            field = extractAttribute(value, fieldName);
        }

        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Loggers.JAVA_SCRIPT.error(e.getMessage(), e);
            }
        }

        return result;
    }

    private Field extractAttribute(final Object value, final String fieldName) {
        Field             result = null;
        final List<Field> fields = extractAllFields(value.getClass());

        for (final Field field : fields) {
            if (fieldName.equals(field.getName())) {
                result = field;
                break;
            }
        }
        return result;
    }

    private List<Field> extractAllFields(final Class<?> value) {
        final List<Field> result = new ArrayList<>();

        if (value != null) {
            result.addAll(Arrays.asList(value.getDeclaredFields()));
            if (value != Object.class) {
                result.addAll(extractAllFields(value.getSuperclass()));
            }
        }

        return result;
    }
}
