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
import java.util.Optional;

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
    @EqualsAndHashCode.Include
    private              String        asset;
    @EqualsAndHashCode.Include
    private              String        environment;
    @EqualsAndHashCode.Include
    private              String        instanceName;
    @EqualsAndHashCode.Include
    private              String        instanceNumber;
    @EqualsAndHashCode.Include
    private              String        counterType;
    @EqualsAndHashCode.Include
    private              String        device;
    @EqualsAndHashCode.Include
    private              String        callType;
    @EqualsAndHashCode.Include
    private              String        service;
    @EqualsAndHashCode.Include
    private              String        subService;
    @EqualsAndHashCode.Include
    private              String        valueType;
    @EqualsAndHashCode.Include
    private              String        timeUnit;
    private              LocalDateTime date;
    private              long          time;
    private              String        errorCode;
    private              String        errorType;
    private              Object        value;
    @ToString.Include
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
            if (other == null) {
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
            value          = other.getValue();
            path           = other.getPath();
            data           = other.getData();
            timestamp      = other.getTimestamp();
            artifactId     = other.getArtifactId();
            groupId        = other.getGroupId();
            version        = other.getVersion();
            commitId       = other.getCommitId();
            commitDate     = other.getCommitDate();
            date           = other.getDate();

            return this;
        }

        public GenericMonitoringModelDTOBuilder addCallType(final GenericModelCallType value) {
            callType = Optional.ofNullable(value).orElse(GenericModelCallType.REST).name();
            return this;
        }

        public GenericMonitoringModelDTOBuilder addCounterType(final GenericModelCounterType value) {
            valueType = Optional.ofNullable(value).orElse(GenericModelCounterType.HITS).getKeywork();
            return this;
        }
    }
}
