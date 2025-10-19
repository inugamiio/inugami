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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface IndexHtmlLoadingContentSpi {
    String getLoadingContent(final String contextPath, final List<FrontPluginSpi> plugins);

    default String buildPluginLoadingImage(final List<FrontPluginSpi> plugins) {
        return Optional.ofNullable(plugins)
                       .orElse(new ArrayList<>())
                       .stream()
                       .map(FrontPluginSpi::getLoadingImage)
                       .filter(Objects::nonNull)
                       .findFirst()
                       .orElse("/images/loading.gif");
    }

    default String buildPluginTitle(final List<FrontPluginSpi> plugins) {
        return Optional.ofNullable(plugins)
                       .orElse(new ArrayList<>())
                       .stream()
                       .map(FrontPluginSpi::getTitle)
                       .filter(Objects::nonNull)
                       .findFirst()
                       .orElse("Inugami project analysis");
    }
}
