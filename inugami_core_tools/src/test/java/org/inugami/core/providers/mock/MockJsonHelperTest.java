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
package org.inugami.core.providers.mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * MockJsonHelperTest
 * 
 * @author patrick_guillerm
 * @since 4 mai 2017
 */
public class MockJsonHelperTest {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testMatchNameWithIndex() {
        final MockJsonHelper mock = new MockJsonHelper(null, null);
        
        assertTrue(mock.matchNameWithIndex("fooo", "fooo-1"));
        assertTrue(mock.matchNameWithIndex("fooo", "fooo-100"));
        assertTrue(mock.matchNameWithIndex("fooo", "fooo-0451"));
        
        assertFalse(mock.matchNameWithIndex("fooo", "fooo"));
        assertFalse(mock.matchNameWithIndex("fooo-1", "fooo"));
        assertFalse(mock.matchNameWithIndex("fooo", "fo-1oo"));
        assertFalse(mock.matchNameWithIndex("fooo", "fooo_1"));
    }
    
    @Test
    public void testGrabRandomIndex() {
        final MockJsonHelper mock = new MockJsonHelper(null, null);
        final int index = mock.grabRandomIndex(5);
        assertTrue(index < 5);
        assertTrue(index >= 0);
        assertEquals(0, mock.grabRandomIndex(-85));
    }
    
    @Test
    public void testContainsWithIndex() {
        final Map<String, String> data = new HashMap<>();
        data.put("foo-1", "hello foo 1");
        data.put("foo-2", "hello foo 2");
        data.put("foo-3", "hello foo 3");
        data.put("bar-1", "hello bar 1");
        data.put("bar-2", "hello bar 2");
        data.put("bar-3", "hello bar 3");
        
        final MockJsonHelper mock = new MockJsonHelper(data);
        assertTrue(mock.getDataRandom("foo").contains("hello foo"));
        assertTrue(mock.getDataRandom("bar").contains("hello bar"));
        
        assertEquals("hello foo 1", mock.getData("foo-1"));
        assertEquals("hello bar 1", mock.getData("bar-1"));
    }
    
    @Test
    public void testCleanFileName() {
        final MockJsonHelper mock = new MockJsonHelper(null, null);
        assertEquals("foo.bar", mock.cleanFileName("/META-INF/foo.bar.json"));
        assertEquals("foo.bar", mock.cleanFileName("/META-INF/mock/foo.bar.json"));
        assertEquals("titi-blabla-foo.bar", mock.cleanFileName("/META-INF/titi/blabla/foo.bar.json"));
        
    }
    
}
