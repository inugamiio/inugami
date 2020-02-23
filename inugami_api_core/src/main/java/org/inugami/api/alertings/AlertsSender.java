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
package org.inugami.api.alertings;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.inugami.api.tools.AnnotationTools;

/**
 * AlertsSender
 * 
 * @author patrickguillerm
 * @since 20 janv. 2018
 */
public interface AlertsSender {
    
    void sendNewAlert(final AlertingResult alert, final List<String> channels) throws AlertsSenderException;
    
    default void send(final AlertingResult alert, final List<String> channels) throws AlertsSenderException {
        // nothing to do
    }
    
    void delete(final List<String> uids, final List<String> channels) throws AlertsSenderException;
    
    boolean enable();
    
    default String getName() {
        return AnnotationTools.resolveNamed(this);
    }
    
    default Map<String, String> getConfiguration() {
        return new LinkedHashMap<>();
    }
}
