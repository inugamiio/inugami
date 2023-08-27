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
package io.inugami.configuration.services.functions;

import io.inugami.api.processors.ConfigHandler;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import io.inugami.core.providers.functions.StartHourAtFunction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FunctionsServicesTest
 *
 * @author patrick_guillerm
 * @since 17 août 2017
 */
class FunctionsServicesTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testContainsFunction() {
        final FunctionsServices services = new FunctionsServices();

        assertTrue(services.containsFunction("#{startHourAt(06:00,-1d)}"));
        assertTrue(services.containsFunction("#{startHourAt(06:00)}"));
        assertFalse(services.containsFunction("-5w"));

    }

    @Test
    void testExtractData() {
        final FunctionsServices services = new FunctionsServices(iniConfig());

        final List<FunctionData> dataGrp = services.extractData("#{startHourAt(06:00,-1d)}");
        assertNotNull(dataGrp);
        assertEquals(1, dataGrp.size());
        final FunctionData data = dataGrp.get(0);

        assertEquals("startHourAt", data.getFunctionName());
        assertEquals(2, data.getParameters().length);
        assertEquals("06:00", data.getParameters()[0]);
        assertEquals("-1d", data.getParameters()[1]);

        final List<FunctionData> dataFooGrp = services.extractData("#{startHourAt({{foobar}},-1d)}");
        assertNotNull(dataFooGrp);
        assertEquals(1, dataFooGrp.size());
        final FunctionData dataFoo = dataFooGrp.get(0);

        assertNotNull(dataFoo);
        assertEquals("startHourAt", dataFoo.getFunctionName());
        assertEquals(2, dataFoo.getParameters().length);
        assertEquals("Hello the world", dataFoo.getParameters()[0]);
        assertEquals("-1d", dataFoo.getParameters()[1]);

    }

    @Test
    void testLoadFunction() {
        final List<ProviderAttributFunction> functions = new ArrayList<>();
        functions.add(new StartHourAtFunction());
        final FunctionsServices services = new FunctionsServices(functions, iniConfig());

        final List<FunctionData> dataGrp = services.extractData("#{startHourAt(06:00,-1d)}");
        final FunctionData       data    = dataGrp.get(0);
        assertNotNull(data);
        final ProviderAttributFunction function = services.loadFunction(data);
        assertNotNull(function);
        assertEquals(StartHourAtFunction.class, function.getClass());

    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    private ConfigHandler<String, String> iniConfig() {
        final Map<String, String> map = new HashMap<>();

        map.put("foobar", "Hello the world");

        return new ConfigHandlerHashMap(map);
    }
}
