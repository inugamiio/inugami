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
package org.inugami.monitoring.api.resolvers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * DefaultServiceNameResolverTest
 * 
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
public class DefaultServiceNameResolverTest {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testResolve() {
        final ServiceNameResolver resolver = new DefaultServiceNameResolver();
        
        assertEquals("my/service", resolver.resolve("http://foo.bar.org/my/service"));
        assertEquals("my/service", resolver.resolve("http://127.0.0.1:8080/my/service"));
        assertEquals("my/service", resolver.resolve("http://foo.bar.org/my/service?foobar=hello"));
        
    }
    
}
