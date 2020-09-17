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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.Config;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.concurrent.FutureDataBuilder;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderTask;

/**
 * AbstractProvider allow to helper to run providers
 * 
 * @author patrick_guillerm
 * @since 3 janv. 2017
 */
public abstract class AbstractProvider {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ProviderRunner providerRunner;
    
    private final ClassBehavior  classBehavior;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public AbstractProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                            final ProviderRunner providerRunner) {
        super();
        this.providerRunner = providerRunner;
        this.classBehavior = classBehavior;
    }
    
    // =========================================================================
    // RUN FUTURE TASK
    // =========================================================================
    protected List<Future<ProviderFutureResult>> runFutureTask(final List<ProviderTask> tasks) {
        final List<Future<ProviderFutureResult>> result = new ArrayList<>();
        
        if (tasks != null) {
            //@formatter:off
            tasks.stream()
                 .map(task->providerRunner.run(getName(), task))
                 .forEach(result::add);
            //@formatter:on
        }
        
        return result;
    }
    
    protected Future<ProviderFutureResult> runFutureTask(final ProviderTask task) {
        return providerRunner.run(getName(), task);
    }
    
    protected Future<ProviderFutureResult> runFutureTask(final String name, final ProviderTask task) {
        return providerRunner.run(name, task);
    }
    
    // =========================================================================
    // RUN TASK
    // =========================================================================
    protected FutureData<ProviderFutureResult> runTask(final ProviderTask task, final SimpleEvent event,
                                                       final FutureData<ProviderFutureResult> future) {
        
        final FutureDataBuilder<ProviderFutureResult> builder = new FutureDataBuilder<ProviderFutureResult>(future);
        builder.addTask(task);
        builder.addEvent(event);
        builder.addFuture(providerRunner.run(getName(), task));
        return builder.build();
        
    }
    
    // =========================================================================
    // GETTERS
    // =========================================================================
    public Optional<String> getConfig(final String key) {
        return classBehavior.getConfig(key);
    }
    
    public String getName() {
        return classBehavior.getName();
    }
    
    public List<Config> getConfigs() {
        return classBehavior.getConfigs();
    }
    
}
