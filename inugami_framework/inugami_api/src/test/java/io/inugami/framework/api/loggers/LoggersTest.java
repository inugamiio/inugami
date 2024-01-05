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
package io.inugami.framework.api.loggers;

import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * LoggersTest
 *
 * @author patrick_guillerm
 * @since 12 avr. 2018
 */
class LoggersTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testRenderImageAscii() throws Exception {
        final URL    imgTv = buildUrl("tv.png");
        final String ascii = Loggers.renderImageAscii(imgTv);
        assertNotNull(ascii);

        Loggers.APPLICATION.info("\n {}", ascii);
    }

    @Test
    void loggerEnabled_nominal() {
        final Logger testDebug = LoggerFactory.getLogger("LOGGERS_TEST_DEBUG");
        final Logger testInfo  = LoggerFactory.getLogger("LOGGERS_TEST_INFO");
        final Logger testWarn  = LoggerFactory.getLogger("LOGGERS_TEST_WARN");
        final Logger testError = LoggerFactory.getLogger("LOGGERS_TEST_ERROR");

        assertThat(Loggers.loggerEnabled(testDebug, Level.DEBUG)).isTrue();
        assertThat(Loggers.loggerEnabled(testDebug, Level.INFO)).isTrue();
        assertThat(Loggers.loggerEnabled(testDebug, Level.WARN)).isTrue();
        assertThat(Loggers.loggerEnabled(testDebug, Level.ERROR)).isTrue();
        //
        assertThat(Loggers.loggerEnabled(testInfo, Level.DEBUG)).isFalse();
        assertThat(Loggers.loggerEnabled(testInfo, Level.INFO)).isTrue();
        assertThat(Loggers.loggerEnabled(testInfo, Level.WARN)).isTrue();
        assertThat(Loggers.loggerEnabled(testInfo, Level.ERROR)).isTrue();
        //
        assertThat(Loggers.loggerEnabled(testWarn, Level.DEBUG)).isFalse();
        assertThat(Loggers.loggerEnabled(testWarn, Level.INFO)).isFalse();
        assertThat(Loggers.loggerEnabled(testWarn, Level.WARN)).isTrue();
        assertThat(Loggers.loggerEnabled(testWarn, Level.ERROR)).isTrue();
        //
        assertThat(Loggers.loggerEnabled(testError, Level.DEBUG)).isFalse();
        assertThat(Loggers.loggerEnabled(testError, Level.INFO)).isFalse();
        assertThat(Loggers.loggerEnabled(testError, Level.WARN)).isFalse();
        assertThat(Loggers.loggerEnabled(testError, Level.ERROR)).isTrue();
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
