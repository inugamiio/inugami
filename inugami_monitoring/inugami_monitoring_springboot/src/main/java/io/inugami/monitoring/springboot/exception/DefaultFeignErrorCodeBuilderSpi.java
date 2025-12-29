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
package io.inugami.monitoring.springboot.exception;

import feign.FeignException;
import io.inugami.framework.interfaces.monitoring.spring.feign.FeignErrorCodeBuilderSpi;
import io.inugami.framework.interfaces.spi.SpiPriority;

import java.util.Optional;

@SpiPriority(Integer.MIN_VALUE)
public class DefaultFeignErrorCodeBuilderSpi implements FeignErrorCodeBuilderSpi {

    public static final String UNDEFINED = "undefined";
    private static final String SEPARATOR = "-";

    @Override
    public boolean accept(final String partner) {
        return true;
    }


    @Override
    public String buildErrorCode(final String partner, final Exception exception) {
        return Optional.ofNullable(partner)
                       .map(p -> partner + SEPARATOR + getStatus(exception))
                       .orElse(UNDEFINED);
    }

    private static int getStatus(final Exception exception) {
        if (exception instanceof FeignException feignError) {
            return feignError.status();
        }
        return 500;
    }
}
