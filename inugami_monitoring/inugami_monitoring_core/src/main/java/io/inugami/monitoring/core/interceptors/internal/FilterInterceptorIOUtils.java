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
package io.inugami.monitoring.core.interceptors.internal;

import io.inugami.framework.api.tools.RunSafeUtils;
import io.inugami.framework.interfaces.monitoring.data.ResponseData;
import io.inugami.framework.interfaces.tools.CalendarTools;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import io.inugami.monitoring.core.interceptors.ResponseWrapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@UtilityClass
public class FilterInterceptorIOUtils {
    private static final int KILO = 1024;

    public static byte[] readInput(@Nullable final ServletInputStream inputStream) {
        if (inputStream == null) {
            return new byte[]{};
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream(64 * KILO);

        final int    bufferSize = 16 * KILO;
        final byte[] buffer     = new byte[bufferSize];

        int bytesLeft;
        try {
            while (-1 != (bytesLeft = inputStream.read(buffer))) {
                out.write(buffer, 0, bytesLeft);
            }
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            RunSafeUtils.runSafeVoid(inputStream::close, log);
        }

        return out.toByteArray();
    }

    public static @NonNull ResponseData convertToResponseData(@NonNull final HttpServletRequest httpRequest,
                                                              @NonNull final ResponseWrapper httpResponse,
                                                              final long duration) {
        final String content = ObfuscatorTools.applyObfuscators(httpResponse.getData());

        final Map<String, String> hearders = new LinkedHashMap<>();

        final List<String> headerNames = new ArrayList<>(Optional.ofNullable(httpResponse.getHeaderNames())
                                                                 .orElse(List.of()));
        Collections.sort(headerNames);
        for (final String key : headerNames) {
            hearders.put(key, httpResponse.getHeader(key));
        }
        return ResponseData.builder()
                           .httpRequest(httpRequest)
                           .httpResponse(httpResponse)
                           .code(httpResponse.getStatus())
                           .content(content)
                           .contentType(httpResponse.getContentType())
                           .duration(duration)
                           .datetime(CalendarTools.buildCalendar().getTimeInMillis())
                           .hearder(hearders)
                           .build();
    }

}
