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
package org.inugami.monitoring.core.obfuscators;

import java.util.regex.Pattern;

import org.inugami.monitoring.api.obfuscators.Obfuscator;
import org.inugami.monitoring.api.obfuscators.ObfuscatorTools;

/**
 * TokenObfuscator
 * 
 * @author patrickguillerm
 * @since Jan 8, 2019
 */

public class TokenObfuscator implements Obfuscator {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Pattern  EMPTY              = ObfuscatorTools.buildJsonFieldPattern("(token|TOKEN)",
                                                                                             "\"\\s*\"");
    
    private static final Pattern  DEFAULT            = ObfuscatorTools.buildJsonFieldPattern("(token|TOKEN)");
    
    private static final String[] ACTIVATED_ELEMENTS = { "token", "TOKEN" };
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public boolean accept(final String data) {
        boolean found = false;
        
        for (final String element : ACTIVATED_ELEMENTS) {
            found = data.contains(element);
            if (found) {
                break;
            }
        }
        
        return found;
    }
    
    @Override
    public String clean(final String data) {
        final String result = replaceAll(DEFAULT, data, "\"token\":\"xxxxx\"");
        
        return replaceAll(EMPTY, result, "\"token\":\"empty\"");
    }
}
