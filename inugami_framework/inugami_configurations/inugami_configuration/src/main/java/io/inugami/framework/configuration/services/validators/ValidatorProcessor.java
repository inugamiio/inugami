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
package io.inugami.framework.configuration.services.validators;

import io.inugami.framework.configuration.exceptions.ConfigurationException;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.MessagesFormatter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Validator
 *
 * @author patrick_guillerm
 * @since 29 déc. 2016
 */
@SuppressWarnings({"java:S2629", "java:S1125"})
@Slf4j
public class ValidatorProcessor {

    // =========================================================================
    // TOOLS
    // =========================================================================
    public static boolean isNull(final Object value) {
        return value == null;
    }

    public static boolean isNotNull(final Object value) {
        return value != null;
    }

    public static boolean ifPresentIsEmpty(final String value) {
        return value == null ? false : value.trim().isEmpty();
    }

    public static boolean isEmpty(final String value) {
        return (value == null) || value.trim().isEmpty();
    }

    public static String format(final String message, final Object... values) {
        return MessagesFormatter.format(message, values);
    }

    public static Condition condition(final String message, final boolean condition) {
        return new Condition(message, condition);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public void validate(final String filePath, final List<Condition> conditions,
                         final MessageFormatter formater) throws ConfigurationException {
        Asserts.assertNotNull(conditions);
        Asserts.assertNotNull(formater);
        final StringBuilder errors = new StringBuilder();

        for (final Condition condition : conditions) {
            if (condition.hasError()) {
                errors.append(formater.format(condition, filePath));
                errors.append('\n');
            }
        }
        if (errors.length() > 0) {
            log.error(errors.toString());
            throw new PluginConfigurationValidatorException(errors.toString());
        }

    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @FunctionalInterface
    public interface MessageFormatter {
        String format(final Condition condition, final String path);
    }

    // =========================================================================
    // EXCEPTION
    // =========================================================================
    @SuppressWarnings({"java:S110"})
    private class PluginConfigurationValidatorException extends ConfigurationException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = -8983187360573647325L;

        public PluginConfigurationValidatorException(final String message, final Object... values) {
            super(message, values);
        }

    }
}
