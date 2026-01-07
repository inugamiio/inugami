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
package io.inugami.framework.interfaces.monitoring.models;

import io.inugami.framework.interfaces.dao.Identifiable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GenericMonitoringModel
 *
 * @author patrick_guillerm
 * @since 27 déc. 2018
 */
@SuppressWarnings({"java:S1948"})
@Slf4j
@ToString(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
public class GenericMonitoringModelDTO implements GenericMonitoringModel, Identifiable<String> {

    private static final long          serialVersionUID = 4735410638475899702L;
    public static final  String        EMPTY            = "";
    public static final  String        SEPARATOR        = ":";
    public static final  String        ANY              = "ANY";
    public static final  String        NONE             = "NONE";
    public static final  String        CALL_TYPE_REST   = "REST";
    public static final  String        CALL_TYPE_JMS    = "JMS";
    private              String        uid;
    private              String        asset;
    private              String        environment;
    private              String        instanceName;
    private              String        instanceNumber;
    private              String        counterType;
    private              String        device;
    private              String        callType;
    private              String        service;
    private              String        subService;
    private              String        valueType;
    private              String        timeUnit;
    private              LocalDateTime date;
    private              long          time;
    private              String        errorCode;
    private              String        errorType;
    private              Object        value;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String        path;
    private              String        data;
    private              long          timestamp;
    private              String        groupId;
    private              String        artifactId;
    private              String        version;
    private              String        commitId;
    private              String        commitDate;

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public boolean isUidSet() {
        return uid != null;
    }

    public String getNonTemporalHash() {
        return String.join(SEPARATOR, List.of(
                orEmpty(asset),
                orEmpty(environment),
                orEmpty(instanceName),
                orEmpty(instanceNumber),
                orEmpty(counterType),
                orEmpty(device),
                orEmpty(callType),
                orEmpty(service),
                orEmpty(subService),
                orEmpty(valueType),
                orEmpty(timeUnit)
                                             ));
    }

    private String orEmpty(final String inputValue) {
        return inputValue == null ? EMPTY : inputValue;
    }

    public static class GenericMonitoringModelDTOBuilder {
        public GenericMonitoringModelDTOBuilder from(final GenericMonitoringModel other) {
            if (value == null) {
                return this;
            }
            uid            = other.getUid();
            asset          = other.getAsset();
            environment    = other.getEnvironment();
            instanceName   = other.getInstanceName();
            instanceNumber = other.getInstanceNumber();
            counterType    = other.getCounterType();
            device         = other.getDevice();
            callType       = other.getCallType();
            service        = other.getService();
            subService     = other.getSubService();
            valueType      = other.getValueType();
            timeUnit       = other.getTimeUnit();
            date           = other.getDate();
            time           = other.getTime();
            errorCode      = other.getErrorCode();
            errorType      = other.getErrorType();
            value          = other.getValueType();
            path           = other.getPath();
            data           = other.getData();
            timestamp      = other.getTimestamp();
            return this;
        }
    }
}
