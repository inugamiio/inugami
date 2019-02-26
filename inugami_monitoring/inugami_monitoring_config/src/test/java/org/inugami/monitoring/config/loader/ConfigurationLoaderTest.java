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
package org.inugami.monitoring.config.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.inugami.api.constants.JvmKeyValues;
import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.inugami.monitoring.api.data.config.Monitoring;
import org.inugami.monitoring.api.sensors.MonitoringSensor;
import org.inugami.monitoring.config.spi.MBeanSensorForTest;
import org.junit.Test;

/**
 * ConfigurationLoaderTest
 * 
 * @author patrickguillerm
 * @since Jan 15, 2019
 */
public class ConfigurationLoaderTest implements TestUnitResources {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testLoadConfiguration() {
        
        final File configFile = FilesUtils.buildFile(initResourcesPath(), "monitoring.xml");
        System.getProperties().put("inugami-home", "foobar");
        System.getProperties().put(JvmKeyValues.MONITORING_FILE.getKey(), configFile.getAbsolutePath());
        
        final Monitoring config = ConfigurationLoader.CONFIGURATION;
        assertNotNull(config);
        assertEquals("PRD1", config.getEnv());
        assertEquals("DTV", config.getAsset());
        assertEquals("galibier", config.getHostname());
        assertEquals("GAL", config.getInstanceName());
        assertEquals("P11", config.getInstanceNumber());
        
        assertEquals("a-correlation-id", config.getHeaders().getCorrelationId());
        assertEquals("a-request-id", config.getHeaders().getRequestId());
        assertEquals("a-conversation-id", config.getHeaders().getConversationId());
        assertEquals("Authorization", config.getHeaders().getToken());
        assertEquals("a-device-identifier", config.getHeaders().getDeviceIdentifier());
        assertEquals("a-device-type", config.getHeaders().getDeviceType());
        assertEquals("a-device-class", config.getHeaders().getDeviceClass());
        assertEquals("a-device-version", config.getHeaders().getDeviceVersion());
        assertEquals("a-device-os-version", config.getHeaders().getDeviceOsVersion());
        assertEquals("a-device-network-type", config.getHeaders().getDeviceNetworkType());
        assertEquals("a-device-network-speed-down", config.getHeaders().getDeviceNetworkSpeedDown());
        assertEquals("a-device-network-speed-up", config.getHeaders().getDeviceNetworkSpeedUp());
        assertEquals("a-device-network-speed-latency", config.getHeaders().getDeviceNetworkSpeedLatency());
        assertEquals("a-device-ip", config.getHeaders().getDeviceIp());
        assertEquals("a-user-agent", config.getHeaders().getUserAgent());
        assertEquals("a-accept-language", config.getHeaders().getLanguage());
        assertEquals("a-country", config.getHeaders().getCountry());
        
        assertEquals(1, config.getHeaders().getSpecificHeaders().size());
        assertEquals("distribution-id", config.getHeaders().getSpecificHeaders().toArray(new String[] {})[0]);
        
        assertEquals(1, config.getSenders().size());
        assertEquals(4, config.getSensors().size());
        
        MBeanSensorForTest sensor = null;
        for (final MonitoringSensor configSensor : config.getSensors()) {
            if (configSensor instanceof MBeanSensorForTest) {
                sensor = (MBeanSensorForTest) configSensor;
                break;
            }
        }
        assertNotNull(sensor);
        assertEquals("MBean:{{path}}", sensor.getQuery());
        assertEquals("java.lang.Compilation.TotalCompilationTime", sensor.getPath());
    }
}
