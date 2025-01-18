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
package io.inugami.framework.interfaces.configurtation;

import java.util.List;
import java.util.Map;

/**
 * In Inugami, the <strong>ConfigHandler</strong> is the root principle for configuration management.
 * All configuration in Inugami are sent to components with this object.
 * The configuration in Inugami are not static, values or functions can be injected or invoked
 * using specific tags. To inject properties, use mustache template (<strong>{{someValue}}</strong>).
 * For functions, it's the same approach than **EL**  expression (<strong>#{myFunction(param1, param2)}</strong>)
 *
 * @param <K> the key type
 * @param <V> the value type
 */
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

    ConfigHandler<K, V> optionnal();

    List<String> grabValues(String prefix);

}
