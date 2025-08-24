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
package io.inugami.monitoring.core.interceptors;

import io.inugami.framework.interfaces.exceptions.FatalException;
import io.inugami.framework.interfaces.spi.SpiLoader;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * ResponseWrapper
 *
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
@SuppressWarnings({"java:S6355", "java:S1123", "java:S3824", "java:S1133", "java:S1133"})
final class ResponseWrapper implements ServletResponse, HttpServletResponse {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String                    HEADER_SEP = ",";
    private final       HttpServletResponse       response;
    private final       OutputWriterWrapper       outputWrapper;
    private final       Map<String, List<String>> localHeaders;
    private final       List<ResponseListener>    responseListeners;



    // =================================================================================================================
    // CONSTRUCTORS
    // =================================================================================================================
    @Builder
    public ResponseWrapper(final HttpServletResponse response,
                           final Map<String, List<String>> localHeaders,
                           final List<ResponseListener> responseListeners) {
        this.response = response;
        this.localHeaders = localHeaders;
        this.responseListeners = responseListeners;
        try {
            this.outputWrapper = new OutputWriterWrapper(response.getOutputStream());
        } catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }

    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    String getData() {
        return outputWrapper.getData();
    }

    // =================================================================================================================
    // OVERRIDES
    // =================================================================================================================
    @Override
    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }

    @Override
    public String getContentType() {
        return response.getContentType();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        responseListeners.forEach(listener -> listener.beforeWriting(response));
        return outputWrapper;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return response.getWriter();
    }

    @Override
    public void setCharacterEncoding(final String charset) {
        response.setCharacterEncoding(charset);
    }

    @Override
    public void setContentLength(final int len) {
        response.setContentLength(len);
    }

    @Override
    public void setContentLengthLong(final long len) {
        response.setContentLengthLong(len);
    }

    @Override
    public void setContentType(final String type) {
        response.setContentType(type);
    }

    @Override
    public void setBufferSize(final int size) {
        response.setBufferSize(size);
    }

    @Override
    public int getBufferSize() {
        return response.getBufferSize();
    }

    @Override
    public void flushBuffer() throws IOException {
        responseListeners.forEach(listener -> listener.onFlush(response));
        response.flushBuffer();
    }

    @Override
    public void resetBuffer() {
        response.resetBuffer();
    }

    @Override
    public boolean isCommitted() {
        return response.isCommitted();
    }

    @Override
    public void reset() {
        response.reset();
    }

    @Override
    public void setLocale(final Locale loc) {
        response.setLocale(loc);
    }

    @Override
    public Locale getLocale() {
        return response.getLocale();
    }

    @Override
    public void addCookie(final Cookie cookie) {
        response.addCookie(cookie);
    }

    @Override
    public boolean containsHeader(final String name) {
        return response.containsHeader(name);
    }

    @Override
    public String encodeURL(final String url) {
        return response.encodeURL(url);
    }

    @Override
    public String encodeRedirectURL(final String url) {
        return response.encodeRedirectURL(url);
    }

    @Override
    public void sendError(final int sc, final String msg) throws IOException {
        response.sendError(sc, msg);
    }

    @Override
    public void sendError(final int sc) throws IOException {
        response.sendError(sc);
    }

    @Override
    public void sendRedirect(final String location) throws IOException {
        response.sendRedirect(location);
    }

    @Override
    public void setDateHeader(final String name, final long date) {
        response.setDateHeader(name, date);

    }

    @Override
    public void addDateHeader(final String name, final long date) {
        response.addDateHeader(name, date);

    }

    @Override
    public void setHeader(final String name, final String value) {
        if (name == null || value == null) {
            return;
        }

        response.setHeader(name, value);
        List<String> currentHeader = localHeaders.get(name);
        if (currentHeader == null) {
            currentHeader = new ArrayList<>();
            localHeaders.put(name, currentHeader);
        }
        currentHeader.add(value);
    }

    @Override
    public void addHeader(final String name, final String value) {
        setHeader(name, value);
    }

    @Override
    public void setIntHeader(final String name, final int value) {
        setHeader(name, String.valueOf(value));
    }

    @Override
    public void addIntHeader(final String name, final int value) {
        setHeader(name, String.valueOf(value));

    }

    @Override
    public void setStatus(final int sc) {
        response.setStatus(sc);
    }


    @Override
    public int getStatus() {
        return response.getStatus();
    }

    @Override
    public String getHeader(final String name) {
        String value = response.getHeader(name);
        if (value == null) {
            final List<String> values = localHeaders.get(name);
            if (values != null) {
                value = String.join(HEADER_SEP, values);
            }
        }
        return value;
    }

    @Override
    public Collection<String> getHeaders(final String name) {
        Collection<String> result = response.getHeaders(name);
        if (result == null) {
            result = localHeaders.get(name);
        }
        return result;
    }

    @Override
    public Collection<String> getHeaderNames() {
        List<String> result = new ArrayList<>();
        result.addAll(response.getHeaderNames());
        result.addAll(localHeaders.keySet());

        return result;
    }

}
