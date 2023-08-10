package io.inugami.core.providers.jenkins;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.AbstractProvider;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.ProviderRunner;
import io.inugami.api.providers.ProviderWithHttpConnector;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.concurrent.FutureDataBuilder;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.api.providers.task.ProviderTask;
import io.inugami.api.spi.SpiLoader;
import io.inugami.core.context.ContextSPI;
import io.inugami.core.providers.jenkins.models.JenkinsInformation;
import io.inugami.core.providers.jenkins.models.JenkinsJob;
import io.inugami.core.services.connectors.HttpConnector;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"java:S3655"})
public class JenkinsProvider extends AbstractProvider implements Provider, ProviderWithHttpConnector {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String TYPE = "JENKINS";

    public static final String CONFIG_JENKINS_PROTOCOL = "jenkins.protocol";

    public static final String CONFIG_JENKINS_REALM = "jenkins.realm";

    public static final String CONFIG_JENKINS_HOST = "jenkins.host";

    public static final String CONFIG_JENKINS_CONTEXT_PATH = "jenkins.context.path";

    public static final String CONFIG_JENKINS_USER_ID = "jenkins.userId";

    public static final String CONFIG_JENKINS_TOKEN = "jenkins.apiToken";

    private final FutureData<ProviderFutureResult> futureDataRef;

    private final String url;

    private int timeout;

    private final ConfigHandler<String, String> config;

    private final HttpConnector httpConnector;

    private final CredentialsProvider credentials;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public JenkinsProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                           final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        this.config = config;
        final ContextSPI ctx  = SpiLoader.getInstance().loadSpiSingleService(ContextSPI.class);
        final String     host = config.grab(CONFIG_JENKINS_HOST);

        final StringBuilder builder = new StringBuilder();
        builder.append(config.grabOrDefault(CONFIG_JENKINS_PROTOCOL, "https"));
        builder.append("://");
        builder.append(host);
        builder.append(config.grabOrDefault(CONFIG_JENKINS_CONTEXT_PATH, "/jenkins/api/json"));
        url = builder.toString();

        this.credentials = buildCredentials(config, host);
        //@formatter:off
        httpConnector = ctx.getHttpConnector(classBehavior.getName(),
                                             getMaxConnections(config, 5),
                                             getTimeout(config, 5000),
                                             getTTL(config, 500),
                                             getMaxPerRoute(config, 50),
                                             getSocketTimeout(config, 60000));
        //@formatter:on

        futureDataRef = FutureDataBuilder.buildDefaultFuture(this.timeout);
    }

    public static CredentialsProvider buildCredentials(final ConfigHandler<String, String> config, final String host) {
        final AuthScope scope = new AuthScope(host, AuthScope.ANY_PORT,
                                              config.grabOrDefault(CONFIG_JENKINS_REALM, "http"));

        final CredentialsProvider result = new BasicCredentialsProvider();
        result.setCredentials(scope, new UsernamePasswordCredentials(config.grab(CONFIG_JENKINS_USER_ID),
                                                                     config.grab(CONFIG_JENKINS_TOKEN)));

        return result;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    @Override
    public ConfigHandler<String, String> getConfig() {
        return config;
    }

    // =========================================================================
    // CALL EVENT
    // =========================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        final ProviderTask providerTask = new JenkinsProviderTask(event, url, httpConnector, config, pluginGav,
                                                                  credentials);
        return runTask(providerTask, event, futureDataRef);
    }

    // =========================================================================
    // AGGREGATE
    // =========================================================================
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> datas) throws ProviderException {
        assertDataType(datas);
        final List<JenkinsJob> resultData = new ArrayList<>();

        //@formatter:off
        datas.stream()
             .filter(this::validResultWithData)
             .map(this::toJenkinsInformation)
             .map(JenkinsInformation::getJobs)
             .forEach(resultData::addAll);
        //@formatter:on

        final JenkinsInformation jenkinsData = new JenkinsInformation(resultData);
        return new ProviderFutureResultBuilder().addData(jenkinsData).build();
    }

    private JenkinsInformation toJenkinsInformation(final ProviderFutureResult item) {
        return (JenkinsInformation) item.getData().get();
    }

    private void assertDataType(final List<ProviderFutureResult> data) throws ProviderException {
        //@formatter:off
        final Optional<ProviderFutureResult> anInvalidResult = data.stream()
                                                                   .filter(this::validResultWithData)
                                                                   .filter(this::notJenkinsInformation)
                                                                   .findAny();
        //@formatter:on

        if (anInvalidResult.isPresent()) {
            final String events = extractEventsNames(data);
            final String types  = extractTypes(data);
            throw new ProviderException("{0} : can't aggregate unknow data type : {1}", events, types);
        }
    }

    private String extractEventsNames(final List<ProviderFutureResult> data) {
        final List<String> events = new ArrayList<>();

        //@formatter:off
        data.stream()
            .map(ProviderFutureResult::getEvent)
            .map(GenericEvent::getName)
            .forEach(events::add);
        //@formatter:on

        return String.join(" : ", events);
    }

    private String extractTypes(final List<ProviderFutureResult> data) {
        final List<String> events = new ArrayList<>();

        for (final ProviderFutureResult futurResult : data) {
            if (futurResult.getData().isPresent()) {
                events.add(futurResult.getData().getClass().getName());
            } else {
                events.add("null");
            }
        }

        return String.join(";", events);
    }

    private boolean validResultWithData(final ProviderFutureResult item) {
        return !item.getException().isPresent() && item.getData().isPresent();
    }

    private boolean notJenkinsInformation(final ProviderFutureResult item) {
        Asserts.assertTrue(item.getData().isPresent());
        Asserts.assertNotNull(item.getData().get());
        return !(item.getData().get() instanceof JenkinsInformation);
    }
}
