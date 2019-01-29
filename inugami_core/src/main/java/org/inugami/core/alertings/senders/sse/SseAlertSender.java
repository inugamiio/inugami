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
package org.inugami.core.alertings.senders.sse;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.inject.Default;
import javax.inject.Named;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.alertings.AlertsSender;
import org.inugami.api.alertings.AlertsSenderException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.data.JsonObject;
import org.inugami.api.models.data.basic.JsonObjects;
import org.inugami.api.models.data.basic.StringJson;
import org.inugami.api.models.events.SimpleEventBuilder;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.core.services.sse.SseService;

/**
 * SseAlertingSender
 * 
 * @author patrickguillerm
 * @since 20 janv. 2018
 */
@Named
@Default
public class SseAlertSender implements AlertsSender {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String ALERTS        = "alert";
    
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
                                                    .addEvent(new SimpleEventBuilder().addName(ALERTS).build())
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
        final JsonObject json = new JsonObjects<StringJson>(jsonUids);
        
        Loggers.ALERTING.info("[SSE] send disabled alert : {}", json.convertToJson());
        for (final String channel : channels) {
            //@formatter:off
            final ProviderFutureResult data = new ProviderFutureResultBuilder()
                                                    .addCronExpression("SYSTEM-SCHEDULER")
                                                    .addChannel(channel)
                                                    .addEvent(new SimpleEventBuilder().addName(ALERT_CONTROL).build())
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
    
}
