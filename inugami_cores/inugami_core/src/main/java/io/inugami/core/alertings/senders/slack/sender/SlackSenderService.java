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
package io.inugami.core.alertings.senders.slack.sender;

import flexjson.JSONSerializer;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.commons.connectors.HttpProxy;
import io.inugami.core.context.ApplicationContext;
import io.inugami.core.services.connectors.HttpConnector;
import io.inugami.core.services.senders.Sender;
import io.inugami.core.services.senders.SenderException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.inugami.api.tools.ConfigHandlerTools.*;

/**
 * SleckSender
 *
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
@Default
@SlackSender
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Named
@ApplicationScoped
public class SlackSenderService implements Sender<ISlackModel<?>>, Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -702896884032189574L;

    @Inject
    private transient ApplicationContext context;

    private transient HttpConnector httpConnector;

    private boolean enable;

    private String url;

    private final Map<String, String> configurations = new LinkedHashMap<>();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    @PostConstruct
    public void init() {
        final ConfigHandler<String, String> config = context.getGlobalConfiguration().optionnal();
        enable = Boolean.parseBoolean(grabConfig(SlackSender.class, ENABLE, config));
        final String baseUrl     = grabConfig(SlackSender.class, "url", config);
        final String token       = grabConfig(SlackSender.class, "token", config);
        final String proxyConfig = grabConfig(SlackSender.class, "proxy.host", config);

        configurations.put("url_base", baseUrl);
        configurations.put("url_token", token);

        HttpProxy proxy = null;

        if (baseUrl == null) {
            Loggers.PROVIDER.error("SlackSender has no URL define!");
            enable = false;
        }
        if (token == null) {
            Loggers.PROVIDER.error("SlackSender has no Token define!");
            enable = false;
        }

        if (proxyConfig != null) {
            final String proxyUser     = grabConfig(SlackSender.class, "proxy.user", config);
            final String proxyPassword = grabConfig(SlackSender.class, "proxy.password", config);
            proxy = new HttpProxy(proxyConfig, buildProxyPort(config), proxyUser, proxyPassword);
            configurations.put("proxy_host", proxyConfig);
            configurations.put("proxy_user", proxyUser);
            configurations.put("proxy_password", proxyPassword);
        }
        if (baseUrl != null) {
            final StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(baseUrl);
            if (!baseUrl.endsWith("/")) {
                urlBuilder.append('/');
            }
            urlBuilder.append(token);
            url = urlBuilder.toString();

            final int maxConnection = grabConfigInt(SlackSender.class, "maxConnection", config, 20);
            final int timeout       = grabConfigInt(SlackSender.class, "timeout", config, 5000);
            final int ttl           = grabConfigInt(SlackSender.class, "ttl", config, 500);
            final int maxPerRoute   = grabConfigInt(SlackSender.class, "maxPerRoute", config, 50);
            final int socketTimeout = grabConfigInt(SlackSender.class, "socketTimeout", config, 5000);

            httpConnector = context.getHttpConnector(SlackSender.class.getSimpleName(), maxConnection, timeout, ttl,
                                                     maxPerRoute, socketTimeout);
            configurations.put("max_connection", String.valueOf(maxConnection));
            configurations.put("timeout", String.valueOf(timeout));
            configurations.put("ttl", String.valueOf(ttl));
            configurations.put("max_per_route", String.valueOf(maxPerRoute));
            configurations.put("socket_timeout", String.valueOf(socketTimeout));

            if (proxy != null) {
                httpConnector.setProxy(proxy);
            }
        }

    }

    private int buildProxyPort(final ConfigHandler<String, String> config) {
        final String port   = grabConfig(SlackSender.class, "proxy.port", config);
        final int    result = port == null ? 80 : Integer.parseInt(port);
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
    public void send(final ISlackModel data) throws SenderException {
        if (!enable) {
            return;
        }
        final String json = new JSONSerializer().exclude("*.class").deepSerialize(data);
        try {
            httpConnector.post(url, json);
        } catch (final ConnectorException e) {
            Loggers.ALERTS_SENDER.error("can't send to slack :{} \n{}", url, json);
            throw new SenderException(e.getMessage(), e);
        }
    }

}
