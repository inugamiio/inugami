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
package io.inugami.configuration.test;

import io.inugami.api.exceptions.services.ProcessorException;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.processors.Processor;
import io.inugami.api.providers.task.ProviderFutureResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PluginProcessor
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
public class PluginProcessor implements Processor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger LOGGER = LoggerFactory.getLogger(PluginProcessor.class);
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public ProviderFutureResult process(final GenericEvent eventName,
                                        final ProviderFutureResult data) throws ProcessorException {
        LOGGER.info("process");
        return null;
    }
    
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
