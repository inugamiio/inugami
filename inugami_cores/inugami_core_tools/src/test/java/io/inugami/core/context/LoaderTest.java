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
package io.inugami.core.context;

import io.inugami.api.processors.Config;
import io.inugami.api.providers.Provider;
import io.inugami.configuration.models.ProviderConfig;
import io.inugami.core.providers.ProviderWithName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * LoaderTest
 *
 * @author patrick_guillerm
 * @since 6 janv. 2017
 */
class LoaderTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testLoadProvider() throws Exception {
        final Loader               loader  = new Loader();
        final List<ProviderConfig> configs = new ArrayList<>();
        //@formatter:off
        configs.add(new ProviderConfig("joe1",
                                       "io.inugami.core.providers.SimpleProvider",
                                       "bar"
                                       ));
        
        configs.add(new ProviderConfig("joe2",
                                        "io.inugami.core.providers.ProviderWithConfig",
                                        "bar",
                                        new Config("test", "true")
                                        ));
        
        configs.add(new ProviderConfig("joe3",
                                       "io.inugami.core.providers.ProviderFull",
                                       "bar",
                                           new Config("test", "true")
                                       ));
        configs.add(new ProviderConfig("joe4",
                                        "io.inugami.core.providers.ProviderWithName",
                                        "bar",
                                        new Config("test", "true")
        ));
        //@formatter:on

        final List<Provider> providers = loader.loadProvider(configs, null, null);
        assertNotNull(providers);
        assertEquals(4, providers.size());

        for (final Provider provider : providers) {
            if (provider instanceof ProviderWithName) {
                assertEquals("joe4", provider.getName());
            }
        }
    }
}
