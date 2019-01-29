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
package org.inugami.api.models.data.graphite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.charset.Charset;

import org.junit.Test;

/**
 * GraphiteTargetsTest
 * 
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public class GraphiteTargetsTest {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testConvertToObject() throws Exception {
        final byte[] data = new TestFileUtils().readFromClassLoader("GraphiteTargetsTest.json");
        
        final GraphiteTargets graphiteTargets = new GraphiteTargets().convertToObject(data, Charset.forName("UTF-8"));
        
        assertNotNull(graphiteTargets);
        assertNotNull(graphiteTargets.getTargets());
        assertEquals(2, graphiteTargets.getTargets().size());
        
        assertEquals("foobar", graphiteTargets.getTargets().get(0).getTarget());
        assertEquals(7, graphiteTargets.getTargets().get(0).getDatapoints().size());
        
        assertEquals("foobar2", graphiteTargets.getTargets().get(1).getTarget());
        assertEquals(7, graphiteTargets.getTargets().get(1).getDatapoints().size());
    }
    
    @Test
    public void testConvertToJson() throws Exception {
        final byte[] data = new TestFileUtils().readFromClassLoader("GraphiteTargetsTest.json");
        final byte[] dataResult = new TestFileUtils().readFromClassLoader("GraphiteTargetsTest-result.json");
        final GraphiteTargets graphiteTargets = new GraphiteTargets().convertToObject(data, Charset.forName("UTF-8"));
        final String json = graphiteTargets.convertToJson();
        assertEquals(new String(dataResult), json);
        
    }
    
}
