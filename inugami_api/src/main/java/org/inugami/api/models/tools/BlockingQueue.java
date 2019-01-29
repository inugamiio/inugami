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
package org.inugami.api.models.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BlockingQueue
 * 
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
public class BlockingQueue<T> extends LinkedBlockingQueue<T> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 5779394354694487033L;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public List<T> pollAll() {
        final List<T> result = new ArrayList<>();
        
        while (!isEmpty()) {
            result.add(poll());
        }
        return result;
    }
    
}
