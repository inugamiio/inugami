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
package io.inugami.api.monitoring.warn;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
public class WarnContext {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final        Map<String, List<WarnCode>> WARNS    = new ConcurrentHashMap<>();
    private static final ThreadLocal<WarnContext>    INSTANCE = new ThreadLocal<>();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public synchronized static WarnContext getInstance() {
        WarnContext result = INSTANCE.get();
        if (result == null) {
            result = new WarnContext();
            INSTANCE.set(result);
        }

        return result;
    }


    // =========================================================================
    // API
    // =========================================================================
    public void clean() {
        WARNS.clear();
    }

    public WarnContext addWarn(final WarnCode warnCode) {
        if (warnCode != null) {
            List<WarnCode> saved = WARNS.get(warnCode.getWarnCode());
            if (saved == null) {
                saved = new ArrayList<>();
                WARNS.put(warnCode.getWarnCode(), saved);
            }
            saved.add(warnCode);
        }
        return this;
    }

    public Map<String, List<WarnCode>> getWarns() {
        final Map<String, List<WarnCode>> result = new LinkedHashMap<>();

        for (final Map.Entry<String, List<WarnCode>> entry : WARNS.entrySet()) {
            result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        return result;
    }
}
