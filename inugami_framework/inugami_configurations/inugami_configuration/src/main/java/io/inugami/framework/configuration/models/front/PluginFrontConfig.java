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
package io.inugami.framework.configuration.models.front;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PluginFrontConfig
 *
 * @author patrick_guillerm
 * @since 18 janv. 2017
 */
public class PluginFrontConfig implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 3567969435620891261L;

    private String pluginBaseName;

    private String routerModuleName;

    private String commonsCss;

    private PluginComponent module;

    private List<Route> router;

    private List<MenuLink> menuLinks;

    private Map<String, String> systemMap;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PluginFrontConfig() {
    }

    public PluginFrontConfig(final String pluginBaseName) {
        this.pluginBaseName = pluginBaseName;
    }

    public PluginFrontConfig(final String pluginBaseName, final String commonsCss, final PluginComponent module,
                             final List<Route> router, final List<MenuLink> menuLinks, final String routerModuleName) {
        super();
        this.pluginBaseName = pluginBaseName;
        this.commonsCss = commonsCss;
        this.module = module;
        this.router = router;
        this.menuLinks = menuLinks;
        this.routerModuleName = routerModuleName;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return (pluginBaseName == null) ? 0 : pluginBaseName.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;

        if (!result && (obj != null) && (obj instanceof PluginFrontConfig)) {
            final PluginFrontConfig other = (PluginFrontConfig) obj;
            result = pluginBaseName == null ? other.getPluginBaseName() == null
                    : pluginBaseName.equals(other.getPluginBaseName());
        }

        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PluginFrontConfig [pluginBaseName=");
        builder.append(pluginBaseName);
        builder.append(", module=");
        builder.append(module);
        builder.append(", router=");
        builder.append(router);
        builder.append(", menuLinks=");
        builder.append(menuLinks);
        builder.append(", commonsCss=");
        builder.append(commonsCss);
        builder.append(", routerModuleName=");
        builder.append(routerModuleName);

        builder.append("]");
        return builder.toString();
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    public String getPluginBaseName() {
        return pluginBaseName;
    }

    public void setPluginBaseName(final String pluginBaseName) {
        this.pluginBaseName = pluginBaseName;
    }

    public PluginComponent getModule() {
        return module;
    }

    public void setModule(final PluginComponent module) {
        this.module = module;
    }

    public List<Route> getRouter() {
        return router;
    }

    public void setRouter(final List<Route> router) {
        this.router = router;
    }

    public List<MenuLink> getMenuLinks() {
        return menuLinks;
    }

    public void setMenuLinks(final List<MenuLink> menuLinks) {
        this.menuLinks = menuLinks;
    }

    public String getCommonsCss() {
        return commonsCss;
    }

    public void setCommonsCss(final String commonsCss) {
        this.commonsCss = commonsCss;
    }

    public void addRoute(final Route route) {
        if (router == null) {
            router = new ArrayList<>();
        }
        if (route != null) {
            router.add(route);
        }
    }

    public void addMenuLink(final MenuLink menuLink) {
        if (menuLinks == null) {
            menuLinks = new ArrayList<>();
        }
        if (menuLink != null) {
            menuLinks.add(menuLink);
        }

    }

    public boolean hasRouterModuleName() {
        return routerModuleName != null;
    }

    public String getRouterModuleName() {
        return routerModuleName;
    }

    public void setRouterModuleName(final String routerModuleName) {
        this.routerModuleName = routerModuleName;
    }

    public Map<String, String> getSystemMap() {
        return systemMap;
    }

    public void setSystemMap(final Map<String, String> systemMap) {
        this.systemMap = systemMap;
    }

}
