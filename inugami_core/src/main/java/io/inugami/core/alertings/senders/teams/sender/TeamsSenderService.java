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
package io.inugami.core.alertings.senders.teams.sender;

import static io.inugami.api.tools.ConfigHandlerTools.ENABLE;
import static io.inugami.api.tools.ConfigHandlerTools.grabConfig;
import static io.inugami.api.tools.ConfigHandlerTools.grabConfigBoolean;
import static io.inugami.api.tools.ConfigHandlerTools.grabConfigInt;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.commons.connectors.HttpProxy;
import io.inugami.core.alertings.senders.teams.sender.models.TeamsModel;
import io.inugami.core.context.ApplicationContext;
import io.inugami.core.services.connectors.HttpConnector;
import io.inugami.core.services.senders.Sender;
import io.inugami.core.services.senders.SenderException;

@Default
@TeamsSender
@Named
@ApplicationScoped
public class TeamsSenderService implements Sender<TeamsModel>, Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long            serialVersionUID = -663827289408776185L;
    
    @Inject
    private transient ApplicationContext context;
    
    private transient HttpConnector      httpConnector;
    
    private boolean                      enable;
    
    private String                       url;
    
    private final Map<String, String>    configurations   = new LinkedHashMap<>();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public TeamsSenderService() {
    }
    
    public TeamsSenderService(final ApplicationContext context) {
        this.context = context;
    }
    
    @PostConstruct
    public void init() {
        
        final ConfigHandler<String, String> config = context.getGlobalConfiguration();
        enable = grabConfigBoolean(TeamsSender.class, ENABLE, config, false);
        
        //@formatter:off
        final String urlBase          = grabConfig(TeamsSender.class, "url.base", config);
        final String urlContext       = grabConfig(TeamsSender.class, "url.context", config);
        final String urlToken         = grabConfig(TeamsSender.class, "url.token", config);
        final String urlIncomingToken = grabConfig(TeamsSender.class, "url.incomingToken", config);
        //@formatter:on
        configurations.put("url_base", urlBase);
        configurations.put("url_context", urlContext);
        configurations.put("url_token", urlToken);
        configurations.put("url_incomingToken", urlIncomingToken);
        
        if (enable) {
            Asserts.notNull("base url is mandatory!", urlBase);
            Asserts.notNull("context url is mandatory!", urlContext);
            Asserts.notNull("token is mandatory!", urlToken);
            Asserts.notNull("incoming token is mandatory!", urlIncomingToken);
        }
        if ((urlBase != null) && (urlContext != null) && (urlToken != null) && (urlIncomingToken != null)) {
            final StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(urlBase);
            urlBuilder.append(urlContext);
            urlBuilder.append("/");
            urlBuilder.append(urlToken);
            urlBuilder.append("/IncomingWebhook/");
            urlBuilder.append(urlIncomingToken);
            
            url = urlBuilder.toString();
        }
        
        configurations.put("url", url);
        
        final String proxyConfig = grabConfig(TeamsSender.class, "proxy.host", config);
        HttpProxy proxy = null;
        
        if (proxyConfig != null) {
            final String proxyUser = grabConfig(TeamsSender.class, "proxy.user", config);
            final String proxyPassword = grabConfig(TeamsSender.class, "proxy.password", config);
            proxy = new HttpProxy(proxyConfig, buildProxyPort(config), proxyUser, proxyPassword);
            configurations.put("proxy_host", proxyConfig);
            configurations.put("proxy_user", proxyUser);
            configurations.put("proxy_password", proxyPassword);
        }
        
       //@formatter:off
       final int maxConnection= grabConfigInt(TeamsSender.class, "maxConnection", config, 20);
       final int timeout = grabConfigInt(TeamsSender.class, "timeout", config, 5000);
       final int ttl =grabConfigInt(TeamsSender.class, "ttl", config, 500);
       final int maxPerRoute =  grabConfigInt(TeamsSender.class, "maxPerRoute", config, 50);
       final int socketTimeout =grabConfigInt(TeamsSender.class, "socketTimeout", config, 5000);
        
       httpConnector = context.getHttpConnector(TeamsSender.class.getSimpleName(),
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
    
    @Override
    public Map<String, String> getConfiguration() {
        return configurations;
    }
    
    private int buildProxyPort(final ConfigHandler<String, String> config) {
        final String port = grabConfig(TeamsSender.class, "proxy.port", config);
        final int result = port == null ? 80 : Integer.parseInt(port);
        configurations.put("proxy_port", String.valueOf(result));
        return result;
    }
    
    @Override
    public void send(final TeamsModel data) throws SenderException {
        final Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", MediaType.APPLICATION_JSON);
        
        if (!enable) {
            return;
        }
        final String json = data.convertToJson();
        
        try {
            httpConnector.post(url, json, null, header);
            
        }
        catch (final ConnectorException e) {
            Loggers.ALERTS_SENDER.error("can't send to teams :{} \n{}", url, json);
            throw new SenderException(e.getMessage(), e);
        }
        
    }
    
}
