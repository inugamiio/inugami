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
package io.inugami.configuration.test;

import io.inugami.api.listeners.EngineListener;
import io.inugami.api.processors.ConfigHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PluginListener
 *
 * @author patrick_guillerm
 * @since 3 janv. 2017
 */
public class PluginListener implements EngineListener {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginListener.class);

    private final String prefix;

    // =========================================================================
    // METHODS
    // =========================================================================
    public PluginListener() {
        prefix = null;
    }

    public PluginListener(final ConfigHandler<String, String> config) {
        prefix = config.get("prefix");
    }

    // =========================================================================
    // METHODS
    // =========================================================================

    @Override
    public void onErrorCallEndProvider(final String providerType, final String request, final int status,
                                       final String message, final long delayTime) {
        LOGGER.info("{} hello", prefix);
    }
}
