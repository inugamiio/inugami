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
import org.slf4j.event.Level;

import java.net.URL;
import java.util.function.Function;
import java.util.function.Supplier;

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
    public static final String APPLICATION_NAME = "APPLICATION";
    public static final Logger APPLICATION      = LoggerFactory.getLogger(APPLICATION_NAME);

    public static final String ALERTING_NAME = "ALERTING";
    public static final Logger ALERTING      = LoggerFactory.getLogger(ALERTING_NAME);

    public static final String ALERTS_EVENT_NAME = "ALERTS_EVENT";
    public static final Logger ALERTS_EVENT      = LoggerFactory.getLogger(ALERTS_EVENT_NAME);

    public static final String JAVA_SCRIPT_NAME = "JAVA_SCRIPT";
    public static final Logger JAVA_SCRIPT      = LoggerFactory.getLogger(JAVA_SCRIPT_NAME);

    public static final String CACHE_NAME = "CACHE";
    public static final Logger CACHE      = LoggerFactory.getLogger(CACHE_NAME);

    public static final String CONFIG_NAME = "CONFIG";
    public static final Logger CONFIG      = LoggerFactory.getLogger(CONFIG_NAME);

    public static final String CHRONOLOG_NAME = "CHRONOLOG";
    public static final Logger CHRONOLOG      = LoggerFactory.getLogger(CHRONOLOG_NAME);

    public static final String PLUGINS_NAME = "PLUGINS";
    public static final Logger PLUGINS      = LoggerFactory.getLogger(PLUGINS_NAME);

    public static final String EVENTSLOG_NAME = "EVENTSLOG";
    public static final Logger EVENTSLOG      = LoggerFactory.getLogger(EVENTSLOG_NAME);

    public static final String SSELOG_NAME = "SSELOG";
    public static final Logger SSELOG      = LoggerFactory.getLogger(SSELOG_NAME);

    public static final String XLLOG_NAME = "XLLOG";
    public static final Logger XLLOG      = LoggerFactory.getLogger(XLLOG_NAME);

    public static final String INIT_NAME = "INITIALIZE";
    public static final Logger INIT      = LoggerFactory.getLogger(INIT_NAME);

    public static final String PROCESSOR_NAME = "PROCESSOR";
    public static final Logger PROCESSOR      = LoggerFactory.getLogger(PROCESSOR_NAME);

    public static final String DEBUGLOG_NAME = "DEBUGLOG";
    public static final Logger DEBUG         = LoggerFactory.getLogger(DEBUGLOG_NAME);

    public static final String PROVIDER_NAME = "PROVIDER";
    public static final Logger PROVIDER      = LoggerFactory.getLogger(PROVIDER_NAME);

    public static final String HANDLER_NAME = "HANDLER";
    public static final Logger HANDLER      = LoggerFactory.getLogger(HANDLER_NAME);

    public static final String IO_NAME = "IO_ACCESS";
    public static final Logger IO      = LoggerFactory.getLogger(IO_NAME);

    public static final String SSE_NAME = "SSE";
    public static final Logger SSE      = LoggerFactory.getLogger(SSE_NAME);

    public static final String METRICS_NAME = "METRICS";
    public static final Logger METRICS      = LoggerFactory.getLogger(METRICS_NAME);

    public static final String EVENTS_NAME = "EVENTS";
    public static final Logger EVENTS      = LoggerFactory.getLogger(EVENTS_NAME);

    public static final String SYSTEM_NAME = "SYSTEM";
    public static final Logger SYSTEM      = LoggerFactory.getLogger(SYSTEM_NAME);

    public static final String SECURITY_NAME = "SECURITY";
    public static final Logger SECURITY      = LoggerFactory.getLogger(SECURITY_NAME);

    public static final String SCRIPTS_NAME = "SCRIPTS";
    public static final Logger SCRIPTS      = LoggerFactory.getLogger(SCRIPTS_NAME);

    public static final String ALERTS_SENDER_NAME = "ALERTS_SENDER";
    public static final Logger ALERTS_SENDER      = LoggerFactory.getLogger(ALERTS_SENDER_NAME);

    public static final String HEALTH_NAME = "HEALTH";
    public static final Logger HEALTH      = LoggerFactory.getLogger(HEALTH_NAME);

    public static final String TASK_NAME = "TASK";
    public static final Logger TASK      = LoggerFactory.getLogger(TASK_NAME);

    public static final String REST_NAME = "REST";
    public static final Logger REST      = LoggerFactory.getLogger(REST_NAME);

    public static final String IOLOG_NAME = "IOLOG";
    public static final Logger IOLOG      = LoggerFactory.getLogger(IOLOG_NAME);

    public static final String PARTNERLOG_NAME = "PARTNERLOG";
    public static final Logger PARTNERLOG      = LoggerFactory.getLogger(PARTNERLOG_NAME);

    public static final String KPI_NAME = "KPI";
    public static final Logger KPI      = LoggerFactory.getLogger(KPI_NAME);

    public static final String LOG_INITIALIZER_NAME = "LOG_INITIALIZER";
    public static final Logger LOG_INITIALIZER      = LoggerFactory.getLogger(LOG_INITIALIZER_NAME);


    // =========================================================================
    // METHODS
    // =========================================================================

    public static void log(final Logger logger, final Level level, final Supplier<String> message, final Object... values) {
        if (logger == null || level == null || message == null) {
            return;
        }
        if (loggerEnabled(logger, level)) {
            switch (level) {
                case TRACE:
                    logger.trace(message.get(), values);
                    break;
                case DEBUG:
                    logger.debug(message.get(), values);
                    break;
                case INFO:
                    logger.info(message.get(), values);
                    break;
                case WARN:
                    logger.warn(message.get(), values);
                    break;
                case ERROR:
                    logger.error(message.get(), values);
                    break;
            }
        }
    }

    private static boolean loggerEnabled(final Logger logger, final Level level) {
        switch (level) {
            case TRACE:
                logger.isTraceEnabled();
                break;
            case DEBUG:
                logger.isDebugEnabled();
                break;
            case INFO:
                logger.isInfoEnabled();
                break;
            case WARN:
                logger.isWarnEnabled();
                break;
            case ERROR:
                logger.isErrorEnabled();
                break;
        }
        return false;
    }

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
