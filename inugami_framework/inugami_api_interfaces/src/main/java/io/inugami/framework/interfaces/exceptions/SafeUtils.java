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
package io.inugami.framework.interfaces.exceptions;

import io.inugami.framework.interfaces.functionnals.SupplierWithException;
import io.inugami.framework.interfaces.functionnals.SupplierWithThrowable;
import io.inugami.framework.interfaces.functionnals.VoidFunctionWithException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SafeUtils {

    public static <T> T grabSafe(final SupplierWithThrowable<T> handler, T defaultValue) {
        try {
            return handler.get();
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
            return defaultValue;
        }
    }

    public static void processSafe(final VoidFunctionWithException handler) {
        try {
            handler.process();
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
