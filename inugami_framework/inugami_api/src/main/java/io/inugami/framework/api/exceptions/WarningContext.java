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
package io.inugami.framework.api.exceptions;


import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

@SuppressWarnings({"java:S5164"})
@UtilityClass
public class WarningContext {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final ThreadLocal<CurrentWarningContext> INSTANCE = new ThreadLocal<>();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public static synchronized @NonNull CurrentWarningContext getInstance() {
        CurrentWarningContext instance = INSTANCE.get();
        if (instance == null) {
            instance = new CurrentWarningContext();
            INSTANCE.set(instance);
        }
        return instance;
    }

    public static synchronized void clean() {
        INSTANCE.remove();
    }
}
