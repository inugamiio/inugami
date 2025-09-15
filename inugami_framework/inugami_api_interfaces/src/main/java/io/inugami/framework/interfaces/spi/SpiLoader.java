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
package io.inugami.framework.interfaces.spi;

import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * SpiLoader
 *
 * @author patrick_guillerm
 * @since 6 juin 2017
 */
@SuppressWarnings({"java:S1181"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpiLoader {

    // =========================================================================
    // METHODS
    // =========================================================================
    private static final String              JAVAX_BEAN_NAMED = "javax.inject.Named";
    private              SpiLoaderServiceSPI loaderService    = loadSpiLoader();
    private static final SpiLoader           INSTANCE         = new SpiLoader();

    public static SpiLoader getInstance() {
        return INSTANCE;
    }


    private SpiLoaderServiceSPI loadSpiLoader() {
        return loadSpiServiceByPriority(SpiLoaderServiceSPI.class, new JavaSpiLoaderServiceSPI());
    }


    public void reloadLoaderService(SpiLoaderServiceSPI loaderService) {
        if (loaderService != null) {
            this.loaderService = loaderService;
        }
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public synchronized <T> List<T> loadSpiService(final Class<?> type) {
        final List<T> result = loaderService.loadServices(type);
        if (result.isEmpty()) {
            Loggers.CONFIG.warn("no SPI implementation of {} found! please check your dependencies!", type.getName());
        }
        return result;
    }

    public synchronized <T> T loadSpiSingleService(final Class<?> type) {
        final List<T> services = loadSpiService(type);
        return services.isEmpty() ? null : services.get(0);
    }

    public synchronized <T> List<T> loadSpiService(final Class<?> type, final T defaultImplementation) {
        final List<T> result = loaderService.loadServices(type);

        result.sort(new PriorityComparator<>());
        if (result.isEmpty() && (defaultImplementation != null)) {
            result.add(defaultImplementation);
        }
        return result;
    }

    public synchronized <T> List<T> loadSpiServicesWithDefault(final Class<?> type, final T defaultImplementation) {
        final List<T> result = loaderService.loadServices(type);
        if (defaultImplementation != null) {
            result.add(defaultImplementation);
        }
        return result;
    }

    public synchronized <T> T loadSpiServiceByPriority(final Class<?> type, final T defaultImplementation) {
        try {
            return loadSpiServicesByPriority(type, defaultImplementation).get(0);
        } catch (Throwable e) {
            return defaultImplementation;
        }
    }


    public synchronized <T> List<T> loadSpiServicesByPriority(final Class<?> type) {
        return loadSpiServicesByPriority(type, null);
    }

    public synchronized <T> T loadSpiSingleServicesByPriority(final Class<?> type) {
        final List<T> services = loadSpiServicesByPriority(type, null);
        return services == null || services.isEmpty() ? null : services.get(0);
    }

    public synchronized <T> List<T> loadSpiServicesByPriority(final Class<?> type, final T defaultImplementation) {
        final List<T> result = loaderService.loadServices(type);
        if (defaultImplementation != null) {
            result.add(defaultImplementation);
        }
        result.sort(new PriorityComparator<>());
        return result;
    }

    public <T> T loadSpiService(final String name, final Class<?> type) {
        return loadSpiService(name, type, true);

    }

    @SuppressWarnings({"java:S1871"})
    public synchronized <T> T loadSpiService(final String name, final Class<?> type, final boolean mandatory) {
        T             result   = null;
        final List<T> services = loadSpiService(type);

        for (final T service : services) {
            final Class<?> serviceClass = service.getClass();

            if (serviceClass.getName().equals(name)) {
                result = service;
            } else if ((service instanceof NamedSpi) && name.equals(((NamedSpi) service).getName())) {
                result = service;
            } else if (classHasNamedAnnotation(serviceClass)) {
                result = service;
            } else if (serviceClass.getSimpleName().equalsIgnoreCase(name)) {
                result = service;
            }

            if (result != null) {
                break;
            }
        }
        if (mandatory) {
            Objects.requireNonNull(result, String.format("SPI implementation found %s is mandatory!", name));
        }
        return result;
    }

    private boolean classHasNamedAnnotation(final Class<?> serviceClass) {
        final Annotation namedAnnotation = searchAnnotation(serviceClass.getDeclaredAnnotations(), JAVAX_BEAN_NAMED);
        return namedAnnotation != null;
    }

    public static Annotation searchAnnotation(final Annotation[] annotations, final String... names) {
        final Annotation result = null;
        if (annotations != null) {
            for (int i = annotations.length - 1; i >= 0; i--) {
                final String className = annotations[i].annotationType().getCanonicalName();

                for (final String name : names) {
                    if (name.equals(className)) {
                        return annotations[i];
                    }
                }
            }
        }
        return result;
    }

    public static Method searchMethod(final Annotation annotation, final String method) {
        if (annotation == null || method == null) {
            return null;
        }
        Method           result      = null;
        final Class<?>[] paramsTypes = {};

        try {
            result = annotation.annotationType().getDeclaredMethod(method, paramsTypes);
        } catch (final NoSuchMethodException | SecurityException e) {
            Loggers.DEBUG.debug(e.getMessage(), e);
        }

        return result;
    }

    public static <T> T invoke(final Method method, final Object object, final Object... params) {
        T result = null;
        if ((method != null) && (object != null)) {
            method.setAccessible(true);
            try {
                result = (T) method.invoke(object, params);
            } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }

        return result;
    }
}
