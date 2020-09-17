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
package io.inugami.monitoring.core.obfuscators;

import java.util.regex.Pattern;

import io.inugami.monitoring.api.obfuscators.Obfuscator;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;

/**
 * PasswordObfuscator
 * 
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
public class AuthorizationObfuscator implements Obfuscator {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Pattern EMPTY = ObfuscatorTools.buildJsonFieldPattern("(AUTHORIZATION|authorization)", "\"\\s*\""); 
    private final static Pattern DEFAULT = ObfuscatorTools.buildJsonFieldPattern("(AUTHORIZATION|authorization)");
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public boolean accept(String data) {
        return data.contains("authorization")||data.contains("AUTHORIZATION");
    }
    
    @Override
    public String clean(String data) {
       String result =  replaceAll(DEFAULT, data, "\"authorization\":\"xxxxx\"");
        return replaceAll(EMPTY, result, "\"authorization\":\"empty\"");
    }
}
