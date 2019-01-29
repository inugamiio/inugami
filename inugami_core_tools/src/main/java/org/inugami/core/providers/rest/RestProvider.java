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
package org.inugami.core.providers.rest;

import java.util.List;

import org.inugami.api.exceptions.services.ProviderException;
import org.inugami.api.models.Gav;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ClassBehavior;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.AbstractProvider;
import org.inugami.api.providers.Provider;
import org.inugami.api.providers.ProviderRunner;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.concurrent.FutureDataBuilder;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderTask;
import org.inugami.commons.connectors.HttpBasicConnector;
import org.inugami.core.providers.mock.MockJsonHelper;
import org.inugami.core.services.connectors.HttpConnector;

/**
 * RestProvider
 * 
 * @author patrick_guillerm
 * @since 9 mai 2017
 */
public class RestProvider extends AbstractProvider implements Provider {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String                     CONFIG_URL = "url";
    
    private final MockJsonHelper                   jsonHelper;
    
    private final String                           url;
    
    private final String                           verbe;
    
    private final HttpConnector                    httpConnector;
    
    private final FutureData<ProviderFutureResult> futureDataRef;
    
    private final int                              timeout;
    
    final ConfigHandler<String, String>            config;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    protected RestProvider(final String url) {
        super(null, null, null);
        jsonHelper = new MockJsonHelper();
        this.url = url;
        verbe = HttpBasicConnector.HTTP_GET;
        httpConnector = new HttpConnector(10);
        timeout = 20000;
        config = null;
        // @formatter:off
		futureDataRef = new FutureDataBuilder<ProviderFutureResult>().addTimeout(timeout).build();
		// @formatter:on
    }
    
    public RestProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                        final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        jsonHelper = new MockJsonHelper();
        timeout = Integer.parseInt(config.optionnal().grabOrDefault(CONFIG_TIMEOUT, "20000"));
        url = buildUrl(config.grab(CONFIG_URL), config);
        verbe = config.optionnal().grabOrDefault("url-verbe", HttpBasicConnector.HTTP_GET);
        httpConnector = new HttpConnector(10);
        this.config = config;
        // @formatter:off
		futureDataRef = new FutureDataBuilder<ProviderFutureResult>().addTimeout(timeout).build();
		// @formatter:on
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        final ProviderTask task = new RestProviderTask(event, url, verbe, httpConnector, pluginGav, config);
        return runTask(task, event, futureDataRef);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        return jsonHelper.aggregate(data);
    }
    
    @Override
    public String getType() {
        return RestProvider.class.getSimpleName();
    }
    
    // =========================================================================
    // BUILDER
    // =========================================================================
    protected final String buildUrl(final String urlBase, final ConfigHandler<String, String> config) {
        return config.applyProperties(urlBase);
    }
}
