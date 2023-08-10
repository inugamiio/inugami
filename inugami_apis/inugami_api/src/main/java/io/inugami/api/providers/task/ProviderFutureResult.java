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
package io.inugami.api.providers.task;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.basic.JsonObjects;
import io.inugami.api.models.data.basic.JsonSerializerSpi;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.spi.SpiLoader;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ProviderFutureResultModel
 *
 * @author patrick_guillerm
 * @since 9 janv. 2017
 */
public class ProviderFutureResult implements JsonObject {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long   serialVersionUID = -3593233046062751124L;
    public static final  String DATA_FIELD       = "data";

    private final String message;

    private final Exception exception;

    private final String scheduler;

    private final JsonObject data;

    private final GenericEvent event;

    private final String channel;

    private final String fieldData;

    private final List<AlertingResult> alerts;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderFutureResult(final String message, final Exception exception, final JsonObject data,
                                final GenericEvent event, final String channel, final String scheduler,
                                final List<AlertingResult> alerts) {
        super();
        this.message = message;
        this.exception = exception;
        this.data = data;
        this.event = event;
        this.channel = channel;
        this.scheduler = scheduler;
        fieldData = initFieldData();
        this.alerts = alerts;
    }

    protected String initFieldData() {
        return DATA_FIELD;
    }

    @Override
    public JsonObject cloneObj() {
        final List<AlertingResult> newAlerts = new ArrayList<>();
        if (alerts != null) {
            newAlerts.addAll(alerts);
        }

        if (event == null) {
            Loggers.PROVIDER.error("provider send null event");
        }

        //@formatter:off
        return new ProviderFutureResult(message,
                                        exception,
                                        data  ==null?null:data.cloneObj(),
                                        event ==null?null:event.cloneObj(),
                                        channel,
                                        scheduler,
                                        newAlerts);
        //@formatter:on
    }

    // =========================================================================
    // OVERRIDE
    // =========================================================================
    @Override
    public String toString() {
        //@formatter:off
        final StringBuilder builder = new StringBuilder()
                .append("ProviderFutureResultModel [message=")
                .append(message)
                .append(", channel=").append(channel)
                .append(", event=").append(event == null ? null : event.getName())
                .append(", data=").append(data)
                .append(", scheduler=").append(scheduler)
                .append(", event=").append(event)
                .append("]");
        //@formatter:off
        return builder.toString();
    }
    
    @Override
    public String convertToJson() {
        final JsonBuilder json = new JsonBuilder();
        json.openObject();
        
        json.addField("channel").valueQuot(channel).addSeparator();
        json.addField("scheduler").valueQuot(scheduler).addSeparator();
        json.addField("event").valueQuot(event.getName());
        
        if (exception != null) {
            json.addSeparator();
            json.addField("exception").write(buildJsonError(exception));
        }
        
        json.addSeparator();
        json.addField("alerts");
        if ((alerts == null) || alerts.isEmpty()) {
            json.valueNull();
        }
        else {
            json.write(new JsonObjects<>(getAlerts()).convertToJson());
        }
        
        json.addSeparator();
        json.addField(fieldData);
        if (data == null) {
            json.valueNull();
        }
        else {
            String jsonData = "{}";
            final String jsonDataRaw = data.convertToJson();
            if (jsonDataRaw != null) {
                final String[] lines = jsonDataRaw.split("\n");
                final StringBuilder buffer = new StringBuilder();
                for (final String line : lines) {
                    buffer.append(line.trim());
                }
                jsonData = buffer.toString();
            }
            
            json.write(jsonData);
        }
        
        json.closeObject();
        return json.toString();
    }
    private String extractStack(final Exception error) {
        final StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
    private String buildJsonError(final Exception error) {
        final JsonSerializerSpi jsonSerializer = SpiLoader.getInstance().loadSpiSingleService(JsonSerializerSpi.class);
        final JsonBuilder       json           = new JsonBuilder();
        json.openObject();
        json.addField("message").valueQuot(error.getMessage());
        if (error.getCause() != null) {
            json.addSeparator();
            json.addField("cause").valueQuot(error.getCause().getMessage());
        }

        json.addSeparator();
        json.addField("stack").write(jsonSerializer.serialize(extractStack(error)));

        json.closeObject();
        return json.toString();
    }


    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Optional<String> getMessage() {
        return message == null ? Optional.empty() : Optional.of(message);
    }
    
    public Optional<Exception> getException() {
        return exception == null ? Optional.empty() : Optional.of(exception);
    }
    
    public <T extends JsonObject> Optional<T> getData() {
        return data == null ? Optional.empty() : Optional.of((T) data);
    }
    
    public GenericEvent getEvent() {
        return event;
    }
    
    public String getChannel() {
        return channel;
    }
    
    public String getScheduler() {
        return scheduler;
    }
    
    public List<AlertingResult> getAlerts() {
        return alerts;
    }
    
}
