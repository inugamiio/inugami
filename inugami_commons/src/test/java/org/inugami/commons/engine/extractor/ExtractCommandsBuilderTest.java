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
package org.inugami.commons.engine.extractor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

/**
 * ExtractCommandsBuilderTest
 * 
 * @author patrick_guillerm
 * @since 22 mai 2018
 */
public class ExtractCommandsBuilderTest {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testBuild() throws Exception {
        final List<ExtractCommand> cmds = ExtractCommandsBuilder.build("foo.bar.[1].titi");
        assertEquals(4, cmds.size());
        assertEquals("foo", cmds.get(0).getFieldName());
        assertEquals("bar", cmds.get(1).getFieldName());
        assertNull(cmds.get(2).getFieldName());
        assertEquals(1, cmds.get(2).getIterationIndex());
        assertEquals("titi", cmds.get(3).getFieldName());
    }
}
