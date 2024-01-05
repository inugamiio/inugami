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
package io.inugami.framework.interfaces.monitoring.threads;

import io.inugami.framework.interfaces.models.tools.Chrono;
import io.inugami.framework.interfaces.concurrent.MonitoredCallable;

public interface MonitoredCallableListener {
    <T> void created(MonitoredCallable<T> result, long createdDate);

    void start(MonitoredCallable<?> callable);

    void done(MonitoredCallable<?> callable, Object result, final Chrono chrono, final Throwable error);
}
