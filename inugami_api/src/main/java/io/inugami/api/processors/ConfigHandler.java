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

import io.inugami.api.mapping.JsonUnmarshalling;

import java.util.List;
import java.util.Map;


public interface ConfigHandler<K, V> extends Map<K, V> {
    

    V applyProperties(final V value);
    

    String grabOrDefault(final K key, final String defaultValue);
    

    String grab(K key);
    

    String grab(final String message, final K key);
    

    Integer grab(final K key, final int defaultValue);
    

    Integer grabInt(final K key);
    

    Integer grabInt(final K key, final Integer defaultValue);
    

    long grabLong(String key, long defaultValue);
    

    boolean grabBoolean(final K key);
    

    boolean grabBoolean(final K key, final boolean defaultValue);
    

    Double grab(final K key, final double defaultValue);
    

    Double grabDouble(final K key);
    

    <T> T grabJson(final K key, final String json, final JsonUnmarshalling unmarshaller);
    

    <T> T grabJson(final K key, final JsonUnmarshalling unmarshaller);
    

    <T> T grabJson(final K key, final Object jsonObj, final JsonUnmarshalling unmarshaller);
    
    ConfigHandler<K, V> optionnal();
    
    List<String> grabValues(String prefix);
    
}
