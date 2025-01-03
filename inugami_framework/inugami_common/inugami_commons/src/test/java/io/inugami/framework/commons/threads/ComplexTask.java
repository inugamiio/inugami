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
package io.inugami.framework.commons.threads;

import io.inugami.framework.interfaces.threads.CallableWithErrorResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * ComplexTask
 *
 * @author patrickguillerm
 * @since 25 mars 2018
 */
public class ComplexTask implements CallableWithErrorResult<String> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String value;

    // =========================================================================
    // METHODS
    // =========================================================================
    public ComplexTask(final String value) {
        this.value = value;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String call() throws Exception {
        final List<Callable<String>> tasks = buildTask();
        //@formatter:off
        final List<String> data = new RunAndCloseService<>("test" + value,
                                                     1500L,
                                                     2,
                                                     tasks).run();
        //@formatter:on
        return String.join(" | ", data);
    }

    private List<Callable<String>> buildTask() {
        final List<Callable<String>> result = new ArrayList<>();

        if ("1".equals(value)) {
            result.add(new SimpleTask(500, "1.1"));
            result.add(new SimpleTask(400, "1.2"));
            result.add(new SimpleTask(1000, "1.3"));
            result.add(new SimpleTask(500, "1.4", true));
        } else {
            result.add(new SimpleTask(100, value + ".1"));
            result.add(new SimpleTask(500, value + ".2"));
            result.add(new SimpleTask(2000, value + ".3"));
            result.add(new SimpleTask(100, value + ".4"));
        }
        return result;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getTimeoutResult() {
        return "null";
    }
}
