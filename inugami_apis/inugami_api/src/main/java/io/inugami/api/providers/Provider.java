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
package io.inugami.api.providers;

import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.tools.NamedComponent;

import java.util.List;

/**
 * Inugami is a solution that gathers information from various sources, combines it and sends it to the front-end.
 * To accomplish this, Inugami uses data providers, which are represented by the <strong>Provider</strong> interface.
 * These providers have two main functions :
 * <ul>
 *     <li>
 *      first one is **callEvent**, which which retrieves data and takes in an event
 *      (simple or composite) and plugin information
 *     </li>
 *     <li>
 *         the second is the <strong>aggregate</strong>, which is used to process the data once
 *         it has been retrieved and is only activated when "callEvent" is run with a composite event.
 *     </li>
 * </ul>
 *
 * @author patrick_guillerm
 * @see io.inugami.api.providers.ProviderWithHttpConnector
 * @since 3 janv. 2017
 */
@SuppressWarnings({"java:S1168"})
public interface Provider extends NamedComponent {
    String CONFIG_TIMEOUT = "timeout";

    <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav);

    ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException;

    String getType();

    default long getTimeout() {
        return 0l;
    }

    default ConfigHandler<String, String> getConfig() {
        return null;
    }
}
