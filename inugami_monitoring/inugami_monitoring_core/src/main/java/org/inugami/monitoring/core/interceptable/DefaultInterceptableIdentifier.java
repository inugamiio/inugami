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
package org.inugami.monitoring.core.interceptable;

import javax.annotation.Priority;

import org.inugami.monitoring.api.resolvers.Interceptable;

/**
 * DefaultResourceIdentifier
 * 
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
@Priority(1)
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
    public boolean isInterceptable(String uri) {
        return !isResource(uri);
    }
    
    protected boolean isResource(String uri) {
        boolean result = false;
        if (uri == null) {
            return result;
        }
        
        result = "/".equals(uri);
        
        if (!result) {
            String path = uri.split("[?]")[0];
            for (String ext : RESOURCES_EXT) {
                result = path.endsWith(ext);
                if (result) {
                    break;
                }
            }
        }
        
        return result;
    }
}
