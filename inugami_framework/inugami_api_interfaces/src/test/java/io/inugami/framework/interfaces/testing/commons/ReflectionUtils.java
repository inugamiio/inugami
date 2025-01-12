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
package io.inugami.framework.interfaces.testing.commons;

import io.inugami.framework.interfaces.tools.reflection.AnnotationTools;
import io.inugami.framework.interfaces.tools.reflection.FieldGetterSetter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"java:S119", "java:S3011", "java:S1452", "java:S1181", "java:S1123", "java:S2326", "java:S1133", "java:S5042", "java:S5361"})
@Slf4j
@UtilityClass
public final class ReflectionUtils {

    public static Map<String, Map<String, Object>> convertEnumToMap(final Class<? extends Enum<?>> enumClass) {
        if (enumClass == null) {
            return Map.of();
        }

        final Map<String, Map<String, Object>> result = new LinkedHashMap<>();
        final Enum<?>[]                        values = enumClass.getEnumConstants();

        try {
            for (final Enum<?> enumItem : values) {
                final Map<String, Object> enumFields;
                enumFields = extractEnumFields(enumItem);
                result.put(enumItem.name(), enumFields);
            }
        } catch (final IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }


        return result;
    }

    private static Map<String, Object> extractEnumFields(final Enum<?> enumItem) throws IllegalAccessException {
        final Map<String, Object> result = new LinkedHashMap<>();

        final Field[] fields = enumItem.getDeclaringClass().getDeclaredFields();
        for (final Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                result.put(field.getName(), field.get(enumItem));
            }
        }
        return result;
    }

    public static <T> List<Method> extractGetters(final T instance) {
        return AnnotationTools.extractGetters(instance);
    }

    public static void sortMethods(final List<Method> methods) {
        if (methods != null) {
            Collections.sort(methods, (ref, val) -> ref.getName().compareTo(val.getName()));
        }
    }

    public static <T> T invoke(final Method method, final Object object, final Object... params) {
        return AnnotationTools.invoke(method, object, params);
    }

    public static <T> List<FieldGetterSetter> extractFieldGetterAndSetter(final T instance) {
        return AnnotationTools.extractFieldGetterAndSetter(instance);
    }
}
