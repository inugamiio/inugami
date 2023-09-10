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

import java.util.Set;

public interface IHeaders {

    String getCorrelationId();

    String getRequestId();

    String getTraceId();

    String getConversationId();

    String getToken();

    String getDeviceIdentifier();

    String getDeviceType();

    String getDeviceSystem();

    String getDeviceClass();

    String getDeviceVersion();

    String getDeviceOsVersion();

    String getDeviceNetworkType();

    String getDeviceNetworkSpeedDown();

    String getDeviceNetworkSpeedUp();

    String getDeviceNetworkSpeedLatency();

    String getDeviceIp();

    String getUserAgent();

    String getLanguage();

    String getCountry();

    String getWarning();

    String getErrorCode();

    String getErrorException();

    String getErrorMessage();

    String getErrorMessageDetail();

    String getFrontVersion();

    String getCallFrom();

    Set<String> getSpecificHeaders();
}
