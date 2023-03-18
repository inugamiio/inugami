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
package io.inugami.logs.obfuscator.encoder;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;
import io.inugami.api.spi.SpiLoader;
import io.inugami.logs.obfuscator.api.LogEventDto;
import io.inugami.logs.obfuscator.api.ObfuscatorSpi;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObfuscatorEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static List<ObfuscatorSpi> OBFUSCATORS = SpiLoader.getInstance().loadSpiServicesByPriority(
            ObfuscatorSpi.class);
    private final static byte[]              EMPTY       = "".getBytes(StandardCharsets.UTF_8);

    // =========================================================================
    // API
    // =========================================================================

    @Override
    public byte[] encode(final ILoggingEvent event) {
        LogEventDto currentEvent = buildEvent(event);
        String      message      = encodeMessage(currentEvent);
        return message == null ? EMPTY : message.getBytes(StandardCharsets.UTF_8);
    }


    // =========================================================================
    // OVERRIDES
    // =========================================================================

    private LogEventDto buildEvent(final ILoggingEvent event) {
        final LogEventDto.LogEventDtoBuilder builder = LogEventDto.builder();

        builder.timestamp(event.getTimeStamp())
               .threadName(event.getThreadName())
               .loggerName(event.getLoggerName())
               .message(event.getFormattedMessage())
               .stacktrace(event.getCallerData())
               .mdc(cloneMap(event.getMDCPropertyMap(), event.getMdc()));


        return builder.build();
    }

    private Map<String, Serializable> cloneMap(final Map<String, String> mdcPropertyMap,
                                               final Map<String, String> mdc) {
        final Map<String, Serializable> result = new LinkedHashMap<>();

        if (mdc != null) {
            for (Map.Entry<String, String> entry : mdc.entrySet()) {
                if (entry.getValue() != null) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (mdcPropertyMap != null) {
            for (Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
                if (entry.getValue() != null) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return result;
    }

    private String encodeMessage(final LogEventDto event) {
        final LogEventDto.LogEventDtoBuilder builder = event.toBuilder();

        String result = event.getMessage();
        for (ObfuscatorSpi obfuscator : OBFUSCATORS) {
            LogEventDto currentEvent = builder.build();
            if (obfuscator.isEnabled() && obfuscator.accept(currentEvent)) {
                result = obfuscator.obfuscate(currentEvent);

                if (result != null) {
                    builder.message(result);
                    if (obfuscator.shouldStop(builder.build())) {
                        break;
                    }
                }
            }
        }

        return result;
    }

}
