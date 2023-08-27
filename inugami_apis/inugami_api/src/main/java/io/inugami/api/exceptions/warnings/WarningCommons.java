package io.inugami.api.exceptions.warnings;

import io.inugami.api.exceptions.Warning;
import io.inugami.api.exceptions.WarningContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class WarningCommons {


    static void addWarningInContext(final Warning warning, final String message, final Object... values) {
        if (warning != null) {
            Warning currentWarning = warning;
            if (message != null) {
                if (values == null || values.length == 0) {
                    currentWarning = currentWarning.addDetail(message);
                } else {
                    currentWarning = currentWarning.addDetail(message, values);
                }
            }
            WarningContext.getInstance().addWarnings(currentWarning);
        }
    }
}
