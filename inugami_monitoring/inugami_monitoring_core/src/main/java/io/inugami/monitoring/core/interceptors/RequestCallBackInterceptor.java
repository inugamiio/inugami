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

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * RequestCallBackInterceptor
 *
 * @author patrickguillerm
 * @since Jan 10, 2019
 */
class RequestCallBackInterceptor implements InvocationHandler {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String METHOD_TO_INTERCEPT = "getInputStream";

    private final ServletRequest request;

    private final ServletInputStream data;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public RequestCallBackInterceptor(final ServletRequest request, final byte[] content) {
        data = new ServletBuffer(content);
        this.request = request;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (METHOD_TO_INTERCEPT.equals(method.getName())) {
            return data;
        } else {
            return method.invoke(request, args);
        }
    }

    private class ServletBuffer extends ServletInputStream {
        private final ByteArrayInputStream data;

        public ServletBuffer(final byte[] content) {
            data = new ByteArrayInputStream(content);
        }

        @Override
        public boolean isFinished() {
            return data.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(final ReadListener readListener) {
            // nothing to do
        }

        @Override
        public int read() throws IOException {
            return data.read();
        }

    }
}
