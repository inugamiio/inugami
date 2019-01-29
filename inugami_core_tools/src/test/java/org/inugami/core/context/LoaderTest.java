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
package org.inugami.core.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.processors.Config;
import org.inugami.api.providers.Provider;
import org.inugami.configuration.models.ProviderConfig;
import org.inugami.core.providers.ProviderWithName;
import org.junit.Test;

/**
 * LoaderTest
 * 
 * @author patrick_guillerm
 * @since 6 janv. 2017
 */
public class LoaderTest {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testLoadProvider() throws Exception {
        final Loader loader = new Loader();
        final List<ProviderConfig> configs = new ArrayList<>();
        //@formatter:off
        configs.add(new ProviderConfig("joe1",
                                       "org.inugami.core.providers.SimpleProvider",
                                       "bar"
                                       ));
        
        configs.add(new ProviderConfig("joe2",
                                        "org.inugami.core.providers.ProviderWithConfig",
                                        "bar",
                                        new Config("test", "true")
                                        ));
        
        configs.add(new ProviderConfig("joe3",
                                       "org.inugami.core.providers.ProviderFull",
                                       "bar",
                                           new Config("test", "true")
                                       ));
        configs.add(new ProviderConfig("joe4",
                                        "org.inugami.core.providers.ProviderWithName",
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
