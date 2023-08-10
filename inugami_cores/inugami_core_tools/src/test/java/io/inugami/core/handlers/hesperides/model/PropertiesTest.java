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
package io.inugami.core.handlers.hesperides.model;

import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.core.context.handlers.hesperides.model.Properties;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PropertiesTest implements TestUnitResources {
    @Test
    void testConvertToObject() throws IOException {
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
