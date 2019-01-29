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
package org.inugami.core.alertings.senders.teams.sender;

import static org.inugami.api.tools.ConfigHandlerTools.ENABLE;
import static org.inugami.api.tools.ConfigHandlerTools.grabConfig;
import static org.inugami.api.tools.ConfigHandlerTools.grabConfigBoolean;
import static org.inugami.api.tools.ConfigHandlerTools.grabConfigInt;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.services.ConnectorException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.commons.connectors.HttpProxy;
import org.inugami.core.alertings.senders.slack.sender.SlackSender;
import org.inugami.core.alertings.senders.teams.sender.models.TeamsModel;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.services.connectors.HttpConnector;
import org.inugami.core.services.senders.Sender;
import org.inugami.core.services.senders.SenderException;

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
        
        Asserts.notNull("base url is mandatory!", urlBase);
        Asserts.notNull("context url is mandatory!", urlContext);
        Asserts.notNull("token is mandatory!", urlToken);
        Asserts.notNull("incoming token is mandatory!", urlIncomingToken);
        
        final StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(urlBase);
        urlBuilder.append(urlContext);
        urlBuilder.append("/");
        urlBuilder.append(urlToken);
        urlBuilder.append("/IncomingWebhook/");
        urlBuilder.append(urlIncomingToken);
        
        url = urlBuilder.toString();
        
        final String proxyConfig = grabConfig(SlackSender.class, "proxy.host", config);
        HttpProxy proxy = null;
        
        if (proxyConfig != null) {
           //@formatter:off
           proxy=new HttpProxy(proxyConfig,
                               buildProxyPort(config),
                               grabConfig(TeamsSender.class, "proxy.user", config),
                               grabConfig(TeamsSender.class, "proxy.password", config)
                               );
           //@formatter:off
       }
       
       
       //@formatter:off
       httpConnector = context.getHttpConnector(TeamsSender.class.getSimpleName(),
                                                grabConfigInt(TeamsSender.class, "maxConnection", config, 20),
                                                grabConfigInt(TeamsSender.class, "timeout", config, 5000),
                                                grabConfigInt(TeamsSender.class, "ttl", config, 500),
                                                grabConfigInt(TeamsSender.class, "maxPerRoute", config, 50),
                                                grabConfigInt(TeamsSender.class, "socketTimeout", config, 5000));
       //@formatter:on
        
        if (proxy != null) {
            httpConnector.setProxy(proxy);
        }
    }
    
    private int buildProxyPort(final ConfigHandler<String, String> config) {
        final String port = grabConfig(TeamsSender.class, "proxy.port", config);
        return port == null ? 80 : Integer.parseInt(port);
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
