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
package io.inugami.commons.test.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.framework.interfaces.tools.ListUtils;
import lombok.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MockContext implements Serializable {
    private static final long   serialVersionUID = -9138471545003861906L;
    public static final  String CONNECT          = "CONNECT";
    public static final  String DELETE           = "DELETE";
    public static final  String GET              = "GET";
    public static final  String HEAD             = "HEAD";
    public static final  String OPTIONS          = "OPTIONS";
    public static final  String PATCH            = "PATCH";
    public static final  String POST             = "POST";
    public static final  String PUT              = "PUT";
    public static final  String TRACE            = "TRACE";

    private           String                          name;
    private           String                          folder;
    private           String                          verb;
    private           String                          url;
    private           String                          description;
    private           Map<String, Serializable>       requestParams;
    private           Map<String, List<Serializable>> requestOptions;
    private           Map<String, Serializable>       requestHeaders;
    private           String                          request;
    private           Map<String, Serializable>       responseHeaders;
    private           String                          response;
    @Builder.Default
    private           String                          contentType = "application/json";
    @Builder.Default
    private transient Charset                         encoding    = StandardCharsets.UTF_8;
    private           int                             status;
    private transient ErrorCode                       errorCode;

    public static class MockContextBuilder {
        public MockContextBuilder addRequestOptions(@NonNull final String name, final Serializable... values) {
            if (requestOptions == null) {
                requestOptions = new LinkedHashMap<>();
            }
            requestOptions.put(name, ListUtils.toList(values));
            return this;
        }

        public MockContextBuilder addRequestParam(@NonNull final String id, @NonNull final Serializable value) {
            if (requestParams == null) {
                requestParams = new LinkedHashMap<>();
            }
            requestParams.put(id, value);
            return this;
        }

        public MockContextBuilder get(@NonNull final String urlValue) {
            verb = GET;
            url = urlValue;
            return this;
        }

        public MockContextBuilder post(@NonNull final String urlValue) {
            verb = POST;
            url = urlValue;
            return this;
        }

        public MockContextBuilder put(@NonNull final String urlValue) {
            verb = PUT;
            url = urlValue;
            return this;
        }

        public MockContextBuilder patch(@NonNull final String urlValue) {
            verb = PATCH;
            url = urlValue;
            return this;
        }

        public MockContextBuilder delete(@NonNull final String urlValue) {
            verb = DELETE;
            url = urlValue;
            return this;
        }

        public MockContextBuilder options(@NonNull final String urlValue) {
            verb = OPTIONS;
            url = urlValue;
            return this;
        }

        public MockContextBuilder head(@NonNull final String urlValue) {
            verb = HEAD;
            url = urlValue;
            return this;
        }

        public MockContextBuilder connect(@NonNull final String urlValue) {
            verb = CONNECT;
            url = urlValue;
            return this;
        }

        public MockContextBuilder trace(@NonNull final String urlValue) {
            verb = TRACE;
            url = urlValue;
            return this;
        }

        public MockContextBuilder statusSuccess() {
            status = 200;
            return this;
        }

        public MockContextBuilder statusFunctionalError() {
            status = 400;
            return this;
        }

        public MockContextBuilder statusTechnicalError() {
            status = 500;
            return this;
        }

        public MockContextBuilder requestPayload(@Nullable final Object value) {
            if (value == null) {
                request = null;
            } else {
                try {
                    request = JsonMarshaller.getInstance().getIndentedObjectMapper().writeValueAsString(value);
                } catch (JsonProcessingException e) {
                }
            }
            return this;
        }

        public MockContextBuilder responsePayload(@Nullable final Object value) {
            if (value == null) {
                response = null;
            } else {
                try {

                    response = JsonMarshaller.getInstance().getIndentedObjectMapper().writeValueAsString(value);
                } catch (JsonProcessingException e) {
                }
            }
            return this;
        }

        public MockContextBuilder errorCode(@NonNull final ErrorCode value) {
            status = Optional.ofNullable(value).map(ErrorCode::getStatusCode).orElse(200);
            errorCode = value;
            return this;
        }

        public MockContextBuilder addRequestHeaderTracking() {
            initRequestHeaders();
            requestHeaders.put(Headers.X_DEVICE_IDENTIFIER, UUID.randomUUID().toString());
            requestHeaders.put(Headers.X_CORRELATION_ID, UUID.randomUUID().toString());
            return this;
        }

        public MockContextBuilder addRequestHeader(@NonNull final String name, final Serializable value) {
            initRequestHeaders();
            requestHeaders.put(name, value);
            return this;
        }

        public MockContextBuilder addResponseHeaderTracking() {
            initResponseHeader();
            responseHeaders.put(Headers.X_DEVICE_IDENTIFIER, UUID.randomUUID().toString());
            responseHeaders.put(Headers.X_CORRELATION_ID, UUID.randomUUID().toString());
            responseHeaders.put(Headers.X_B_3_TRACEID, UUID.randomUUID().toString());
            return this;
        }

        public MockContextBuilder addResponseHeader(@NonNull final String name, final Serializable value) {
            initResponseHeader();
            responseHeaders.put(name, value);
            return this;
        }

        //--------------------------------------------------------------------------------------------------------------
        private void initResponseHeader() {
            if (responseHeaders == null) {
                responseHeaders = new LinkedHashMap<>();
            }
        }

        private void initRequestHeaders() {
            if (requestHeaders == null) {
                requestHeaders = new LinkedHashMap<>();
            }
        }


    }
}
