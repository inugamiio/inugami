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
package io.inugami.api.metrics;

import java.util.List;

import io.inugami.interfaces.models.basic.JsonObject;
import io.inugami.interfaces.processors.ConfigHandler;
import io.inugami.interfaces.spi.SpiConfigurable;

/**
 * MetricsProvider
 * 
 * @author patrick_guillerm
 * @since 6 juin 2017
 */
public interface MetricsMapper extends SpiConfigurable {
    
    boolean accept(Class<? extends JsonObject> clazz);
    
    List<MetricsData> process(JsonObject data, ConfigHandler<String, String> config);
    
}
