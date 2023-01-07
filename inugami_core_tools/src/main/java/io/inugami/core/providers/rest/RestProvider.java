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
import io.inugami.api.providers.task.ProviderTask;
import io.inugami.commons.connectors.ConnectorConstants;
import io.inugami.commons.providers.MockJsonHelper;
import io.inugami.core.services.connectors.HttpConnector;

import java.util.List;

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
    public static final String CONFIG_URL = "url";

    private final MockJsonHelper jsonHelper;

    private final String url;

    private final String verbe;

    private final HttpConnector httpConnector;

    private final FutureData<ProviderFutureResult> futureDataRef;

    private final int timeout;

    final ConfigHandler<String, String> config;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    protected RestProvider(final String url) {
        super(null, null, null);
        jsonHelper    = new MockJsonHelper();
        this.url      = url;
        verbe         = ConnectorConstants.HTTP_GET;
        httpConnector = new HttpConnector(10);
        timeout       = 20000;
        config        = null;
        // @formatter:off
		futureDataRef = new FutureDataBuilder<ProviderFutureResult>().addTimeout(timeout).build();
		// @formatter:on
    }

    public RestProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                        final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        jsonHelper    = new MockJsonHelper();
        timeout       = Integer.parseInt(config.optionnal().grabOrDefault(CONFIG_TIMEOUT, "20000"));
        url           = buildUrl(config.grab(CONFIG_URL), config);
        verbe         = config.optionnal().grabOrDefault("url-verbe", ConnectorConstants.HTTP_GET);
        httpConnector = new HttpConnector(10);
        this.config   = config;
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
