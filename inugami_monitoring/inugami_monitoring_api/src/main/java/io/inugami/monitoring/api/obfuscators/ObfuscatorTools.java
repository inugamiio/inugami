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
package io.inugami.monitoring.api.obfuscators;

import java.util.List;
import java.util.regex.Pattern;

import io.inugami.api.spi.SpiLoader;

/**
 * ObfuscatorTools
 * 
 * @author patrickguillerm
 * @since Jan 10, 2019
 */
public final class ObfuscatorTools {
    
    // =========================================================================
    // ATTRIBTUTES
    // =========================================================================
    private final static List<Obfuscator> OBFUSCATORS = SpiLoader.getInstance().loadSpiServicesByPriority(Obfuscator.class);
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private ObfuscatorTools() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static String applyObfuscators(String data) {
        if (data == null) {
            return data;
        }
        String result = data;
        for (Obfuscator obfuscator : OBFUSCATORS) {
            if (obfuscator.accept(result)) {
                final String cleanResult = obfuscator.clean(result);
                result = cleanResult == null ? result : cleanResult;
            }
        }
        return result;
    }
    
    public static Pattern buildJsonFieldPattern(final String fieldName) {
        return buildJsonFieldPattern(fieldName, "[^\"]+", true);
    }
    
    public static Pattern buildJsonFieldPattern(final String fieldName, final String content) {
        return buildJsonFieldPattern(fieldName, content, true);
    }
    
    public static Pattern buildJsonFieldPattern(final String fieldName, final String content, boolean quot) {
        //@formatter:off
        return Pattern.compile(new StringBuilder()
                                       .append('"').append(fieldName).append('"')
                                       .append("\\s*:\\s*")
                                       .append(quot?'"':"")
                                       .append(content)
                                       .append(quot?'"':"")
                                       .toString());
        //@formatter:on
    }
}
