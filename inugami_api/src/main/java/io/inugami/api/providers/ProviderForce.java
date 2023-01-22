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
package io.inugami.api.providers;

import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.providers.task.ProviderFutureResult;

/**
 * The <strong>ProviderForce</strong> is an interface marker to identify all providers
 * how allow to force retrieve information.
 * 
 * @author patrick_guillerm
 * @since 3 janv. 2017
 */
public interface ProviderForce {
    <T extends SimpleEvent> ProviderFutureResult callEvent(final T event);
}
