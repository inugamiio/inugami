package io.inugami.api.exceptions.warnings;

import io.inugami.api.exceptions.Warning;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class WarningNull {
    static void warningNull(final Warning warning, final Object value, final String messageDetail, final Object... values) {
        if (value == null) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

    static void warningNotNull(final Warning warning, final Object value, final String messageDetail, final Object... values) {
        if (value != null) {
            WarningCommons.addWarningInContext(warning, messageDetail, values);
        }
    }

}
