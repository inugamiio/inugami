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
package io.inugami.webapp.rest;

import io.inugami.api.spi.SpiPriority;
import io.inugami.monitoring.api.resolvers.Interceptable;

@SpiPriority(2)
public class SseRestInterceptorHandler implements Interceptable {
    
    public SseRestInterceptorHandler() {
    }

    @Override
    public boolean isInterceptable(final String uri) {
        return !uri.contains("/sse");
    }
}
