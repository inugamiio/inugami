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
package org.inugami.commons.spi;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.spi.NamedSpi;
import org.inugami.api.tools.AnnotationTools;

/**
 * SpiLoader
 * 
 * @author patrick_guillerm
 * @since 6 juin 2017
 */
public class SpiLoader {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    private static final String JAVAX_BEAN_NAMED = "javax.inject.Named";
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public synchronized <T> List<T> loadSpiService(final Class<?> type) {
        final List<T> result = new ArrayList<>();
        final ServiceLoader<T> servicesLoaders = (ServiceLoader<T>) ServiceLoader.load(type);
        servicesLoaders.forEach(result::add);
        
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
        final List<T> result = new ArrayList<>();
        final ServiceLoader<T> servicesLoaders = (ServiceLoader<T>) ServiceLoader.load(type);
        servicesLoaders.forEach(result::add);
        
        result.sort(new PriorityComparator<T>());
        if (result.isEmpty() && (defaultImplementation != null)) {
            result.add(defaultImplementation);
        }
        return result;
    }
    
    public synchronized <T> List<T> loadSpiServicesWithDefault(final Class<?> type, final T defaultImplementation) {
        final List<T> result = new ArrayList<>();
        final ServiceLoader<T> servicesLoaders = (ServiceLoader<T>) ServiceLoader.load(type);
        servicesLoaders.forEach(result::add);
        if (defaultImplementation != null) {
            result.add(defaultImplementation);
        }
        return result;
    }
    
    public synchronized <T> T loadSpiServiceByPriority(final Class<?> type, final T defaultImplementation) {
        return loadSpiServicesByPriority(type, defaultImplementation).get(0);
    }
    
    public synchronized <T> List<T> loadSpiServicesByPriority(final Class<?> type) {
        return loadSpiServicesByPriority(type, null);
    }
    
    public synchronized <T> List<T> loadSpiServicesByPriority(final Class<?> type, final T defaultImplementation) {
        final List<T> result = new ArrayList<>();
        final ServiceLoader<T> servicesLoaders = (ServiceLoader<T>) ServiceLoader.load(type);
        servicesLoaders.forEach(result::add);
        if (defaultImplementation != null) {
            result.add(defaultImplementation);
        }
        result.sort(new PriorityComparator<>());
        return result;
    }
    
    public <T> T loadSpiService(final String name, final Class<?> type) {
        return loadSpiService(name, type, true);
        
    }
    
    public synchronized <T> T loadSpiService(final String name, final Class<?> type, final boolean mandatory) {
        T result = null;
        final List<T> services = loadSpiService(type);
        
        for (final T service : services) {
            final Class<?> serviceClass = service.getClass();
            
            if (serviceClass.getName().equals(name)) {
                result = service;
            }
            else if ((service instanceof NamedSpi) && name.equals(((NamedSpi) service).getName())) {
                result = service;
            }
            else if (classHasNamedAnnotation(serviceClass)) {
                result = service;
            }
            else if (serviceClass.getSimpleName().equalsIgnoreCase(name)) {
                result = service;
            }
            
            if (result != null) {
                break;
            }
        }
        if (mandatory) {
            Asserts.notNull(String.format("SPI implementation found %s is mandatory!", name), result);
        }
        return result;
    }
    
    private boolean classHasNamedAnnotation(final Class<?> serviceClass) {
        final String result = null;
        
        final Annotation namedAnnotation = AnnotationTools.searchAnnotation(serviceClass.getDeclaredAnnotations(),
                                                                            JAVAX_BEAN_NAMED);
        return namedAnnotation != null;
    }
    
}
