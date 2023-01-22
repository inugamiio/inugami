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
package io.inugami.logs.obfuscator.api;

import ch.qos.logback.classic.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class LogEventDto {
    private final long                      timestamp;
    private final String                    threadName;
    private final String                    loggerName;
    private final LogLevel                  level;
    private final String                    message;
    private final long                      logContextBirthTime;
    private final String                    logContextName;
    private final Map<String, String>       logContextProperties;
    private final StackTraceElement[]       stacktrace;
    private final Map<String, Serializable> mdc;
}
