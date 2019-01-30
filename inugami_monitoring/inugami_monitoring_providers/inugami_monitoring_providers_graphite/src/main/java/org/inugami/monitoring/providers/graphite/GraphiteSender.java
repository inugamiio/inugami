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
package org.inugami.monitoring.providers.graphite;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.inugami.api.exceptions.FatalException;
import org.inugami.api.functionnals.ApplyIfNotNull;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.monitoring.api.data.GenericMonitoringModel;
import org.inugami.monitoring.api.senders.MonitoringSender;
import org.inugami.monitoring.api.senders.MonitoringSenderException;

import com.codahale.metrics.graphite.PickledGraphite;

public class GraphiteSender implements MonitoringSender, ApplyIfNotNull {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String   SERVICE      = "service";
    
    private static final String   CALL_TYPE    = "callType";
    
    private static final String   COUNTER_TYPE = "counterType";
    
    public static final String    KEY_HOST     = "host";
    
    public static final String    KEY_PREFIX   = "prefix";
    
    public static final String    KEY_PORT     = "port";
    
    public static final String    ANY          = "any";
    
    public static final String    DEVICE       = "device";
    
    private final PickledGraphite graphite;
    
    private final String          prefix;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public GraphiteSender() {
        graphite = null;
        prefix = null;
    }
    
    public GraphiteSender(final ConfigHandler<String, String> config) {
        final String host = config.grab(KEY_HOST);
        final int port = config.grabInt(KEY_PORT);
        prefix = initPrefix(config.grabOrDefault(KEY_PREFIX, null));
        Loggers.PROVIDER.info("graphite sender to : {}:{}", host, port);
        graphite = new PickledGraphite(new InetSocketAddress(host, port), 200);
        connect();
    }
    
    @Override
    public MonitoringSender buildInstance(final ConfigHandler<String, String> config) {
        return new GraphiteSender(config);
    }
    
    private String initPrefix(String value) {
        String result = null;
        if (value == null) {
            value = "";
        }
        else {
            result = String.join(".", value.split("[.]")) + ".";
        }
        return result;
    }
    
    @Override
    public String getName() {
        return "graphite";
    }
    // =========================================================================
    // process
    // =========================================================================
    
    @Override
    public void shutdown() {
        flush();
        close();
    }
    
    @Override
    public void process(final List<GenericMonitoringModel> data) throws MonitoringSenderException {
        
        if (data != null) {
            if (!graphite.isConnected()) {
                connect();
            }
            
            int cursor = 0;
            for (final GenericMonitoringModel target : data) {
                cursor++;
                try {
                    sendToGraphite(target);
                }
                catch (final IOException error) {
                    Loggers.DEBUG.error(error.getMessage(), error);
                    Loggers.PROVIDER.error(error.getMessage());
                }
                if ((cursor % 100) == 200) {
                    flush();
                }
            }
            flush();
            close();
        }
        
    }
    
    private void sendToGraphite(final GenericMonitoringModel data) throws IOException {
        
        if ((data.getAsset() != null) && (data.getEnvironment() != null) && (data.getInstanceName() != null)) {
            final List<String> parts = new ArrayList<>();
            
            parts.add(data.getAsset());
            parts.add(data.getEnvironment());
            parts.add(data.getInstanceName());
            
            applyIfNotNull(data.getInstanceNumber(), ANY, parts::add);
            applyIfNotNull(data.getCounterType(), COUNTER_TYPE, parts::add);
            parts.add(cleanDeviceName(data.getDevice()));
            applyIfNotNull(data.getCallType(), CALL_TYPE, parts::add);
            applyIfNotNull(data.getService(), SERVICE, parts::add);
            
            applyIfNotNull(data.getSubService(), parts::add);
            applyIfNotNull(data.getValueType(), parts::add);
            
            applyIfNotNull(data.getErrorType(), parts::add);
            applyIfNotNull(data.getErrorCode(), parts::add);
            applyIfNotNull(data.getTimeUnit(), parts::add);
            
            final String target = prefix + String.join(".", parts);
            graphite.send(target, data.getValue().rendering(), data.getTime() / 1000);
        }
    }
    
    private String cleanDeviceName(final String device) {
        String result = DEVICE;
        if (device != null) {
            result = String.join("_", device.split("[.]")).replaceAll("\\s", "-");
        }
        return result;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private void connect() {
        try {
            if (!graphite.isConnected()) {
                graphite.connect();
            }
        }
        catch (IllegalStateException | IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.PROVIDER.error(e.getMessage());
            throw new FatalException(e.getMessage(), e);
        }
    }
    
    private void flush() {
        try {
            graphite.flush();
        }
        catch (final IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.PROVIDER.error(e.getMessage());
        }
    }
    
    private void close() {
        try {
            graphite.close();
        }
        catch (final IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.PROVIDER.error(e.getMessage());
        }
    }
}
