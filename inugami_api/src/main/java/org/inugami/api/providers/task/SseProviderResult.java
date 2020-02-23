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
package org.inugami.api.providers.task;

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.models.JsonBuilder;
import org.inugami.api.models.data.basic.JsonObject;

/**
 * SseProviderResult
 * 
 * @author patrick_guillerm
 * @since 24 janv. 2017
 */
public class SseProviderResult implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long          serialVersionUID = -1676324414315300420L;
    
    private final String               chanel;
    
    private final String               event;
    
    private final String               message;
    
    private final String               errorMessage;
    
    private final List<AlertingResult> alerts;
    
    private final boolean              error;
    
    private final JsonObject           values;
    
    private final String               scheduler;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SseProviderResult(final ProviderFutureResult providerFutureResult) {
        chanel = providerFutureResult.getChannel();
        event = providerFutureResult.getEvent().getName();
        scheduler = providerFutureResult.getScheduler();
        message = providerFutureResult.getMessage().orElse(null);
        values = providerFutureResult.getData().orElse(null);
        error = providerFutureResult.getException().isPresent();
        errorMessage = providerFutureResult.getException().isPresent() ? providerFutureResult.getException().get().getMessage()
                                                                       : null;
        alerts = providerFutureResult.getAlerts();
        
    }
    
    public SseProviderResult(final String chanel, final String eventName, final String message,
                             final String errorMessage, final boolean error, final JsonObject values,
                             final String scheduler, final List<AlertingResult> alerts) {
        super();
        this.chanel = chanel;
        event = eventName;
        this.message = message;
        this.errorMessage = errorMessage;
        this.error = error;
        this.values = values;
        this.scheduler = scheduler;
        this.alerts = alerts;
    }
    
    @Override
    public JsonObject cloneObj() {
        final List<AlertingResult> newAlerts = new ArrayList<>();
        if (alerts != null) {
            newAlerts.addAll(alerts);
        }
        
        //@formatter:off
        return new SseProviderResult(chanel,event,message,errorMessage,error,values==null?null:values.cloneObj(), scheduler,newAlerts);
        //@formatter:on
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String convertToJson() {
        final JsonBuilder json = new JsonBuilder();
        json.openObject();
        json.addField("chanel").valueQuot(chanel).addSeparator();
        json.addField("event").valueQuot(event).addSeparator();
        json.addField("scheduler").valueQuot(scheduler).addSeparator();
        json.addField("alerts").writeListJsonObject(alerts).addSeparator();
        
        json.addField("message").valueQuot(message).addSeparator();
        
        json.addField("errorMessage").valueQuot(errorMessage);
        json.addSeparator();
        json.addField("error").write(String.valueOf(error));
        json.addSeparator();
        json.addField("values");
        if (values == null) {
            json.valueNull();
        }
        else {
            json.write(values.convertToJson());
        }
        json.closeObject();
        return json.toString();
    }
    
}
