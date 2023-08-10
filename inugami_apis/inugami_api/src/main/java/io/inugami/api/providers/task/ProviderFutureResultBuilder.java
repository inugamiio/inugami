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

import java.util.List;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.models.Builder;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.events.GenericEvent;

/**
 * ProviderFutureResultBuilder
 *
 * @author patrick_guillerm
 * @since 9 janv. 2017
 */
public class ProviderFutureResultBuilder implements Builder<ProviderFutureResult> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ProviderFutureResultFront data;
    
    private JsonObject                      jsonData;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderFutureResultBuilder() {
        super();
        data = new ProviderFutureResultFront();
    }
    
    public ProviderFutureResultBuilder(final ProviderFutureResult model) {
        super();
        data = new ProviderFutureResultFront(model);
        if (model != null) {
            jsonData = model.getData().orElse(null);
        }
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    public ProviderFutureResultBuilder addMessage(final String message) {
        data.setMessage(message);
        return this;
    }
    
    public ProviderFutureResultBuilder addException(final Exception exception) {
        data.setException(exception);
        return this;
    }
    
    public ProviderFutureResultBuilder addData(final JsonObject value) {
        jsonData = value;
        return this;
    }
    
    public ProviderFutureResultBuilder addEvent(final GenericEvent event) {
        data.setEvent(event);
        return this;
    }
    
    public ProviderFutureResultBuilder addChannel(final String channel) {
        data.setChannel(channel);
        return this;
    }
    
    public ProviderFutureResultBuilder addCronExpression(final String cronExpression) {
        data.setCronExpression(cronExpression);
        return this;
    }
    
    public ProviderFutureResultBuilder addAlerts(final List<AlertingResult> alerts) {
        data.setAlerts(alerts);
        return this;
    }
    
    public ProviderFutureResultBuilder addAlert(final AlertingResult alert) {
        data.addAlert(alert);
        return this;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public ProviderFutureResult build() {
        return new ProviderFutureResult(data.getMessage(), data.getException(), jsonData, data.getEvent(),
                                        data.getChannel(), data.getCronExpression(), data.getAlerts());
    }
    
    public ProviderFutureResultFront buildForFront() {
        data.setData(jsonData == null ? null : jsonData.convertToJson());
        return data;
    }
    
}
