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
package io.inugami.framework.interfaces.connectors;

import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConnectorConstants {

    public static final String HTTP_GET  = "GET";
    public static final String HTTP_POST = "POST";
    public static final String PUT       = "PUT";
    public static final String PATCH     = "PATCH";
    public static final String DELETE    = "DELETE";
    public static final String OPTION    = "OPTION";

    /**
     * <strong>-Dhttp.connector.timeout=20000</strong>
     */
    public static final String TIME_OUT_CONFIG = "http.connector.timeout";

    /**
     * <strong>-Dhttp.connector.timeToLive=30000</strong>
     */
    public static final String TIME_TO_LIVE_CONFIG = "http.connector.timeToLive";

    /**
     * <strong>-Dhttp.connector.maxConnections=300</strong>
     */
    public static final String MAX_CONNEXIONS = "http.connector.maxConnections";

    /**
     * <strong>-Dhttp.connector.maxPerRoute=50</strong>
     */
    public static final String MAX_PER_ROUTE = "http.connector.maxPerRoute";

    /**
     * <strong>-Dapplication.name=inugami</strong>
     */
    public static final String APPLICATION_NAME = "application.name";

    /**
     * <strong>-Dapplication.name.header=APPLICATION-NAME</strong>
     */
    public static final String APPLICATION_NAME_HEADER = "application.name.header";

    /**
     * <strong>-Dapplication.hostname=</strong>
     */
    public static final String APPLICATION_HOSTNAME = "application.hostname";

    public static final String APPLICATION_HOSTNAME_HEADER_KEY = "application.hostname.header";

    /**
     * <strong>-Dhttp.connector.socket.timeout=60000</strong>
     */
    public static final String SOCKET_TIMEOUT = "http.connector.socket.timeout";

    public static final String APPLICATION_JSON = "application/json";

    public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

    public static final String CONTENT_TYPE = "content-type";

    public static final String CURRENT_APPLICATION_NAME     = initValue(APPLICATION_NAME,
                                                                        JvmKeyValues.APPLICATION_NAME.or(
                                                                                JvmKeyValues.DEFAULT_APPLICATION_NAME));
    public static final String HEADER_APPLICATION_NAME      = initValue(APPLICATION_NAME_HEADER, "application-name");
    public static final String CURRENT_APPLICATION_HOSTNAME = initValue(APPLICATION_HOSTNAME, "");
    public static final String APPLICATION_HOSTNAME_HEADER  = initValue(APPLICATION_HOSTNAME_HEADER_KEY,
                                                                        "application-host");

    private static String initValue(final String key, final String defaultValue) {
        String       result = defaultValue;
        final String value  = System.getProperty(key);
        if (value != null) {
            result = value;
        }
        return result;
    }

}
