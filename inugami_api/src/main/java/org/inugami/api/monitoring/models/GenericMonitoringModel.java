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
package org.inugami.api.monitoring.models;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.inugami.api.dao.Identifiable;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.mapping.DateTransformer;
import org.inugami.api.models.data.JsonObject;
import org.inugami.api.models.data.graphite.number.GraphiteNumber;
import org.inugami.api.models.data.graphite.number.GraphiteNumberTransformer;

import flexjson.JSON;

/**
 * GenericMonitoringModel
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2018
 */
public class GenericMonitoringModel implements JsonObject, Identifiable<String> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long    serialVersionUID = 4735410638475899702L;
    
    private final String         uid;
    
    private final String         asset;
    
    private final String         environment;
    
    private final String         instanceName;
    
    private final String         instanceNumber;
    
    private final String         counterType;
    
    private final String         device;
    
    private final String         callType;
    
    private final String         service;
    
    private final String         subService;
    
    private final String         valueType;
    
    private final String         timeUnit;
    
    @JSON(transformer = DateTransformer.class)
    private final Date           date;
    
    private final long           time;
    
    private final String         errorCode;
    
    private final String         errorType;
    
    @JSON(transformer = GraphiteNumberTransformer.class)
    private final GraphiteNumber value;
    
    private final String         path;
    
    private final String         data;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    /* package */ GenericMonitoringModel(final String asset, final String environment, final String instanceName,
                                         final String instanceNumber, final String counterType, final String device,
                                         final String callType, final String service, final String subService,
                                         final String valueType, final String timeUnit, final Date date,
                                         final long timestamp, final String errorCode, final String errorType,
                                         final GraphiteNumber value, final String data) {
        super();
        this.asset = asset;
        this.environment = environment;
        this.instanceName = instanceName;
        this.instanceNumber = instanceNumber;
        this.counterType = counterType;
        this.device = device;
        this.callType = callType;
        this.service = service;
        this.subService = subService;
        this.valueType = valueType;
        this.timeUnit = timeUnit;
        this.date = date;
        this.time = timestamp;
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.value = value;
        this.data = data;
        //@formatter:off
        this.path = String.join(".", asset,
                                     environment,
                                     instanceName,
                                     instanceNumber,
                                     counterType,
                                     callType,
                                     service,
                                     subService,
                                     device,
                                     errorCode,
                                     errorType,
                                     valueType,
                                     timeUnit,
                                     String.valueOf(timestamp)
                                );
        //@formatter:on
        uid = hashPath(path);
    }
    
    private static String hashPath(final String value) {
        String result = null;
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-512");
            final byte[] bytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
            result = Hex.encodeHexString(bytes);
        }
        catch (final NoSuchAlgorithmException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }
        return result;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((path == null) ? 0 : path.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof GenericMonitoringModel)) {
            final GenericMonitoringModel other = (GenericMonitoringModel) obj;
            result = path.equals(other.getPath());
        }
        
        return result;
    }
    
    public String getNonTemporalHash() {
        //@formatter:off
        return String.join(".", asset,
                           environment,
                           instanceName,
                           instanceNumber,
                           counterType,
                           callType,
                           service,
                           subService,
                           device,
                           errorCode,
                           errorType,
                           valueType,
                           timeUnit
                      );
          //@formatter:on
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getUid() {
        return uid;
    }
    
    @Override
    public void setUid(final String uid) {
        // nothing to do
    }
    
    @Override
    public boolean isUidSet() {
        return true;
    }
    
    public String getAsset() {
        return asset;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public String getInstanceName() {
        return instanceName;
    }
    
    public String getInstanceNumber() {
        return instanceNumber;
    }
    
    public String getCounterType() {
        return counterType;
    }
    
    public String getDevice() {
        return device;
    }
    
    public String getCallType() {
        return callType;
    }
    
    public String getService() {
        return service;
    }
    
    public String getSubService() {
        return subService;
    }
    
    public String getValueType() {
        return valueType;
    }
    
    public String getTimeUnit() {
        return timeUnit;
    }
    
    public Date getDate() {
        return date;
    }
    
    public long getTime() {
        return time;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorType() {
        return errorType;
    }
    
    public GraphiteNumber getValue() {
        return value;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getData() {
        return data;
    }
    
}
