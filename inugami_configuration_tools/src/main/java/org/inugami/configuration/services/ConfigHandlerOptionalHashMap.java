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
package org.inugami.configuration.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.mapping.JsonUnmarshalling;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.commons.spi.SpiLoader;
import org.inugami.configuration.services.functions.FunctionsServices;
import org.inugami.configuration.services.functions.ProviderAttributFunction;

import flexjson.JSONDeserializer;

/**
 * ConfigHandlerService
 * 
 * @author patrick_guillerm
 * @since 5 janv. 2017
 */
public class ConfigHandlerOptionalHashMap extends HashMap<String, String> implements ConfigHandler<String, String> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long       serialVersionUID   = -6282728615557622361L;
    
    private static final Pattern    BOOLEAN_MATCH      = Pattern.compile("^(true|false|yes|no|y|n)$",
                                                                         Pattern.CASE_INSENSITIVE);
    
    private static final Pattern    BOOLEAN_TRUE_MATCH = Pattern.compile("^(true|yes|y)$", Pattern.CASE_INSENSITIVE);
    
    private final FunctionsServices functions;
    
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public ConfigHandlerOptionalHashMap() {
        super();
        final List<ProviderAttributFunction> attributFunctions = new SpiLoader().loadSpiService(ProviderAttributFunction.class);
        functions = new FunctionsServices(attributFunctions, this);
    }
    
    public ConfigHandlerOptionalHashMap(final boolean enableFunction) {
        super();
        final List<ProviderAttributFunction> attributFunctions = new ArrayList<>();
        if (enableFunction) {
            new SpiLoader().loadSpiService(ProviderAttributFunction.class);
        }
        
        functions = new FunctionsServices(attributFunctions, this);
    }
    
    public ConfigHandlerOptionalHashMap(final Map<String, String> map, final ProviderAttributFunction[] functions) {
        super();
        this.functions = new FunctionsServices(functions, this);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String applyProperties(final String value) {
        final String valueWithValues = new ConfigTemplateValues().applyProperties(value, this);
        return functions.applyFunctions(valueWithValues);
    }
    
    // -------------------------------------------------------------------------
    // STRING
    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public String grabOrDefault(final String key, final String defaultValue) {
        final String value = getValue(key);
        return value == null ? defaultValue : value;
    }
    
    @Override
    public String grab(final String key) {
        return grab(null, key);
    }
    
    @Override
    public String grab(final String message, final String key) {
        final String value = get(key);
        return value == null ? null : applyProperties(value);
    }
    
    // -------------------------------------------------------------------------
    // INTEGER
    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer grab(final String key, final int defaultValue) {
        Integer result = defaultValue;
        final Object value = getValue(key);
        if (value != null) {
            final String valueStr = convertToString(value);
            result = convertToInt(key, valueStr);
        }
        return result;
    }
    
    @Override
    public Integer grabInt(final String key) {
        return convertToInt(key, grab(key));
    }
    
    @Override
    public Integer grabInt(final String key, final Integer defaultValue) {
        final Integer value = grabInt(key);
        return value == null ? defaultValue : value;
    }
    
    // -------------------------------------------------------------------------
    // BOOLEAN
    // -------------------------------------------------------------------------
    @Override
    public boolean grabBoolean(final String key) {
        return convertToBoolean(key, grab(key));
    }
    
    @Override
    public boolean grabBoolean(final String key, final boolean defaultValue) {
        boolean result = defaultValue;
        final Object value = getValue(key);
        if (value != null) {
            result = convertToBoolean(key, value);
        }
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
        Double result = defaultValue;
        final Object value = getValue(key);
        if (value != null) {
            final String valueStr = convertToString(value);
            result = convertToDouble(key, valueStr);
        }
        return result;
    }
    
    @Override
    public Double grabDouble(final String key) {
        return convertToDouble(key, grab(key));
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
        T result = null;
        
        final Object value = getValue(key);
        if (value == null) {
            result = convertToObject(json, unmarshaller);
        }
        else {
            result = convertToObject(value, unmarshaller);
        }
        
        return result;
    }
    
    @Override
    public <T> T grabJson(final String key, final JsonUnmarshalling unmarshaller) {
        return convertToObject(grab(key), unmarshaller);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T grabJson(final String key, final Object jsonObj, final JsonUnmarshalling unmarshaller) {
        T result = null;
        final String value = getValue(key);
        if (value == null) {
            result = (T) jsonObj;
        }
        else {
            result = unmarshalling(value, unmarshaller);
        }
        return result;
    }
    
    private <T> T unmarshalling(final String value, final JsonUnmarshalling unmarshaller) {
        T result = null;
        if (value != null) {
            if (unmarshaller == null) {
                result = new JSONDeserializer<T>().deserialize(value);
            }
            else {
                result = unmarshaller.process(value);
            }
        }
        return result;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    protected String getValue(final String key) {
        Asserts.notNull("Key mustn't be null!", key);
        return get(key);
    }
    
    /* package */ Integer convertToInt(final String key, final String valueStr) {
        Integer result = null;
        if (valueStr != null) {
            try {
                result = Integer.parseInt(valueStr);
            }
            catch (final NumberFormatException e) {
                Loggers.XLLOG.error("defined key configuration isn't number : {}={}", key, valueStr);
            }
        }
        return result;
    }
    
    /* package */ Double convertToDouble(final String key, final String valueStr) {
        Double result = null;
        try {
            result = Double.parseDouble(valueStr);
        }
        catch (final NumberFormatException e) {
            Loggers.XLLOG.error("defined key configuration isn't double number : {}={}", key, valueStr);
        }
        return result;
    }
    
    /* package */ boolean convertToBoolean(final String key, final Object value) {
        boolean result = false;
        if (value != null) {
            final String strValue = convertToString(value);
            
            if (BOOLEAN_MATCH.matcher(strValue).matches()) {
                result = BOOLEAN_TRUE_MATCH.matcher(strValue).matches();
            }
            else {
                Loggers.XLLOG.error("defined key configuration isn't boolean value : {}={}", key, strValue);
            }
        }
        
        return result;
    }
    
    /* package */ String convertToString(final Object value) {
        return value == null ? null : String.valueOf(value).trim();
    }
    
    /* package */ <T> T convertToObject(final Object value, final JsonUnmarshalling unmarshaller) {
        T result;
        result = (value instanceof String) ? unmarshalling((String) value, unmarshaller) : (T) value;
        return result;
    }
    
    @Override
    public ConfigHandler<String, String> optionnal() {
        return this;
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
}
