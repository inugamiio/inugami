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
package io.inugami.framework.api.tools;

import io.inugami.framework.interfaces.functionnals.GenericActionWithException;
import io.inugami.framework.interfaces.functionnals.VoidFunctionWithException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Objects;

@Slf4j
@UtilityClass
public class RunSafeUtils {
    public static <T> T runSafeOrElse(@NonNull final GenericActionWithException<T> action, @Nullable T defaultValue) {
        Objects.requireNonNull(action);
        try {
            return action.process();
        } catch (final Throwable e) {
            traceError(e, log);
            return defaultValue;
        }
    }

    public static void runSafeVoid(@NonNull final VoidFunctionWithException action) {
        runSafeVoid(action, log);
    }

    public static void runSafeVoid(@NonNull final VoidFunctionWithException action, @NonNull final Logger logger) {
        Objects.requireNonNull(action);
        try {
            action.process();
        } catch (final Throwable e) {
            traceError(e, logger);
        }
    }

    public static <T> T runSafe(@NonNull final GenericActionWithException<T> action) {
        return runSafe(action, log);
    }

    public static <T> T runSafe(@NonNull final GenericActionWithException<T> action, @NonNull final Logger logger) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(logger);
        try {
            return action.process();
        } catch (final Throwable e) {
            traceError(e, logger);
            return null;
        }
    }

    public static void traceError(final Throwable e, final Logger logger) {
        if (logger.isDebugEnabled()) {
            logger.error(e.getMessage(), e);
        }
    }
}
