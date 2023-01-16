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
package io.inugami.logs.obfuscator.tools;

import io.inugami.logs.obfuscator.api.ConfigurationSpi;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class DefaultConfigurationSpi implements ConfigurationSpi {


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Map<String, String> CONFIGURATION = loadConfiguration();


    // =========================================================================
    // INIT
    // =========================================================================
    private static Map<String, String> loadConfiguration() {
        Map<String, String> result = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            if (entry.getValue() != null) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        final Properties properties = System.getProperties();
        if (properties != null) {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                if (entry.getValue() != null) {
                    result.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }
            }
        }

        return result;
    }

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public String getProperty(final String key) {
        return key == null ? null : CONFIGURATION.get(key);
    }

    @Override
    public String getProperty(final String key, final String defaultValue) {
        final String result = getProperty(key);
        return result == null ? defaultValue : result;
    }

    @Override
    public boolean getBooleanProperty(final String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    @Override
    public boolean getBooleanProperty(final String key, final boolean defaultValue) {
        final String result = getProperty(key);
        return result == null ? defaultValue : Boolean.parseBoolean(result);
    }

    @Override
    public int getIntProperty(final String key) {
        return getIntProperty(key, 0);
    }

    @Override
    public int getIntProperty(final String key, final int defaultValue) {
        try {
            final String value = getProperty(key);
            return value == null ? defaultValue : Integer.parseInt(value);
        }
        catch (Throwable e) {
            return 0;
        }
    }

    @Override
    public long getLongProperty(final String key) {
        return getLongProperty(key, 0);
    }

    @Override
    public long getLongProperty(final String key, final long defaultValue) {
        try {
            final String value = getProperty(key);
            return value == null ? defaultValue : Integer.parseInt(value);
        }
        catch (Throwable e) {
            return 0;
        }
    }

    @Override
    public double getDoubleProperty(final String key) {
        return getDoubleProperty(key, 0.0);
    }

    @Override
    public double getDoubleProperty(final String key, final double defaultValue) {
        try {
            final String value = getProperty(key);
            return value == null ? defaultValue : Integer.parseInt(value);
        }
        catch (Throwable e) {
            return 0;
        }
    }
}
