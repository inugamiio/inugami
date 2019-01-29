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
 * PasswordObfuscator
 * 
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
public class PasswordObfuscator implements Obfuscator {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Pattern EMPTY = ObfuscatorTools.buildJsonFieldPattern("(password|PASSWORD)", "\"\\s*\""); 
    private final static Pattern DEFAULT = ObfuscatorTools.buildJsonFieldPattern("(password|PASSWORD)");
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public boolean accept(String data) {
        return data.contains("password");
    }
    
    @Override
    public String clean(String data) {
       String result =  replaceAll(DEFAULT, data, "\"password\":\"xxxxx\"");
        return replaceAll(EMPTY, result, "\"password\":\"empty\"");
    }
}
