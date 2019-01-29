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
package org.inugami.monitoring.core.interceptors;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.inugami.api.exceptions.FatalException;
import org.inugami.commons.tools.ProxyBuilder;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * ResponseWrapper
 * 
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
final class ResponseWrapper implements ServletResponse, HttpServletResponse {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final HttpServletResponse response;
    
    private final OutputWriterWrapper outputWrapper;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    ResponseWrapper(ServletResponse response) {
        this.response = (HttpServletResponse) response;
        
        try {
            this.outputWrapper = new OutputWriterWrapper(response.getOutputStream());
        }
        catch (IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }
    
    // =========================================================================
    // METHODES
    // =========================================================================
    String getData() {
        return outputWrapper.getData();
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
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
        return outputWrapper;
    }
    
    @Override
    public PrintWriter getWriter() throws IOException {
        return response.getWriter();
    }
    
    @Override
    public void setCharacterEncoding(String charset) {
        response.setCharacterEncoding(charset);
    }
    
    @Override
    public void setContentLength(int len) {
        response.setContentLength(len);
    }
    
    @Override
    public void setContentLengthLong(long len) {
        response.setContentLengthLong(len);
    }
    
    @Override
    public void setContentType(String type) {
        response.setContentType(type);
    }
    
    @Override
    public void setBufferSize(int size) {
        response.setBufferSize(size);
    }
    
    @Override
    public int getBufferSize() {
        return response.getBufferSize();
    }
    
    @Override
    public void flushBuffer() throws IOException {
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
    public void setLocale(Locale loc) {
        response.setLocale(loc);
    }
    
    @Override
    public Locale getLocale() {
        return response.getLocale();
    }
    
    @Override
    public void addCookie(Cookie cookie) {
        response.addCookie(cookie);
    }
    
    @Override
    public boolean containsHeader(String name) {
        return response.containsHeader(name);
    }
    
    @Override
    public String encodeURL(String url) {
        return response.encodeURL(url);
    }
    
    @Override
    public String encodeRedirectURL(String url) {
        return response.encodeRedirectURL(url);
    }
    
    @Deprecated
    @Override
    public String encodeUrl(String url) {
        return response.encodeUrl(url);
    }
    
    @Deprecated
    @Override
    public String encodeRedirectUrl(String url) {
        return response.encodeRedirectUrl(url);
    }
    
    @Override
    public void sendError(int sc, String msg) throws IOException {
        response.sendError(sc, msg);
    }
    
    @Override
    public void sendError(int sc) throws IOException {
        response.sendError(sc);
    }
    
    @Override
    public void sendRedirect(String location) throws IOException {
        response.sendRedirect(location);
    }
    
    @Override
    public void setDateHeader(String name, long date) {
        response.setDateHeader(name, date);
        
    }
    
    @Override
    public void addDateHeader(String name, long date) {
        response.addDateHeader(name, date);
        
    }
    
    @Override
    public void setHeader(String name, String value) {
        response.setHeader(name, value);
        
    }
    
    @Override
    public void addHeader(String name, String value) {
        response.setHeader(name, value);
    }
    
    @Override
    public void setIntHeader(String name, int value) {
        response.setIntHeader(name, value);
    }
    
    @Override
    public void addIntHeader(String name, int value) {
        response.addIntHeader(name, value);
        
    }
    
    @Override
    public void setStatus(int sc) {
        response.setStatus(sc);
    }
    
    @Deprecated
    @Override
    public void setStatus(int sc, String sm) {
        response.setStatus(sc, sm);
    }
    
    @Override
    public int getStatus() {
        return response.getStatus();
    }
    
    @Override
    public String getHeader(String name) {
        return response.getHeader(name);
    }
    
    @Override
    public Collection<String> getHeaders(String name) {
        return response.getHeaders(name);
    }
    
    @Override
    public Collection<String> getHeaderNames() {
        return response.getHeaderNames();
    }
    
}
