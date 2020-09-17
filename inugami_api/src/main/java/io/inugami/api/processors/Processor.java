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
package io.inugami.api.processors;

import io.inugami.api.exceptions.services.ProcessorException;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.tools.NamedComponent;

/**
 * PostProcessor
 * 
 * @author patrick_guillerm
 * @since 6 oct. 2016
 */
public interface Processor extends NamedComponent {
    
    ProviderFutureResult process(final GenericEvent event, final ProviderFutureResult data) throws ProcessorException;
    
}
