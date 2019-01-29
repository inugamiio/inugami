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
package org.inugami.core.providers.jira;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.inugami.api.exceptions.services.ConnectorException;
import org.inugami.api.exceptions.services.ProviderException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.Gav;
import org.inugami.api.models.Tuple;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.api.providers.task.ProviderTask;
import org.inugami.commons.connectors.HttpConnectorResult;
import org.inugami.configuration.services.ConfigHandlerHashMap;
import org.inugami.core.providers.jira.models.JiraSearch;
import org.inugami.core.services.connectors.HttpConnector;

public class JiraProviderTask implements ProviderTask {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String                 CONFIG_KEY_JIRA_HOST     = "host.jira";
    
    private static final String                 CONFIG_KEY_REALM         = "realm";
    
    private static final String                 CONFIG_KEY_USER_ID       = "userId";
    
    private static final String                 CONFIG_KEY_PASSWORD      = "password";
    
    private static final String                 CONFIG_KEY_MAX_RESULTS   = "maxResults";
    
    private static final String                 CONFIG_KEY_CUSTOM_FIELDS = "customFields";
    
    private final HttpConnector                 httpConnector;
    
    private final SimpleEvent                   event;
    
    private final String                        url;
    
    private final String                        providerName;
    
    private final Gav                           pluginGav;
    
    private final ConfigHandler<String, String> config;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public <T extends SimpleEvent> JiraProviderTask(final T event, final String url, final String providerName,
                                                    final HttpConnector httpConnector,
                                                    final ConfigHandler<String, String> config, final Gav pluginGav) {
        this.event = event;
        this.url = url;
        this.providerName = providerName;
        this.httpConnector = httpConnector;
        this.pluginGav = pluginGav;
        this.config = config == null ? new ConfigHandlerHashMap(new HashMap<>()) : config;
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
        catch (final Exception e) {
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
            httpResult = callJira(request);
        }
        catch (final ConnectorException e) {
            throw new ProviderException(e.getMessage(), e);
        }
        
        if (httpResult.getStatusCode() != 200) {
            Loggers.IO.error("error-http-{} on request : {}", httpResult.getStatusCode(), request);
        }
        
        return buildResult(event, httpResult);
    }
    
    protected HttpConnectorResult callJira(final String request) throws ConnectorException {
        final CredentialsProvider credentials = new BasicCredentialsProvider();
        
        credentials.setCredentials(new AuthScope(config.grab(CONFIG_KEY_JIRA_HOST), AuthScope.ANY_PORT,
                                                 config.grab(CONFIG_KEY_REALM)),
                                   new UsernamePasswordCredentials(config.grab(CONFIG_KEY_USER_ID),
                                                                   config.grab(CONFIG_KEY_PASSWORD)));
        
        return httpConnector.get(request, credentials);
    }
    
    // =========================================================================
    // BUILDERS
    // =========================================================================
    protected <T extends SimpleEvent> String buildRequest(final T event) throws ProviderException {
        String result;
        final List<Tuple<String, String>> params = new ArrayList<>();
        
        params.add(new Tuple<>("jql", event.getQuery()));
        
        params.add(new Tuple<>("maxResults", config.grabOrDefault(CONFIG_KEY_MAX_RESULTS, "100")));
        
        try {
            result = HttpConnector.buildRequest(url, params);
        }
        catch (final ConnectorException e) {
            throw new JiraProviderTaskException(e.getMessage(), e);
        }
        
        return result;
    }
    
    protected ProviderFutureResult buildResult(final SimpleEvent currentEvent,
                                               final HttpConnectorResult httpResult) throws JiraProviderTaskException {
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
    
    private ProviderFutureResultBuilder buildSuccessResult(final SimpleEvent currentEvent, final byte[] data,
                                                           final Charset charset) throws JiraProviderTaskException {
        final ProviderFutureResultBuilder result = new ProviderFutureResultBuilder();
        
        final Class<?> customClass = buildClass(config.grab(CONFIG_KEY_CUSTOM_FIELDS));
        final JiraSearch eventData = new JiraSearch().convertToObject(data, charset, customClass);
        
        result.addData(eventData);
        result.addEvent(currentEvent);
        return result;
    }
    
    private Class<?> buildClass(final String customClassPath) throws JiraProviderTaskException {
        Class<?> result;
        try {
            result = Class.forName(customClassPath);
        }
        catch (final ClassNotFoundException e) {
            throw new JiraProviderTaskException(e.getMessage(), e);
        }
        
        return result;
    }
    
    public String getName() {
        return this.providerName;
    }
    
    // =========================================================================
    // Exception
    // =========================================================================
    public class JiraProviderTaskException extends ProviderException {
        
        private static final long serialVersionUID = 7799240586676311875L;
        
        public JiraProviderTaskException(final String message, final Object... values) {
            super(message, values);
        }
        
        public JiraProviderTaskException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
