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
package io.inugami.core.services.connectors.mock;

import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * MockHttpProcessing
 * 
 * @author patrick_guillerm
 * @since 6 sept. 2018
 */
public class MockHttpProcessing {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Pattern                   methodPattern;
    
    private final Function<String, Boolean> acceptHandler;
    
    private final int                       statusCode;
    
    private final byte[]                    content;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MockHttpProcessing(final Pattern methodPattern, final Function<String, Boolean> acceptHandler,
                              final int statusCode, final byte[] content) {
        this.methodPattern = methodPattern;
        this.acceptHandler = acceptHandler;
        this.statusCode = statusCode;
        this.content = content;
    }
    
    // =========================================================================
    // GETTER
    // =========================================================================
    
    public Pattern getMethodPattern() {
        return methodPattern;
    }
    
    public Function<String, Boolean> getAcceptHandler() {
        return acceptHandler;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public byte[] getContent() {
        return content;
    }
    
}
