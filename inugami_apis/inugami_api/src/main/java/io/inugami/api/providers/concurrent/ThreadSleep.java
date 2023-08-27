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
package io.inugami.api.providers.concurrent;

import io.inugami.api.loggers.Loggers;

/**
 * ThreadSleep
 *
 * @author patrick_guillerm
 * @since 27 sept. 2017
 */
public class ThreadSleep {
    private final long time;

    public static ThreadSleep build50ms() {
        return new ThreadSleep(50);
    }

    public static ThreadSleep build100ms() {
        return new ThreadSleep(100);
    }

    public static ThreadSleep build250ms() {
        return new ThreadSleep(250);
    }

    public static ThreadSleep build500ms() {
        return new ThreadSleep(500);
    }

    public static ThreadSleep build1000ms() {
        return new ThreadSleep(1000);
    }

    public ThreadSleep(final long time) {
        super();
        this.time = time;
    }

    public void sleep() {
        try {
            Thread.sleep(time);
        } catch (final InterruptedException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
}
