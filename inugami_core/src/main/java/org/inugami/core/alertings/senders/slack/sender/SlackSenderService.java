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
package org.inugami.core.alertings.senders.slack.sender;

import static org.inugami.api.tools.ConfigHandlerTools.ENABLE;
import static org.inugami.api.tools.ConfigHandlerTools.grabConfig;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

import org.inugami.api.exceptions.services.ConnectorException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.commons.connectors.HttpProxy;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.services.connectors.HttpConnector;
import org.inugami.core.services.senders.Sender;
import org.inugami.core.services.senders.SenderException;

import flexjson.JSONSerializer;

/**
 * SleckSender
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
@Default
@SlackSender
@Named
@ApplicationScoped
public class SlackSenderService implements Sender<AbstractSlackModel>, Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long            serialVersionUID = -702896884032189574L;
    
    @Inject
    private transient ApplicationContext context;
    
    private transient HttpConnector      httpConnector;
    
    private boolean                      enable;
    
    private String                       url;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SlackSenderService() {
    }
    
    public SlackSenderService(final ApplicationContext context) {
        this.context = context;
    }
    
    @PostConstruct
    public void init() {
        final ConfigHandler<String, String> config = context.getGlobalConfiguration().optionnal();
        enable = Boolean.parseBoolean(grabConfig(SlackSender.class, ENABLE, config));
        final String baseUrl = grabConfig(SlackSender.class, "url", config);
        final String token = grabConfig(SlackSender.class, "token", config);
        final String proxyConfig = grabConfig(SlackSender.class, "proxy.host", config);
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
            //@formatter:off
            proxy=new HttpProxy(proxyConfig,
                                buildProxyPort(config),
                                grabConfig(SlackSender.class, "proxy.user", config),
                                grabConfig(SlackSender.class, "proxy.password", config)
                                );
            //@formatter:off
        }
        if(baseUrl !=null) {
            final StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(baseUrl);
            if (!baseUrl.endsWith("/")) {
                urlBuilder.append('/');
            }
            urlBuilder.append(token);
            url = urlBuilder.toString();
            
            httpConnector = context.getHttpConnector(SlackSender.class.getSimpleName(), 20, 3000, 500, 50, 60000);
            if(proxy!=null) {
                httpConnector.setProxy(proxy);
            } 
        }
        
    }
    
    private int buildProxyPort(final ConfigHandler<String, String> config) {
        final String port = grabConfig(SlackSender.class, "proxy.port", config);
        return port==null?80 : Integer.parseInt(port);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void send(final AbstractSlackModel data) throws SenderException {
        if (!enable) {
            return;
        }
        final String json = new JSONSerializer().exclude("*.class").deepSerialize(data);
        try {
            httpConnector.post(url, json);
        }
        catch (final ConnectorException e) {
            Loggers.ALERTS_SENDER.error("can't send to slack :{} \n{}", url, json);
            throw new SenderException(e.getMessage(), e);
        }
    }
    
}
