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
package io.inugami.commons.cli;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;

/**
 * MojoHelper
 * 
 * @author patrick_guillerm
 * @since 20 juin 2017
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CliHelper {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String EMPTY_STR = "";

    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static void drawDeco(final String message, final String deco, final Logger logger) {
        final String line = createLine(deco, 80);
        logger.info(line);
        logger.info("{} {}", deco, message);
        logger.info(line);
    }
    
    public static void newLine(final Logger logger) {
        logger.info(EMPTY_STR);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    public static void drawLine(final String deco, final int size, final Logger logger) {
        logger.info(createLine(deco, size));
    }
    
    public static String createLine(final String deco, final int size) {
        final StringBuilder result = new StringBuilder();
        for (int i = size - 1; i >= 0; i--) {
            result.append(deco);
        }
        return result.toString();
    }
    
}
