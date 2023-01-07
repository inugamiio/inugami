package io.inugami.core.providers.jenkins;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Gav;
import io.inugami.api.models.Tuple;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.api.providers.task.ProviderTask;
import io.inugami.commons.connectors.HttpConnectorResult;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import io.inugami.core.providers.jenkins.transformers.JenkinsInformationTransformer;
import io.inugami.core.services.connectors.HttpConnector;

public class JenkinsProviderTask implements ProviderTask {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final HttpConnector httpConnector;

    private final SimpleEvent event;

    private final String url;

    private final Gav pluginGav;

    private final ConfigHandler<String, String> config;

    private final CredentialsProvider credentials;

    private static final JenkinsProviderTaskResolver jenkinsResolver = new JenkinsProviderTaskResolver();

    private static final JenkinsInformationTransformer jenkinsTransformer = new JenkinsInformationTransformer();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    //@formatter:off
    public <T extends SimpleEvent> JenkinsProviderTask(final T event,
                                                       final String url,
                                                       final HttpConnector httpConnector,
                                                       final ConfigHandler<String, String> config,
                                                       final Gav pluginGav,
                                                       final CredentialsProvider credentials) {
        
        //@formatter:on
        this.event         = event;
        this.url           = url;
        this.httpConnector = httpConnector;
        this.pluginGav     = pluginGav;
        this.config        = config == null ? new ConfigHandlerHashMap(new HashMap<>()) : config;
        this.credentials   = credentials;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public ProviderFutureResult callProvider() {
        ProviderFutureResult result;

        try {
            result = process();
        }
        catch (Exception e) {
            result = new ProviderFutureResultBuilder().addException(e).build();
        }
        return result;
    }

    @Override
    public GenericEvent getEvent() {
        return event;
    }

    @Override
    public Gav getPluginGav() {
        return pluginGav;
    }

    // =========================================================================
    // PROCESS
    // =========================================================================
    private ProviderFutureResult process() throws ProviderException {
        final String request = buildRequest(event);

        HttpConnectorResult httpResult;
        try {
            httpResult = callJenkins(request);
        }
        catch (ConnectorException e) {
            throw new ProviderException(e.getMessage(), e);
        }

        if (httpResult.getStatusCode() != 200) {
            Loggers.PARTNERLOG.error("error-http-{} on request : {}", httpResult.getStatusCode(), request);
        }

        return buildResult(event, httpResult);
    }

    /*package*/ HttpConnectorResult callJenkins(String request) throws ConnectorException {

        return httpConnector.get(request, credentials);
    }

    // =========================================================================
    // BUILDERS
    // =========================================================================
    protected <T extends SimpleEvent> String buildRequest(final T event) throws ProviderException {
        String                            result;
        final List<Tuple<String, String>> params = new ArrayList<>();

        params.add(new Tuple<>(config.grab("treeKey"), config.grab("treeValue")));

        try {
            result = httpConnector.buildRequest(url, params);
        }
        catch (ConnectorException e) {
            throw new JenkinsProviderTaskException(e.getMessage(), e);
        }

        return result;
    }

    protected ProviderFutureResult buildResult(final SimpleEvent currentEvent, HttpConnectorResult httpResult) {
        ProviderFutureResultBuilder result;

        if (httpResult.getStatusCode() == 200) {
            result = buildSuccessResult(currentEvent, httpResult.getData(), httpResult.getCharset());
        }
        else {
            result = new ProviderFutureResultBuilder();
            result.addException(new ProviderException("HTTP-{0} on calling {1}", httpResult.getStatusCode(),
                                                      httpResult.getHashHumanReadable()));
        }

        return result.build();
    }

    private ProviderFutureResultBuilder buildSuccessResult(final SimpleEvent currentEvent, byte[] data,
                                                           Charset charset) {
        final ProviderFutureResultBuilder result    = new ProviderFutureResultBuilder();
        JenkinsInformation                eventData = new JenkinsInformation().convertToObject(data, charset);

        result.addData(jenkinsResolver.build(currentEvent.getQuery(), jenkinsTransformer.process(eventData)));
        result.addEvent(currentEvent);
        return result;
    }

    // =========================================================================
    // Exception
    // =========================================================================
    public class JenkinsProviderTaskException extends ProviderException {

        private static final long serialVersionUID = -4201970876341805157L;

        public JenkinsProviderTaskException(String message, Object... values) {
            super(message, values);
        }

        public JenkinsProviderTaskException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
