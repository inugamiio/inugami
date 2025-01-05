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

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * PluginFrontConfig
 *
 * @author patrick_guillerm
 * @since 18 janv. 2017
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PluginFrontConfig implements Serializable {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long                serialVersionUID = 3567969435620891261L;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String              pluginBaseName;
    @ToString.Include
    private              String              routerModuleName;
    @ToString.Include
    private              String              commonsCss;
    private              PluginComponent     module;
    @Singular("router")
    private              List<Route>         router;
    @Singular("menuLinks")
    private              List<MenuLink>      menuLinks;
    private              Map<String, String> systemMap;
}
