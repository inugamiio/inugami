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


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings({"java:S1948"})
@JsonIgnoreProperties(value = {"listener", "marshaller"})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
public final class HttpRequest implements Serializable {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long serialVersionUID = -7915238183192597988L;

    @ToString.Include
    @EqualsAndHashCode.Include
    private           String                  verb;
    @ToString.Include
    @EqualsAndHashCode.Include
    private           String                  url;
    private           Map<String, String>     headers;
    private           Map<String, String>     options;
    private           String                  token;
    private           Object                  body;
    @Builder.Default
    private           boolean                 throwable = true;
    private           String                  partner;
    private           String                  partnerService;
    @Singular("listener")
    private transient List<ConnectorListener> listener;
    private           boolean                 disableListener;
    private transient HttpPayloadMarshaller   marshaller;

    // =================================================================================================================
    // BUILDER
    // =================================================================================================================
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

        public HttpRequestBuilder addOption(final String key, final Serializable value) {
            if (options == null) {
                options = new LinkedHashMap<>();
            }
            if (key != null && value != null) {
                options.put(key, String.valueOf(value));
            }
            return this;
        }
    }
}
