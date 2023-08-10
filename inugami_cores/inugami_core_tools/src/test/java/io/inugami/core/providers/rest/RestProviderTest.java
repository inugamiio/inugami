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
package io.inugami.core.providers.rest;

import io.inugami.api.processors.ConfigHandler;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * RestProviderTest
 *
 * @author patrick_guillerm
 * @since 9 mai 2017
 */
class RestProviderTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testBuildUrl() {
        final RestProvider provider = new RestProvider(null);

        final ConfigHandler<String, String> config = new ConfigHandlerHashMap("unit-test");
        config.put("context", "foo/bar");
        config.put("path", "mock/data.json");

        final String resultUrl = provider.buildUrl("http://localhost:8080/{{context}}/{{path}}", config);
        assertEquals("http://localhost:8080/foo/bar/mock/data.json", resultUrl);

    }
}
