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
package io.inugami.framework.interfaces.alertings;

import io.inugami.framework.interfaces.models.event.AlertingModel;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import io.inugami.framework.interfaces.tools.NamedComponent;

import java.util.List;

/**
 * AlertingProvider
 *
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
@SuppressWarnings({"java:S107"})
public interface AlertingProvider extends NamedComponent {

    void postConstruct();

    AlertingResult process(final Gav gav, final GenericEvent event, final ProviderFutureResult data,
                           final AlertingModel alert, String preload);

    void appendAlert(final AlertingResult alert);

    void processSavedAlerts(final List<AlertingResult> entities);

    void processDisableAlerts(final List<String> alertsUids, final String channel);

    default void processDynamicAlert(final Gav gav,
                                     final SimpleEvent event,
                                     final ProviderFutureResult data,
                                     final List<DynamicAlertingLevel> levels,
                                     final String message,
                                     final String subMessage,
                                     final List<String> tags,
                                     final List<String> alertSenders) {
    }
}
