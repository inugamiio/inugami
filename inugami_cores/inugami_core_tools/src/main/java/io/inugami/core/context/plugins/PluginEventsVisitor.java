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
package io.inugami.core.context.plugins;

import io.inugami.configuration.models.EventConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * PluginValidator
 *
 * @author patrickguillerm
 * @since 4 sept. 2018
 */
class PluginEventsVisitor {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String DEFAULT_SCHEDULER = "0 * * * * ?";

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    PluginEventsVisitor() {
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @SuppressWarnings({"java:S2789"})
    List<EventConfig> visite(final Optional<List<EventConfig>> configs,
                             final Consumer<String> onCronExpressionDetected) {
        final List<EventConfig> result = new ArrayList<>();
        onCronExpr(DEFAULT_SCHEDULER, onCronExpressionDetected);
        if (configs == null) {
            return result;
        }

        for (final EventConfig config : configs.orElse(new ArrayList<>())) {
            result.add(visiteEventConfig(config, onCronExpressionDetected));
        }

        return result;
    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================

    private EventConfig visiteEventConfig(final EventConfig config, final Consumer<String> onCronExpressionDetected) {
        manageCron(config.getScheduler(), DEFAULT_SCHEDULER, config::setScheduler, onCronExpressionDetected);

        //@formatter:off
        if (config.getSimpleEvents() != null) {
            config.getSimpleEvents()
                           .forEach(simpleEvent -> manageCron(simpleEvent.getScheduler(),
                                                              config.getScheduler(),
                                                              simpleEvent::setScheduler,
                                                              onCronExpressionDetected));
        }

        if (config.getEvents() != null) {
            config.getEvents()
                           .forEach(event -> manageCron(event.getScheduler(),
                                                        config.getScheduler(),
                                                        event::setScheduler,
                                                        onCronExpressionDetected));
        }

        // @formatter:on

        return config;
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private void manageCron(final String cronExpr, final String defaultCron, final Consumer<String> function,
                            final Consumer<String> onCronExpressionDetected) {
        if (cronExpr != null) {
            onCronExpr(cronExpr, onCronExpressionDetected);
        } else {
            function.accept(defaultCron);
        }
    }

    private void onCronExpr(final String cronExpr, final Consumer<String> onCronExpressionDetected) {
        if (onCronExpressionDetected != null) {
            onCronExpressionDetected.accept(cronExpr);
        }
    }

}
