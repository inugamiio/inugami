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
package io.inugami.core.providers;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ClassBehaviorParametersSPI;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.ProviderRunner;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderTask;
import io.inugami.api.spi.SpiLoader;
import io.inugami.commons.threads.ThreadsExecutorService;
import io.inugami.core.context.ContextSPI;

/**
 * MultiThreadedProviderRunner
 * 
 * @author patrick_guillerm
 * @since 5 janv. 2017
 */
public class MultiThreadedProviderRunner implements ProviderRunner, ThreadFactory, ClassBehaviorParametersSPI {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String     CONFIG_THREADS = "max_thread";
    
    public static final String     CONFIG_TIMEOUT = "timeout";
    
    private ThreadGroup            threadGroup;
    
    private String                 namePrefix;
    
    private ThreadsExecutorService threadsExecutor;
    
    private int                    nbMaxThreads;
    
    private int                    threadNumber   = 0;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MultiThreadedProviderRunner() {
    }
    
    public MultiThreadedProviderRunner(final ClassBehavior behavior, final ConfigHandler<String, String> config) {
        threadGroup = Thread.currentThread().getThreadGroup();
        
        nbMaxThreads = Integer.parseInt(config.getOrDefault(CONFIG_THREADS, "1"));
        
        final ContextSPI ctx = new SpiLoader().loadSpiSingleService(ContextSPI.class);
        
        threadsExecutor = ctx == null ? initThreadsExecutor()
                                      : ctx.getThreadsExecutor(behavior.getName(), nbMaxThreads);
        
        namePrefix = behavior.getName() + "-";
        
    }
    
    private ThreadsExecutorService initThreadsExecutor() {
        return new ThreadsExecutorService("MultiThreadedProviderRunner.default", 1, false);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Future<ProviderFutureResult> run(final String name, final ProviderTask task) {
        return threadsExecutor.submit(name == null ? namePrefix : name, task,
                                      new ProviderTaskStopListener(task.getEvent(), task.getPluginGav()),
                                      new ProviderTaskStartListener(task.getEvent(), task.getPluginGav()));
    }
    
    @Override
    public Thread newThread(final Runnable runnable) {
        return new Thread(threadGroup, runnable, namePrefix + incrementThreadNumber(), 0);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private synchronized int incrementThreadNumber() {
        threadNumber++;
        return threadNumber;
    }
    
    // =========================================================================
    // Override ClassBehaviorAttributes
    // =========================================================================
    @Override
    public boolean accept(final Class<?> clazz) {
        return clazz.isAssignableFrom(this.getClass());
    }
    
    @Override
    public <T> T build(final ClassBehavior behavior, final ConfigHandler<String, String> config) {
        return (T) new MultiThreadedProviderRunner(behavior, config);
    }
}
