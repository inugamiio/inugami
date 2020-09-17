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
package io.inugami.core.providers.mock;

import java.util.List;

import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.AbstractProvider;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.ProviderRunner;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.concurrent.FutureDataBuilder;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.commons.providers.MockJsonHelper;

/**
 * MockJsonProvider
 * 
 * @author patrick_guillerm
 * @since 4 mai 2017
 */
public class MockJsonProvider extends AbstractProvider implements Provider {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private MockJsonHelper mockJsonData;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MockJsonProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                            final ProviderRunner providerRunner) {
        this(classBehavior, config, providerRunner, false);
        
    }
    
    public MockJsonProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                            final ProviderRunner providerRunner, final boolean absolute) {
        super(classBehavior, config, providerRunner);
        final String files = resolveProperties(config.optionnal().grab("files"), config);
        //@formatter:off
        setMockJsonHelper(new MockJsonHelper(files,
                                             config.optionnal().grabBoolean("random", true),
                                             absolute,
                                             config.optionnal().grabBoolean("latency.enable", false),
                                             config.optionnal().grabInt("latency.max.time", 10000),
                                             classBehavior.getManifest()));
        //@formatter:on
    }
    
    private String resolveProperties(final String data, final ConfigHandler<String, String> config) {
        String result = null;
        if (data != null) {
            result = config.applyProperties(data);
        }
        return result;
    }
    
    protected void setMockJsonHelper(final MockJsonHelper mockJsonHelper) {
        this.mockJsonData = mockJsonHelper;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        return buildResult(event);
    }
    
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        return mockJsonData.aggregate(data);
    }
    
    @Override
    public String getType() {
        return MockJsonProvider.class.getSimpleName();
    }
    
    public MockJsonHelper getMockJsonData() {
        return mockJsonData;
    }
    
    // =========================================================================
    // PRIVATE API
    // =========================================================================
    private <T extends SimpleEvent> FutureData<ProviderFutureResult> buildResult(final T event) {
        final String data = grabData(event);
        final ProviderFutureResult providerData = mockJsonData.buildStringResult(event, data);
        return new FutureDataBuilder<ProviderFutureResult>().addImmediateFuture(providerData).build();
    }
    
    private <T extends SimpleEvent> String grabData(final T event) {
        final String name = event.getName();
        String json = "null";
        
        if (mockJsonData.containsWithIndex(name)) {
            json = mockJsonData.getDataRandom(name);
        }
        else if (mockJsonData.contains(name)) {
            json = mockJsonData.getData(name);
        }
        
        return json;
    }
    
}
