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
package io.inugami.core.services.senders;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Sender
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
public interface Sender<T> {
    void send(T data) throws SenderException;
    
    default Map<String, String> getConfiguration() {
        return new LinkedHashMap<>();
    }
}
