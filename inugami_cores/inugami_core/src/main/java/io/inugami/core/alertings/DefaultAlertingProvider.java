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
package io.inugami.core.alertings;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import io.inugami.api.alertings.AlertingProvider;
import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.alertings.DynamicAlertingLevel;
import io.inugami.api.dao.Dao;
import io.inugami.api.dao.SaveEntitiesResult;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.mapping.Mapper;
import io.inugami.api.models.Gav;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.models.data.JsonObjectStrFactory;
import io.inugami.api.models.data.basic.Json;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.events.AlertingModel;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.commons.engine.JavaScriptEngine;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.threads.RunAndCloseService;
import io.inugami.commons.tools.ProxyBuilder;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.core.alertings.dynamic.services.ComputeDynamicAlert;
import io.inugami.core.alertings.senders.AlertSenderService;
import io.inugami.core.context.Context;
import io.inugami.core.services.sse.SseService;

import javax.enterprise.inject.spi.CDI;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * DefaultAlertingProvider
 *
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
@SuppressWarnings({"java:S2789", "java:S5361", "java:S3655", "java:S108", "java:S2629"})
public class DefaultAlertingProvider implements AlertingProvider {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String                              MSG                            = "{}-{} : {}";
    public static final  String                              LIST_SEPARATOR                 = ";";
    public static final  String                              NO_ALERT_SENDER_SERVICE_DEFINE = "no alertSenderService define!";
    private final        String                              name;
    private final        JavaScriptEngine                    engine                         = JavaScriptEngine.getInstance();
    private final        Mapper<AlertEntity, AlertingResult> alertResultToEntity            = new AlertResultToEntityMapper();
    private final        Mapper<AlertingResult, AlertEntity> transformEntityToResult        = new TransformAlertEntityToResult();
    private final        ComputeDynamicAlert                 computeDynamicAlert            = new ComputeDynamicAlert();
    private              AlertSenderService                  alertSenderService;
    private              Dao                                 dao;
    private final        Map<String, Long>                   cacheHasModif                  = new ConcurrentHashMap<>();
    private final        List<File>                          files                          = new ArrayList<>();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DefaultAlertingProvider(final String name, final ConfigHandler<String, String> config) {
        this.name = name;
        final String filesRaw = config.optionnal().get("files");
        //@formatter:off
        if(filesRaw!=null) {
            for(final String path : filesRaw.split(LIST_SEPARATOR)) {
                final File localFile = new File(path.trim());
                if(localFile.exists()) {
                    files.add(localFile);
                }
            }
        }
        //@formatter:on                   

        try {
            alertSenderService = CDI.current().select(AlertSenderService.class).get();
            dao = CDI.current().select(Dao.class).get();
        } catch (final IllegalStateException e) {
            Loggers.XLLOG.error(e.getMessage());
            //@formatter:off
            alertSenderService = new ProxyBuilder<AlertSenderService>()
										.addSuperClass(AlertSenderService.class)
										.build();
            //@formatter:on
            dao = new ProxyBuilder<Dao>().addInterface(Dao.class).build();
        }
        Loggers.INIT.info("initialize {}", DefaultAlertingProvider.class.getSimpleName());
    }

    // =========================================================================
    // INIT
    // =========================================================================
    @Override
    public void postConstruct() {
        loadFiles();
    }

    private void loadFiles() {
        if (hasScriptChange()) {
            processLoadFiles();
        }
    }

    private synchronized void processLoadFiles() {
        for (final File path : files) {
            String scriptContent;
            try {
                scriptContent = FilesUtils.readContent(path);
                engine.register(scriptContent, path.getAbsolutePath());
                cacheHasModif.put(path.getAbsolutePath(), path.lastModified());
            } catch (final IOException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
                Loggers.SCRIPTS.error(e.getMessage());
            }
        }
    }

    private synchronized boolean hasScriptChange() {
        boolean result = cacheHasModif.isEmpty();
        if (!files.isEmpty() && !result) {
            for (final File file : files) {
                final Long lastReferencedModif = cacheHasModif.get(file.getAbsolutePath());
                result = (lastReferencedModif == null) || (lastReferencedModif != file.lastModified());
                if (result) {
                    break;
                }
            }
        }
        return !files.isEmpty() && result;
    }

    // =========================================================================
    // METHODS PROCESS
    // =========================================================================
    @Override
    public AlertingResult process(final Gav gav,
                                  final GenericEvent event,
                                  final ProviderFutureResult data,
                                  final AlertingModel alertConfig,
                                  final String preload) {
        loadFiles();
        AlertingResult result = null;
        result = processAlertingCheck(gav, event, data, alertConfig, preload);

        if (result != null) {
            final String alertGenericName = alertConfig.getName() == null ? event.getName() : alertConfig.getName();

            SaveEntitiesResult<AlertEntity> registerResult = null;
            registerResult = saveAlerts(result, alertGenericName);
            postProcessAlerting(registerResult);
        }
        return result;
    }

    @Override
    public void processDynamicAlert(final Gav gav,
                                    final SimpleEvent event,
                                    final ProviderFutureResult data,
                                    final List<DynamicAlertingLevel> levels,
                                    final String message,
                                    final String subMessage,
                                    final List<String> tags,
                                    final List<String> alertSenders) {
        final List<AlertingResult> dynamicAlerts = computeDynamicAlert.compute(event, data, levels, message, subMessage, tags, alertSenders);
        if (dynamicAlerts != null) {
            for (final AlertingResult alert : dynamicAlerts) {
                appendAlert(alert);
            }
        }
    }

    // =========================================================================
    // SAVE ALERTS
    // =========================================================================
    @Override
    public void appendAlert(final AlertingResult alert) {
        if ((alert != null) && (alert.getAlerteName() != null)) {
            saveAlerts(alert, AlertingProvider.class.getSimpleName());
            processSavedAlerts(Arrays.asList(alert));
        }
    }

    @Override
    public void processSavedAlerts(final List<AlertingResult> alerts) {
        Loggers.ALERTING.info("processSavedAlerts : nb alerts :{}", alerts == null ? 0 : alerts.size());
        if (alerts != null) {
            for (final AlertingResult alert : alerts) {
                if (!alert.isMultiAlerts()) {
                    sendAlert(alert);
                }
            }
        }
    }

    private SaveEntitiesResult<AlertEntity> saveAlerts(final AlertingResult alert, final String alertGenericName) {
        SaveEntitiesResult<AlertEntity> result   = null;
        final List<AlertEntity>         entities = buildEntities(alert, alertGenericName);
        try {
            result = dao.register(entities, AlertEntity.class);
            SseService.sendAlertsUpdate();
        } catch (final Exception e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.ALERTING.error(e.getMessage());
        }
        return result;
    }

    private List<AlertEntity> buildEntities(final AlertingResult alert, final String alertGenericName) {
        final List<AlertEntity> result = new ArrayList<>();
        if (isMultiAlerts(alert)) {
            result.addAll(extractMutliAlerts(alert, alertGenericName));
        } else {
            final AlertEntity entity = alertResultToEntity.mapping(alert);
            if (entity.getAlerteName() == null) {
                entity.setAlerteName(alertGenericName);
            }
            if (!result.contains(entity)) {
                result.add(entity);
            }

        }
        return result;
    }

    private boolean isMultiAlerts(final AlertingResult alert) {
        return alert.isMultiAlerts();
    }

    private List<AlertEntity> extractMutliAlerts(final AlertingResult alert, final String alertGenericName) {
        final List<AlertEntity> result = new ArrayList<>();
        List<AlertingResult>    alerts = null;

        if (alert.getData() == null) {
            Loggers.DEBUG.error("multi alert hasn't any data! can't extracts alerts on {}", alert.getAlerteName());
        } else if (alert.getData() instanceof Json) {
            final Json   rawData = (Json) alert.getData();
            final String json    = rawData.convertToJson();
            alerts = deserializeMutliAlerts(json);
        }

        if (alerts != null) {
            int cursor = 0;
            for (final AlertingResult item : alerts) {
                if (item.getAlerteName() == null) {
                    Loggers.ALERTING.error("alert hasn't name : {}", alertGenericName);
                    item.setAlerteName(String.join(alertGenericName, "unamed", String.valueOf(cursor), "_"));
                    cursor++;
                }

                if (!contains(item.getAlerteName(), result)) {
                    result.add(alertResultToEntity.mapping(item));
                }
            }
        }
        return result;
    }

    private boolean contains(final String alerteName, final List<AlertEntity> values) {
        boolean result = false;
        if (alerteName != null) {
            result = values.stream()
                           .map(AlertEntity::getAlerteName)
                           .anyMatch(alerteName::equals);
        }
        return result;
    }

    protected List<AlertingResult> deserializeMutliAlerts(final String json) {
        //@formatter:off
        final MultiAlertsModel result= new JSONDeserializer<MultiAlertsModel>()
                        .use(null,MultiAlertsModel.class)
                        .use("alerts",ArrayList.class)
                        .use("alerts.values",AlertingResult.class)
                        .use("alerts.values.data",new JsonObjectStrFactory())
                        .deserialize(json);
        //@formatter:on

        return result == null ? null : result.getAlerts();
    }

    // =========================================================================
    // PROCESS
    // =========================================================================
    protected AlertingResult processAlertingCheck(final Gav gav,
                                                  final GenericEvent event,
                                                  final ProviderFutureResult data,
                                                  final AlertingModel alert,
                                                  final String preload) {
        AlertingResult result = null;
        if ((alert != null) && alert.grabFunction().isPresent()) {
            result = processFunction(gav, event, data, alert, preload);
        } else if (alert != null) {
            result = processCondition(gav, event, data, alert, preload);
        }
        return result;
    }

    // =========================================================================
    // PROCESS FUNCTION
    // =========================================================================
    private AlertingResult processFunction(final Gav gav,
                                           final GenericEvent event,
                                           final ProviderFutureResult data,
                                           final AlertingModel alert,
                                           final String preload) {
        String rawResult = null;

        if ((alert == null) || alert.grabFunction() == null || alert.grabFunction().isEmpty() || (data == null) ||
            (data.getData() == null)) {
            return null;
        }

        final Callable<String> task = () -> {
            MdcService.getInstance().initialize();
            String result = null;
            try {
                JsonObject jsonObj = null;
                if (data.getData().isPresent()) {
                    jsonObj = data.getData().get();
                }
                result = engine.execute(preload, alert.grabFunction().get(), gav, event, jsonObj);
            } catch (final Exception e) {
                Loggers.ALERTING.error("unable to process alerting check on {} - {} : {}", gav.getHash(), event.getName(), e.getMessage());
                Loggers.DEBUG.error(e.getMessage(), e);
            }
            return result;
        };

        final String threadName = String.join("_", DefaultAlertingProvider.class.getSimpleName(), String.valueOf(System.currentTimeMillis()), String.valueOf(System.nanoTime()));

        final List<String> alertDataRaw = new RunAndCloseService<String>(threadName, 1000L, 1, task).run();
        rawResult = alertDataRaw.isEmpty() ? null : alertDataRaw.get(0);

        return rawResult == null ? null : buildResult(rawResult, alert);
    }

    // =========================================================================
    // PROCESS CONDITION
    // =========================================================================
    private AlertingResult processCondition(final Gav gav,
                                            final GenericEvent event,
                                            final ProviderFutureResult data,
                                            final AlertingModel alert,
                                            final String preload) {

        final String functionName = buildFunctionName(gav, alert);
        final String function     = buildFunctionFromCondition(functionName, alert);

        String rawResult = null;
        try {
            final String     script      = String.join("\n", preload == null ? "" : preload, function);
            final JsonObject currentData = extractData(data);
            rawResult = engine.execute(script, functionName, gav, event, currentData);
        } catch (final ScriptException e) {
            Loggers.ALERTING.error(e.getMessage(), e);
        }

        return rawResult == null ? null : buildResult(rawResult, alert);
    }

    private JsonObject extractData(final ProviderFutureResult data) {
        return data == null || data.getData() == null || data.getData().isEmpty() ? null : data.getData().get();
    }

    // =========================================================================
    // BUILDER
    // =========================================================================

    private AlertingResult buildResult(final String json, final AlertingModel alert) {
        AlertingResult result = null;

        if ("true".equals(json) || "1".equals(json)) {
            result = buildFromBoolean(alert);
        } else {
            result = buildFromObject(json, alert);
        }
        return result;
    }

    private AlertingResult buildFromBoolean(final AlertingModel alert) {
        final AlertingResult result = new AlertingResult();
        result.setAlerteName(alert.getName());
        result.setLevel(alert.getLevel());
        result.setMessage(alert.getMessage());
        return result;
    }

    private AlertingResult buildFromObject(final String json, final AlertingModel alert) {
        AlertingResult result = null;
        try {
            final AlertingResult localResult = new JSONDeserializer<AlertingResult>().use(null, AlertingResult.class)
                                                                                     .deserialize(json);
            final Map<String, Object> rawData = new JSONDeserializer<Map<String, Object>>().deserialize(json);
            final JsonObject data = new Json(new JSONSerializer().exclude("*.class")
                                                                 .deepSerialize(rawData.get("data")));
            localResult.setData(data);

            localResult.setAlerteName(alert.getName());
            processIfNull(localResult.getLevel(), alert.getLevel(), localResult::setLevel);
            processIfNull(localResult.getMessage(), alert.getMessage(), localResult::setMessage);
            result = localResult;
        } catch (final Exception e) {
        }
        return result;
    }

    protected String buildFunctionName(final Gav gav, final AlertingModel alert) {
        //@formatter:off
        return String.join("_", gav.buildHash(),alert.getName()).replaceAll(":", "_").replaceAll("-", "_").replaceAll("[.]", "_");
        //@formatter:on

    }

    protected String buildFunctionFromCondition(final String functionName, final AlertingModel alert) {

        String result = engine.getFunction(functionName);
        if (result == null) {
            final JsonBuilder js = new JsonBuilder();
            js.writeFunction(functionName, "gav", "event", "data");
            js.openObject();

            if (!alert.getCondition().contains("return ")) {
                js.addReturnKeyword();
            }
            js.write(alert.getCondition());

            js.closeObject();
            result = js.toString();

            engine.addFunction(functionName, result);
        }

        return result;
    }

    // =========================================================================
    // POST PROCESS
    // =========================================================================
    private void postProcessAlerting(final SaveEntitiesResult<AlertEntity> alerts) {
        if (alerts != null) {
            for (final AlertEntity alert : alerts.getNewEntities()) {
                logAlert(alert);
                sendNewAlert(transformEntityToResult.mapping(alert));
            }

            for (final AlertEntity alert : alerts.getMergeEntities()) {
                logAlert(alert);
                sendAlert(transformEntityToResult.mapping(alert));
            }
        }
    }

    private void logAlert(final AlertEntity result) {

        switch (result.getLevelType()) {
            case FATAL:
                Loggers.ALERTS_EVENT.error(MSG, result.getAlerteName(), result.getLevel(), result.getLabel());
                break;
            case ERROR:
                Loggers.ALERTS_EVENT.error(MSG, result.getAlerteName(), result.getLevel(), result.getLabel());
                break;
            case WARN:
                Loggers.ALERTS_EVENT.warn(MSG, result.getAlerteName(), result.getLevel(), result.getLabel());
                break;
            case INFO:
                Loggers.ALERTS_EVENT.info(MSG, result.getAlerteName(), result.getLevel(), result.getLabel());
                break;
            case DEBUG:
                Loggers.ALERTS_EVENT.debug(MSG, result.getAlerteName(), result.getLevel(), result.getLabel());
                break;
            case TRACE:
                Loggers.ALERTS_EVENT.trace(MSG, result.getAlerteName(), result.getLevel(), result.getLabel());
                break;
            case UNDEFINE:
                Loggers.ALERTS_EVENT.warn(MSG, result.getAlerteName(), result.getLevel(), result.getLabel());
                break;
            default:
                break;
        }

    }

    // =========================================================================
    // SEND ALERTS
    // =========================================================================
    private void sendNewAlert(final AlertingResult alert) {
        if (alertSenderService != null) {
            Loggers.ALERTING.info("send alert : {}", alert.convertToJson());
            alertSenderService.sendNewAlert(alert, computeChannels(alert.getChannel()));
        } else {
            Loggers.ALERTING.warn(NO_ALERT_SENDER_SERVICE_DEFINE);
        }
    }

    private void sendAlert(final AlertingResult alert) {
        if (alertSenderService != null) {
            Loggers.ALERTING.info("send alert : {}", alert.convertToJson());
            alertSenderService.send(alert, computeChannels(alert.getChannel()));
        } else {
            Loggers.ALERTING.warn(NO_ALERT_SENDER_SERVICE_DEFINE);
        }
    }

    @Override
    public void processDisableAlerts(final List<String> alertsUids, final String channel) {
        if (alertSenderService != null) {
            Loggers.ALERTING.info("send disable alert : {}", alertsUids);
            alertSenderService.sendDisable(alertsUids, computeChannels(channel));
        } else {
            Loggers.ALERTING.warn(NO_ALERT_SENDER_SERVICE_DEFINE);
        }
    }

    private List<String> computeChannels(final String channels) {
        final List<String> result = new ArrayList<>();
        if ((channels == null) || "@all".equals(channels)) {
            //@formatter:off
           Context.getInstance().getPlugins().orElse(new ArrayList<>())
                                .stream()
                                .map(Plugin::getGav)
                                .map(Gav::getArtifactId)
                                .forEach(result::add);
           //@formatter:on
        } else {
            Arrays.asList(channels.split(" ")).forEach(result::add);
        }
        return result;
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private <T> void processIfNull(final T value, final T ref, final Consumer<T> consumer) {
        if (value == null) {
            consumer.accept(ref);
        }
    }

    // =========================================================================
    // OVERRIDE
    // =========================================================================
    @Override
    public String getName() {
        return name;
    }

}
