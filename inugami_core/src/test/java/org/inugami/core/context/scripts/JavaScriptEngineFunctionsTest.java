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
package org.inugami.core.context.scripts;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.inugami.commons.engine.JavaScriptEngine;
import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.inugami.core.context.Context;
import org.junit.Test;

/**
 * JavaScriptEngineFunctionsTest
 * 
 * @author patrick_guillerm
 * @since 9 févr. 2018
 */
public class JavaScriptEngineFunctionsTest implements TestUnitResources {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testStringifyObject() throws Exception {
        final String test = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(), "testStringifyObject.js"));
        final JavaScriptEngine scriptEngine = Context.getInstance().getScriptEngine();
        
        final String result = scriptEngine.execute(test, "checkJsonStringify");
        assertNotNull(result);
        assertNotEquals("null", result);
        
    }
}
