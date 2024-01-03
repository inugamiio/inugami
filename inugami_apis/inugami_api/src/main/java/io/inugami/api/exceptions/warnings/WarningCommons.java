package io.inugami.api.exceptions.warnings;

import io.inugami.api.exceptions.WarningContext;
import io.inugami.interfaces.exceptions.Warning;
import lombok.experimental.UtilityClass;

@UtilityClass
class WarningCommons {


    public static void addWarningInContext(final Warning warning, final String message, final Object... values) {
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
