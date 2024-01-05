package io.inugami.framework.api.exceptions.warnings;

import io.inugami.framework.api.exceptions.WarningContext;
import io.inugami.framework.interfaces.exceptions.Warning;
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
