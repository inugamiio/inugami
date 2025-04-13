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
package io.inugami.monitoring.core.interceptors;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

/**
 * OutputWriterWrapper
 *
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
@SuppressWarnings({"java:S1197"})
final class OutputWriterWrapper extends ServletOutputStream {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ServletOutputStream outputStream;

    private final StringBuilder buffer = new StringBuilder();
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

        byte[] data = new byte[len];
        System.arraycopy(b, off, data, 0, len);

        buffer.append(new String(data));
        super.write(b, off, len);
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    String getData() {
        return buffer.toString().trim();
    }
}
