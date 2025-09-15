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
package io.inugami.framework.commons.messages;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.commons.files.FilesUtils;
import io.inugami.framework.interfaces.exceptions.FatalException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * MessagesRegisterServices
 *
 * @author patrick_guillerm
 * @since 22 mars 2018
 */
@SuppressWarnings({"java:S3824"})
@Slf4j
@UtilityClass
public final class MessagesServices {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final String                           DEFAULT_LOCALE = "default";
    private static final AtomicReference<String>          json           = new AtomicReference<>();
    private static final Map<String, Map<String, String>> MESSAGES       = new ConcurrentHashMap<>();
    private static final ObjectMapper                     OBJECT_MAPPER  = JsonMarshaller.getInstance()
                                                                                         .getDefaultObjectMapper();

    // =================================================================================================================
    // GET MESSAGE
    // =================================================================================================================
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

    // =================================================================================================================
    // REGISTER
    // =================================================================================================================
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
                log.warn(e.getMessage());
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
        try {
            json.set(OBJECT_MAPPER.writeValueAsString(MESSAGES));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String getJson() {
        return json.get();
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
