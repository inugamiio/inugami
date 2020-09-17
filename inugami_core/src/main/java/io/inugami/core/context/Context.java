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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.inugami.api.alertings.AlertingProvider;
import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.services.ProcessorException;
import io.inugami.api.functionnals.VoidFunction;
import io.inugami.api.handlers.Handler;
import io.inugami.api.listeners.EngineListener;
import io.inugami.api.listeners.EngineListenerProcessor;
import io.inugami.api.listeners.TaskFinishListener;
import io.inugami.api.listeners.TaskStartListener;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.tools.RunnableContext;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.processors.Processor;
import io.inugami.api.processors.ProcessorModel;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.spi.SpiLoader;
import io.inugami.api.tools.NamedComponent;
import io.inugami.commons.engine.JavaScriptEngine;
import io.inugami.commons.messages.MessagesServices;
import io.inugami.commons.threads.ThreadsExecutorService;
import io.inugami.configuration.models.EventConfig;
import io.inugami.configuration.models.app.ApplicationConfig;
import io.inugami.configuration.models.app.SecurityConfiguration;
import io.inugami.configuration.models.app.UserConfig;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.configuration.services.resolver.ConfigurationResolver;
import io.inugami.core.alertings.DefaultAlertingProvider;
import io.inugami.core.context.runner.CallableEvent;
import io.inugami.core.context.runner.EventRunnerFuture;
import io.inugami.core.context.runner.PluginEventsRunner;
import io.inugami.core.context.system.SystemInfosManager;
import io.inugami.core.listerners.CoreListerner;
import io.inugami.core.services.cache.CacheService;
import io.inugami.core.services.cache.CacheTypes;
import io.inugami.core.services.connectors.HttpConnector;
import io.inugami.core.services.scheduler.SchedulerService;
import io.inugami.core.services.sse.SseReconnect;
import io.inugami.core.services.sse.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Context
 * 
 * @author patrick_guillerm
 * @since 16 déc. 2016
 */
//@formatter:off
public final class Context implements ApplicationContext,
                                      TaskStartListener,
                                      TaskFinishListener,
                                      BootstrapContext{
//@formatter:on
    
    // =========================================================================
    // CONFIG
    // =========================================================================
    public static final TimeZone DEFAULT_TIMEZONE = initDefaultTimeZone();
    
    private final static Logger  LOGGER           = LoggerFactory.getLogger(Context.class);
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Plugin                 ROOT_DYNAMIC_PLUGIN    = new Plugin("io.inugami", "inugami_core");
    
    private final GenericContext<Context>       genericContext         = new GenericContext<Context>();
    
    private final ConfigurationHolder           configuration;
    
    private ThreadsExecutorService              threadsExecutor;
    
    private ThreadsExecutorService              threadsStandaloneExecutor;
    
    private final CacheService                  cache;
    
    private final ContextProcessEventFromCache  processEventFromCache;
    
    private final Map<String, SchedulerService> schedulers             = new HashMap<>();
    
    private final List<RunnableContext>         runnableContext        = new ArrayList<>();
    
    private final SpiLoader                     spiLoader;
    
    private final AdminVersionInformations      adminVersionInfos;
    
    private final SystemInfosManager            systemInfosManager;
    
    private final boolean                       disableCron;
    
    private final MetricsEventsSenderSse        metricsEventsSenderSse = new MetricsEventsSenderSse();
    
    private boolean                             starting               = true;
    
    private final JavaScriptEngine              javaScriptEngine       = JavaScriptEngine.getInstance();
    
    private final List<BootstrapContext>        subContexts            = new ArrayList<>();
    
    private volatile static Context             instance;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    protected Context(final EngineListener listener, final boolean disableCron) {
        MdcService.initialize();
        cache = new CacheService();
        this.disableCron = disableCron;
        spiLoader = new SpiLoader();
        
        MessagesServices.registerFromClassloader("META-INF/inugami-core.properties");
        MessagesServices.registerFromClassloader("META-INF/inugami-core_fr.properties", "fr");
        javaScriptEngine.registerFromClassLoader("META-INF/inugami_js_api.js");
        
        if (!disableCron) {
            SseService.start();
        }
        //@formatter:off
        configuration = listener == null ? new ConfigurationHolder(genericContext,new CoreListerner())
                                         : new ConfigurationHolder(genericContext,new CoreListerner(), listener);
        
        systemInfosManager      = disableCron ? null : SystemInfosManager.getInstance();
        
        adminVersionInfos       = initAdminVersionInfos();
        processEventFromCache   = new ContextProcessEventFromCache(cache);
        //@formatter:on
    }
    
    private void initialize() {
        configuration.initialize();
        final ApplicationConfig appConfig = configuration.getApplicationConfig();
        MessagesServices.registerConfig(getGlobalConfiguration());
        
        threadsExecutor = new ThreadsExecutorService("Context", appConfig.getMaxPluginRunning(), true,
                                                     appConfig.getTimeout());
        
        threadsStandaloneExecutor = new ThreadsExecutorService("Context-standalone-plugins",
                                                               appConfig.getMaxPluginRunningStandalone(), true,
                                                               appConfig.getTimeout());
        if (!disableCron) {
            initializeSchedulers();
            metricsEventsSenderSse.start();
            processEventsForce();
        }
        starting = false;
    }
    
    private void initializeSchedulers() {
        for (final String cronExpression : configuration.getSchedulers()) {
            final String realCronExpression = getGlobalConfiguration().applyProperties(cronExpression);
            
            if (!schedulers.containsKey(cronExpression)) {
                final SchedulerService scheduler = new SchedulerService(realCronExpression, cronExpression);
                schedulers.put(cronExpression, scheduler);
            }
        }
        
        runnableContext.add(new SseReconnect());
    }
    
    private AdminVersionInformations initAdminVersionInfos() {
        final AdminVersionInformations result = new AdminVersionInformations();
        
        result.setPlugins(getPlugins().orElseGet(Collections::emptyList));
        
        if (systemInfosManager != null) {
            result.setSystemInfo(systemInfosManager.getSystemInfos());
        }
        return result;
    }
    
    @Override
    public void registerForShutodown(final BootstrapContext subContext) {
        Asserts.notNull("sub context is mandatory", subContext);
        subContexts.add(subContext);
    }
    
    // =========================================================================
    // GET INSTANCE
    // =========================================================================
    public static ApplicationContext initializeStandalone() {
        return getInstance(null, true);
    }
    
    public static ApplicationContext getInstance() {
        return getInstance(null, false);
    }
    
    protected static ApplicationContext getInstance(final EngineListener listener) {
        return getInstance(listener, false);
    }
    
    protected synchronized static ApplicationContext getInstance(final EngineListener listener,
                                                                 final boolean disableCron) {
        if (instance == null) {
            instance = new Context(listener, disableCron);
            instance.initialize();
        }
        return instance;
    }
    
    // =========================================================================
    // ROOT INFORMATIONS
    // =========================================================================
    @Override
    public AdminVersionInformations getAdminVersionInformations() {
        adminVersionInfos.setPlugins(getPlugins().orElseGet(Collections::emptyList));
        adminVersionInfos.setSystemInfo(systemInfosManager.getSystemInfos());
        return adminVersionInfos.cloneObj();
    }
    
    @Override
    public File getHome() {
        return ConfigurationResolver.HOME_PATH;
    }
    
    // =========================================================================
    // EVENTS
    // =========================================================================
    
    @Override
    public FutureData<ProviderFutureResult> processPluginEvent(final String eventName, final Gav gav) throws Exception {
        Asserts.notEmpty("event name mustn't be null!", eventName);
        Asserts.notNull("plugin GAV mustn't be null!", gav);
        
        final Optional<List<Plugin>> plugins = getPlugins();
        Asserts.isTrue("no plugin found!", plugins.isPresent());
        
        //@formatter:off
        final Optional<Plugin> foundPlugin = plugins.orElseGet(Collections::emptyList)
                                                    .stream()
                                                    .filter(plugin-> matchPlugin(plugin,gav))
                                                    .findFirst();
        //@formatter:on
        
        Asserts.isTrue(String.format("plugin not found !(%s)", gav.getHash()), foundPlugin.isPresent());
        final Plugin plugin = foundPlugin.get();
        final String pluginHash = plugin.getGav().getHash();
        
        Asserts.isTrue(String.format("plugin has no one event!(%s)", pluginHash), plugin.getEvents().isPresent());
        
        //@formatter:off
        final List<GenericEvent> events = new ArrayList<>();
        plugin.getEvents().get()
              .stream()
              .map(EventConfig::getSimpleEvents)
              .filter(Objects::nonNull)
              .flatMap(simpleEvents-> simpleEvents.stream())
              .forEach(events::add);
        
        plugin.getEvents().get()
              .stream()
              .map(EventConfig::getEvents)
              .filter(Objects::nonNull)
              .flatMap(confEvents-> confEvents.stream())
              .forEach(events::add);
        //@formatter:on
        Asserts.isFalse(String.format("no event define in plugin %s !", pluginHash), events.isEmpty());
        
        final Optional<GenericEvent> event = events.stream().filter(item -> item.getName().equals(eventName)).findFirst();
        
        Asserts.isTrue(String.format("event (%s) not found in plugin %s !", eventName, pluginHash), event.isPresent());
        
        return new CallableEvent(plugin, event.get(), plugin.buildChannelName(), this, null,
                                 getApplicationConfiguration().getTimeout(), starting).call();
    }
    
    private boolean matchPlugin(final Plugin plugin, final Gav gav) {
        Asserts.notNull("plugin must have GAV informations!", plugin.getGav());
        //@formatter:off
        return     plugin.getGav().getGroupId().equals(gav.getGroupId())
                && plugin.getGav().getArtifactId().equals(gav.getArtifactId());
        //@formatter:on
    }
    
    @Override
    public List<ProviderFutureResult> processEvents() {
        List<ProviderFutureResult> result = null;
        final Optional<List<Plugin>> plugins = getPlugins();
        if (plugins.isPresent()) {
            result = processEventFromCache.process(plugins.get(), null);
        }
        return result;
    }
    
    @Deprecated
    @Override
    public List<ProviderFutureResult> processPluginEvents(final Gav gav, final String excludeRegex) {
        final Optional<List<Plugin>> plugins = getPlugins();
        List<ProviderFutureResult> result = null;
        if (plugins.isPresent()) {
            //@formatter:off
            final List<Plugin> pluginsToProcess = plugins.get().stream()
                                                         .filter(Plugin::isEnabled)
                                                         .filter(plugin-> plugin.getGav().equals(gav))
                                                         .collect(Collectors.toList());
            //@formatter:on
            result = processEventFromCache.process(pluginsToProcess, null);
        }
        return result;
    }
    
    @Override
    public List<Future<EventRunnerFuture>> processEventsForce() {
        return processingEvents(null);
    }
    
    @Override
    public List<Future<EventRunnerFuture>> processEventsForce(final String cronExpre) {
        return processingEvents(cronExpre);
    }
    
    private List<Future<EventRunnerFuture>> processingEvents(final String cronExpre) {
        final List<Future<EventRunnerFuture>> futureResults = new ArrayList<>();
        final Optional<List<Plugin>> plugins = getPlugins();
        if (plugins.isPresent()) {
            //@formatter:off
            plugins.get()
                   .stream()
                   .filter(Plugin::isEnabled)
                   .filter(plugin->plugin.getEvents().isPresent())
                   .forEach(plugin-> {
                      final Future<EventRunnerFuture> futureResult = threadsExecutor.submit(plugin.getGav().getHash(),
                                                                                            new PluginEventsRunner(plugin,
                                                                                                             getApplicationConfiguration().getMaxRunningEvents(),
                                                                                                             cronExpre,
                                                                                                             this,
                                                                                                             getApplicationConfiguration().getTimeout(),
                                                                                                             starting),
                                                                                            this,
                                                                                            this);
                      futureResults.add(futureResult);
                   });
            //@formatter:on
            
        }
        else {
            Loggers.CONFIG.warn("no plugin define, can't process events...");
        }
        return futureResults;
    }
    
    @Override
    public void onStart(final long time, final String name) {
        LOGGER.debug("plugin start : {} @ {}", name, time);
        
    }
    
    @Override
    public void onFinish(final long time, final long delais, final String name, final Object result,
                         final Exception error) {
        if (error == null) {
            LOGGER.debug("plugin finish :{} @ {}", name, time);
        }
        else {
            LOGGER.debug("plugin finish : {} @ {} - error : {}", name, time, error.getMessage());
        }
    }
    
    @Override
    public void notifyDynamicEventResult(final GenericEvent event, final ProviderFutureResult providerResult,
                                         final String channelName) {
        new CallableEvent(ROOT_DYNAMIC_PLUGIN, event, channelName, this, null,
                          getApplicationConfiguration().getTimeout(), starting).onDone(providerResult, event, null);
    }
    
    // =========================================================================
    // PLUGINS
    // =========================================================================
    @Override
    public Optional<List<Plugin>> getPlugins() {
        return configuration.getPlugins();
    }
    
    @Override
    public void disablePlugin(final Gav gav) {
        getPlugin(gav).ifPresent(plugin -> plugin.disablePlugin());
    }
    
    @Override
    public void enablePlugin(final Gav gav) {
        getPlugin(gav).ifPresent(plugin -> plugin.enablePlugin());
    }
    
    public Optional<Plugin> getPlugin(final Gav gav) {
        final Plugin result = configuration.getPlugin(gav);
        return result == null ? Optional.empty() : Optional.of(result);
    }
    
    @Override
    public Provider getProvider(final String name) {
        return getNamedComponent(name, (plugin) -> plugin.getProviders().orElse(null));
    }
    
    @Override
    public <T extends Handler> T getHandler(final String name) {
        return (T) getNamedComponent(name, (plugin) -> plugin.getHandlers().orElse(null));
    }
    
    @Override
    public Processor getProcessor(final String name) {
        return getNamedComponent(name, (plugin) -> plugin.getProcessors().orElse(null));
    }
    
    @Override
    public AlertingProvider getAlertingProvider(final String name) {
        return getNamedComponent(name, (plugin) -> plugin.getAlertingProviders().orElse(null));
    }
    
    @Override
    public AlertingProvider getAlertingProvider() {
        AlertingProvider result = null;
        final List<AlertingProvider> providers = getAlertingProviders();
        if (providers != null) {
            for (final AlertingProvider provider : providers) {
                if (provider instanceof DefaultAlertingProvider) {
                    result = provider;
                    break;
                }
            }
        }
        
        return result;
    }
    
    @Override
    public List<AlertingProvider> getAlertingProviders() {
        final List<AlertingProvider> result = new ArrayList<>();
        for (final Plugin plugin : getPlugins().orElse(new ArrayList<>())) {
            if (plugin.getAlertingProviders().isPresent()) {
                result.addAll(plugin.getAlertingProviders().get());
            }
        }
        return result;
    }
    
    public <T extends NamedComponent> T getNamedComponent(final String name,
                                                          final Function<Plugin, List<T>> componentLoader) {
        Asserts.notNull("NamedComponent name mustn't be null!", name);
        final List<T> namedComponents = new ArrayList<>();
        
        if (getPlugins().isPresent()) {
            for (final Plugin plugin : getPlugins().orElseGet(Collections::emptyList)) {
                final List<T> localComponents = componentLoader.apply(plugin);
                if (localComponents != null) {
                    namedComponents.addAll(localComponents);
                }
            }
        }
        
        //@formatter:off
        final Optional<T> component = namedComponents.stream()
                                                     .filter(item->name.equals(item.getName()))
                                                     .findFirst();
        //@formatter:on        
        
        Asserts.isTrue(String.format("NamedComponent \"%s\" doesn't exists!", name), component.isPresent());
        return component.get();
    }
    
    @Override
    public ProviderFutureResult applyProcessors(final ProviderFutureResult data, final GenericEvent currentEvent) {
        if (currentEvent == null) {
            return data;
        }
        
        ProviderFutureResult result = data;
        try {
            result = applyProcessor(data, currentEvent);
        }
        catch (final ProcessorException e) {
            Loggers.PROCESSOR.error(e.getMessage());
            Loggers.DEBUG.error(e.getMessage(), e);
        }
        return result;
        
    }
    
    private synchronized ProviderFutureResult applyProcessor(final ProviderFutureResult data,
                                                             final GenericEvent currentEvent) throws ProcessorException {
        ProviderFutureResult result = data;
        if (data == null) {
            return data;
        }
        
        if (currentEvent.getProcessors().isPresent()) {
            for (final ProcessorModel proc : currentEvent.getProcessors().get()) {
                ProviderFutureResult modifiedResult = null;
                try {
                    modifiedResult = getProcessor(proc.getName()).process(currentEvent, result);
                }
                catch (final Exception e) {
                    result = null;
                    Loggers.DEBUG.error(e.getMessage(), e);
                    Loggers.PLUGINS.error(e.getMessage());
                }
                
                if (modifiedResult != null) {
                    result = modifiedResult;
                }
            }
        }
        return result;
    }
    
    // =========================================================================
    // SCHEDULER
    // =========================================================================
    @Override
    public void startup() {
        subContexts.stream().forEach(BootstrapContext::startup);
        //@formatter:off
        schedulers.entrySet()
                  .stream()
                  .map(entry->entry.getValue())
                  .forEach(SchedulerService::start);
        //@formatter:on
        
        final List<Provider> providers = getBottstrapProviders();
        for (final Provider provider : providers) {
            ((io.inugami.api.ctx.BootstrapContext) provider).bootrap(this);
        }
        processEventsForce();
    }
    
    @Override
    public void shutdown() {
        
        final List<Provider> providers = getBottstrapProviders();
        for (final Provider provider : providers) {
            ((io.inugami.api.ctx.BootstrapContext) provider).shutdown(this);
        }
        
        metricsEventsSenderSse.interrupt();
        SseService.shutdown();
        runnableContext.stream().forEach(RunnableContext::shutdown);
        forceShutdownSubContext();
        genericContext.shutdown(this);
        
        //@formatter:off
        schedulers.entrySet()
                  .stream()
                  .map(entry->entry.getValue())
                  .forEach(SchedulerService::shutdown);

        processShutdown(threadsExecutor,           ()->threadsExecutor.shutdown());
        processShutdown(threadsStandaloneExecutor, ()->threadsStandaloneExecutor.shutdown());
        processShutdown(systemInfosManager,        ()->systemInfosManager.shutdown());
        //@formatter:on
    }
    
    private List<Provider> getBottstrapProviders() {
        //@formatter:off
        return getPlugins().orElseGet(Collections::emptyList)
                            .stream()
                            .map(Plugin::getProviders)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .flatMap(List::stream)
                            .filter(provider-> provider instanceof io.inugami.api.ctx.BootstrapContext)
                            .collect(Collectors.toList());
       //@formatter:on
    }
    
    @Override
    public void forceShutdownSubContext() {
        subContexts.stream().forEach(BootstrapContext::shutdown);
    }
    
    // =========================================================================
    // LISTENERS
    // =========================================================================
    @Override
    public synchronized void callAllListener(final EngineListenerProcessor processor) {
        configuration.callAllListener(processor);
    }
    
    // =========================================================================
    // ALERTS
    // =========================================================================
    @Override
    public void sendAlert(final AlertingResult alert) {
        if ((alert != null) && getPlugins().isPresent()) {
            for (final Plugin plugin : getPlugins().get()) {
                final List<AlertingProvider> alertsProviders = plugin.getAlertingProviders().orElse(new ArrayList<>());
                for (final AlertingProvider provider : alertsProviders) {
                    provider.appendAlert(alert);
                }
            }
        }
    }
    
    // =========================================================================
    // SPI
    // =========================================================================
    @Override
    public synchronized <T> List<T> loadSpiService(final Class<?> type) {
        return spiLoader.loadSpiService(type);
    }
    
    @Override
    public synchronized <T> List<T> loadSpiService(final Class<?> type, final T defaultImplementation) {
        return spiLoader.loadSpiService(type, defaultImplementation);
    }
    
    @Override
    public synchronized <T> T loadSpiService(final String name, final Class<?> type) {
        return spiLoader.loadSpiService(name, type);
    }
    
    // =========================================================================
    // METRICS
    // =========================================================================
    
    // =========================================================================
    // CONFIGURATIONS
    // =========================================================================
    @Override
    public ConfigHandler<String, String> getGlobalConfiguration() {
        return configuration.getGlobalProperties();
    }
    
    @Override
    public ApplicationConfig getApplicationConfiguration() {
        return configuration.getApplicationConfig();
    }
    
    // =========================================================================
    // START DAY TIME
    // =========================================================================
    @Override
    @Deprecated
    public Optional<String> getStartDayTime() {
        Optional<String> result = Optional.empty();
        
        final String dayTime = configuration.getStartDayTime();
        if (dayTime != null) {
            result = Optional.of(dayTime);
        }
        return result;
    }
    
    @Override
    @Deprecated
    public long getStartDayTimeValue() {
        return configuration.getStartDayTimeValue();
    }
    
    @Override
    @Deprecated
    public Optional<Calendar> getStartDayTimeCalendar() {
        Optional<Calendar> result = Optional.empty();
        final Calendar data = configuration.getStartDayTimeCalendar();
        if (data != null) {
            result = Optional.of(data);
        }
        return result;
    }
    
    @Override
    @Deprecated
    public void setStartDayTime(final String dayTime) {
        Asserts.notNull("Error day start time musn't be null!", dayTime);
        configuration.setStartDayTime(dayTime);
    }
    
    // =========================================================================
    // CONNECTORS
    // =========================================================================
    @Override
    public HttpConnector getHttpConnector(final String name, final int maxConnections, final int timeout, final int ttl,
                                          final int maxPerRoute, final int socketTimeout) {
        return genericContext.getHttpConnector(name, maxConnections, socketTimeout, ttl, maxPerRoute);
    }
    
    @Override
    public HttpConnector getHttpConnector(final ClassBehavior behavior, final int maxConnections, final int timeout,
                                          final int ttl, final int maxPerRoute, final int socketTimeout) {
        return getHttpConnector(behavior.getName(), maxConnections, timeout, ttl, maxPerRoute, socketTimeout);
    }
    
    @Override
    public HttpConnector getHttpConnector(final ClassBehavior behavior, final String name, final int maxConnections,
                                          final int timeout, final int ttl, final int maxPerRoute,
                                          final int socketTimeout) {
        return getHttpConnector(String.join("_", behavior.getName(), name), maxConnections, timeout, ttl, maxPerRoute,
                                socketTimeout);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    
    @Override
    public CacheService getCache() {
        return cache;
    }
    
    public static <T extends Serializable> T getFromCache(final CacheTypes type, final String key) {
        return instance.getCache().get(type, key);
    }
    
    public static void putInCache(final CacheTypes type, final String key, final Serializable value) {
        instance.getCache().put(type, key, value);
    }
    
    @Override
    public void cleanCache() {
        cache.clear();
    }
    
    @Override
    public ThreadsExecutorService getThreadsExecutor(final String name, final int nbMaxThreads) {
        return genericContext.getThreadsExecutor(name, nbMaxThreads);
    }
    
    private static int initIntValue(final String key, final int defaultValue) {
        final String value = System.getProperty(key);
        return value == null ? defaultValue : Integer.parseInt(value);
    }
    
    private static TimeZone initDefaultTimeZone() {
        final String jvmDefineTimeZone = System.getProperty("user.timezone");
        return TimeZone.getTimeZone(jvmDefineTimeZone == null ? "Europe/Paris" : jvmDefineTimeZone);
    }
    
    private void processShutdown(final Object obj, final VoidFunction function) {
        if (obj != null) {
            function.process();
        }
    }
    
    @Override
    public boolean isTechnicalUser(final String loginName) {
        //@formatter:off
        return getApplicationConfiguration()
                .getSecurityTechnicalConfig()
                .stream()
                .map(SecurityConfiguration::getUsers)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(UserConfig::getLogin)
                .filter(login->login.equals(loginName))
                .findFirst()
                .isPresent();
            //@formatter:off
    }

    @Override
    public JavaScriptEngine getScriptEngine() {
        return javaScriptEngine;
    }






}
