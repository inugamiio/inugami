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
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

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
public final class GenericMonitoringModelDto implements Identifiable<String> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 4735410638475899702L;

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

}
