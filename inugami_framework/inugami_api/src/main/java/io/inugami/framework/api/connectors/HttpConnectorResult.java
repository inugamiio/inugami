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
package io.inugami.framework.api.connectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.connectors.IHttpConnectorResult;
import lombok.*;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * HttpConnectorResult
 *
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
@Builder(toBuilder = true)
public final class HttpConnectorResult implements Serializable, IHttpConnectorResult {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long   serialVersionUID = -576629010706862497L;
    public static final  int    HTTP_ERROR       = 400;
    public static final  String PARAMETERS       = "parameters";
    public static final  String ERROR_CODE       = "errorCode";

    private final           String    verb;
    private final           String    url;
    @EqualsAndHashCode.Include
    private final           String    hashHumanReadable;
    private final           String    jsonInputData;
    private final           int       statusCode;
    private final           String    message;
    private final           byte[]    data;
    private final           byte[]    bodyData;
    private final           int       length;
    private final           String    contentType;
    private final           long      responseAt;
    private final           long      delay;
    private final           String    encoding;
    private final           Exception error;
    private final transient Charset   charset;

    private Map<String, String> requestHeaders;
    private Map<String, String> responseHeaders;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public static class HttpConnectorResultBuilder {
        private Map<String, String> requestHeaders;
        private Map<String, String> responseHeaders;

        public HttpConnectorResult build() {
            final StringBuilder hashBuilder = new StringBuilder().append('[')
                                                                 .append(this.verb)
                                                                 .append(']')
                                                                 .append(this.url);

            this.encoding = encoding == null ? "UTF-8" : encoding;
            this.length = data == null ? 0 : data.length;
            if (jsonInputData != null) {
                hashBuilder.append("?data=").append(jsonInputData.replaceAll("\\n", ""));
            }

            this.hashHumanReadable = hashBuilder.toString();

            return new HttpConnectorResult(verb, url, hashHumanReadable, jsonInputData, statusCode, message, data, bodyData, length, contentType, responseAt, delay, encoding, error,
                                           charset ==
                                           null ? StandardCharsets.UTF_8 : charset, requestHeaders, responseHeaders);
        }

        public HttpConnectorResultBuilder addRequestHeader(final String key, final String value) {
            if (requestHeaders == null) {
                requestHeaders = new LinkedHashMap<>();
            }
            if (key != null) {
                requestHeaders.put(key, value);
            }

            return this;
        }

        public HttpConnectorResultBuilder addResponseHeader(final String key, final String value) {
            if (responseHeaders == null) {
                responseHeaders = new LinkedHashMap<>();
            }
            if (key != null) {
                responseHeaders.put(key, value);
            }

            return this;
        }
    }

    public <T> T getBodyFromJson(final Class<? extends T> objectClass) throws JsonProcessingException {
        if (bodyData == null) {
            return null;
        }
        final String       currentData = new String(bodyData, charset == null ? StandardCharsets.UTF_8 : charset);
        final ObjectMapper marshaller  = JsonMarshaller.getInstance().getDefaultObjectMapper();

        return marshaller.readValue(currentData, objectClass);
    }

    public <T> T getResponseBody(final Class<? extends T> objectClass) throws JsonProcessingException {
        if (data == null) {
            return null;
        }
        final String       currentData = new String(data, charset == null ? StandardCharsets.UTF_8 : charset);
        final ObjectMapper marshaller  = JsonMarshaller.getInstance().getDefaultObjectMapper();

        return marshaller.readValue(currentData, objectClass);
    }

    public <T> T getRequestDataFromJson(final TypeReference<T> refObjectType) throws JsonProcessingException {
        if (bodyData == null) {
            return null;
        }
        final String       currentData = new String(bodyData, charset == null ? StandardCharsets.UTF_8 : charset);
        final ObjectMapper marshaller  = JsonMarshaller.getInstance().getDefaultObjectMapper();

        return marshaller.readValue(currentData, refObjectType);
    }

    public <T> T getResponseBody(final TypeReference<T> refObjectType) throws JsonProcessingException {
        if (data == null) {
            return null;
        }
        final String       currentData = new String(data, charset == null ? StandardCharsets.UTF_8 : charset);
        final ObjectMapper marshaller  = JsonMarshaller.getInstance().getDefaultObjectMapper();

        return marshaller.readValue(currentData, refObjectType);
    }

    public <T> T getBodyFromYaml(final Class<? extends T> objectClass) {
        if (bodyData == null) {
            return null;
        }
        final String currentData = new String(bodyData, charset == null ? StandardCharsets.UTF_8 : charset);
        return YamlMarshaller.getInstance().convertFromYaml(currentData, objectClass);
    }

    public <T> T getResponseBodyYaml(final Class<? extends T> objectClass) {
        if (data == null) {
            return null;
        }
        final String currentData = new String(data, charset == null ? StandardCharsets.UTF_8 : charset);
        return YamlMarshaller.getInstance().convertFromYaml(currentData, objectClass);
    }

    public <T> T getBodyFromYaml(final TypeReference<T> refObjectType) {
        if (bodyData == null) {
            return null;
        }
        final String currentData = new String(bodyData, charset == null ? StandardCharsets.UTF_8 : charset);

        return YamlMarshaller.getInstance().convertFromYaml(currentData, refObjectType);
    }

    public <T> T getResponseBodyYaml(final TypeReference<T> refObjectType) {
        if (data == null) {
            return null;
        }
        final String currentData = new String(data, charset == null ? StandardCharsets.UTF_8 : charset);

        return YamlMarshaller.getInstance().convertFromYaml(currentData, refObjectType);
    }

    public Optional<String> getErrorCode() {
        if (statusCode < HTTP_ERROR || data == null) {
            return Optional.empty();
        }

        final String currentData = new String(data, charset == null ? StandardCharsets.UTF_8 : charset);

        JsonNode node = null;
        try {
            node = JsonMarshaller.getInstance().getDefaultObjectMapper().readTree(currentData);
            final JsonNode parameters = node.get(PARAMETERS);
            final JsonNode errorCode  = parameters == null ? null : parameters.get(ERROR_CODE);
            return errorCode == null ? Optional.empty() : Optional.of(errorCode.asText());

        } catch (Throwable e) {
            return Optional.empty();
        }


    }

}
