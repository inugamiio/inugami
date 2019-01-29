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
package org.inugami.core.providers;

import java.util.List;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.models.Gav;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ClassBehavior;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.AbstractProvider;
import org.inugami.api.providers.Provider;
import org.inugami.api.providers.ProviderRunner;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.concurrent.FutureDataBuilder;
import org.inugami.api.providers.concurrent.ThreadSleep;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.api.providers.task.ProviderTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SimpleProvider
 * 
 * @author patrick_guillerm
 * @since 6 janv. 2017
 */
public class ProviderFull extends AbstractProvider implements Provider {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger                    TASK_LOGGER = LoggerFactory.getLogger(ProviderFullTask.class);
    
    private final String                           test;
    
    private final FutureData<ProviderFutureResult> futureDataRef;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderFull(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                        final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        test = config.grab("test");
        Asserts.notNull(test);
        Asserts.notNull(providerRunner);
        
        //@formatter:off
        futureDataRef = new FutureDataBuilder<ProviderFutureResult>()
                                .addTimeout(2000)
                                .build();
        //@formatter:on
        
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event) {
        return runTask(new ProviderFullTask(event), event, futureDataRef);
    }
    
    // =========================================================================
    // TASK
    // =========================================================================
    private class ProviderFullTask implements ProviderTask {
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // ATTRIBUTS
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        private final ProviderFutureResultBuilder resultBuilder = new ProviderFutureResultBuilder();
        
        private final SimpleEvent                 event;
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // CONSTRUCTOR
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
        public ProviderFullTask(final SimpleEvent event) {
            super();
            this.event = event;
        }
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // RUN
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
        @Override
        public ProviderFutureResult callProvider() {
            TASK_LOGGER.info("task begin : {}", Thread.currentThread().getName());
            final String result = null;
            
            if ("error".equals(event.getName())) {
                TASK_LOGGER.info("process error...");
                new ThreadSleep(3000).sleep();
                TASK_LOGGER.info("wainting done");
                
                resultBuilder.addException(new TechnicalException("YOLO ^_^ New error!"));
            }
            else {
                new ThreadSleep(2000).sleep();
                resultBuilder.addMessage("Hello the world");
            }
            
            TASK_LOGGER.info("task end : {}", Thread.currentThread().getName());
            return resultBuilder.build();
        }
        
        @Override
        public GenericEvent getEvent() {
            return event;
        }
        
        @Override
        public Gav getPluginGav() {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        // TODO Auto-generated method stub
        return null;
    }
}
