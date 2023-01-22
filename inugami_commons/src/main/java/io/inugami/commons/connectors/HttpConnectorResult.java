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

import lombok.*;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

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
public class HttpConnectorResult implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -576629010706862497L;

    private final String verb;
    private final String url;

    @EqualsAndHashCode.Include
    private final String hashHumanReadable;

    private final String jsonInputData;

    private final int statusCode;

    private final String message;

    private final int hashCode;

    private final byte[] data;
    private final byte[] bodyData;

    private final int length;

    private final String contentType;

    private final long responseAt;

    private final long delais;

    private final String encoding;

    private final Exception error;

    private final Charset charset;

    private Map<String, String> requestHeaders;
    private Map<String, String> responseHeaders;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public static class HttpConnectorResultBuilder {
        private Map<String, String> requestHeaders  = new LinkedHashMap<>();
        private Map<String, String> responseHeaders = new LinkedHashMap<>();

        public HttpConnectorResult build() {
            final StringBuilder hashBuilder = new StringBuilder()
                    .append('[').append(this.verb).append(']')
                    .append(this.url);

            this.encoding = encoding == null ? "UTF-8" : encoding;
            this.length   = data == null ? 0 : data.length;
            if (jsonInputData != null) {
                hashBuilder.append("?data=").append(jsonInputData.replaceAll("\\n", ""));
            }

            this.hashHumanReadable = hashBuilder.toString();
            hashCode               = this.hashHumanReadable.hashCode();
            return new HttpConnectorResult(
                    verb,
                    url,
                    hashHumanReadable,
                    jsonInputData,
                    statusCode,
                    message,
                    hashCode,
                    data,
                    bodyData,
                    length,
                    contentType,
                    responseAt,
                    delais,
                    encoding,
                    error,
                    charset == null ? StandardCharsets.UTF_8 : charset,
                    requestHeaders,
                    responseHeaders
            );
        }

        public HttpConnectorResultBuilder addRequestHeader(final String key, final String value) {
            if (key != null) {
                requestHeaders.put(key, value);
            }

            return this;
        }

        public HttpConnectorResultBuilder addResponseHeader(final String key, final String value) {
            if (key != null) {
                responseHeaders.put(key, value);
            }

            return this;
        }
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return hashCode;
    }


}
