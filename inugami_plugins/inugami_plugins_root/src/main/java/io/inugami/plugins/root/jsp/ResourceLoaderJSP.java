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
package io.inugami.plugins.root.jsp;

import lombok.experimental.UtilityClass;

/**
 * ResourceLoaderJSP
 *
 * @author patrick_guillerm
 * @since 18 janv. 2017
 */
@UtilityClass
public class ResourceLoaderJSP {

    // =========================================================================
    // METHODS
    // =========================================================================
    public static final String PLUGINS_GAVS = ResourcesRenderer.renderPluginsGavs();

    public static final String JAVASCRIPT_I18N = ResourcesRenderer.renderPluginsI18N();

    // =========================================================================
    // METHODS
    // =========================================================================
    public static String getPluginCss(final String contextPath) {
        return ResourcesRenderer.renderPluginsCss(contextPath);
    }

    public static String getPluginJavaScripts(final String contextPath) {
        return ResourcesRenderer.renderPluginsJavaScript(contextPath);
    }

    public static String getPluginSystemMap() {
        return ResourcesRenderer.renderPluginsSystemMap();
    }

    public static String getApplicationVersion() {
        return ResourcesRenderer.renderApplicationVersion();
    }

    public static String getApplicationTitle() {
        return ResourcesRenderer.getApplicationTitle();
    }
}
