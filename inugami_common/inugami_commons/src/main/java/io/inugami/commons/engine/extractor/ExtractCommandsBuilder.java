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
package io.inugami.commons.engine.extractor;

import java.util.ArrayList;
import java.util.List;

/**
 * ExtractCommandsBuilder
 * 
 * @author patrick_guillerm
 * @since 22 mai 2018
 */
public final class ExtractCommandsBuilder {
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private ExtractCommandsBuilder() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static List<ExtractCommand> build(final String path) {
        List<ExtractCommand> result = null;
        if (path != null) {
            result = new ArrayList<>();
            final String[] cmds = path.split("[.]");
            for (final String cmd : cmds) {
                result.add(new ExtractCommand(cmd));
            }
            
        }
        return result;
    }
    
}
