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
package io.inugami.core.context;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import io.inugami.api.alertings.AlertingProvider;
import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.handlers.Handler;
import io.inugami.api.listeners.EngineListenerProcessor;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.processors.Processor;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.commons.engine.JavaScriptEngine;
import io.inugami.commons.threads.ThreadsExecutorService;
import io.inugami.configuration.models.app.ApplicationConfig;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.core.context.runner.EventRunnerFuture;
import io.inugami.core.services.cache.CacheService;
import io.inugami.core.services.connectors.HttpConnector;

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
    
    void notifyDynamicEventResult(final GenericEvent event, ProviderFutureResult providerResult,
                                  final String channelName);
    
    // =========================================================================
    // PLUGINS
    // =========================================================================
    Optional<List<Plugin>> getPlugins();
    
    Provider getProvider(final String providerName);
    
    <T extends Handler> T getHandler(final String name);
    
    Processor getProcessor(final String name);
    
    AlertingProvider getAlertingProvider(final String name);
    
    AlertingProvider getAlertingProvider();
    
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
    
    // =========================================================================
    // LIFECYCLE
    // =========================================================================
    void registerForShutodown(BootstrapContext subContext);
    
    default void forceShutdownSubContext() {
    };
}
