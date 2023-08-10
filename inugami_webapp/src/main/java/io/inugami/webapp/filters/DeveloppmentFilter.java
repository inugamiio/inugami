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
package io.inugami.webapp.filters;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.commons.files.FilesUtils;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.core.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * DeveloppmentFilter
 *
 * @author patrick_guillerm
 * @since 27 nov. 2017
 */
@SuppressWarnings({"java:S3655", "java:S108"})
@WebFilter(asyncSupported = true, value = {"*"})
public class DeveloppmentFilter implements Filter {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger LOGGER = LoggerFactory.getLogger(DeveloppmentFilter.class.getSimpleName());

    private static final Map<String, File> RESOURCES_CACHES = new ConcurrentHashMap<>();

    private static final File NOT_PLUGIN_RESOURCES = new File(".notPluginResource");

    //@formatter:off
    private static final  String[] EXCLUDE_PATHS = { "/rest/"};
    //@formatter:on

    private final List<Plugin> plugins = new ArrayList<>();

    private boolean enable = false;

    private Pattern banUriPattern;

    private String urlRegex;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    /**
     * read resource on development server mode. For enable this method, plugin
     * must be compile on development mode, with specific MANIFEST. Workspace
     * specify in MANIFEST must exists on local server and allow to read it.
     * Only plugins resources is allow to be reading. URI ban can be enforce by
     * define JVM argument "dev.filter.ban.uri.regex"
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        enable = "dev".equalsIgnoreCase(JvmKeyValues.ENVIRONMENT.or("prod"));
        banUriPattern = Pattern.compile(JvmKeyValues.DEV_FILTER_BAN_URI_REGEX.or(".*([.](xml|class)).*"),
                                        Pattern.CASE_INSENSITIVE);
        //@formatter:off
        Context.getInstance()
               .getPlugins()
               .orElse(new ArrayList<>())
               .stream()
               .filter(plugin->plugin.getManifest()!=null)
               .filter(plugin->plugin.getManifest().getWorkspace()!=null)
               .filter(plugin->workspaceExists(plugin.getManifest().getWorkspace()))
               .forEach(plugins::add);
        //@formatter:on

        if (!plugins.isEmpty()) {
            final StringBuilder regex = new StringBuilder();
            regex.append('(');
            regex.append(".*plugins/");
            regex.append('(');
            for (int i = 0; i < plugins.size(); i++) {
                final Plugin plugin = plugins.get(0);
                if (plugin.getFrontConfig().isPresent()) {
                    if (i != 0) {
                        regex.append('|');
                    }
                    regex.append('(');
                    regex.append(plugin.getFrontConfig().get().getPluginBaseName());
                    regex.append(')');
                }
            }
            regex.append(')');
            regex.append(".*");
            regex.append(')');
            regex.append("|");
            regex.append("(.*[.].*)");

            urlRegex = regex.toString();
        }

    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (enable && isInterceptableResource(httpRequest.getRequestURI())) {
            final String uriResource = buildUriResource(httpRequest.getRequestURI(), httpRequest.getContextPath());
            final File   resource    = searchResources(uriResource);
            if (resource == null) {
                chain.doFilter(request, response);
            } else {
                intercept(resource, (HttpServletResponse) response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    // =========================================================================
    // PRIVATE
    // =========================================================================
    private boolean isInterceptableResource(final String requestURI) {
        boolean result = !plugins.isEmpty();
        if (result) {
            result = !isBanUri(requestURI) && matchUriWithPluginResources(requestURI);
        }
        return result;
    }

    private boolean isBanUri(final String requestURI) {
        return banUriPattern.matcher(requestURI).matches();
    }

    private boolean matchUriWithPluginResources(final String requestURI) {
        return notInExcluseUri(requestURI) && Pattern.compile(urlRegex).matcher(requestURI).matches();
    }

    private boolean notInExcluseUri(final String requestURI) {
        boolean result = false;
        for (final String excludePath : EXCLUDE_PATHS) {
            result = requestURI.contains(excludePath);
            if (result) {
                break;
            }
        }
        return !result;
    }

    // =========================================================================
    // SEARCH RESOURCES
    // =========================================================================
    private File searchResources(final String requestURI) {
        File resource = RESOURCES_CACHES.get(requestURI);
        if (resource == null) {
            resource = processSearchInPlugins(requestURI);
        }
        return NOT_PLUGIN_RESOURCES == resource ? null : resource;
    }

    private File processSearchInPlugins(final String resourceUri) {
        File result = NOT_PLUGIN_RESOURCES;

        for (final Plugin plugin : plugins) {
            final File currentPath = FilesUtils.buildFile(plugin.getWorkspace(),
                                                          "src/main/resources/META-INF/resources", resourceUri);
            if (currentPath.exists()) {
                result = currentPath;
                break;
            }
        }
        return result;
    }

    // =========================================================================
    // INTERCEPT RESOURCES
    // =========================================================================

    /**
     * read resource on development server mode. For enable this method, plugin
     * must be compile on development mode, with specific MANIFEST. Workspace
     * specify in MANIFEST must exists on local server and allow to read it.
     * Only plugins resources is allow to be reading.
     */
    private void intercept(final File resource, final HttpServletResponse response) {
        response.setStatus(200);
        response.setContentType(FilesUtils.getContentType(resource));
        response.addIntHeader("Expires", 0);
        response.addHeader("Cache-Control", "no-store, must-revalidate");

        try (OutputStream out = response.getOutputStream()) {
            try (InputStream ios = new FileInputStream(resource)) {
                final byte[] buffer = new byte[4096];

                int read = 0;
                while ((read = ios.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private String buildUriResource(final String requestURI, final String contextPath) {
        return requestURI.length() < contextPath.length() ? null : requestURI.substring(contextPath.length());
    }

    private boolean workspaceExists(final File workspace) {
        return workspace.exists() && workspace.canRead();
    }

    // =========================================================================
    // DESTROY
    // =========================================================================
    @Override
    public void destroy() {
        // nothing to do
    }
}
