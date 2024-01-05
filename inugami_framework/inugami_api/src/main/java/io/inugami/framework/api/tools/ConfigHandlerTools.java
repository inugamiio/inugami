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
package io.inugami.framework.api.tools;


import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.processors.ConfigHandler;
import lombok.experimental.UtilityClass;

/**
 * ConfigHandlerTools
 *
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
@UtilityClass
public final class ConfigHandlerTools {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String ENABLE = "enable";


    // =========================================================================
    // METHODS
    // =========================================================================
    public static String grabConfig(final Class<?> prefix, final String key,
                                    final ConfigHandler<String, String> config) {
        return grabConfig(prefix, key, null, config);
    }

    public static String grabConfig(final Class<?> prefix, final String key, final String defaultValue,
                                    final ConfigHandler<String, String> config) {
        Asserts.assertNotNull(prefix, key, config);
        final StringBuilder reelKey = new StringBuilder();
        reelKey.append(prefix.getSimpleName().substring(0, 1).toLowerCase());
        reelKey.append(prefix.getSimpleName().substring(1));
        reelKey.append('.');
        reelKey.append(key);
        return config.grabOrDefault(reelKey.toString(), defaultValue);
    }

    public static int grabConfigInt(final Class<?> prefix, final String key, final ConfigHandler<String, String> config,
                                    final int defaultValue) {
        final String value = grabConfig(prefix, key, config);
        return Asserts.checkIsBlank(value) ? defaultValue : Integer.parseInt(value);
    }

    public static long grabConfigLong(final Class<?> prefix, final String key,
                                      final ConfigHandler<String, String> config, final long defaultValue) {
        final String value = grabConfig(prefix, key, config);
        return Asserts.checkIsBlank(value) ? defaultValue : Long.parseLong(value);
    }

    public static boolean grabConfigBoolean(final Class<?> prefix, final String key,
                                            final ConfigHandler<String, String> config, final boolean defaultValue) {
        final String value = grabConfig(prefix, key, config);
        return Asserts.checkIsBlank(value) ? defaultValue : Boolean.parseBoolean(value);
    }

}
