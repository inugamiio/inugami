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
package org.inugami.core.providers.kibana;

import org.inugami.api.processors.ConfigHandler;
import org.inugami.core.providers.kibana.models.ResultData;

/**
 * KibanaHandler
 * 
 * @author patrick_guillerm
 * @since 20 sept. 2017
 */
@FunctionalInterface
public interface KibanaHandler {
    
    void onData(final ResultData data, final ConfigHandler<String, String> config) throws KibanaExcpetion;
}
