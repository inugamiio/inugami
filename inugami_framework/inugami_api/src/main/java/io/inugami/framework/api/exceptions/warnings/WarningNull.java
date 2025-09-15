package io.inugami.framework.api.exceptions.warnings;

import io.inugami.framework.interfaces.exceptions.Warning;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WarningNull {
    public static void warningNull(final Warning warning,
                                   final Object value,
                                   final String messageDetail,
                                   final Object... values) {
        if (value == null) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

    public static void warningNotNull(final Warning warning,
                                      final Object value,
                                      final String messageDetail,
                                      final Object... values) {
        if (value != null) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

}
