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
package io.inugami.framework.configuration.services;

import io.inugami.api.models.events.Event;
import io.inugami.api.models.events.TargetConfig;
import io.inugami.configuration.models.EventConfig;

import java.util.Optional;

/**
 * PluginConfigurationDefaultValueLoading
 *
 * @author patrick_guillerm
 * @since 15 juin 2017
 */
class PluginConfigurationInitilizer {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final EventConfig eventConfig;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PluginConfigurationInitilizer(final EventConfig eventConfig) {
        this.eventConfig = eventConfig;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public void process() {
        if (eventConfig != null) {
            initializeTargetDefaultValues();
        }
    }

    // =========================================================================
    // INITIALIZE TARGETS
    // =========================================================================
    private void initializeTargetDefaultValues() {
        if (eventConfig.getEvents() != null) {
            for (final Event event : eventConfig.getEvents()) {
                if (event.getTargets() != null) {
                    event.getTargets().forEach(target -> initialierTarget(target, event));
                }
            }
        }
    }

    private void initialierTarget(final TargetConfig target, final Event event) {
        final Optional<String> parentFrom  = event.getFrom();
        final Optional<String> parentUntil = event.getUntil();

        if (!target.getFrom().isPresent() && parentFrom.isPresent()) {
            target.buildFrom(parentFrom.get());
        }

        if (!target.getUntil().isPresent() && parentUntil.isPresent()) {
            target.buildUntil(parentUntil.get());
        }
    }

}
