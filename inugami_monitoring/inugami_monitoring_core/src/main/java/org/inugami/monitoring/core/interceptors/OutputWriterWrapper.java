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
package org.inugami.monitoring.core.interceptors;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * OutputWriterWrapper
 * 
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
final class OutputWriterWrapper extends ServletOutputStream {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ServletOutputStream outputStream;
    
    private final StringBuilder       buffer = new StringBuilder();
    // =========================================================================
    // METHODS
    // =========================================================================
    
    public OutputWriterWrapper(final ServletOutputStream outputStream) {
        this.outputStream = outputStream;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public boolean isReady() {
        return outputStream.isReady();
    }
    
    @Override
    public void setWriteListener(final WriteListener writeListener) {
        outputStream.setWriteListener(writeListener);
    }
    
    @Override
    public void write(final int b) throws IOException {
        outputStream.write(b);
        
    }
    
    @Override
    public void write(final byte b[], final int off, final int len) throws IOException {
        buffer.append(new String(b));
        super.write(b, off, len);
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    String getData() {
        return buffer.toString().trim();
    }
}
