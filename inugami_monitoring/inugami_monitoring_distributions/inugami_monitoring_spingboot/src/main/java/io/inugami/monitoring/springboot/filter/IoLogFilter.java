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
package io.inugami.monitoring.springboot.filter;

import io.inugami.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.monitoring.core.interceptors.FilterInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Order(Ordered.LOWEST_PRECEDENCE)
@Component
public class IoLogFilter extends GenericFilterBean {

    @Value("${inugami.monitoring.iolog.enabled:true}")
    private boolean enabled;

    private FilterInterceptor filter = null;

    @PostConstruct
    public void init(){
        filter = new FilterInterceptor();
        DefaultApplicationLifecycleSPI.register(filter);
    }


    // =========================================================================
    // API
    // =========================================================================
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        if (enabled) {
            filter.doFilter(request, response, chain);
        }
        else {
            chain.doFilter(request, response);
        }
    }
}
