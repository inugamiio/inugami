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
package io.inugami.logs.obfuscator.dto;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;
import lombok.*;
import org.slf4j.Marker;
import org.slf4j.event.KeyValuePair;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class LoggingEventDTO implements ILoggingEvent {
    private String              threadName;
    private Level               level;
    private String              message;
    private Object[]            argumentArray;
    private String              formattedMessage;
    private String              loggerName;
    private LoggerContextVO     loggerContextVO;
    private IThrowableProxy     throwableProxy;
    private StackTraceElement[] callerData;
    @Singular("markerList")
    private List<Marker>        markerList;
    private Map<String, String> mDCPropertyMap;
    private Map<String, String> mdc;
    private long                timeStamp;
    private int                 nanoseconds;
    private long                sequenceNumber;
    @Singular("keyValuePairs")
    private List<KeyValuePair>  keyValuePairs;

    @Override
    public boolean hasCallerData() {
        return callerData != null && callerData.length > 0;
    }

    @Override
    public void prepareForDeferredProcessing() {
    }
}
