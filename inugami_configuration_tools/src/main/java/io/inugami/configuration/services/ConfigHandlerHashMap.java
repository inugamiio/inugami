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
package io.inugami.configuration.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.mapping.JsonUnmarshalling;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ClassBehaviorParametersSPI;
import io.inugami.api.processors.Config;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.spi.SpiLoader;
import io.inugami.configuration.services.functions.FunctionsServices;
import io.inugami.configuration.services.functions.ProviderAttributFunction;

/**
 * ConfigHandlerService
 * 
 * @author patrick_guillerm
 * @since 5 janv. 2017
 */
public class ConfigHandlerHashMap extends HashMap<String, String>
        implements ConfigHandler<String, String>, ClassBehaviorParametersSPI {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long                  serialVersionUID = -6282728615557622361L;
    
    private final ConfigHandlerOptionalHashMap optional;
    
    private final String                       serviceName;
    
    private final FunctionsServices            functions;
    
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public ConfigHandlerHashMap() {
        this("ConfigHandlerHashMap");
    }
    
    public ConfigHandlerHashMap(final String serviceName) {
        super();
        this.serviceName = serviceName;
        optional = new ConfigHandlerOptionalHashMap();
        
        functions = new FunctionsServices(new SpiLoader().loadSpiService(ProviderAttributFunction.class), this);
    }
    
    public ConfigHandlerHashMap(final Map<String, String> map) {
        this(map, true);
    }
    
    private ConfigHandlerHashMap(final Map<String, String> map, boolean enableFunction,
                                 final ProviderAttributFunction... functions) {
        super(map);
        this.serviceName = null;
        enableFunction = functions.length > 0;
        optional = new ConfigHandlerOptionalHashMap(map, functions);
        optional.putAll(map);
        this.functions = new FunctionsServices(functions, this);
    }
    
    public ConfigHandlerHashMap(final List<Config> configs) {
        this(convertToMap(configs));
    }
    
    // =========================================================================
    // BUILDERS
    // =========================================================================
    public static ConfigHandlerHashMap buildWithoutFunction() {
        return new ConfigHandlerHashMap(new HashMap<>(), false, new ProviderAttributFunction[] {});
    }
    
    public static ConfigHandlerHashMap buildWithoutFunction(final Map<String, String> map) {
        return new ConfigHandlerHashMap(map, false, new ProviderAttributFunction[] {});
    }
    
    public static ConfigHandlerHashMap buildWithSpecificFunctions(final Map<String, String> map,
                                                                  final ProviderAttributFunction... functions) {
        return new ConfigHandlerHashMap(map, false, functions);
    }
    
    // =========================================================================
    // APPLY PROPERTIES
    // =========================================================================
    @Override
    public String applyProperties(final String value) {
        if (value == null) {
            return null;
        }
        final String valueWithValues = new ConfigTemplateValues().applyProperties(value, this);
        return functions.applyFunctions(valueWithValues);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    // -------------------------------------------------------------------------
    // STRING
    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public String grabOrDefault(final String key, final String defaultValue) {
        final Object value = get(key);
        final String result = value == null ? defaultValue : optional.convertToString(value);
        return applyProperties(result);
    }
    
    @Override
    public String grab(final String key) {
        final String result = grab(null, key);
        return applyProperties(result);
    }
    
    @Override
    public String grab(final String message, final String key) {
        return String.valueOf(getValueMandatory(message, key));
    }
    
    // -------------------------------------------------------------------------
    // INTEGER
    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer grab(final String key, final int defaultValue) {
        return optional.grab(key, defaultValue);
    }
    
    @Override
    public Integer grabInt(final String key) {
        return optional.convertToInt(key, grab(key));
    }
    
    @Override
    public Integer grabInt(final String key, final Integer defaultValue) {
        final Integer value = optional.grabInt(key, defaultValue);
        assertNotNull(key, value);
        return value;
    }
    
    // -------------------------------------------------------------------------
    // BOOLEAN
    // -------------------------------------------------------------------------
    @Override
    public boolean grabBoolean(final String key) {
        return optional.convertToBoolean(key, getValueMandatory(key));
    }
    
    @Override
    public boolean grabBoolean(final String key, final boolean defaultValue) {
        final boolean result = optional.grabBoolean(key, defaultValue);
        return result;
    }
    
    // -------------------------------------------------------------------------
    // DOUBLE
    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public Double grab(final String key, final double defaultValue) {
        final Double result = optional.grab(key, defaultValue);
        assertNotNull(key, result);
        return result;
    }
    
    @Override
    public Double grabDouble(final String key) {
        return optional.convertToDouble(key, grab(key));
    }
    
    // -------------------------------------------------------------------------
    // LONG
    // -------------------------------------------------------------------------
    @Override
    public long grabLong(final String key, final long defaultValue) {
        final String value = get(key);
        return value == null ? defaultValue : Long.parseLong(value);
    }
    
    // -------------------------------------------------------------------------
    // JSON
    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T grabJson(final String key, final String json, final JsonUnmarshalling unmarshaller) {
        final T result = optional.grabJson(key, json, unmarshaller);
        assertNotNull(key, result);
        return result;
    }
    
    @Override
    public <T> T grabJson(final String key, final JsonUnmarshalling unmarshaller) {
        return optional.convertToObject(grab(key), unmarshaller);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T grabJson(final String key, final Object jsonObj, final JsonUnmarshalling unmarshaller) {
        final T result = optional.grabJson(key, unmarshaller);
        Asserts.notNull(result);
        return result;
    }
    
    @Override
    public ConfigHandler<String, String> optionnal() {
        return optional;
    }
    
    @Override
    public List<String> grabValues(final String prefix) {
        Asserts.notNull("property prefix is mandatory!", prefix);
        final List<String> result = new ArrayList<>();
        final Pattern regex = Pattern.compile("^" + prefix + "[.][0-9]+$");
        
        for (final Map.Entry<String, String> entry : entrySet()) {
            if (regex.matcher(entry.getKey()).matches()) {
                result.add(applyProperties(entry.getValue()));
            }
        }
        return result;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    protected String getValueMandatory(final String key) {
        return getValueMandatory(null, key);
    }
    
    protected String getValueMandatory(final String message, final String key) {
        Asserts.notNull("Key mustn't be null! (" + serviceName + ")", key);
        final String result = get(key);
        assertNotNull(message, key, result);
        return applyProperties(result);
    }
    
    private void assertNotNull(final String key, final Object value) {
        assertNotNull(null, key, value);
    }
    
    private void assertNotNull(final String message, final String key, final Object result) {
        if (result == null) {
            String msg = message;
            if (message == null) {
                msg = String.format("Value of \"%s\" is mandatory! (%s)", key, serviceName);
            }
            throw new IllegalArgumentException(msg);
        }
    }
    
    private static Map<String, String> convertToMap(final List<Config> configs) {
        final Map<String, String> result = new HashMap<>();
        if (configs != null) {
            for (final Config entry : configs) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
    
    // =========================================================================
    // Override ClassBehaviorAttributes
    // =========================================================================
    @Override
    public boolean accept(final Class<?> clazz) {
        return clazz.isAssignableFrom(this.getClass());
    }
    
    @Override
    public <T> T build(final ClassBehavior behavior, final ConfigHandler<String, String> config) {
        return (T) config;
    }
}
