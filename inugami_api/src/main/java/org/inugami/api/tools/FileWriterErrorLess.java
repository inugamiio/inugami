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
package org.inugami.api.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.inugami.api.exceptions.FatalException;
import org.inugami.api.functionnals.VoidFunctionWithException;

/**
 * FileWriterErrorLess
 * 
 * @author patrick_guillerm
 * @since 9 nov. 2017
 */
public class FileWriterErrorLess extends Writer {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private transient final Writer fileWriter;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FileWriterErrorLess(final String file) {
        this(new File(file));
    }
    
    public FileWriterErrorLess(final File file) {
        this(file, false);
    }
    
    public FileWriterErrorLess(final File file, final boolean append) {
        try {
            fileWriter = new FileWriter(file, append);
        }
        catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    public void write(final boolean value) {
        write(String.valueOf(value));
    }
    
    public void write(final byte value) {
        write(String.valueOf(value));
    }
    
    @Override
    public void write(final int value) {
        write(String.valueOf(value));
    }
    
    public void write(final long value) {
        write(String.valueOf(value));
    }
    
    public void write(final float value) {
        write(String.valueOf(value));
    }
    
    public void write(final double value) {
        write(String.valueOf(value));
    }
    
    @Override
    public void write(final char[] cbuf, final int off, final int len) {
        process(() -> fileWriter.write(cbuf, off, len));
    }
    
    @Override
    public void write(final char cbuf[]) {
        process(() -> fileWriter.write(cbuf));
    }
    
    @Override
    public void write(final String value) {
        process(() -> fileWriter.write(value));
    }
    
    @Override
    public void write(final String str, final int off, final int len) {
        process(() -> fileWriter.write(str, off, len));
    }
    
    public void writeObj(final Object value) {
        process(() -> fileWriter.write(String.valueOf(value)));
    }
    
    public void newLine() {
        write("\n");
    }
    
    public void csvSeparator() {
        write(";");
    }
    
    @Override
    public Writer append(final CharSequence csq) {
        process(() -> fileWriter.append(csq));
        return this;
    }
    
    @Override
    public Writer append(final CharSequence csq, final int start, final int end) {
        process(() -> fileWriter.append(csq, start, end));
        return this;
    }
    
    @Override
    public Writer append(final char value) {
        process(() -> fileWriter.append(value));
        return this;
    }
    
    @Override
    public void flush() {
        process(() -> fileWriter.flush());
    }
    
    @Override
    public void close() {
        process(() -> fileWriter.close());
    }
    
    // =========================================================================
    // EXCEPTION
    // =========================================================================
    private void process(final VoidFunctionWithException function) {
        try {
            function.process();
        }
        catch (final Exception e) {
            throw new FatalException(e.getMessage(), e);
        }
    }
    
}
