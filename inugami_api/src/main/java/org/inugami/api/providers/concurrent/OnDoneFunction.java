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
package org.inugami.api.providers.concurrent;

import org.inugami.api.models.events.GenericEvent;

/**
 * OnDoneFunction
 * 
 * @author patrick_guillerm
 * @since 9 août 2017
 */
@FunctionalInterface
public interface OnDoneFunction<T> {
    T onDone(T resultData, GenericEvent currentEvent, final String channel);
}
