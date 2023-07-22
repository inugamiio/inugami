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
package io.inugami.api.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConfigTemplateValues
 *
 * @author patrick_guillerm
 * @since 9 mai 2017
 */
@SuppressWarnings({"java:S5361"})
public class ConfigTemplateValues implements TemplateProviderSPI {

    public static final String  EMPTY          = "";
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final       Pattern PROPERTY_REGEX = Pattern.compile("(?:[{]{2})([^}]+)(?:[}]{2})");

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String applyProperties(final String value, final Map<String, String> config) {
        String result = value;
        if ((value != null) && (config != null)) {
            final List<String> keys = searchKeys(value);
            for (final String key : keys) {
                final Optional<String> propValue = grabPropertyValue(key, config);
                if (propValue.isPresent()) {
                    result = replacePropertyValue(result, key, propValue.get());
                }
            }

        }

        return result;
    }

    private Optional<String> grabPropertyValue(final String key, final Map<String, String> config) {
        Optional<String> result = Optional.empty();
        String           value  = config.get(key);
        if (value != null) {
            if (!searchKeys(value).isEmpty()) {
                value = applyProperties(value, config);
            }
            result = Optional.of(value);
        }
        return result;
    }

    private String replacePropertyValue(final String result, final String key, final String propValue) {
        final String realKey  = new StringBuilder("[{]{2}").append(key).append("[}]{2}").toString();
        final String property = propValue == null ? EMPTY : (propValue == null ? EMPTY : propValue);
        if (property.contains("\\")) {
            return result.replaceAll(realKey, propValue.replaceAll("\\\\", "\\\\\\\\"));
        } else {
            return result.replaceAll(realKey, propValue);
        }
    }

    public List<String> searchKeys(final String value) {
        final List<String> result  = new ArrayList<>();
        final Matcher      matcher = PROPERTY_REGEX.matcher(value);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }
}
