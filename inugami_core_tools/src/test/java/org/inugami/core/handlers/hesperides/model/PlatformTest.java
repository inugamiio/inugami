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
package org.inugami.core.handlers.hesperides.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.inugami.core.context.handlers.hesperides.model.Module;
import org.inugami.core.context.handlers.hesperides.model.Platform;
import org.junit.Test;

public class PlatformTest implements TestUnitResources {
    
    @Test
    public void testConvertToObject() throws IOException {
        final String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(), "PlatformTest.json"));
        
        final Platform platform = new Platform().convertToObject(data.getBytes(), Charset.forName("UTF-8"));
        
        assertNotNull(platform);
        
        assertEquals("INTEGRATION", platform.getPlatformName());
        assertEquals("FOOBAR", platform.getApplicationName());
        assertEquals("v1.2.3.4", platform.getApplicationVersion());
        assertEquals(84, platform.getVersionId());
        assertFalse(platform.getProduction());
        
        assertNotNull(platform.getModules());
        assertEquals(2, platform.getModules().size());
        
        final List<Module> modules = platform.getModules();
        
        final Module firstModule = modules.get(0);
        final Module expectedFirstModule = new Module(5, "foobar-module", "2.3.4", false,
                                                      "#FOOBAR#MODULE#module#2.3.4#RELEASE", "#FOOBAR#MODULE");
        
        assertEquals(expectedFirstModule, firstModule);
        
        final Module secondModule = modules.get(1);
        final Module expectedSecondModule = new Module(9, "joe-module", "9.8.7", false,
                                                       "#FOOBAR#MODULE#joe#9.8.7#RELEASE", "#FOOBAR#MODULE");
        
        assertEquals(expectedSecondModule, secondModule);
    }
}
