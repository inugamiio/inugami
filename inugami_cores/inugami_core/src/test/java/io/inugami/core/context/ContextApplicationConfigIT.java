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
package io.inugami.core.context;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.loggers.Loggers;
import io.inugami.configuration.models.app.ApplicationConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * ContextTest
 *
 * @author patrick_guillerm
 * @since 18 déc. 2017
 */
public class ContextApplicationConfigIT {

    // =========================================================================
    // METHODS
    // =========================================================================
    public static void main(final String[] aargs) {
        try {
            System.setProperty("instanceName", "myInstanceName");
            System.setProperty("instanceName", "myInstanceName");
            final String appName = "myApplicationFoobar";
            setProperty(JvmKeyValues.APPLICATION_NAME, appName);
            setProperty(JvmKeyValues.APPLICATION_PLUGIN_RUNNING, 10);
            setProperty(JvmKeyValues.APPLICATION_PLUGIN_RUNNING_STANDALONE, 100);
            setProperty(JvmKeyValues.APPLICATION_TIMEOUT, 75000);

            setProperty(JvmKeyValues.HTTP_CONNECTION_MAX_PER_ROUTE, 50);
            setProperty(JvmKeyValues.HTTP_CONNECTION_HEADER_FIELD, ".APPLICATION-NAME",
                        JvmKeyValues.DEFAULT_APPLICATION_NAME);

            setProperty(JvmKeyValues.HTTP_CONNECTION_HEADER_FIELD, ".APPLICATION-HOSTNAME", "{{instanceName}}");

            final ApplicationConfig config = Context.initializeStandalone().getApplicationConfiguration();
            assertNotNull(config);

            assertEquals("myApplicationFoobar", config.getApplicationName());
            assertEquals(10, config.getMaxPluginRunning());
            assertEquals(100, config.getMaxPluginRunningStandalone());
            assertEquals(75000, config.getTimeout());
            assertEquals(50, config.getHttpDefaultConfig().getMaxPerRoute());
            assertEquals(JvmKeyValues.DEFAULT_APPLICATION_NAME,
                         config.getHttpDefaultConfig().getHeaderFields().get(0).getValue());
            assertEquals("myInstanceName", config.getHttpDefaultConfig().getHeaderFields().get(1).getValue());

            Loggers.DEBUG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            Loggers.DEBUG.info(">> OK");
            Loggers.DEBUG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        } catch (final Throwable e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            System.exit(1);
        }

    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private static void setProperty(final JvmKeyValues key, final Object value) {
        System.setProperty(key.getKey(), String.valueOf(value));
    }

    private static void setProperty(final JvmKeyValues key, final String sufix, final Object value) {
        System.setProperty(key.getKey() + sufix, String.valueOf(value));

    }

}
