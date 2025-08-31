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
package io.inugami.framework.interfaces.metrics.dto;

import io.inugami.framework.interfaces.dao.Identifiable;
import io.inugami.framework.interfaces.models.number.GraphiteNumber;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.NullCipher;
import java.util.Date;
import java.util.List;

/**
 * GenericMonitoringModel
 *
 * @author patrick_guillerm
 * @since 27 déc. 2018
 */
@Slf4j
@ToString(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
public final class GenericMonitoringModelDto implements GenericMonitoringModel, Identifiable<String> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 4735410638475899702L;
    public static final String EMPTY = "";
    public static final String SEPARATOR = ":";

    private String uid;

    private String asset;

    private String environment;

    private String instanceName;

    private String instanceNumber;

    private String counterType;

    private String device;

    private String callType;

    private String service;

    private String subService;

    private String valueType;

    private String timeUnit;

    private Date date;

    private long time;

    private String errorCode;

    private String errorType;

    private GraphiteNumber value;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String path;

    private String data;

    private long timestamp;

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public boolean isUidSet() {
        return uid != null;
    }

    @Override
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
        return inputValue == null ? EMPTY :inputValue;
    }

    @Override
    public Object cloneObj() {
        return toBuilder().build();
    }

    public static class GenericMonitoringModelDtoBuilder {

        public GenericMonitoringModelDtoBuilder init(final GenericMonitoringModel inputValue) {
            if (inputValue != null) {
                uid = inputValue.getUid();
                asset = inputValue.getAsset();
                environment = inputValue.getEnvironment();
                instanceName = inputValue.getInstanceName();
                instanceNumber = inputValue.getInstanceNumber();
                counterType = inputValue.getCounterType();
                device = inputValue.getDevice();
                callType = inputValue.getCallType();
                service = inputValue.getSubService();
                subService = inputValue.getSubService();
                valueType = inputValue.getValueType();
                timeUnit = inputValue.getTimeUnit();
                date = inputValue.getDate();
                time = inputValue.getTime();
                errorCode = inputValue.getErrorCode();
                errorType = inputValue.getErrorType();
                value = inputValue.getValue();
                path = inputValue.getPath();
                data = inputValue.getData();
                timestamp = inputValue.getTimestamp();
            }

            return this;
        }
    }
}
