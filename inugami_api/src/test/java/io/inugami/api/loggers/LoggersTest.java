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
package io.inugami.api.loggers;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * LoggersTest
 *
 * @author patrick_guillerm
 * @since 12 avr. 2018
 */
public class LoggersTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testRenderImageAscii() throws Exception {
        final URL    imgTv = buildUrl("tv.png");
        final String ascii = Loggers.renderImageAscii(imgTv);
        assertNotNull(ascii);

        Loggers.APPLICATION.info("\n {}", ascii);
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    public URL buildUrl(final String fileName) throws MalformedURLException {
        final File          file = new File(".");
        final StringBuilder path = new StringBuilder();
        path.append(file.getAbsoluteFile().getParentFile());
        path.append(File.separator);
        path.append("src");
        path.append(File.separator);
        path.append("test");
        path.append(File.separator);
        path.append("resources");
        path.append(File.separator);
        path.append(fileName);

        return new File(path.toString()).toURI().toURL();
    }

}
