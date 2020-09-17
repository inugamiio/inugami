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
package io.inugami.monitoring.core.interceptable;

import io.inugami.api.spi.SpiPriority;
import io.inugami.monitoring.api.resolvers.Interceptable;

/**
 * DefaultResourceIdentifier
 * 
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
@SpiPriority(10000)
public class DefaultInterceptableIdentifier implements Interceptable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    //@formatter:off
    private final static String[] RESOURCES_EXT = { ".js", ".ts",
                                                    ".jpg", ".png", ".gif",".svg",
                                                    ".ttf",".otf",".eot",".woff",".woff2",
                                                    ".css",".map",
                                                    ".jsp",".html", ".xhtml"};
    //@formatter:on
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public boolean isInterceptable(final String uri) {
        return !isResource(uri);
    }
    
    protected boolean isResource(final String uri) {
        boolean result = false;
        if (uri == null) {
            return result;
        }
        
        result = uri.trim().isEmpty() || uri.endsWith("/");
        
        if (!result) {
            final String path = uri.split("[?]")[0];
            for (final String ext : RESOURCES_EXT) {
                result = path.endsWith(ext);
                if (result) {
                    break;
                }
            }
        }
        
        return result;
    }
}
