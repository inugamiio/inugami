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
package io.inugami.framework.interfaces.plugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface FrontPluginSpi {
    String getPluginName();

    default String getNgModuleName() {
        return null;
    }

    default String getNgModuleFileName() {
        return null;
    }

    default String getModuleFolder() {
        return null;
    }

    default String getRootClass() {
        return null;
    }

    default String getTitle() {
        return null;
    }

    default String getLoadingImage() {
        return null;
    }

    default String getFavIcon() {
        return null;
    }

    default List<String> getJavaScriptLink() {
        return new ArrayList<>();
    }

    default List<String> getCssFiles() {
        return new ArrayList<>();
    }

    default List<String> getMessageProperties() {
        return new ArrayList<>();
    }

    default Map<String, String> getVendorModulesMap() {
        return new LinkedHashMap<>();
    }
}
