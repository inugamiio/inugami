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
package org.inugami.core.providers.graphite;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.inugami.api.exceptions.services.ConnectorException;
import org.inugami.api.exceptions.services.ProviderException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.Gav;
import org.inugami.api.models.Tuple;
import org.inugami.api.models.data.graphite.GraphiteTargets;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.api.providers.task.ProviderTask;
import org.inugami.commons.connectors.HttpConnectorResult;
import org.inugami.configuration.services.ConfigHandlerHashMap;
import org.inugami.configuration.services.functions.ProviderAttributFunction;
import org.inugami.core.services.connectors.HttpConnector;

/**
 * GraphiteProviderTask
 * 
 * @author patrick_guillerm
 * @since 10 janv. 2017
 */
public class GraphiteProviderTask implements ProviderTask {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final HttpConnector                 httpConnector;
    
    private final SimpleEvent                   event;
    
    private final String                        url;
    
    private final String                        providerName;
    
    private final boolean                       enableStartTime;
    
    private final Gav                           pluginGav;
    
    private final ConfigHandler<String, String> config;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public <T extends SimpleEvent> GraphiteProviderTask(final T event, final String url, final String providerName,
                                                        final HttpConnector httpConnector,
                                                        final List<ProviderAttributFunction> attributsFunctions,
                                                        final ConfigHandler<String, String> config,
                                                        final Gav pluginGav) {
        this(event, url, providerName, true, httpConnector, attributsFunctions, config, pluginGav);
    }
    
    protected <T extends SimpleEvent> GraphiteProviderTask(final T event, final String url, final String providerName,
                                                           final boolean enableStartTime,
                                                           final HttpConnector httpConnector,
                                                           final List<ProviderAttributFunction> attributsFunctions,
                                                           final ConfigHandler<String, String> config,
                                                           final Gav pluginGav) {
        this.event = event;
        this.url = url;
        this.providerName = providerName;
        this.enableStartTime = enableStartTime;
        this.httpConnector = httpConnector;
        this.pluginGav = pluginGav;
        this.config = config == null ? new ConfigHandlerHashMap(new HashMap<String, String>()) : config;
        
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public ProviderFutureResult callProvider() {
        ProviderFutureResult result = null;
        
        try {
            result = process();
        }
        catch (final Exception e) {
            result = new ProviderFutureResultBuilder().addException(e).build();
        }
        return result;
    }
    
    // =========================================================================
    // PROCESS
    // =========================================================================
    private ProviderFutureResult process() throws ProviderException {
        final String request = buildRequest(event);
        
        HttpConnectorResult httpResult;
        try {
            httpResult = callGraphite(request);
        }
        catch (final ConnectorException e) {
            throw new ProviderException(e.getMessage(), e);
        }
        
        if (httpResult.getStatusCode() != 200) {
            Loggers.IO.error("error-http-{} on request : {}", httpResult.getStatusCode(), request);
        }
        
        final ProviderFutureResult result = buildResult(event, httpResult);
        return result;
    }
    
    protected HttpConnectorResult callGraphite(final String request) throws ConnectorException {
        return httpConnector.get(request);
    }
    
    // =========================================================================
    // BUILDERS
    // =========================================================================
    protected <T extends SimpleEvent> String buildRequest(final T event) throws ProviderException {
        String result;
        final List<Tuple<String, String>> params = new ArrayList<>();
        
        params.add(new Tuple<>("format", "json"));
        
        final List<String> targets = buildTargets(event);
        for (final String target : targets) {
            params.add(new Tuple<>("target", target));
        }
        
        final Optional<String> from = buildFrom(event.getFrom());
        if (from.isPresent()) {
            params.add(new Tuple<>("from", config.applyProperties(from.get())));
        }
        else if (event.getUntil().isPresent()) {
            params.add(new Tuple<>("from", "-0min"));
        }
        
        if (event.getUntil().isPresent()) {
            params.add(new Tuple<>("until", config.applyProperties(event.getUntil().get())));
        }
        
        try {
            result = httpConnector.buildRequest(url, params);
        }
        catch (final ConnectorException e) {
            throw new GraphiteProviderTaskException(e.getMessage(), e);
        }
        return result;
    }
    
    private <T extends SimpleEvent> List<String> buildTargets(final T event) {
        final List<String> result = new ArrayList<>();
        final String[] targets = event.getQuery().split("@target=");
        
        for (final String target : targets) {
            result.add(buildTarget(target, event));
        }
        return result;
    }
    
    private <T extends SimpleEvent> String buildTarget(final String target, final SimpleEvent event) {
        final StringBuilder result = new StringBuilder();
        
        if (target.contains("@no_alias=")) {
            result.append(target.substring(10).trim());
        }
        if (target.contains("alias")) {
            result.append(target.trim());
        }
        else {
            result.append("alias(");
            result.append(target.trim());
            result.append(',');
            result.append(buildEventFullName(event));
            result.append(')');
        }
        return result.toString();
    }
    
    private <T extends SimpleEvent> String buildEventFullName(final T event) {
        final StringBuilder result = new StringBuilder();
        result.append('"');
        if ((event.getParent() != null) && event.getParent().isPresent()) {
            result.append(event.getParent().get());
            result.append('_');
        }
        result.append(event.getName());
        result.append('"');
        return result.toString();
    }
    
    private Optional<String> buildFrom(final Optional<String> from) {
        String result = null;
        if (from.isPresent()) {
            result = from.get();
        }
        return result == null ? Optional.empty() : Optional.of(result);
    }
    
    protected ProviderFutureResult buildResult(final SimpleEvent currentEvent, final HttpConnectorResult httpResult) {
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
    
    protected ProviderFutureResultBuilder buildSuccessResult(final SimpleEvent currentEvent, final byte[] data,
                                                             final Charset charset) {
        final ProviderFutureResultBuilder result = new ProviderFutureResultBuilder();
        result.addData(new GraphiteTargets().convertToObject(data, charset));
        result.addEvent(currentEvent);
        return result;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private String clean(final String query) {
        final StringBuilder result = new StringBuilder();
        if (query != null) {
            final String[] parts = query.split("\n");
            for (final String item : parts) {
                result.append(item.trim());
            }
        }
        return result.toString().replaceAll("\\n", "");
    }
    
    // =========================================================================
    // Exception
    // =========================================================================
    public class GraphiteProviderTaskException extends ProviderException {
        
        private static final long serialVersionUID = 4462935115104129195L;
        
        public GraphiteProviderTaskException(final String message, final Object... values) {
            super(message, values);
        }
        
        public GraphiteProviderTaskException(final String message, final Throwable cause) {
            super(message, cause);
        }
        
    }
    
    @Override
    public GenericEvent getEvent() {
        return event;
    }
    
    @Override
    public Gav getPluginGav() {
        return pluginGav;
    }
}
