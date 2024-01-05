package io.inugami.framework.api.exceptions.warnings;

import io.inugami.framework.interfaces.exceptions.MessagesFormatter;
import io.inugami.framework.interfaces.exceptions.Warning;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Map;

@UtilityClass
public class WarningEmpty {

    public static void warningEmpty(final Warning warning,
                                    final String value,
                                    final String messageDetail,
                                    final Object... msgArgs) {
        if (value == null || value.isEmpty()) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }

    public static void warningNotEmpty(final Warning warning,
                                       final String value,
                                       final String messageDetail,
                                       final Object... msgArgs) {
        if (value != null && !value.isEmpty()) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }


    public static void warningEmpty(final Warning warning,
                                    final Collection<?> values,
                                    final String messageDetail,
                                    final Object... msgArgs) {
        if (values == null || values.isEmpty()) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }

    public static <T> void warningNotEmpty(final Warning warning,
                                           final T[] values,
                                           final String messageDetail,
                                           final Object... msgArgs) {
        if (values != null && values.length == 0) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }


    public static <T> void warningEmpty(final Warning warning,
                                        final T[] values,
                                        final String messageDetail,
                                        final Object... msgArgs) {
        if (values == null || values.length == 0) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }

    public static void warningNotEmpty(final Warning warning,
                                       final Collection<?> values,
                                       final String messageDetail,
                                       final Object... msgArgs) {
        if (values != null && !values.isEmpty()) {
            WarningCommons.addWarningInContext(warning, MessagesFormatter.format(messageDetail, msgArgs), values);
        }
    }

    public static void warningEmpty(final Warning warning,
                                    final Map<?, ?> values,
                                    final String messageDetail,
                                    final Object... msgArgs) {
        if (values == null || values.isEmpty()) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }

    public static void warningNotEmpty(final Warning warning,
                                       final Map<?, ?> values,
                                       final String messageDetail,
                                       final Object... msgArgs) {
        if (values != null && !values.isEmpty()) {
            WarningCommons.addWarningInContext(warning, MessagesFormatter.format(messageDetail, msgArgs), values);
        }
    }
}
