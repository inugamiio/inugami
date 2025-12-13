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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public interface GenericMonitoringModel extends Serializable {
    String getUid();

    String getAsset();

    String getEnvironment();

    String getInstanceName();

    String getInstanceNumber();

    String getCounterType();

    String getDevice();

    String getCallType();

    String getService();

    String getSubService();

    String getValueType();

    String getTimeUnit();

    LocalDateTime getDate();

    long getTime();

    long getTimestamp();

    String getErrorCode();

    String getErrorType();

    Object getValue();

    String getPath();

    String getData();

    String getNonTemporalHash();
}
