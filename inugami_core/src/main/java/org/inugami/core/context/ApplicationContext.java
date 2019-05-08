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
package org.inugami.core.context;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import org.inugami.api.alertings.AlertingProvider;
import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.constants.JvmKeyValues;
import org.inugami.api.handlers.Handler;
import org.inugami.api.listeners.EngineListenerProcessor;
import org.inugami.api.models.Gav;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.processors.ClassBehavior;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.processors.Processor;
import org.inugami.api.providers.Provider;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.commons.engine.JavaScriptEngine;
import org.inugami.commons.threads.ThreadsExecutorService;
import org.inugami.configuration.models.app.ApplicationConfig;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.core.context.runner.EventRunnerFuture;
import org.inugami.core.services.cache.CacheService;
import org.inugami.core.services.connectors.HttpConnector;

/**
 * ApplicationContext
 * 
 * @author patrick_guillerm
 * @since 17 janv. 2017
 */
public interface ApplicationContext {
    
    // =========================================================================
    // ROOT CONFIGURATION
    // =========================================================================
    @Deprecated
    Optional<String> getStartDayTime();
    
    @Deprecated
    long getStartDayTimeValue();
    
    @Deprecated
    Optional<Calendar> getStartDayTimeCalendar();
    
    @Deprecated
    void setStartDayTime(String dayTime);
    
    AdminVersionInformations getAdminVersionInformations();
    
    File getHome();
    
    boolean isTechnicalUser(String loginName);
    
    default boolean isDevMode() {
        return "dev".equalsIgnoreCase(JvmKeyValues.ENVIRONMENT.or("prod"));
    }
    
    // =========================================================================
    // LISTENERS
    // =========================================================================
    void callAllListener(final EngineListenerProcessor processor);
    
    // =========================================================================
    // EVENTS
    // =========================================================================
    List<ProviderFutureResult> processEvents();
    
    List<Future<EventRunnerFuture>> processEventsForce();
    
    List<Future<EventRunnerFuture>> processEventsForce(String cronExpre);
    
    ProviderFutureResult applyProcessors(ProviderFutureResult data, GenericEvent currentEvent);
    
    List<ProviderFutureResult> processPluginEvents(Gav gav, String excludeRegex);
    
    FutureData<ProviderFutureResult> processPluginEvent(final String eventName, final Gav gav) throws Exception;
    
    // =========================================================================
    // PLUGINS
    // =========================================================================
    Optional<List<Plugin>> getPlugins();
    
    Provider getProvider(final String providerName);
    
    <T extends Handler> T getHandler(final String name);
    
    Processor getProcessor(final String name);
    
    AlertingProvider getAlertingProvider(final String name);
    
    List<AlertingProvider> getAlertingProviders();
    
    void disablePlugin(final Gav gav);
    
    void enablePlugin(final Gav gav);
    
    default Plugin getPlugin(final String groupId, final String artifactId) {
        Plugin result = null;
        
        final Optional<List<Plugin>> allPlugins = getPlugins();
        if ((groupId != null) && (artifactId != null) && allPlugins.isPresent()) {
            //@formatter:off
            result= allPlugins.get()
                              .stream()
                              .filter(plugin-> groupId.equals(plugin.getGav().getGroupId()) && 
                                            artifactId.equals(plugin.getGav().getArtifactId()))
                              .findFirst()
                              .orElse(null);
            //@formatter:on
        }
        return result;
    }
    
    // =========================================================================
    // ALERTS
    // =========================================================================
    void sendAlert(AlertingResult alert);
    
    // =========================================================================
    // SPI
    // =========================================================================
    <T> List<T> loadSpiService(Class<?> type);
    
    <T> T loadSpiService(String name, Class<?> type);
    
    <T> List<T> loadSpiService(Class<?> type, T defaultImplementation);
    
    // =========================================================================
    // CACHE
    // =========================================================================
    CacheService getCache();
    
    void cleanCache();
    
    // =========================================================================
    // METRICS
    // =========================================================================
    ConfigHandler<String, String> getGlobalConfiguration();
    
    ApplicationConfig getApplicationConfiguration();
    
    // =========================================================================
    // ThreadsExecutorService
    // =========================================================================
    ThreadsExecutorService getThreadsExecutor(String name, int nbMaxThreads);
    
    HttpConnector getHttpConnector(final String name, final int maxConnections, final int timeout, final int ttl,
                                   final int maxPerRoute, final int socketTimeout);
    
    HttpConnector getHttpConnector(final ClassBehavior behavior, final int maxConnections, final int timeout,
                                   final int ttl, final int maxPerRoute, final int socketTimeout);
    
    HttpConnector getHttpConnector(final ClassBehavior behavior, final String name, final int maxConnections,
                                   final int timeout, final int ttl, final int maxPerRoute, final int socketTimeout);
    
    // =========================================================================
    // SCRIPTS
    // =========================================================================
    JavaScriptEngine getScriptEngine();
    
}
