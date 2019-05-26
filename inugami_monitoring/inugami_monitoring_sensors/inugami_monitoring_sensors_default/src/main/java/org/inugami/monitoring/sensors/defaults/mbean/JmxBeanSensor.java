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
package org.inugami.monitoring.sensors.defaults.mbean;

import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.data.JsonObject;
import org.inugami.api.monitoring.models.GenericMonitoringModel;
import org.inugami.api.monitoring.models.GenericMonitoringModelBuilder;
import org.inugami.api.monitoring.sensors.MonitoringSensor;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.monitoring.api.tools.GenericMonitoringModelTools;

public class JmxBeanSensor implements MonitoringSensor {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final long                          interval;
    
    private final String                        query;
    
    private final MBeanServer                   jmx = ManagementFactory.getPlatformMBeanServer();
    
    private final String                        timeUnit;
    
    private final ConfigHandler<String, String> configuration;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public JmxBeanSensor() {
        super();
        interval = 60000L;
        query = null;
        timeUnit = null;
        configuration = null;
    }
    
    public JmxBeanSensor(final long interval, final String query, final String timeUnit,
                         final ConfigHandler<String, String> configuration) {
        this.interval = interval;
        this.query = query;
        this.timeUnit = null;
        this.configuration = configuration;
        
    }
    
    @Override
    public MonitoringSensor buildInstance(final long interval, final String query,
                                          final ConfigHandler<String, String> configuration) {
        
        return new JmxBeanSensor(interval, query, configuration.grabOrDefault("timeUnit", null), configuration);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> process() {
        final List<GenericMonitoringModel> result = new ArrayList<>();
        if (query != null) {
            
            final String localQuery = configuration == null ? query : configuration.applyProperties(query);
            final JmxBeanSensorQuery queryData = new JmxBeanSensorQuery().convertToObject(localQuery.getBytes(),
                                                                                          Charset.forName("UTF-8"));
            
            //@formatter:off
            final String normalizePath = queryData.getPath()
                                                  .replaceAll("[\\W]", "_")
                                                  .replaceAll("\"", "")
                                                  .replaceAll("'", "");
            //@formatter:on
            
            Loggers.METRICS.debug("processing mbean sensor for : {}", normalizePath);
            if (queryData.getMbeanAttibute() != null) {
                result.addAll(processAttribute(normalizePath, queryData));
            }
            
        }
        
        return result;
    }
    
    private List<GenericMonitoringModel> processAttribute(final String normalizePath,
                                                          final JmxBeanSensorQuery queryData) {
        Object value = null;
        try {
            value = jmx.getAttribute(new ObjectName(queryData.getPath()), queryData.getMbeanAttibute());
        }
        catch (InstanceNotFoundException | AttributeNotFoundException | MalformedObjectNameException
                | ReflectionException | MBeanException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }
        
        return value == null ? new ArrayList<>()
                             : convertToMonitoringModel(normalizePath, value, queryData.getMbeanAttibute());
    }
    
    // =========================================================================
    // Convertor
    // =========================================================================
    private List<GenericMonitoringModel> convertToMonitoringModel(final String normalizePath, final Object value,
                                                                  final String valueComeFrom) {
        
        final GenericMonitoringModelBuilder builder = GenericMonitoringModelTools.initResultBuilder();
        
        builder.setCounterType("system");
        builder.setService("jmx");
        builder.setSubService(String.join("@", normalizePath, valueComeFrom));
        builder.setTimeUnit(GenericMonitoringModelTools.buildTimeUnit(timeUnit, interval));
        builder.setValueType("mbean");
        
        if ((value instanceof Byte) || (value instanceof Short) || (value instanceof Integer)
            || (value instanceof Long)) {
            builder.setValue((long) value);
        }
        else if ((value instanceof Float) || (value instanceof Double)) {
            builder.setValue((double) value);
        }
        else if (value instanceof JsonObject) {
            builder.setData(((JsonObject) value).convertToJson());
        }
        else {
            builder.setData(String.valueOf(value));
        }
        return GenericMonitoringModelTools.buildSingleResult(builder);
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public long getInterval() {
        return interval;
    }
}
