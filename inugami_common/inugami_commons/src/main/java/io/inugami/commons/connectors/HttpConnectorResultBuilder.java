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

import io.inugami.api.exceptions.FatalException;
import io.inugami.api.models.Builder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HttpConnectorResultBuilder
 *
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public class HttpConnectorResultBuilder implements Builder<HttpConnectorResult> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private URL url;

    private String verb;

    private byte[] bodyPayload;
    private byte[] data;

    private String contentType;

    private String jsonInputData;

    private long begin = 0L;

    private int statusCode;

    private String encoding;

    private String message;

    private long responseAt;

    private final Map<String, String> requestHeaders  = new LinkedHashMap<>();
    private final Map<String, String> responseHeaders = new LinkedHashMap<>();


    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public HttpConnectorResultBuilder() {
        super();
    }

    public HttpConnectorResultBuilder(final byte[] data) {
        this.data = data;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public HttpConnectorResultBuilder addUrl(final URL url) {
        this.url = url;
        return this;
    }

    public HttpConnectorResultBuilder addVerb(final String verb) {
        this.verb = verb;
        return this;
    }

    public HttpConnectorResultBuilder addData(final byte[] data) {
        this.data = data;
        return this;
    }

    public HttpConnectorResultBuilder addBodyData(final byte[] data) {
        this.bodyPayload = data;
        return this;
    }

    public HttpConnectorResultBuilder addContentType(final String contentType) {
        this.contentType = contentType;
        return this;
    }

    public HttpConnectorResultBuilder addBegin() {
        begin = System.currentTimeMillis();
        return this;
    }

    public HttpConnectorResultBuilder addResponseAt() {
        responseAt = System.currentTimeMillis();
        return this;
    }

    public HttpConnectorResultBuilder addMessage(final String message) {
        this.message = message;
        return this;
    }

    public HttpConnectorResultBuilder addStatusCode(final int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpConnectorResultBuilder addJsonInputData(final String jsonInputData) {
        this.jsonInputData = jsonInputData;
        return this;
    }

    public HttpConnectorResultBuilder addEncoding(final String encoding) {
        this.encoding = encoding;
        return this;
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

    @Override
    public HttpConnectorResult build() {
        return HttpConnectorResult.builder()
                                  .url(url == null ? null : url.toString())
                                  .verb(verb)
                                  .jsonInputData(jsonInputData)
                                  .data(data)
                                  .bodyData(bodyPayload)
                                  .contentType(contentType)
                                  .statusCode(statusCode)
                                  .message(message)
                                  .responseAt(responseAt)
                                  .delay(responseAt - begin)
                                  .encoding(encoding)
                                  .requestHeaders(requestHeaders)
                                  .responseHeaders(responseHeaders)
                                  .build();
    }

    public void addUrl(final String url) {
        try {
            this.url = new URL(url);
        } catch (final MalformedURLException e) {
            throw new FatalException(e.getMessage(), e);
        }

    }

}
