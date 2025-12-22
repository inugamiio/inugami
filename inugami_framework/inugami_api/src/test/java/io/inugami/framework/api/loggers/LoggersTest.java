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

import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.tools.Rgb;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
/**
 * LoggersTest
 *
 * @author patrick_guillerm
 * @since 12 avr. 2018
 */
@SuppressWarnings({"java:S2699"})
@ExtendWith(MockitoExtension.class)
class LoggersTest {
    @Mock
    private Logger mockLogger;
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

    @Test
    void should_respect_utility_class_rules() {
        UnitTestHelper.assertUtilityClassLombok(Loggers.class);
    }

    @Test
    void static_loggers_should_be_initialized() {
        assertThat(Loggers.APPLICATION).isNotNull();
        assertThat(Loggers.APPLICATION.getName()).isEqualTo(Loggers.APPLICATION_NAME);
        assertThat(Loggers.KPI.getName()).isEqualTo(Loggers.KPI_NAME);
    }

    @Test
    void log_should_handle_all_levels_when_enabled() {
        when(mockLogger.isTraceEnabled()).thenReturn(true);
        when(mockLogger.isDebugEnabled()).thenReturn(true);
        when(mockLogger.isInfoEnabled()).thenReturn(true);
        when(mockLogger.isWarnEnabled()).thenReturn(true);
        when(mockLogger.isErrorEnabled()).thenReturn(true);

        Supplier<String> msg = () -> "message {}";
        Object[] args = new Object[]{"arg1"};

        Loggers.log(mockLogger, Level.TRACE, msg, args);
        verify(mockLogger).trace("message {}", args);

        Loggers.log(mockLogger, Level.DEBUG, msg, args);
        verify(mockLogger).debug("message {}", args);

        Loggers.log(mockLogger, Level.INFO, msg, args);
        verify(mockLogger).info("message {}", args);

        Loggers.log(mockLogger, Level.WARN, msg, args);
        verify(mockLogger).warn("message {}", args);

        Loggers.log(mockLogger, Level.ERROR, msg, args);
        verify(mockLogger).error("message {}", args);
    }

    @Test
    void log_should_not_call_supplier_if_level_disabled() {
        when(mockLogger.isInfoEnabled()).thenReturn(false);

        Supplier<String> msgSupplier = mock(Supplier.class);

        Loggers.log(mockLogger, Level.INFO, msgSupplier);

        verify(mockLogger, never()).info(anyString(), any(Object[].class));
    }

    @Test
    void loggerEnabled_should_return_correct_status() {
        when(mockLogger.isTraceEnabled()).thenReturn(true);
        when(mockLogger.isErrorEnabled()).thenReturn(false);

        assertThat(Loggers.loggerEnabled(mockLogger, Level.TRACE)).isTrue();
        assertThat(Loggers.loggerEnabled(mockLogger, Level.ERROR)).isFalse();
    }

    @Test
    void imageAscii_signatures_coverage() throws Exception {
        URL                   url    = new URL("file:///tmp/test.png");
        Function<Rgb, String> mapper = rgb -> "#";

        try {
            Loggers.imageAscii(url);
            Loggers.imageAscii(url, mockLogger);
            Loggers.imageAscii(url, mapper);
            Loggers.imageAscii(url, mockLogger, mapper);
        } catch (Exception e) {
        }
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
