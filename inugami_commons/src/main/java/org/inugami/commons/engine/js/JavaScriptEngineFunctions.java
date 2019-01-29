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
package org.inugami.commons.engine.js;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.inugami.api.exceptions.services.ConnectorException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.mapping.events.json.EventTransformer;
import org.inugami.api.mapping.events.json.SimpleEventTransformer;
import org.inugami.api.models.JsonBuilder;
import org.inugami.api.models.events.Event;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.models.events.TargetConfig;
import org.inugami.api.tools.NashornTools;
import org.inugami.commons.connectors.HttpBasicConnector;
import org.inugami.commons.connectors.HttpConnectorResult;
import org.inugami.commons.engine.HttpConnectorResultJs;
import org.inugami.commons.engine.extractor.ExtractCommand;
import org.inugami.commons.engine.extractor.ExtractCommandsBuilder;
import org.inugami.commons.transformer.NashornTransformer;
import org.inugami.commons.transformer.OptionalTransformer;

import flexjson.JSONSerializer;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * JavaScriptEngineFunctions
 * 
 * @author patrickguillerm
 * @since 20 déc. 2017
 */
public class JavaScriptEngineFunctions {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final HttpBasicConnector HTTP_BASIC_CONNECTOR = new HttpBasicConnector(120000, 500, 500, 30, 60000);
    
    // =========================================================================
    // JSON
    // =========================================================================
    public static String stringify(final Object data) {
        //@formatter:off
        return data instanceof String ? String.valueOf(data)
                                      : stringifyObject(data);
        //@formatter:on
    }
    
    public static String stringifyObject(final Object data) {
        if (data == null) {
            return JsonBuilder.VALUE_NULL;
        }
        
        //@formatter:off
        String result = null;
        try {
            
            result  = new JSONSerializer()
                            .transform(new NashornTransformer(), ScriptObjectMirror.class)
                            .transform(new OptionalTransformer(), Optional.class)
                            .transform(new EventTransformer(), Event.class)
                            .transform(new SimpleEventTransformer(), SimpleEvent.class)
                            .transform(new SimpleEventTransformer(), TargetConfig.class)
                            .exclude("*.class")
                            .deepSerialize(data);
        }catch (final Exception e) {
            Loggers.JAVA_SCRIPT.error("can't serialize object : {}",data);
        }
        //@formatter:on
        return result;
    }
    
    // =========================================================================
    // CONSOLE
    // =========================================================================
    public static void trace(final Object data) {
        Loggers.JAVA_SCRIPT.trace(stringify(data));
    }
    
    public static void debug(final Object data) {
        Loggers.JAVA_SCRIPT.debug(stringify(data));
    }
    
    public static void log(final Object data) {
        Loggers.JAVA_SCRIPT.info(stringify(data));
    }
    
    public static void warn(final Object data) {
        Loggers.JAVA_SCRIPT.warn(stringify(data));
    }
    
    public static void error(final Object data) {
        Loggers.JAVA_SCRIPT.error(stringify(data));
    }
    
    // =========================================================================
    // DATE
    // =========================================================================
    public static long dateNow() {
        return System.currentTimeMillis();
    }
    
    public static Calendar dateNew() {
        return Calendar.getInstance();
    }
    
    // =========================================================================
    // HTTP
    // =========================================================================
    public static HttpConnectorResultJs httpGet(final String url, final Map<String, String> header) {
        HttpConnectorResult result = null;
        try {
            result = HTTP_BASIC_CONNECTOR.get(url, null, header);
        }
        catch (final ConnectorException e) {
            Loggers.SCRIPTS.error(e.getMessage());
        }
        return new HttpConnectorResultJs(result);
    }
    
    public static HttpConnectorResultJs httpPost(final String url, final String json,
                                                 final Map<String, String> header) {
        HttpConnectorResult result = null;
        
        try {
            result = HTTP_BASIC_CONNECTOR.post(url, json, header);
        }
        catch (final ConnectorException e) {
            Loggers.SCRIPTS.error(e.getMessage());
        }
        
        return new HttpConnectorResultJs(result);
    }
    
    // =========================================================================
    // extract
    // =========================================================================
    public static Object extract(final Object value, final String path, final Object defaultValue) {
        Object result = null;
        List<ExtractCommand> commands = null;
        if ((value != null) && (path != null)) {
            commands = ExtractCommandsBuilder.build(path);
        }
        
        if (commands != null) {
            Object cursor = value;
            for (final ExtractCommand command : commands) {
                cursor = command.extract(cursor);
                if (cursor == null) {
                    break;
                }
            }
            
            if (!value.equals(cursor)) {
                result = cursor;
            }
        }
        
        if ((result == null) && !NashornTools.isUndefine(defaultValue)) {
            result = defaultValue;
        }
        return result;
    }
    
}
