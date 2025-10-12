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
package io.inugami.framework.commons.threads.runner;

import io.inugami.framework.interfaces.listeners.TaskStartListener;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.models.event.MetricsEvents;
import io.inugami.framework.interfaces.models.maven.Gav;

public class ProviderTaskStartListener implements TaskStartListener {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final GenericEvent event;

    private final Gav pluginGav;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public ProviderTaskStartListener(final GenericEvent event, final Gav pluginGav) {
        this.event = event;
        this.pluginGav = pluginGav;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void onStart(final long time, final String name) {
        MetricsEvents.onStart(pluginGav, event.getName());
    }
}
