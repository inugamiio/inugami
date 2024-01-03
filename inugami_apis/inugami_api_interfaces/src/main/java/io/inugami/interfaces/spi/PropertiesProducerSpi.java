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
package io.inugami.interfaces.spi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In Inugami, the root configuration is defined in XML files. One XML file per plugin with another XML
 * to define all events. It's possible to define properties in the plugin XML file. However, if your plugin
 * required to generate programmatically some properties, use the <strong>PropertiesProducerSpi</strong>.
 * It's a SPI interface, so it's required to create the file
 * <strong>/META-INF/services/io.inugami.api.spi.PropertiesProducerSpi</strong>
 * with all implementations.
 */
public interface PropertiesProducerSpi {
    Map<String, String> produce();
    
    default Map<String, String> producerFromClasses(final Class<?>... classes) {
        final Map<String, String> result = new ConcurrentHashMap<>();
        
        for (final Class<?> clazz : classes) {
            final String className = clazz.getSimpleName();
            String name = null;
            if (className.length() == 1) {
                name = className.toLowerCase();
            }
            else {
                name = className.substring(0, 1).toLowerCase() + className.substring(1);
            }
            
            result.put(name, clazz.getName());
        }
        
        return result;
    }
}
