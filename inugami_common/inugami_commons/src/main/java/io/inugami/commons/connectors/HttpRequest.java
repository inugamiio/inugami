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
package io.inugami.commons.connectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.http.HttpEntity;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class HttpRequest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String              verb;
    private String              url;
    private Map<String, String> headers;
    private Map<String, String> options;
    private int                 nbRetry;
    private CredentialsProvider credentialsProvider;
    private String              token;
    private Object              body;
    private boolean             throwable = true;
    private String              partner;
    private String              partnerService;
    private HttpEntity          httpBody;
    private CloseableHttpClient httpclient;
    private RequestConfig       requestConfig;
    private ConnectorListener   listener;
    private boolean             disableListener;

    // =========================================================================
    // BUILDER
    // =========================================================================
    public static class HttpRequestBuilder {

        public HttpRequestBuilder addHeader(final String key, final String value) {

            if (headers == null) {
                headers = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                headers.put(key, value);
            }
            return this;
        }

        public HttpRequestBuilder addOption(final String key, final String value) {
            if (options == null) {
                options = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                options.put(key, value);
            }
            return this;
        }
    }
}
