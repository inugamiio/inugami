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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * HttpDefaultConfig
 *
 * @author patrickguillerm
 * @since 16 déc. 2017
 */

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("httpDefaultConfig")
public final class HttpDefaultConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -2699549119145273117L;

    @XStreamAsAttribute
    private long timeout = 65000;

    @XStreamAsAttribute
    private long socketTimeout = 60000;

    @XStreamAsAttribute
    private long ttl = 30000;

    @XStreamAsAttribute
    private int maxConnections = 300;

    @XStreamAsAttribute
    private int maxPerRoute = 50;

    private List<HttpHeaderField> headerFields;

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> ctx) throws TechnicalException {
        //@formatter:off
        timeout = Long.parseLong(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_TIMEOUT.or(timeout)));
        socketTimeout = Long.parseLong(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_SOCKET_TIMEOUT.or(socketTimeout)));
        ttl = Long.parseLong(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_TTL.or(ttl)));
        maxConnections = Integer.parseInt(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_MAX_CONNECTIONS.or(maxConnections)));
        maxPerRoute = Integer.parseInt(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_MAX_PER_ROUTE.or(maxPerRoute)));
        //@formatter:on

        for (final HttpHeaderField field : headerFields) {
            field.postProcessing(ctx);
        }
    }


}
