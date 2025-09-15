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
package io.inugami.framework.commons.testing.threads;

import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.threads.CallableWithErrorResult;

/**
 * SimpleTask
 *
 * @author patrickguillerm
 * @since 24 mars 2018
 */
@SuppressWarnings({"java:S2925"})
public class SimpleTask implements CallableWithErrorResult<String> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final int sleepTime;

    private final String result;

    private final boolean error;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SimpleTask(final int sleepTime, final String result) {
        this(sleepTime, result, false);

    }

    public SimpleTask(final int sleepTime, final String result, final boolean error) {
        this.sleepTime = sleepTime;
        this.result = result;
        this.error = error;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String call() throws Exception {
        Thread.sleep(sleepTime);
        if (error) {
            throw new TechnicalException("Error occurs!");
        }
        return result;
    }

    @Override
    public String getTimeoutResult() {
        return "timeout - " + result;
    }

    @Override
    public String getErrorResult(final Exception error) {
        return "error - " + result;
    }
}
