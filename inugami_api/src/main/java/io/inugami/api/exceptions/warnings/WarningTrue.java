package io.inugami.api.exceptions.warnings;

import io.inugami.api.exceptions.Warning;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class WarningTrue {
    static void warningTrue(final Warning warning, final boolean condition, final String messageDetail, final Object... values) {
        if (condition) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }


    static void warningTrue(final Warning warning, final Supplier<Boolean> condition, final String messageDetail, final Object... values) {
        if (condition != null) {
            final Boolean value = condition.get();
            if (value != null && value) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }


    static void warningFalse(final Warning warning, final boolean condition, final String messageDetail, final Object... values) {
        if (condition) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

    static void warningFalse(final Warning warning, final Supplier<Boolean> condition, final String messageDetail, final Object... values) {
        if (condition != null) {
            final Boolean value = condition.get();
            if (value != null && !value) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }
}
