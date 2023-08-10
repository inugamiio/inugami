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

import java.util.ArrayList;
import java.util.List;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.models.events.GenericEvent;

/**
 * ProviderFutureResultFront
 * 
 * @author patrick_guillerm
 * @since 30 janv. 2018
 */
public class ProviderFutureResultFront {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String               message;
    
    private String               channel;
    
    private Exception            exception;
    
    private GenericEvent         event;
    
    private String               cronExpression;
    
    private String               data;
    
    private List<AlertingResult> alerts = new ArrayList<>();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderFutureResultFront() {
        super();
    }
    
    public ProviderFutureResultFront(final ProviderFutureResult model) {
        super();
        if (model != null) {
            channel = model.getChannel();
            event = model.getEvent();
            
            message = model.getMessage().orElse(null);
            exception = model.getException().orElse(null);
        }
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public String getChannel() {
        return channel;
    }
    
    public void setChannel(final String channel) {
        this.channel = channel;
    }
    
    public Exception getException() {
        return exception;
    }
    
    public void setException(final Exception exception) {
        this.exception = exception;
    }
    
    public GenericEvent getEvent() {
        return event;
    }
    
    public void setEvent(final GenericEvent event) {
        this.event = event;
    }
    
    public String getCronExpression() {
        return cronExpression;
    }
    
    public void setCronExpression(final String cronExpression) {
        this.cronExpression = cronExpression;
    }
    
    public String getData() {
        return data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
    
    public List<AlertingResult> getAlerts() {
        return alerts;
    }
    
    public void setAlerts(final List<AlertingResult> alerts) {
        this.alerts = alerts;
    }
    
    public void addAlert(final AlertingResult alert) {
        if (alerts == null) {
            alerts = new ArrayList<>();
        }
        alerts.add(alert);
    }
    
}
