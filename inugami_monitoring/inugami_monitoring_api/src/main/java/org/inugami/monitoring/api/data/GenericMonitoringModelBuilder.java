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
package org.inugami.monitoring.api.data;

import java.util.Date;

import org.inugami.api.models.data.graphite.number.FloatNumber;
import org.inugami.api.models.data.graphite.number.GraphiteNumber;
import org.inugami.api.models.data.graphite.number.LongNumber;

/**
 * GenericMonitoringModelBuilder
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2018
 */
public class GenericMonitoringModelBuilder {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String         asset;
    
    private String         environment;
    
    private String         instanceName;
    
    private String         instanceNumber;
    
    private String         counterType;
    
    private String         device;
    
    private String         callType;
    
    private String         service;
    
    private String         subService;
    
    private String         valueType;
    
    private String         timeUnit;
    
    private Date           date;
    
    private long           timestamp;
    
    private String         errorCode;
    
    private String         errorType;
    
    private String         data;
    
    private GraphiteNumber value;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public GenericMonitoringModelBuilder() {
    }
    
    public GenericMonitoringModelBuilder(final GenericMonitoringModel toClone) {
        this(toClone, toClone == null ? null : toClone.getValue());
    }
    
    public GenericMonitoringModelBuilder(final GenericMonitoringModel toClone, final GraphiteNumber value) {
        if (toClone != null) {
            this.asset = toClone.getAsset();
            this.environment = toClone.getEnvironment();
            this.instanceName = toClone.getInstanceName();
            this.instanceNumber = toClone.getInstanceNumber();
            this.counterType = toClone.getCounterType();
            this.device = toClone.getDevice();
            this.callType = toClone.getCallType();
            this.service = toClone.getService();
            this.subService = toClone.getSubService();
            this.valueType = toClone.getValueType();
            this.timeUnit = toClone.getTimeUnit();
            this.date = toClone.getDate();
            this.timestamp = toClone.getTime();
            this.errorCode = toClone.getErrorCode();
            this.errorType = toClone.getErrorType();
            this.data = toClone.getData();
        }
        
        this.value = value;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public GenericMonitoringModel build() {
        return new GenericMonitoringModel(asset, environment, instanceName, instanceNumber, counterType, device,
                                          callType, service, subService, valueType, timeUnit, date, timestamp,
                                          errorCode, errorType, value, data);
    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("GenericMonitoringModelBuilder [");
        builder.append("asset=");
        builder.append(asset);
        builder.append(", environment=");
        builder.append(environment);
        builder.append(", instanceName=");
        builder.append(instanceName);
        builder.append(", instanceNumber=");
        builder.append(instanceNumber);
        builder.append(", counterType=");
        builder.append(counterType);
        builder.append(", device=");
        builder.append(device);
        builder.append(", callType=");
        builder.append(callType);
        builder.append(", service=");
        builder.append(service);
        builder.append(", subService=");
        builder.append(subService);
        builder.append(", valueType=");
        builder.append(valueType);
        builder.append(", timeUnit=");
        builder.append(timeUnit);
        builder.append(", date=");
        builder.append(date);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append(", errorCode=");
        builder.append(errorCode);
        builder.append(", errorType=");
        builder.append(errorType);
        builder.append(", value=");
        builder.append(value);
        builder.append(", data=");
        builder.append(data);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getAsset() {
        return asset;
    }
    
    public GenericMonitoringModelBuilder setAsset(final String asset) {
        this.asset = asset;
        return this;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public GenericMonitoringModelBuilder setEnvironment(final String environment) {
        this.environment = environment;
        return this;
    }
    
    public String getInstanceName() {
        return instanceName;
    }
    
    public GenericMonitoringModelBuilder setInstanceName(final String instanceName) {
        this.instanceName = instanceName;
        return this;
    }
    
    public String getInstanceNumber() {
        return instanceNumber;
    }
    
    public GenericMonitoringModelBuilder setInstanceNumber(final String instanceNumber) {
        this.instanceNumber = instanceNumber;
        return this;
    }
    
    public String getCounterType() {
        return counterType;
    }
    
    public GenericMonitoringModelBuilder setCounterType(final String counterType) {
        this.counterType = counterType;
        return this;
    }
    
    public String getDevice() {
        return device;
    }
    
    public GenericMonitoringModelBuilder setDevice(final String device) {
        this.device = device;
        return this;
    }
    
    public String getCallType() {
        return callType;
    }
    
    public GenericMonitoringModelBuilder setCallType(final String callType) {
        this.callType = callType;
        return this;
    }
    
    public String getService() {
        return service;
    }
    
    public GenericMonitoringModelBuilder setService(final String service) {
        this.service = service;
        return this;
    }
    
    public String getSubService() {
        return subService;
    }
    
    public GenericMonitoringModelBuilder setSubService(final String subService) {
        this.subService = subService;
        return this;
    }
    
    public String getValueType() {
        return valueType;
    }
    
    public GenericMonitoringModelBuilder setValueType(final String valueType) {
        this.valueType = valueType;
        return this;
    }
    
    public String getTimeUnit() {
        return timeUnit;
    }
    
    public GenericMonitoringModelBuilder setTimeUnit(final String timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }
    
    public Date getDate() {
        return date;
    }
    
    public GenericMonitoringModelBuilder setDate(final Date date) {
        this.date = date;
        if (date != null) {
            this.timestamp = date.getTime();
        }
        
        return this;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public GenericMonitoringModelBuilder setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
        this.date = new Date(timestamp);
        return this;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public GenericMonitoringModelBuilder setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
        return this;
    }
    
    public String getErrorType() {
        return errorType;
    }
    
    public GenericMonitoringModelBuilder setErrorType(final String errorType) {
        this.errorType = errorType;
        return this;
    }
    
    public GraphiteNumber getValue() {
        return value;
    }
    
    public GenericMonitoringModelBuilder setValue(final GraphiteNumber value) {
        this.value = value;
        return this;
    }
    
    public GenericMonitoringModelBuilder setValue(final Long value) {
        this.value = value == null ? null : new LongNumber(value);
        return this;
    }
    
    public GenericMonitoringModelBuilder setValue(final long value) {
        this.value = new LongNumber(value);
        return this;
    }
    
    public GenericMonitoringModelBuilder setValue(final Double value) {
        this.value = value == null ? null : new FloatNumber(value);
        return this;
    }
    
    public GenericMonitoringModelBuilder setValue(final double value) {
        this.value = new FloatNumber(value);
        return this;
    }
    
    public GenericMonitoringModelBuilder setData(final String value) {
        this.data = value;
        return this;
    }
}
