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
import io.inugami.framework.interfaces.exceptions.ExceptionResolver;
import io.inugami.framework.interfaces.monitoring.*;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.spi.SpiLoader;
import io.inugami.framework.interfaces.spi.SpiLoaderServiceSPI;
import io.inugami.monitoring.core.context.MonitoringBootstrapService;
import io.inugami.monitoring.core.context.MonitoringContext;
import io.inugami.monitoring.core.interceptors.DefaultFilterInterceptorCachePurgeStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@Slf4j
public final class FilterInterceptorContext {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private SpiLoaderServiceSPI                       spiLoaderServiceSPI;
    private Boolean                                   initialized;
    private List<JavaRestMethodResolver>              javaRestMethodResolvers;
    private List<JavaRestMethodTracker>               javaRestMethodTrackers;
    private List<Interceptable>                       interceptableResolvers;
    private List<ExceptionResolver>                   exceptionResolvers;
    private List<ResponseListener>                    responseListeners;
    private List<MonitoringFilterInterceptor>         monitoringFilterInterceptors;
    private List<FilterInterceptorCachePurgeStrategy> cachePurgeStrategies;

    // =================================================================================================================
    // INIT
    // =================================================================================================================

    public static class FilterInterceptorContextBuilder {
        public FilterInterceptorContextBuilder initSpi() {
            SpiLoader.getInstance();
            javaRestMethodResolvers = resolveSpi(JavaRestMethodResolver.class);
            javaRestMethodTrackers  = resolveSpi(JavaRestMethodTracker.class);
            interceptableResolvers  = resolveSpi(Interceptable.class);
            responseListeners       = resolveSpi(ResponseListener.class);
            exceptionResolvers      = resolveSpi(ExceptionResolver.class);
            cachePurgeStrategies    = resolveSpi(FilterInterceptorCachePurgeStrategy.class,
                                                 new DefaultFilterInterceptorCachePurgeStrategy());

            monitoringFilterInterceptors = new ArrayList<>();
            final var monitoringInterceptors = Optional.ofNullable(MonitoringBootstrapService.getContext())
                                                       .map(MonitoringContext::getInterceptors)
                                                       .orElse(List.of());
            for (final MonitoringFilterInterceptor interceptor : monitoringInterceptors) {
                monitoringFilterInterceptors.add(interceptor);
            }
            resolveSpi(MonitoringFilterInterceptor.class).stream()
                                                         .filter(i -> isNotContains(i, monitoringInterceptors))
                                                         .forEach(monitoringFilterInterceptors::add);
            initialized = true;
            return this;
        }

        private <U, T extends U> List<T> resolveSpi(final Class<U> spiClass,
                                                    final T... defaultValues) {
            if (spiLoaderServiceSPI == null) {
                return new ArrayList<>();
            }
            return RunSafeUtils.runSafe(() -> {
                final List<T> result    = new ArrayList<>();
                List<U>       instances = spiLoaderServiceSPI.loadSpiServicesByPriority(spiClass);
                for (U instance : Optional.ofNullable(instances).orElse(List.of())) {
                    result.add((T) instance);
                }
                for (T instance : defaultValues) {
                    result.add(instance);
                }
                return result;
            }, log);
        }

        private boolean isNotContains(final MonitoringFilterInterceptor interceptor,
                                      final List<MonitoringFilterInterceptor> monitoringFilterInterceptors) {
            final boolean result = true;
            if (monitoringFilterInterceptors == null) {
                return result;
            }
            for (final MonitoringFilterInterceptor monitoringInterceptor : monitoringFilterInterceptors) {
                if (interceptor.getClass() == monitoringInterceptor.getClass()) {
                    return false;
                }
            }
            return result;
        }
    }

    public List<JavaRestMethodResolver> getJavaRestMethodResolvers() {
        return Optional.ofNullable(javaRestMethodResolvers).orElse(List.of());
    }

    public List<JavaRestMethodTracker> getJavaRestMethodTrackers() {
        return Optional.ofNullable(javaRestMethodTrackers).orElse(List.of());
    }

    public List<Interceptable> getInterceptableResolvers() {
        return Optional.ofNullable(interceptableResolvers).orElse(List.of());
    }

    public List<ExceptionResolver> getExceptionResolvers() {
        return Optional.ofNullable(exceptionResolvers).orElse(List.of());
    }

    public List<ResponseListener> getResponseListeners() {
        return Optional.ofNullable(responseListeners).orElse(List.of());
    }

    public List<MonitoringFilterInterceptor> getMonitoringFilterInterceptors() {
        return Optional.ofNullable(monitoringFilterInterceptors).orElse(List.of());
    }

    public List<FilterInterceptorCachePurgeStrategy> getCachePurgeStrategies() {
        return Optional.ofNullable(cachePurgeStrategies).orElse(List.of());
    }
}
