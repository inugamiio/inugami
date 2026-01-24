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
package io.inugami.monitoring.springboot.partnerlog.feign;

import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.monitoring.springboot.config.InugamiMonitoringProperties;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.net.InetAddress;

import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.fromErrorCode;
import static io.inugami.framework.interfaces.exceptions.DefaultInugamiErrors.HTTP_SSRF;

/**
 * @since 2026-01-23
 */
@RequiredArgsConstructor
public class OkClientAntiSsrfInterceptor implements Interceptor {
    private final InugamiMonitoringProperties.InugamiMonitoringPropertiesFeign properties;

    @Override
    public Response intercept(Chain chain) throws IOException {
        final var request = chain.request();
        if (properties.isSsrfEnabled()) {
            var           host      = request.url().host();
            InetAddress[] addresses = InetAddress.getAllByName(host);

            for (InetAddress address : addresses) {
                if (address.isLoopbackAddress() || address.isSiteLocalAddress() || address.isLinkLocalAddress()) {
                    throw new UncheckedException(fromErrorCode(HTTP_SSRF)
                                                         .addMessageDetail("SSRF Protection: Forbidden internal IP address: {0}",
                                                                           address.getHostAddress())
                                                         .build());
                }
            }

            return chain.proceed(request);
        } else {
            return chain.proceed(request);
        }
    }
}