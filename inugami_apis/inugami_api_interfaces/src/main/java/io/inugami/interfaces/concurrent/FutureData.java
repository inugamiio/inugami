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
package io.inugami.interfaces.concurrent;

import io.inugami.interfaces.models.event.GenericEvent;
import io.inugami.interfaces.task.ProviderTask;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Inugami heavily use multi-threading principle. All tasks will return a <strong>FutureData&lt;T&gt;</strong>.
 *
 * @author patrick_guillerm
 * @since 9 août 2017
 */
public interface FutureData<T> {

    String getChannel();

    Future<T> getFuture();

    T getData();

    GenericEvent getEvent();

    long getTimeout();

    TimeUnit getTimeUnit();

    List<OnDoneFunction<T>> onDone();

    List<OnErrorFunction<T>> onError();

    ProviderTask getTask();

}
