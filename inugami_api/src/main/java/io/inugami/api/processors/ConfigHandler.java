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
package io.inugami.api.processors;

import java.util.List;
import java.util.Map;

import io.inugami.api.mapping.JsonUnmarshalling;

/**
 * ConfigHandler.
 *
 * @author patrick_guillerm
 * @param <K> the key type
 * @param <V> the value type
 * @since 5 janv. 2017
 */
public interface ConfigHandler<K, V> extends Map<K, V> {
    
    /**
     * Allow to apply properties values on a value. Can't only be apply on
     * <String,String> map type.
     * 
     * @param value current value
     * @return modified with all inner properties
     */
    V applyProperties(final V value);
    
    /**
     * Allow to grab configuration from Key or grab the default value
     *
     * @param key configuration key
     * @param defaultValue the default value
     * @return the configuration
     */
    String grabOrDefault(final K key, final String defaultValue);
    
    /**
     * Allow to grab configuration from key. Value is mandatory!
     * 
     * @param key the key
     * @return configuration
     */
    String grab(K key);
    
    /**
     * Allow to grab configuration from key. Value is mandatory!
     *
     * @param message the error message if value isn't define
     * @param key the key
     * @return configuration
     */
    String grab(final String message, final K key);
    
    /**
     * Grab.
     *
     * @param key configuration key
     * @param defaultValue the default value
     * @return the configuration
     */
    Integer grab(final K key, final int defaultValue);
    
    /**
     * Allow to grab configuration from key. Value is mandatory!
     * 
     * @param configuration key
     * @throws Runtime exception if no value define
     * @return configuration
     */
    Integer grabInt(final K key);
    
    /**
     * Grab int.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the integer
     */
    Integer grabInt(final K key, final Integer defaultValue);
    
    /**
     * Grab long value from configuration
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    long grabLong(String key, long defaultValue);
    
    /**
     * Grab boolean.
     *
     * @param configuration key
     * @throws Runtime exception if no value define
     * @return true, if successful
     */
    boolean grabBoolean(final K key);
    
    /**
     * Grab boolean.
     *
     * @param configuration key
     * @param defaultValue the default value
     * @return if value is define, it will return value, else it return default
     *         value
     */
    boolean grabBoolean(final K key, final boolean defaultValue);
    
    /**
     * Grab.
     *
     * @param key configuration key
     * @param defaultValue the default value
     * @return the configuration
     */
    Double grab(final K key, final double defaultValue);
    
    /**
     * Allow to grab configuration from key. Value is mandatory!
     * 
     * @param configuration key
     * @throws Runtime exception if no value define
     * @return configuration
     */
    Double grabDouble(final K key);
    
    /**
     * Grab json.
     *
     * @param <T> the generic type
     * @param key configuration key
     * @param json the json
     * @param unmarshaller the unmarshaller
     * @return the configuration
     */
    <T> T grabJson(final K key, final String json, final JsonUnmarshalling unmarshaller);
    
    /**
     * Allow to grab configuration from key. Value is mandatory!
     * 
     * @param configuration key
     * @throws Runtime exception if no value define
     * @return configuration
     */
    <T> T grabJson(final K key, final JsonUnmarshalling unmarshaller);
    
    /**
     * Grab json.
     *
     * @param <T> the generic type
     * @param key configuration key
     * @param jsonObj the json obj
     * @param unmarshaller the unmarshaller
     * @return the configuration
     */
    <T> T grabJson(final K key, final Object jsonObj, final JsonUnmarshalling unmarshaller);
    
    ConfigHandler<K, V> optionnal();
    
    List<String> grabValues(String prefix);
    
}
