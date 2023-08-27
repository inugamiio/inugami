package io.inugami.api.exceptions.warnings;

import io.inugami.api.exceptions.MessagesFormatter;
import io.inugami.api.exceptions.Warning;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class WarningEmpty {

    static void warningEmpty(final Warning warning,
                             final String value,
                             final String messageDetail,
                             final Object... msgArgs) {
        if (value == null || value.isEmpty()) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }

    static void warningNotEmpty(final Warning warning,
                                final String value,
                                final String messageDetail,
                                final Object... msgArgs) {
        if (value != null && !value.isEmpty()) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }


    static void warningEmpty(final Warning warning,
                             final Collection<?> values,
                             final String messageDetail,
                             final Object... msgArgs) {
        if (values == null || values.isEmpty()) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }

    static <T> void warningNotEmpty(final Warning warning,
                                    final T[] values,
                                    final String messageDetail,
                                    final Object... msgArgs) {
        if (values != null && values.length == 0) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }


    static <T> void warningEmpty(final Warning warning,
                                 final T[] values,
                                 final String messageDetail,
                                 final Object... msgArgs) {
        if (values == null || values.length == 0) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }

    static void warningNotEmpty(final Warning warning,
                                final Collection<?> values,
                                final String messageDetail,
                                final Object... msgArgs) {
        if (values != null && !values.isEmpty()) {
            WarningCommons.addWarningInContext(warning, MessagesFormatter.format(messageDetail, msgArgs), values);
        }
    }

    static void warningEmpty(final Warning warning,
                             final Map<?, ?> values,
                             final String messageDetail,
                             final Object... msgArgs) {
        if (values == null || values.isEmpty()) {
            WarningCommons.addWarningInContext(warning, messageDetail, msgArgs);
        }
    }

    static void warningNotEmpty(final Warning warning,
                                final Map<?, ?> values,
                                final String messageDetail,
                                final Object... msgArgs) {
        if (values != null && !values.isEmpty()) {
            WarningCommons.addWarningInContext(warning, MessagesFormatter.format(messageDetail, msgArgs), values);
        }
    }
}
