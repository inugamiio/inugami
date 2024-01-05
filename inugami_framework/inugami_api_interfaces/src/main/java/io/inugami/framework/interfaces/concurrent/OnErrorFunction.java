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
package io.inugami.framework.interfaces.concurrent;

import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.task.ProviderFutureResult;

import java.util.concurrent.Callable;

/**
 * Inugami use events to retrieve information. To be able to aggregate information Inugami required to known
 * when a provider have complete its process.
 * The <strong>OnErrorFunction&lt;T&gt;</strong> is designed to intercept the case where a provider throws an error.
 *
 * @author patrick_guillerm
 * @see OnDoneFunction
 * @since 9 août 2017
 */
public interface OnErrorFunction<T> {

    T onError(GenericEvent event, String channel, Callable<ProviderFutureResult> task, Exception error);
}
