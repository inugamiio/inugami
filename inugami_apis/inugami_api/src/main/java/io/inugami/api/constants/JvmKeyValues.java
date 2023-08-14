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
package io.inugami.api.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * JvmKeyValues
 *
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
@SuppressWarnings({"java:S3066"})
@Getter
@RequiredArgsConstructor
public enum JvmKeyValues {
    // =========================================================================
    // ENUM
    // =========================================================================
    /**
     * JVM key for define home path
     *
     * <pre>
     * <code>-Dinugami-home=/path/to/specific/home</code>
     * </pre>
     */
    JVM_HOME_PATH("inugami-home"),
    MONITORING_FILE("monitoring-file"),

    /**
     * JVM key prefix for define specific plugin configuration file
     *
     * <pre>
     * <code>-Dinugami-plugin-{pluginName}=/path/to/specific/plugin/configuration/file.xml</code>
     * </pre>
     */
    PLUGIN_PREFIX("inugami-plugin"),

    /**
     * JVM key for enable developpment mode
     *
     * <pre>
     * <code>-Denv=dev</code>
     * </pre>
     */
    ENVIRONMENT("env"),

    ASSET("asset"),
    INSTANCE("instance"),
    INSTANCE_NUMBER("instance.number"),

    APPLICATION_NAME("application.name"),
    APPLICATION_HOST_NAME("application.hostname"),
    APPLICATION_VERSION("application.version"),
    APPLICATION_TITLE("application.title"),
    APPLICATION_TIMEOUT("application.timeout"),
    APPLICATION_SCRIPT_TIMEOUT("application.script.timeout"),
    APPLICATION_PLUGIN_RUNNING("application.plugin.running"),
    APPLICATION_PLUGIN_RUNNING_STANDALONE("application.plugin.running.standalone"),
    APPLICATION_MAX_THREADS("application.max.threads"),
    APPLICATION_VERBOSE("application.verbose"),
    APPLICATION_MONITORING("application.monitoring"),
    APPLICATION_MAX_USER_SOCKETS("application.max.user.sockets"),

    HTTP_CONNECTION_TIMEOUT("http.connection.timeout"),
    HTTP_CONNECTION_SOCKET_TIMEOUT("http.connection.socket.timeout"),
    HTTP_CONNECTION_TTL("http.connection.ttl"),
    HTTP_CONNECTION_MAX_CONNECTIONS("http.connection.max.connections"),
    HTTP_CONNECTION_MAX_PER_ROUTE("http.connection.max.per.route"),
    HTTP_CONNECTION_HEADER_FIELD("http.connection.header.field"),

    /**
     * JVM key for specify AES secret token. AES Token must have 16 chars.
     *
     * <pre>
     * <code>-Dsecurity.aes.secret.key="MySecretTokenAES"</code>
     * </pre>
     */
    SECURITY_USERS("security.users"),
    SECURITY_ROLES("security.roles"),
    SECURITY_AES_SECRET_KET("security.aes.secret.key"),
    SECURITY_FILE_TOKEN("security.file.token"),
    SECURITY_FILE_USER("security.file.user"),
    SECURITY_SQL_INJECT_REGEX("security.sql.inject.regex"),
    SERCURITY_ENCODING("security.encoding"),
    SECURITY_CRYPTOGRAPHIC_KEYS("security.crypto.keys"),
    SECURITY_TOKEN_MAX_SIZE("security.token.max.size"),
    SERCURITY_CIPHER_ALGO("security.cipher.algo"),
    SERCURITY_CIPHER_ALGO_KEY("security.cipher.algo.key"),
    SECURITY_AES_SECRET_KEY("security.aes.secret.key"),

    DATA_STORAGE_DRIVER("data.storage.driver"),
    DATA_STORAGE_DIALECT("data.storage.dialect"),
    DATA_STORAGE_URL("data.storage.url"),
    DATA_STORAGE_USER("data.storage.user"),
    DATA_STORAGE_PASSWORD("data.storage.password"),
    DATA_STORAGE_VERBOSE("data.verbose"),
    DATA_STORAGE_HBM2DDL("data.hbm2ddl"),

    ALERTING_ENABLE("alerting.enable"),
    ALERTING_DYNAMIC_MAX_THREADS("alerting.dynamic.max.threads"),

    SCRIPT_FULL_SCRIPT_PATH("script.full.script.path"),

    CHAOS("chaos.enable"),

    HEADER_KEY_CORRELATION_ID("header.key.correlation.id"),
    HEADER_KEY_REQUEST_ID("header.key.request.id"),
    HEADER_KEY_CONVERSATION_ID("header.key.conversation.id"),
    HEADER_KEY_TOKEN("header.key.token"),
    HEADER_KEY_DEVICE_IDENTIFIER("header.key.device.identifier"),
    HEADER_KEY_DEVICE_TYPE("header.key.device.type"),
    HEADER_KEY_DEVICE_CLASS("header.key.device.class"),
    HEADER_KEY_DEVICE_VERSION("header.key.device.version"),
    HEADER_KEY_DEVICE_OS_VERSION("header.key.device.os.version"),
    HEADER_KEY_DEVICE_NETWORK_TYPE("header.key.device.network.type"),
    HEADER_KEY_DEVICE_NETWORK_SPEED_DOWN("header.key.device.network.speed.down"),
    HEADER_KEY_DEVICE_NETWORK_SPEED_UP("header.key.device.network.speed.up"),
    HEADER_KEY_DEVICE_NETWORK_SPEED_LATENCY("header.key.device.network.speed.latency"),

    HEADER_KEY_DEVICE_IP("header.key.device.os.ip"),
    HEADER_KEY_USER_AGENT("header.key.user.agent"),
    HEADER_KEY_LANGUAGE("header.key.language"),
    HEADER_KEY_COUNTRY("header.key.country"),

    MONITORING_ENABLE("monitoring.enable"),
    DEV_FILTER_BAN_URI_REGEX("dev.filter.ban.uri.regex"),

    CACHE_CONFIG_PATH("cache.configuration.path");

    public static final String DEFAULT_APPLICATION_NAME = "inugami";

    // =========================================================================
    // ENUM COMPOSITE
    // =========================================================================
    private final String key;


    public String or(final Object defaultValue) {
        return getValue(this, defaultValue == null ? null : String.valueOf(defaultValue));
    }

    public String or(final String sufix, final Object defaultValue) {
        final String systemValue = System.getProperty(String.join(".", key, sufix));
        return systemValue == null ? String.valueOf(defaultValue) : systemValue;
    }

    public String get() {
        return getValue(this, null);
    }

    public static String getValue(final JvmKeyValues key, final String defaultValue) {
        final String systemValue = System.getProperty(key.key);
        return systemValue == null ? defaultValue : systemValue;
    }
}
