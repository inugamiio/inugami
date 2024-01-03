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

import io.inugami.interfaces.concurrent.FutureData;
import io.inugami.interfaces.concurrent.OnDoneFunction;
import io.inugami.interfaces.concurrent.OnErrorFunction;
import io.inugami.interfaces.models.event.GenericEvent;
import io.inugami.interfaces.task.ProviderTask;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * FutureDataModel
 *
 * @author patrick_guillerm
 * @since 9 août 2017
 */
@Getter
@ToString(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FutureDataModel<T> implements FutureData<T> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private Future<T>                future;
    private T                        data;
    private ProviderTask             task;
    @ToString.Include
    private GenericEvent             event;
    @ToString.Include
    private String                   channel;
    @ToString.Include
    private long                     timeout;
    @ToString.Include
    private TimeUnit                 timeUnit;
    private List<OnErrorFunction<T>> onError;
    private List<OnDoneFunction<T>>  onDone;


    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    @Override
    public List<OnDoneFunction<T>> onDone() {
        return Collections.synchronizedList(onDone);
    }

    @Override
    public List<OnErrorFunction<T>> onError() {
        return Collections.synchronizedList(onError);
    }

    @Override
    public TimeUnit getTimeUnit() {
        return timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit;
    }

}
