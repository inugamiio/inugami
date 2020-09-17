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
package io.inugami.commons.connectors.config;

import java.io.Serializable;
import java.util.List;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * HttpDefaultConfig
 * 
 * @author patrickguillerm
 * @since 16 déc. 2017
 */
@XStreamAlias("httpDefaultConfig")
public class HttpDefaultConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long     serialVersionUID = -2699549119145273117L;
    
    @XStreamAsAttribute
    private long                  timeout          = 65000;
    
    @XStreamAsAttribute
    private long                  socketTimeout    = 60000;
    
    @XStreamAsAttribute
    private long                  ttl              = 30000;
    
    @XStreamAsAttribute
    private int                   maxConnections   = 300;
    
    @XStreamAsAttribute
    private int                   maxPerRoute      = 50;
    
    private List<HttpHeaderField> headerFields;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("HttpDefaultConfig [timeout=");
        builder.append(timeout);
        builder.append(", socketTimeout=");
        builder.append(socketTimeout);
        builder.append(", ttl=");
        builder.append(ttl);
        builder.append(", maxConnections=");
        builder.append(maxConnections);
        builder.append(", maxPerRout=");
        builder.append(maxPerRoute);
        builder.append(", headerFields=");
        builder.append(headerFields);
        builder.append("]");
        return builder.toString();
    }
    
    @Override
    public void postProcessing(final ConfigHandler<String, String> ctx) throws TechnicalException {
        //@formatter:off
        timeout         = Long.parseLong(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_TIMEOUT.or(timeout)));
        socketTimeout   = Long.parseLong(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_SOCKET_TIMEOUT.or(socketTimeout)));
        ttl             = Long.parseLong(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_TTL.or(ttl)));
        maxConnections  = Integer.parseInt(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_MAX_CONNECTIONS.or(maxConnections)));
        maxPerRoute     = Integer.parseInt(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_MAX_PER_ROUTE.or(maxPerRoute)));
        //@formatter:on
        
        for (final HttpHeaderField field : headerFields) {
            field.postProcessing(ctx);
        }
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public long getTimeout() {
        return timeout;
    }
    
    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }
    
    public long getSocketTimeout() {
        return socketTimeout;
    }
    
    public void setSocketTimeout(final long socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
    
    public long getTtl() {
        return ttl;
    }
    
    public void setTtl(final long ttl) {
        this.ttl = ttl;
    }
    
    public int getMaxConnections() {
        return maxConnections;
    }
    
    public void setMaxConnections(final int maxConnections) {
        this.maxConnections = maxConnections;
    }
    
    public int getMaxPerRoute() {
        return maxPerRoute;
    }
    
    public void setMaxPerRoute(final int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }
    
    public List<HttpHeaderField> getHeaderFields() {
        return headerFields;
    }
    
    public void setHeaderFields(final List<HttpHeaderField> headerFields) {
        this.headerFields = headerFields;
    }
    
}
