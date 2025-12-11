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
package io.inugami.framework.commons.spring.feign;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import io.inugami.framework.interfaces.rest.PartnerConfigurationDTO;
import lombok.Builder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

@Builder
public class FeignBuilder {
    public static final String                   URL_SEPARATOR = "/";
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private             Client                   client;
    private             Encoder                  encoder;
    private             Decoder                  decoder;
    private             ErrorDecoder             errorDecoder;
    private             PartnerConfigurationDTO  partnerConfiguration;
    private             List<RequestInterceptor> interceptors;
    private             Contract                 contract;

    public FeignBuilder init() {
        Objects.requireNonNull(partnerConfiguration);
        Objects.requireNonNull(partnerConfiguration.getBaseUrl());
        return this;
    }

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public <T> T buildClient(Class<? extends T> clientClass) {
        return Feign.builder()
                    .client(client)
                    .decoder(decoder)
                    .contract(contract)
                    .requestInterceptors(interceptors)
                    .errorDecoder(errorDecoder)
                    .target(clientClass, buildFullPath(clientClass));
    }

    private <T> String buildFullPath(final Class<? extends T> clientClass) {
        final RequestMapping annotation = clientClass.getDeclaredAnnotation(RequestMapping.class);
        if (annotation == null || annotation.path().length == 0) {
            return partnerConfiguration.getBaseUrl();
        }
        final String        domainPath = annotation.path()[0];
        final StringBuilder fullUrl    = new StringBuilder(partnerConfiguration.getBaseUrl());

        if (!partnerConfiguration.getBaseUrl().endsWith(URL_SEPARATOR)) {
            fullUrl.append(URL_SEPARATOR);
        }
        if (domainPath.startsWith(URL_SEPARATOR)) {
            fullUrl.append(domainPath.substring(1));
        } else {
            fullUrl.append(domainPath);
        }
        return fullUrl.toString();
    }
}
