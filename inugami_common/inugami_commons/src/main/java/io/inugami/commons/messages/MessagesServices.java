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
package io.inugami.commons.messages;

import flexjson.JSONSerializer;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.loggers.Loggers;
import io.inugami.commons.files.FilesUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MessagesRegisterServices
 *
 * @author patrick_guillerm
 * @since 22 mars 2018
 */
@SuppressWarnings({"java:S3824"})
public final class MessagesServices {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String DEFAULT_LOCALE = "default";

    private static String json = null;

    private static final Map<String, Map<String, String>> MESSAGES = new ConcurrentHashMap<>();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private MessagesServices() {
    }

    // =========================================================================
    // GET MESSAGE
    // =========================================================================
    public static String getMessage(final String key) {
        String reuslt = null;
        for (final Map.Entry<String, Map<String, String>> entry : MESSAGES.entrySet()) {
            reuslt = entry.getValue().get(key);
            if (reuslt != null) {
                break;
            }
        }
        return reuslt == null ? key : reuslt;
    }

    public static String getMessage(final String key, final String locale) {
        String result = null;
        if (MESSAGES.containsKey(locale)) {
            result = MESSAGES.get(locale).get(key);
        } else {
            result = MESSAGES.get(DEFAULT_LOCALE).get(key);
        }
        return result == null ? key : result;
    }

    // =========================================================================
    // REGISTER
    // =========================================================================
    public static synchronized void registerConfig(final Map<String, String> properties) {
        if (properties != null) {
            final String currentLocale = initLocale(null);
            MESSAGES.get(currentLocale).putAll(properties);
            updateJson();
        }
    }

    public static synchronized void register(final Map<String, Map<String, String>> properties) {
        if (properties != null) {
            for (final Map.Entry<String, Map<String, String>> entry : properties.entrySet()) {
                final String currentLocale = initLocale(entry.getKey());
                MESSAGES.get(currentLocale).putAll(entry.getValue());
            }
            updateJson();
        }
    }

    public static void registerFromClassloader(final String path) {
        registerFromClassloader(path, DEFAULT_LOCALE);
    }

    public static synchronized void registerFromClassloader(final String path, final String locale) {
        if (path != null) {
            Map<String, String> properties = null;
            try {
                properties = FilesUtils.readPropertiesInClassLoader(path);
            } catch (final FatalException e) {
                Loggers.DEBUG.warn(e.getMessage());
            }

            if (properties != null) {
                final String currentLocale = initLocale(locale);
                MESSAGES.get(currentLocale).putAll(properties);
                updateJson();
            }
        }
    }


    // =========================================================================
    // JSON
    // =========================================================================
    private static void updateJson() {
        json = new JSONSerializer().exclude("*.class").deepSerialize(MESSAGES);
    }

    public static String getJson() {
        return json;
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    public static String initLocale() {
        return initLocale(null);
    }

    public static String initLocale(final String locale) {
        final String currentLocale = locale == null ? DEFAULT_LOCALE : locale;
        if (!MESSAGES.containsKey(currentLocale)) {
            MESSAGES.put(currentLocale, new HashMap<>());
        }

        return currentLocale;
    }

    public static void registerFrontProperties(final Map<String, String> frontProperties) {
        if (frontProperties != null) {
            if (MESSAGES.containsKey(DEFAULT_LOCALE)) {
                MESSAGES.put(DEFAULT_LOCALE, frontProperties);
            } else {
                MESSAGES.get(DEFAULT_LOCALE).putAll(frontProperties);
            }
        }
    }

}
