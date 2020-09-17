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
package io.inugami.core.context.handlers.hesperides;

import io.inugami.core.context.ContextSPI;
import io.inugami.core.context.ContextSpiLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HesperidesHandlerTest {
    private HesperidesHandler hesperidesHandler;

    @BeforeEach
    public void init() {
        final ContextSPI ctx = ContextSpiLoader.load().initializeStandalone();
        hesperidesHandler = ctx.getHandler("hesperides.handler");
    }

    @Test
    public void testBuildPropertiesRequest() throws HesperidesHandler.HesperidesHandlerException {
        final String request = hesperidesHandler.buildPropertiesRequest("FOOBAR", "DEV", "#");
        assertEquals("https://hesperides:1234/rest/applications/FOOBAR/platforms/DEV/properties?path=%23", request);
    }
}
