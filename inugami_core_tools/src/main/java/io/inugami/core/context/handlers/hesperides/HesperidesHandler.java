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
package io.inugami.core.context.handlers.hesperides;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.exceptions.services.HandlerException;
import io.inugami.api.handlers.Handler;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Tuple;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.Config;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.commons.connectors.HttpConnectorResult;
import io.inugami.core.context.ContextSpiLoader;
import io.inugami.core.context.handlers.hesperides.model.Module;
import io.inugami.core.context.handlers.hesperides.model.Platform;
import io.inugami.core.context.handlers.hesperides.model.Properties;
import io.inugami.core.context.handlers.hesperides.model.Property;
import io.inugami.core.services.connectors.HttpConnector;

public class HesperidesHandler implements Handler {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    private final HttpConnector httpConnector;

    private final ConfigHandler<String, String> config;

    private final CredentialsProvider credentials = new BasicCredentialsProvider();

    private final static String TYPE = "HesperidesHandler";

    private final static String CONFIG_KEY_REALM = "realm";

    private final static String CONFIG_KEY_HOST = "hesperides.host";

    private final static String CONFIG_KEY_PORT = "port";

    private final static String CONFIG_KEY_USER_ID = "userId";

    private final static String CONFIG_KEY_PASSWORD = "password";

    private final String url;

    private final ClassBehavior classBehavior;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public HesperidesHandler(final ClassBehavior classBehavior, final ConfigHandler<String, String> config) {
        this.config        = config;
        this.classBehavior = classBehavior;

        httpConnector = ContextSpiLoader.load().getHttpConnector(this.classBehavior.getName(),
                                                                 getConfigElement("httpMaxConnection", "5"),
                                                                 getConfigElement("httpTimeout", "6000"),
                                                                 getConfigElement("httpTTL", "50"),
                                                                 getConfigElement("httpMaxPerRoute", "10"),
                                                                 getConfigElement("socketTimeout", "60000"));

        final StringBuilder builder = new StringBuilder();
        builder.append(config.optionnal().grab(CONFIG_KEY_REALM));
        builder.append("://");
        builder.append(config.optionnal().grab(CONFIG_KEY_HOST));
        builder.append(":");
        builder.append(config.optionnal().grab(CONFIG_KEY_PORT));
        builder.append("/rest/");

        url = builder.toString();

        credentials.setCredentials(new AuthScope(this.config.grab(CONFIG_KEY_HOST), AuthScope.ANY_PORT,
                                                 this.config.grab(CONFIG_KEY_REALM)),
                                   new UsernamePasswordCredentials(this.config.grab(CONFIG_KEY_USER_ID),
                                                                   this.config.grab(CONFIG_KEY_PASSWORD)));
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    // -------------------------------------------------------------------------
    // Global Property
    // -------------------------------------------------------------------------
    public String getGlobalProperty(final String applicationName, final String platformName, final String propertyKey) {
        String result = null;

        Properties globalProperties = null;
        try {
            globalProperties = getProperties(applicationName, platformName, "#");
        }
        catch (final HesperidesHandlerException e) {
            Loggers.PARTNERLOG.error(e.getMessage(), e);
        }

        if (globalProperties != null) {
            result = findProperty(globalProperties, propertyKey);
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Local Property
    // -------------------------------------------------------------------------
    public String getLocalProperty(final String applicationName, final String platformName, final String componentName,
                                   final String propertyKey) {
        String result = null;

        List<Module> modules         = null;
        Module       componentModule = null;
        Properties   properties      = null;

        try {
            modules = getModules(applicationName, platformName);
        }
        catch (final HesperidesHandlerException e) {
            Loggers.PARTNERLOG.error(e.getMessage(), e);
        }

        if (modules != null) {
            componentModule = findModule(modules, componentName);
        }

        if (componentModule != null) {
            try {
                properties = getProperties(applicationName, platformName, componentModule.getPropertiesPath());
            }
            catch (final HesperidesHandlerException e) {
                Loggers.PARTNERLOG.error(e.getMessage(), e);
            }

            if (properties != null) {
                result = findProperty(properties, propertyKey);
            }
        }
        else {
            Loggers.HANDLER.error("error-module-filter of the componentName: {} not found", componentName);
        }

        return result;
    }

    private Module findModule(final List<Module> modules, final String componentName) {
        Module result;
        // formatter:off
        result = modules.stream().filter(module -> getComponentName(module.getPath()).equals(componentName)).findFirst()
                        .orElse(null);
        // formatter:on

        return result;
    }

    private List<Module> getModules(final String applicationName,
                                    final String platformName) throws HesperidesHandlerException {
        return getPlatform(applicationName, platformName).getModules();
    }

    private Platform getPlatform(final String applicationName,
                                 final String platformName) throws HesperidesHandlerException {
        final StringBuilder builder = new StringBuilder();
        builder.append(url);
        builder.append("applications/");
        builder.append(applicationName);
        builder.append("/platforms/");
        builder.append(platformName);

        final String request = builder.toString();

        HttpConnectorResult httpResult;
        try {
            httpResult = callHesperides(request);
        }
        catch (final ConnectorException e) {
            throw new HesperidesHandlerException(e.getMessage(), e);
        }

        return (Platform) buildResult(httpResult, request, new Platform());
    }

    // -------------------------------------------------------------------------
    // Common
    // -------------------------------------------------------------------------
    protected HttpConnectorResult callHesperides(final String request) throws ConnectorException {
        return httpConnector.get(request, credentials);
    }

    private String findProperty(final Properties properties, final String propertyKey) {
        String               result         = null;
        final List<Property> propertiesList = properties.getProperties();

        // formatter:off
        final Property propertyValue = propertiesList.stream()
                                                     .filter(property -> property.getName().equals(propertyKey))
                                                     .findFirst().orElse(null);
        // formatter:on

        if (propertyValue != null) {
            result = propertyValue.getValue();
        }
        else {
            Loggers.HANDLER.error("error-propertyKey-filter: {} not found", propertyKey);
        }

        return result;
    }

    private Properties getProperties(final String applicationName, final String platformName,
                                     final String path) throws HesperidesHandlerException {

        final String request = buildPropertiesRequest(applicationName, platformName, path);

        HttpConnectorResult httpResult;
        try {
            httpResult = callHesperides(request);
        }
        catch (final ConnectorException e) {
            throw new HesperidesHandlerException(e.getMessage(), e);
        }

        return (Properties) buildResult(httpResult, request, new Properties());
    }

    private JsonObject buildResult(final HttpConnectorResult httpResult, final String request,
                                   final JsonObject receivedDataObject) {
        if (httpResult.getStatusCode() != 200) {
            Loggers.PARTNERLOG.error("error-http-{} on request : {}", httpResult.getStatusCode(), request);
        }

        return receivedDataObject.convertToObject(httpResult.getData(), httpResult.getCharset());
    }

    protected String buildPropertiesRequest(final String applicationName, final String platformName,
                                            final String path) throws HesperidesHandlerException {

        String result;

        final StringBuilder builder = new StringBuilder();
        builder.append(url);
        builder.append("applications/");
        builder.append(applicationName);
        builder.append("/platforms/");
        builder.append(platformName);
        builder.append("/properties");

        final String requestUrl = builder.toString();

        final List<Tuple<String, String>> params = new ArrayList<>();

        params.add(new Tuple<>("path", path));

        try {
            result = httpConnector.buildRequest(requestUrl, params);
        }
        catch (final ConnectorException e) {
            throw new HesperidesHandlerException(e.getMessage(), e);
        }

        return result;
    }

    private String getComponentName(final String modulePropertyPath) {
        return modulePropertyPath.substring(1, modulePropertyPath.indexOf('#', 2));
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private int getConfigElement(final String key, final String defaultValue) {
        return Integer.parseInt(config.grabOrDefault(key, defaultValue));
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getType() {
        return TYPE;
    }

    public Optional<String> getConfig(final String key) {
        return classBehavior.getConfig(key);
    }

    @Override
    public String getName() {
        return classBehavior.getName();
    }

    public List<Config> getConfigs() {
        return classBehavior.getConfigs();
    }

    // =========================================================================
    // EXCEPTION
    // =========================================================================
    public class HesperidesHandlerException extends HandlerException {
        private static final long serialVersionUID = 4703103462803393986L;

        public HesperidesHandlerException(final String message, final Object... values) {
            super(message, values);
        }

        public HesperidesHandlerException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
