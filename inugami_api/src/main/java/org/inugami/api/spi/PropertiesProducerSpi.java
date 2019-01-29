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
package org.inugami.api.spi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PropertiesInjectorSpi <label>SPI
 * file:</label><strong>org.inugami.api.spi.PropertiesProducerSpi</strong>
 * 
 * @author patrickguillerm
 * @since 3 sept. 2018
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
