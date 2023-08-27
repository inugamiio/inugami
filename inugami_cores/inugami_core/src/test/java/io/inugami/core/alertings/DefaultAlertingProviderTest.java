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
package io.inugami.core.alertings;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DefaultAlertingProviderTest
 *
 * @author patrick_guillerm
 * @since 9 mars 2018
 */
class DefaultAlertingProviderTest implements TestUnitResources {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testDeserializeMutliAlerts() throws Exception {

        final String json = read("defaultAlertingProviderTest.multiAlerts.json");
        final DefaultAlertingProvider provider = new DefaultAlertingProvider(null,
                                                                             new ConfigHandlerHashMap("unit-test"));

        final List<AlertingResult> alerts = provider.deserializeMutliAlerts(json);
        assertNotNull(alerts);
        assertEquals(2, alerts.size());

        assertEquals("alerte.1", alerts.get(0).getAlerteName());
        assertEquals("@joePlugin", alerts.get(0).getChannel());
        assertEquals(1520600619125L, alerts.get(0).getCreated());
        assertEquals(300L, alerts.get(0).getDuration());
        assertEquals("warn duration", alerts.get(0).getLevel());
        assertEquals("_services.duration.err", alerts.get(0).getMessage());
        assertNull(alerts.get(0).getSubLabel());
        assertEquals(read("defaultAlertingProviderTest.multiAlerts.data-1.json"), alerts.get(0).getData());

        assertEquals("alerte.2", alerts.get(1).getAlerteName());
        assertEquals("@all", alerts.get(1).getChannel());
        assertEquals(1520600619119L, alerts.get(1).getCreated());
        assertEquals(500L, alerts.get(1).getDuration());
        assertEquals("error duration", alerts.get(1).getLevel());
        assertEquals("_services.duration.err2", alerts.get(1).getMessage());
        assertEquals("Foobar", alerts.get(1).getSubLabel());

    }

    private String read(final String fileName) throws IOException {
        return FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(), fileName));
    }
}
