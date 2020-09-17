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

import java.util.List;

import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.providers.task.ProviderFutureResult;

/**
 * Aggregator
 * 
 * @author patrick_guillerm
 * @since 29 mai 2017
 */
public interface Aggregator {
    
    ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException;
}
