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
package io.inugami.core.providers;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.task.ProviderFutureResult;

import java.util.List;

/**
 * SimpleProvider
 *
 * @author patrick_guillerm
 * @since 6 janv. 2017
 */
public class ProviderWithConfig implements Provider {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String test;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderWithConfig(final ConfigHandler<String, String> config) {
        test = config.grab("test");
        Asserts.notNull(test);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        // TODO Auto-generated method stub
        return null;
    }

}
