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
package io.inugami.core.alertings.senders.sse;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.alertings.AlertsSender;
import io.inugami.api.alertings.AlertsSenderException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.basic.JsonObjects;
import io.inugami.api.models.data.basic.StringJson;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.api.tools.NamedComponent;
import io.inugami.core.services.sse.SseService;

import javax.enterprise.inject.Default;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SseAlertingSender
 *
 * @author patrickguillerm
 * @since 20 janv. 2018
 */
@SuppressWarnings({"java:S2629"})
@Named
@Default
public class SseAlertSender implements AlertsSender, NamedComponent {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String ALERTS = "alert";

    private static final String ALERT_CONTROL = "alert-control";

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void sendNewAlert(final AlertingResult alert, final List<String> channels) throws AlertsSenderException {
        send(alert, channels);
    }

    @Override
    public void send(final AlertingResult alert, final List<String> channels) throws AlertsSenderException {

        for (final String channel : channels) {
            final AlertingResult localAlert = (AlertingResult) alert.cloneObj();
            localAlert.setChannel(channel);

            //@formatter:off
            final ProviderFutureResult data = new ProviderFutureResultBuilder()
                                                    .addCronExpression("SYSTEM-SCHEDULER")
                                                    .addEvent(SimpleEvent.simpleEventBuilder().name(ALERTS).build())
                                                    .addAlert(localAlert)
                                                    .addChannel(channel)
                                                    .build();
            //@formatter:on
            Loggers.ALERTING.info("[SSE] send alert : {}", data.convertToJson());
            SseService.sendEvent(channel, ALERTS, data);
        }

    }

    @Override
    public void delete(final List<String> uids, final List<String> channels) throws AlertsSenderException {
        final List<StringJson> jsonUids = uids.stream().map(StringJson::new).collect(Collectors.toList());
        final JsonObject       json     = new JsonObjects<StringJson>(jsonUids);

        Loggers.ALERTING.info("[SSE] send disabled alert : {}", json.convertToJson());
        for (final String channel : channels) {
            //@formatter:off
            final ProviderFutureResult data = new ProviderFutureResultBuilder()
                                                    .addCronExpression("SYSTEM-SCHEDULER")
                                                    .addChannel(channel)
                                                    .addEvent(SimpleEvent.simpleEventBuilder().name(ALERT_CONTROL).build())
                                                    .addData(json)
                                                    .build();
            //@formatter:on
            SseService.sendEvent(channel, ALERT_CONTROL, data);
        }
    }

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public String getName() {
        return "sse";
    }

}
