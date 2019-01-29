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
package org.inugami.commons.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * RapportWriter
 * 
 * @author patrick_guillerm
 * @since 28 juin 2017
 */
public class RapportWriter extends FileWriter {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String SPACE = " ";
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public RapportWriter(final File file) throws IOException {
        super(file, file.exists());
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public RapportWriter newLine() throws IOException {
        write("\n");
        flush();
        return this;
    }
    
    public RapportWriter writeLn(final String value) throws IOException {
        write(value);
        newLine();
        return this;
    }
    
    public RapportWriter deco(final String decoChars) throws IOException {
        deco(decoChars, 80);
        return this;
    }
    
    public RapportWriter deco(final String decoChars, final int nbChars) throws IOException {
        processWriteDeco(decoChars, nbChars);
        newLine();
        return this;
    }
    
    public RapportWriter deco(final String message, final String decoChars) throws IOException {
        return deco(message, decoChars, 80);
        
    }
    
    public RapportWriter deco(final String message, final String decoChars, final int nbChars) throws IOException {
        processWriteDeco(decoChars, nbChars);
        newLine();
        
        write(decoChars);
        write(SPACE);
        write(message);
        newLine();
        
        processWriteDeco(decoChars, nbChars);
        return this;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private void processWriteDeco(final String decoChars, final int nbChars) throws IOException {
        final int size = nbChars < 0 ? 1 : nbChars;
        for (int i = size - 1; i >= 0; i--) {
            write(decoChars);
        }
    }
}
