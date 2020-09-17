package io.inugami.core.alertings.senders.opsgenie.sender;

import static io.inugami.api.tools.ConfigHandlerTools.ENABLE;
import static io.inugami.api.tools.ConfigHandlerTools.grabConfig;
import static io.inugami.api.tools.ConfigHandlerTools.grabConfigBoolean;
import static io.inugami.api.tools.ConfigHandlerTools.grabConfigInt;


import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.commons.connectors.HttpProxy;
import io.inugami.core.alertings.senders.opsgenie.sender.model.OpsgenieModel;
import io.inugami.core.context.ApplicationContext;
import io.inugami.core.services.connectors.HttpConnector;
import io.inugami.core.services.senders.Sender;
import io.inugami.core.services.senders.SenderException;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.inugami.api.tools.ConfigHandlerTools.*;
import static io.inugami.api.tools.ConfigHandlerTools.grabConfigInt;

@Default
@OpsgenieSender
@Named
@ApplicationScoped
public class OpsgenieSenderService implements Sender<OpsgenieModel>, Serializable {
    private static final long serialVersionUID = 6343857347952651197L;

    @Inject
    private transient ApplicationContext context;

    private transient HttpConnector httpConnector;

    private boolean                      enable;

    private String                       url;

    private String                       token;

    private final Map<String, String> configurations   = new LinkedHashMap<>();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================


    public OpsgenieSenderService() {
    }


    public OpsgenieSenderService(final ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {

        final ConfigHandler<String, String> config = context.getGlobalConfiguration();
        enable = grabConfigBoolean(OpsgenieSender.class, ENABLE, config, false);

        //@formatter:off
        final String urlBase          = grabConfig(OpsgenieSender.class, "url", config);
        final String genieToken          = grabConfig(OpsgenieSender.class, "token", config);

        //@formatter:on
        configurations.put("url", urlBase);
        configurations.put("token", genieToken);


        if (enable) {
            Asserts.notNull("url is mandatory!", urlBase);
            Asserts.notNull("token is mandatory!", genieToken);
        }

        if(genieToken != null){
            token = genieToken;
        }


        final String proxyConfig = grabConfig(OpsgenieSender.class, "proxy.host", config);
        HttpProxy proxy = null;

        if (proxyConfig != null) {
            final String proxyUser = grabConfig(OpsgenieSender.class, "proxy.user", config);
            final String proxyPassword = grabConfig(OpsgenieSender.class, "proxy.password", config);
            proxy = new HttpProxy(proxyConfig, buildProxyPort(config), proxyUser, proxyPassword);
            configurations.put("proxy_host", proxyConfig);
            configurations.put("proxy_user", proxyUser);
            configurations.put("proxy_password", proxyPassword);
        }

        //@formatter:off
        final int maxConnection= grabConfigInt(OpsgenieSender.class, "maxConnection", config, 20);
        final int timeout = grabConfigInt(OpsgenieSender.class, "timeout", config, 5000);
        final int ttl =grabConfigInt(OpsgenieSender.class, "ttl", config, 500);
        final int maxPerRoute =  grabConfigInt(OpsgenieSender.class, "maxPerRoute", config, 50);
        final int socketTimeout =grabConfigInt(OpsgenieSender.class, "socketTimeout", config, 5000);

        httpConnector = context.getHttpConnector(OpsgenieSender.class.getSimpleName(),
                maxConnection,
                timeout,
                ttl,
                maxPerRoute,
                socketTimeout);
        //@formatter:on
        configurations.put("max_connection", String.valueOf(maxConnection));
        configurations.put("timeout", String.valueOf(timeout));
        configurations.put("ttl", String.valueOf(ttl));
        configurations.put("max_per_route", String.valueOf(maxPerRoute));
        configurations.put("socket_timeout", String.valueOf(socketTimeout));

        if (proxy != null) {
            httpConnector.setProxy(proxy);
        }
    }

    private int buildProxyPort(final ConfigHandler<String, String> config) {
        final String port = grabConfig(OpsgenieSender.class, "proxy.port", config);
        final int result = port == null ? 80 : Integer.parseInt(port);
        configurations.put("proxy_port", String.valueOf(result));
        return result;
    }

    @Override
    public Map<String, String> getConfiguration() {
        return configurations;
    }

    // =========================================================================
    // METHODS
    // =========================================================================

    @Override
    public void send(final OpsgenieModel data) throws SenderException {
        final Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", MediaType.APPLICATION_JSON);
        header.put("Authorization",token);

        if (!enable) {
            return;
        }
        final String json = data.convertToJson();

        try {
            httpConnector.post(url, json, null, header);

        }
        catch (final ConnectorException e) {
            Loggers.ALERTS_SENDER.error("can't send to opsgenie :{} \n{}", url, json);
            throw new SenderException(e.getMessage(), e);
        }

    }

}
