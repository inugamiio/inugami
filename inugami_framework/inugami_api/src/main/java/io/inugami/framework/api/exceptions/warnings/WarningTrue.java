package io.inugami.framework.api.exceptions.warnings;

import io.inugami.framework.interfaces.exceptions.Warning;
import lombok.experimental.UtilityClass;

import java.util.function.BooleanSupplier;

@UtilityClass
public class WarningTrue {
    public static void warningTrue(final Warning warning,
                                   final boolean condition,
                                   final String messageDetail,
                                   final Object... values) {
        if (condition) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }


    public static void warningTrue(final Warning warning,
                                   final BooleanSupplier condition,
                                   final String messageDetail,
                                   final Object... values) {
        if (condition != null) {
            final Boolean value = condition.getAsBoolean();
            if (value != null && value) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }


    public static void warningFalse(final Warning warning,
                                    final boolean condition,
                                    final String messageDetail,
                                    final Object... values) {
        if (condition) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

    public static void warningFalse(final Warning warning,
                                    final BooleanSupplier condition,
                                    final String messageDetail,
                                    final Object... values) {
        if (condition != null) {
            final Boolean value = condition.getAsBoolean();
            if (value != null && !value) {
                WarningCommons.addWarningInContext(warning, messageDetail, values);
            }
        }
    }
}
