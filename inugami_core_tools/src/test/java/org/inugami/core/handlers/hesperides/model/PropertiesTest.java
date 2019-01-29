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
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.Charset;

import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.inugami.core.context.handlers.hesperides.model.Properties;
import org.junit.Test;

public class PropertiesTest implements TestUnitResources {
    @Test
    public void testConvertToObject() throws IOException {
        final String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(), "PropertiesTest.json"));
        
        final Properties properties = new Properties().convertToObject(data.getBytes(), Charset.forName("UTF-8"));
        
        assertNotNull(properties);
        assertNotNull(properties.getProperties());
        assertEquals(2, properties.getProperties().size());
        
        assertEquals("healthcheck.app.password", properties.getProperties().get(0).getName());
        assertEquals("password", properties.getProperties().get(0).getValue());
        
        assertEquals("jms.app.stream.send.timeout", properties.getProperties().get(1).getName());
        assertEquals("4000", properties.getProperties().get(1).getValue());
    }
}
