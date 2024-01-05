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

import io.inugami.framework.interfaces.processors.ConfigHandler;

/**
 * The monitoring context require to be initialized and has a shutdown process.
 * The <strong>BootstrapMonitoringSpi</strong> allow to materialize this lifecycle.
 * 
 * @author patrick_guillerm
 * @since 8 juin 2017
 */
public interface SpiConfigurable {
    
    default String grabConfig(final String key, final ConfigHandler<String, String> configuration) {
        final String shortName = String.join(".", this.getClass().getSimpleName(), key);
        final String longName = String.join(".", this.getClass().getName(), key);
        
        String value = configuration.optionnal().grab(longName);
        if (value == null) {
            value = configuration.optionnal().grab(shortName);
        }
        return value;
    }
    
}
