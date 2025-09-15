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
package io.inugami.framework.interfaces.connectors.config;


import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.functionnals.PostProcessing;
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
public final class HttpDefaultConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -2699549119145273117L;


    private long                  timeout        = 65000;
    private long                  socketTimeout  = 60000;
    private long                  ttl            = 30000;
    private int                   maxConnections = 300;
    private int                   maxPerRoute    = 50;
    @Singular("headerFields")
    private List<HttpHeaderField> headerFields;

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> ctx) throws TechnicalException {
        timeout = Long.parseLong(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_TIMEOUT.or(timeout)));
        socketTimeout = Long.parseLong(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_SOCKET_TIMEOUT.or(socketTimeout)));
        ttl = Long.parseLong(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_TTL.or(ttl)));
        maxConnections = Integer.parseInt(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_MAX_CONNECTIONS.or(maxConnections)));
        maxPerRoute = Integer.parseInt(ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_MAX_PER_ROUTE.or(maxPerRoute)));

        if (headerFields != null) {
            for (final HttpHeaderField field : headerFields) {
                field.postProcessing(ctx);
            }
        }
    }


}
