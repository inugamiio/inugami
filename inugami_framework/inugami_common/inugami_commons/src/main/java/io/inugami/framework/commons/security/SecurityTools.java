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
package io.inugami.framework.commons.security;

import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.processors.ItemProcessor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SqlSecurityTools
 *
 * @author patrickguillerm
 * @since 11 sept. 2018
 */
@UtilityClass
public final class SecurityTools {


    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final Pattern REGEX_INJECT = Pattern.compile(JvmKeyValues.SECURITY_SQL_INJECT_REGEX.or("([\'\\-;=\\?$/]+)|([<>])"));


    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    public static String checkInjection(final String value) {
        if (!Asserts.checkIsBlank(value)) {
            final Matcher matcher = REGEX_INJECT.matcher(value);
            if (matcher.matches() || value.contains("'") || value.contains("\\")) {
                throw new SecurityException("invalide query! (" + value + ")");
            }
        }
        return StringEscapeUtils.escapeSql(value);
    }

    public static String escapeSql(final String value) {
        return StringEscapeUtils.escapeSql(value);
    }

    public static String escapeJavaScriptAndHtml(final String value) {
        return escape(value, StringEscapeUtils::escapeJavaScript, StringEscapeUtils::escapeHtml);
    }

    public static String escape(final String value, final UnaryOperator<String>... processors) {
        String result = value;
        if (result != null) {
            for (final UnaryOperator<String> function : processors) {
                result = function.apply(result);
            }
        }
        return result;
    }


    // =================================================================================================================
    // ESCAPE ENTITY
    // =================================================================================================================
    public static void secureSql(final Supplier<String> getter, final Consumer<String> setter) {
        secureEntity(getter, setter, StringEscapeUtils::escapeSql);
    }

    public static void secureJavaScript(final Supplier<String> getter, final Consumer<String> setter) {
        secureEntity(getter, setter, StringEscapeUtils::escapeJavaScript);
    }

    public static void secureXml(final Supplier<String> getter, final Consumer<String> setter) {
        secureEntity(getter, setter, StringEscapeUtils::escapeXml);
    }

    public static void secureHtml(final Supplier<String> getter, final Consumer<String> setter) {
        secureEntity(getter, setter, StringEscapeUtils::escapeHtml);
    }

    public static void secureJavaScriptAndHtml(final Supplier<String> getter, final Consumer<String> setter) {
        secureEntity(getter, setter, StringEscapeUtils::escapeJavaScript, StringEscapeUtils::escapeHtml);
    }

    public static <T> void secureJavaScriptAndHtml(final Collection<T> values,
                                                   final ItemProcessor<T>... itemProcessors) {
        if ((values != null) && (itemProcessors != null)) {
            for (final T value : values) {
                for (final ItemProcessor<T> processor : itemProcessors) {
                    final String content = processor.getExtractor().apply(value);
                    if (value != null) {
                        final String securedContent = escapeJavaScriptAndHtml(content);
                        processor.getSetter().accept(value, securedContent);
                    }
                }

            }
        }

    }

    public static void secureEntity(final Supplier<String> getter, final Consumer<String> setter,
                                    final UnaryOperator<String>... processors) {
        Asserts.assertNotNull("getter is mandatory!", getter);
        Asserts.assertNotNull("setter is mandatory!", setter);
        Asserts.assertNotNull("processor is mandatory!", processors);

        String value = getter.get();
        if (value != null) {

            for (final UnaryOperator<String> function : processors) {
                value = function.apply(value);
            }

            setter.accept(SecurityTools.escapeSql(value));
        }
    }

}
