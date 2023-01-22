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

import io.inugami.api.models.events.GenericEvent;

/**
 * Inugami use events to retrieve information. To be able to aggregate information Inugami required to known
 * when a provider have complete its process. The <strong>OnDoneFunction&lt;T&gt;</strong> is designed to intercept this event.
 *
 * @author patrick_guillerm
 * @see io.inugami.api.providers.concurrent.OnErrorFunction
 * @since 9 août 2017
 */
@FunctionalInterface
public interface OnDoneFunction<T> {
    T onDone(T resultData, GenericEvent currentEvent, final String channel);
}
