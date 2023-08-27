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
package io.inugami.api.monitoring.models;

import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.graphite.number.GraphiteNumber;

import java.util.Date;

public interface GenericMonitoringModel extends JsonObject {
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

    Date getDate();

    long getTime();

    long getTimestamp();

    String getErrorCode();

    String getErrorType();

    GraphiteNumber getValue();

    String getPath();

    String getData();

    String getNonTemporalHash();
}
