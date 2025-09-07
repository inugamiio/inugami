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
package io.inugami.monitoring.core.spi;

import io.inugami.framework.interfaces.monitoring.IoLogContentDisplayResolverSPI;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.spi.SpiPriority;

import java.util.List;

import static io.inugami.framework.interfaces.spi.SpiPriority.LOWER_PRIORITY;

@SpiPriority(LOWER_PRIORITY)
public class DefaultIoLogContentDisplayResolverSPI implements IoLogContentDisplayResolverSPI {

    public static final String       TEXT              = "TEXT";
    public static final List<String> ACCEPTED_MIMETYPE = List.of("APPLICATION/JSON",
                                                                 "APPLICATION/XML");

    @Override
    public boolean display(final RequestData request) {
        final String contentType = request.getContentType() == null ? "" : request.getContentType().toUpperCase();
        return contentType.startsWith(TEXT) || ACCEPTED_MIMETYPE.contains(contentType);
    }
}
