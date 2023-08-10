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
package io.inugami.core.context.config;

import java.util.Map;

import io.inugami.api.spi.PropertiesProducerSpi;
import io.inugami.core.alertings.DefaultAlertingProvider;

/**
 * SpecificConfigProducer
 * 
 * @author patrickguillerm
 * @since 3 sept. 2018
 */
public class SpecificConfigProducer implements PropertiesProducerSpi {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Map<String, String> produce() {
        //@formatter:off
        return producerFromClasses(DefaultAlertingProvider.class);
        //@formatter:on
    }
    
}
