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

import io.inugami.api.models.Rgb;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.function.Function;

/**
 * LoggerFactory
 *
 * @author patrick_guillerm
 * @since 5 janv. 2017
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Loggers {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final Logger APPLICATION = LoggerFactory.getLogger("APPLICATION");

    public static final Logger ALERTING = LoggerFactory.getLogger("ALERTING");

    public static final Logger ALERTS_EVENT = LoggerFactory.getLogger("ALERTS_EVENT");

    public static final Logger JAVA_SCRIPT = LoggerFactory.getLogger("JAVA_SCRIPT");

    public static final Logger CACHE = LoggerFactory.getLogger("CACHE");

    public static final Logger CONFIG = LoggerFactory.getLogger("CONFIG");

    public static final Logger CHRONOLOG = LoggerFactory.getLogger("CHRONOLOG");

    public static final Logger PLUGINS = LoggerFactory.getLogger("PLUGINS");

    public static final Logger EVENTSLOG = LoggerFactory.getLogger("EVENTSLOG");

    public static final Logger SSELOG = LoggerFactory.getLogger("SSELOG");

    public static final Logger XLLOG = LoggerFactory.getLogger("XLLOG");

    public static final Logger INIT = LoggerFactory.getLogger("INITIALIZE");

    public static final Logger PROCESSOR = LoggerFactory.getLogger("PROCESSOR");

    public static final Logger DEBUG = LoggerFactory.getLogger("DEBUGLOG");

    public static final Logger PROVIDER = LoggerFactory.getLogger("PROVIDER");

    public static final Logger HANDLER = LoggerFactory.getLogger("HANDLER");

    public static final Logger IO = LoggerFactory.getLogger("IO_ACCESS");

    public static final Logger SSE = LoggerFactory.getLogger("SSE");

    public static final Logger METRICS = LoggerFactory.getLogger("METRICS");

    public static final Logger EVENTS = LoggerFactory.getLogger("EVENTS");

    public static final Logger SYSTEM = LoggerFactory.getLogger("SYSTEM");

    public static final Logger SECURITY = LoggerFactory.getLogger("SECURITY");

    public static final Logger SCRIPTS = LoggerFactory.getLogger("SCRIPTS");

    public static final Logger ALERTS_SENDER = LoggerFactory.getLogger("ALERTS_SENDER");

    public static final Logger HEALTH = LoggerFactory.getLogger("HEALTH");

    public static final Logger TASK = LoggerFactory.getLogger("TASK");

    public static final Logger REST = LoggerFactory.getLogger("REST");


    // =========================================================================
    // METHODS
    // =========================================================================
    public static void imageAscii(final URL file) {
        imageAscii(file, APPLICATION, null);
    }

    public static void imageAscii(final URL file, final Logger logger) {
        imageAscii(file, logger, null);
    }

    public static void imageAscii(final URL file, final Function<Rgb, String> asciiMapper) {
        imageAscii(file, APPLICATION, asciiMapper);
    }

    public static void imageAscii(final URL file, final Logger logger, final Function<Rgb, String> asciiMapper) {
        final String data = renderImageAscii(file, asciiMapper);
        logger.info("\n{}", data);
    }

    public static String renderImageAscii(final URL file) {
        return renderImageAscii(file, null);
    }

    public static String renderImageAscii(final URL file, final Function<Rgb, String> asciiMapper) {
        return AsciiRenderer.renderImageAscii(file, asciiMapper);
    }
}
